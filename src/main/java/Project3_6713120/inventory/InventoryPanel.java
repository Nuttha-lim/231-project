/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.inventory;

import Project3_6713120.GameManager;
import Project3_6713120.resource.fonts;
import Project3_6713120.ui.UiConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryPanel extends JPanel {

    private Inventory inventory;
    private GameManager gm;
    private Image bgInventory;

    private JLabel[][] iconLabels;
    private JLabel[][] amountLabels;

    // State
    private ItemStack heldItem = null;
    private Slot hoveredSlot = null;

    private JLabel dragIconLabel;
    private JLabel dragAmountLabel;

    private Set<Slot> dragVisitedSlots = new HashSet<>();
    private Map<String, ImageIcon> iconCache = new HashMap<>();

    private int startX = 3;
    private int startY = 71;
    private int slotSize = 132;
    private int gapX = 0;
    private int gapY = 14;
    private int itemIconSize = 75;

    private int fixedWidth;
    private int fixedHeight;

    public InventoryPanel(Inventory inventory, GameManager gm) {
        this.inventory = inventory;
        this.gm = gm;
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();

        // [สำคัญ] ปิดระบบเลื่อนปุ่มด้วย Tab เพื่อให้เราใช้ปุ่ม Tab เป็นปุ่มกดในเกมได้
        setFocusTraversalKeysEnabled(false);

        String path = UiConfig.Path + "Inventory/BigInventory/FullInventory.PNG";
        File f = new File(path);
        if (!f.exists()) path = "Project3_6713120/resource/Inventory/BigInventory/FullInventory.PNG";

        ImageIcon icon = new ImageIcon(path);
        Image rawBg = icon.getImage();

        fixedWidth = 800;
        if (rawBg.getWidth(null) > 0) {
            fixedHeight = (rawBg.getHeight(null) * fixedWidth) / rawBg.getWidth(null);
        } else {
            fixedHeight = 600;
        }
        this.bgInventory = rawBg;

        setPreferredSize(new Dimension(fixedWidth, fixedHeight));
        setOpaque(false);

        iconLabels = new JLabel[inventory.rows][inventory.cols];
        amountLabels = new JLabel[inventory.rows][inventory.cols];
        setupDragLabels();

        for (int r = 0; r < inventory.rows; r++) {
            for (int c = 0; c < inventory.cols; c++) {
                int x = startX + (slotSize + gapX) * c;
                int y = startY + (slotSize + gapY) * r;

                JLabel imgLbl = new JLabel();
                imgLbl.setHorizontalAlignment(SwingConstants.CENTER);
                imgLbl.setVerticalAlignment(SwingConstants.CENTER);
                imgLbl.setBounds(x, y, slotSize, slotSize);
                add(imgLbl);

                JLabel numLbl = new JLabel();
                numLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                numLbl.setVerticalAlignment(SwingConstants.BOTTOM);
                numLbl.setForeground(Color.BLACK);
                numLbl.setFont(fonts.getPixelFont(20f));
                numLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 25));
                numLbl.setBounds(x, y, slotSize, slotSize);
                add(numLbl);

                setComponentZOrder(numLbl, 2);
                setComponentZOrder(imgLbl, 3);

                int dataRow = (r == inventory.rows - 1) ? 0 : r + 1;
                iconLabels[dataRow][c] = imgLbl;
                amountLabels[dataRow][c] = numLbl;
            }
        }

        setupEvents();
        setupCloseKeys();
        refreshInventory();
    }

    private void setupCloseKeys() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // [แก้ไขตรงนี้] เปลี่ยนจาก VK_E เป็น VK_TAB
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "closeInv");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeInv");

        // ลบ VK_E ออก (ถ้ามี) หรือไม่ต้องใส่

        am.put("closeInv", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (heldItem != null) {
                    returnHeldItemToInventory();
                }
                gm.toggleInventory(); // ปิดหน้าต่าง
            }
        });
    }

    private ImageIcon getCachedIcon(String itemName) {
        if (itemName == null) return null;
        if (iconCache.containsKey(itemName)) return iconCache.get(itemName);
        String path = getIconPath(itemName);
        Image img = ItemIconLoader.loadScaled(path, itemIconSize);
        ImageIcon icon = new ImageIcon(img);
        iconCache.put(itemName, icon);
        return icon;
    }

    private void setupDragLabels() {
        dragIconLabel = new JLabel();
        dragIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dragIconLabel.setVerticalAlignment(SwingConstants.CENTER);
        dragIconLabel.setBounds(0, 0, slotSize, slotSize);
        dragIconLabel.setVisible(false);
        add(dragIconLabel);

        dragAmountLabel = new JLabel();
        dragAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dragAmountLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        dragAmountLabel.setForeground(Color.BLACK);
        dragAmountLabel.setFont(fonts.getPixelFont(20f));
        dragAmountLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 25));
        dragAmountLabel.setBounds(0, 0, slotSize, slotSize);
        dragAmountLabel.setVisible(false);
        add(dragAmountLabel);

        setComponentZOrder(dragAmountLabel, 0);
        setComponentZOrder(dragIconLabel, 1);
    }

    private void setupEvents() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragVisitedSlots.clear();

                Slot clickedSlot = findSlotAt(e.getX(), e.getY());

                // คลิกนอกช่อง -> คืนของ/โยนทิ้ง
                if (clickedSlot == null && heldItem != null) {
                    returnHeldItemToInventory();
                }
                // คลิกโดนช่อง
                else if (clickedSlot != null) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (e.getClickCount() == 2) handleDoubleClick(clickedSlot);
                        else handleLeftClick(clickedSlot);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        handleRightClick(clickedSlot);
                        if (heldItem != null) dragVisitedSlots.add(clickedSlot);
                    }
                }

                refreshInventory();
                requestFocusInWindow();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                updateDragPosition(e.getX(), e.getY());

                // ลากขวา (Paint)
                if (SwingUtilities.isRightMouseButton(e) && heldItem != null) {
                    Slot currentSlot = findSlotAt(e.getX(), e.getY());
                    if (currentSlot != null && !dragVisitedSlots.contains(currentSlot)) {
                        boolean placed = performPlaceOne(currentSlot);
                        if (placed) {
                            dragVisitedSlots.add(currentSlot);
                            updateSingleSlotVisual(currentSlot);
                            refreshDragLabel();
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateDragPosition(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragVisitedSlots.clear();
            }
        };

        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Point p = getMousePosition();
                if (p != null) {
                    Slot slot = findSlotAt(p.x, p.y);
                    if (slot != null) handleHotbarKeys(e.getKeyCode(), slot);
                }
            }
        });
    }

    // --- Logic 1: คลิกซ้าย (Pick Up / Place All / Swap) ---
    private void handleLeftClick(Slot slot) {
        // มือว่าง -> หยิบ
        if (heldItem == null) {
            if (!slot.isEmpty()) {
                heldItem = slot.stack;
                slot.stack = null;
            }
        }
        // ถือของ -> วาง / รวม / สลับ
        else {
            if (slot.isEmpty()) {
                slot.stack = heldItem;
                heldItem = null;
            } else {
                if (slot.stack.item.getName().equals(heldItem.item.getName())) {
                    int space = 64 - slot.stack.count;
                    int toAdd = Math.min(space, heldItem.count);
                    slot.stack.count += toAdd;
                    heldItem.count -= toAdd;
                    if (heldItem.count <= 0) heldItem = null;
                } else {
                    ItemStack temp = slot.stack;
                    slot.stack = heldItem;
                    heldItem = temp;
                }
            }
        }
    }

    // --- Logic 2: คลิกขวา (Split / Place One) ---
    private void handleRightClick(Slot slot) {
        // มือว่าง -> หยิบครึ่ง
        if (heldItem == null) {
            if (!slot.isEmpty()) {
                int take = (int) Math.ceil(slot.stack.count / 2.0);
                heldItem = new ItemStack(slot.stack.item, take);
                slot.stack.count -= take;
                if (slot.stack.count <= 0) slot.stack = null;
            }
        }
        // ถือของ -> วาง 1 ชิ้น
        else {
            performPlaceOne(slot);
        }
    }

    private boolean performPlaceOne(Slot slot) {
        if (heldItem == null) return false;

        if (slot.isEmpty()) {
            slot.stack = new ItemStack(heldItem.item, 1);
            heldItem.count--;
            if (heldItem.count <= 0) heldItem = null;
            return true;
        } else if (slot.stack.item.getName().equals(heldItem.item.getName())) {
            if (slot.stack.count < 64) {
                slot.stack.count++;
                heldItem.count--;
                if (heldItem.count <= 0) heldItem = null;
                return true;
            }
        }
        return false;
    }

    private void returnHeldItemToInventory() {
        if (heldItem == null) return;
        boolean success = inventory.add(heldItem.item, heldItem.count);
        if (success) {
            heldItem = null;
        } else {
            dropItem(heldItem); // เต็มก็โยน
            heldItem = null;
        }
    }

    private void dropItem(ItemStack stack) {
        System.out.println("Drop: " + stack.item.getName() + " x" + stack.count);
    }

    private Slot findSlotAt(int x, int y) {
        for (int r = 0; r < inventory.rows; r++) {
            for (int c = 0; c < inventory.cols; c++) {
                int dataRow = (r == inventory.rows - 1) ? 0 : r + 1;
                if (iconLabels[dataRow][c].getBounds().contains(x, y)) {
                    return inventory.getSlot(dataRow, c);
                }
            }
        }
        return null;
    }

    private void updateSingleSlotVisual(Slot slot) {
        for (int r = 0; r < inventory.rows; r++) {
            for (int c = 0; c < inventory.cols; c++) {
                Slot s = inventory.getSlot(r, c);
                if (s == slot) {
                    JLabel imgLbl = iconLabels[r][c];
                    JLabel numLbl = amountLabels[r][c];

                    if (!slot.isEmpty()) {
                        imgLbl.setIcon(getCachedIcon(slot.stack.item.getName()));
                        numLbl.setText(String.valueOf(slot.stack.count));
                    } else {
                        imgLbl.setIcon(null);
                        numLbl.setText("");
                    }
                    return;
                }
            }
        }
    }

    private void updateDragPosition(int mouseX, int mouseY) {
        if (heldItem != null) {
            int x = mouseX - (slotSize / 2);
            int y = mouseY - (slotSize / 2);
            dragIconLabel.setLocation(x, y);
            dragAmountLabel.setLocation(x, y);

            if (!dragIconLabel.isVisible()) refreshDragLabel();
        } else {
            if (dragIconLabel.isVisible()) {
                dragIconLabel.setVisible(false);
                dragAmountLabel.setVisible(false);
            }
        }
    }

    private void handleHotbarKeys(int keyCode, Slot targetSlot) {
        int hotbarIndex = -1;
        if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_6) {
            hotbarIndex = keyCode - KeyEvent.VK_1;
        }
        if (hotbarIndex != -1) {
            Slot hotbarSlot = inventory.getSlot(0, hotbarIndex);
            ItemStack temp = targetSlot.stack;
            targetSlot.stack = hotbarSlot.stack;
            hotbarSlot.stack = temp;
            updateSingleSlotVisual(targetSlot);
            updateSingleSlotVisual(hotbarSlot);
        }
    }

    private void handleDoubleClick(Slot targetSlot) {
        if (heldItem == null && !targetSlot.isEmpty()) handleLeftClick(targetSlot);
        if (heldItem != null) {
            String targetName = heldItem.item.getName();
            for (int r = 0; r < inventory.rows; r++) {
                for (int c = 0; c < inventory.cols; c++) {
                    Slot s = inventory.getSlot(r, c);
                    if (!s.isEmpty() && s.stack.item.getName().equals(targetName) && s != targetSlot && s.stack.count < 64) {
                        int space = 64 - heldItem.count;
                        int toTake = Math.min(space, s.stack.count);
                        heldItem.count += toTake;
                        s.stack.count -= toTake;
                        if (s.stack.count <= 0) s.stack = null;
                        if (heldItem.count >= 64) return;
                    }
                }
            }
        }
    }

    public void refreshInventory() {
        for (int r = 0; r < inventory.rows; r++) {
            for (int c = 0; c < inventory.cols; c++) {
                Slot slot = inventory.getSlot(r, c);
                updateSingleSlotVisual(slot);
            }
        }
        refreshDragLabel();
    }

    private void refreshDragLabel() {
        if (heldItem != null && heldItem.count > 0) {
            dragIconLabel.setIcon(getCachedIcon(heldItem.item.getName()));
            dragAmountLabel.setText(String.valueOf(heldItem.count));
            dragIconLabel.setVisible(true);
            dragAmountLabel.setVisible(true);
        } else {
            dragIconLabel.setVisible(false);
            dragAmountLabel.setVisible(false);
        }
    }

    private String getIconPath(String itemName) {
        if (itemName == null) return UiConfig.Path + "Default.PNG";
        switch (itemName) {
            case "Watering Can":
                return UiConfig.wateringcanimg;
            case "Hoe":
                return UiConfig.hoeimg;
            case "Carrot Seeds":
                return UiConfig.carrotseed;
            case "Corn Seeds":
                return UiConfig.cornseeds;
            case "Potato Seeds":
                return UiConfig.potatoseed;
            case "Pumpkin Seeds":
                return UiConfig.pumkinseed;
            case "Blueberry Seeds":
                return UiConfig.blueberryseed;
            case "Cherry Seeds":
                return UiConfig.cherryseed;
            case "Melon Seeds":
                return UiConfig.melonseed;
            case "Strawberry Seeds":
                return UiConfig.strawberryseed;
            case "Carrot":
                return UiConfig.carrotimg;
            case "Corn":
                return UiConfig.Cornimg;
            case "Potato":
                return UiConfig.Potatoimg;
            case "Pumpkin":
                return UiConfig.pumknimg;
            case "Blueberry":
                return UiConfig.blueberryimg;
            case "Cherry":
                return UiConfig.cherryimg;
            case "Melon":
                return UiConfig.melonimg;
            case "Strawberry":
                return UiConfig.strawberryimg;
            case "Bean Seeds":
                return UiConfig.Beanseed;
            default:
                return UiConfig.Path + "Default.PNG";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgInventory != null) {
            g.drawImage(bgInventory, 0, 0, fixedWidth, fixedHeight, this);
        }
    }
}