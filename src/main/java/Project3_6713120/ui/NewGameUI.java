/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.resource.fonts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NewGameUI extends ScenePanel {

    // -------------------------
    // IMAGES & CACHE
    // -------------------------
    private Image popupImg;
    private Image hairBarImg;
    private Image shirtBarImg;
    private Image pantsBarImg;
    // (ลบ okButtonImg, backButtonImg ออก เพราะ ImageButton จัดการเองแล้ว)

    private Map<String, BufferedImage> imageCache = new HashMap<>();

    // -------------------------
    // DATA
    // -------------------------
    private final String[] HAIR_FILES = {"BlackHair", "BrownHair", "LightBrownHair", "PinkHair", "PurpleHair"};
    private final String[] SHIRT_FILES = {"BlackTankTop", "BrownShirt", "Jacket", "Overall", "ScarletShirt"};
    private final String[] PANTS_FILES = {"BlackShorts", "BlueSkirt", "BrownShorts", "Jeans", "PinkSkirt"};

    // -------------------------
    // CONSTANTS (Positioning)
    // -------------------------
    private static final int POPUP_ORIG_SIZE = 2048;

    private static final Rectangle ORIG_FARM_FIELD = new Rectangle(570, 750, 770, 70);
    private static final Rectangle ORIG_NAME_FIELD = new Rectangle(570, 980, 770, 70);
    private static final Rectangle ORIG_PREVIEW = new Rectangle(125, 700, 320, 320);

    private static final Rectangle ORIG_ICON_HAIR = new Rectangle(150, 1160, 105, 187);
    private static final Rectangle ORIG_ICON_SHIRT = new Rectangle(300, 1160, 105, 187);
    private static final Rectangle ORIG_ICON_PANTS = new Rectangle(420, 1160, 105, 187);

    private static final int ORIG_BOX1_X = 130;
    private static final int ORIG_BOX_Y = 1293;
    private static final int ORIG_BOX_W = 291;
    private static final int ORIG_BOX_H = 291;
    private static final int ORIG_BOX_GAP = 80;

    private static final Rectangle ORIG_OK = new Rectangle(1574, 967, 225, 225);

    // -------------------------
    // STATE
    // -------------------------
    private int currentCategory = 0;
    private int selectedHair = 0;
    private int selectedShirt = 0;
    private int selectedSkin = 0;

    private int pressedSlotIndex = -1;
    private int hoveredSlotIndex = -1;

    // -------------------------
    // COMPONENTS
    // -------------------------
    private JTextField farmField;
    private JTextField nameField;

    // [แก้ไข] ใช้ ImageButton
    private ImageButton okButton;
    private ImageButton backButton;
    private JRadioButton min, mid, max;
    private int radiox = 850;
    private JButton hairTabBtn, shirtTabBtn, pantsTabBtn;
    private JButton[] boxButtons = new JButton[5];

    public NewGameUI(GameManager gm) {
        setLayout(null);

        // 1. โหลดภาพหลัก
        popupImg = new MyImageIcon(UiConfig.NEWFRAME_IMG).getImage();
        hairBarImg = new MyImageIcon(UiConfig.TAB_HAIR).getImage();
        shirtBarImg = new MyImageIcon(UiConfig.TAB_SHIRT).getImage();
        pantsBarImg = new MyImageIcon(UiConfig.TAB_PANTS).getImage();

        // 2. Pre-load
        preloadImages();

        // 3. Setup Components
        setupTextFields();
        setupButtons(gm);
        setupBackButton(gm);

        SwingUtilities.invokeLater(() -> farmField.requestFocusInWindow());
    }

    private void preloadImages() {
        loadImageToCache(UiConfig.PATH_BODY + "Front/Front.PNG");
        for (String s : HAIR_FILES) loadImageToCache(UiConfig.PATH_HAIR + "Front/" + s + ".PNG");
        for (String s : SHIRT_FILES) loadImageToCache(UiConfig.PATH_SHIRT + "Front/" + s + ".PNG");
        for (String s : PANTS_FILES) loadImageToCache(UiConfig.PATH_PANTS + "Front/" + s + ".PNG");
    }

    private void loadImageToCache(String fullPath) {
        try {
            File f = new File(fullPath);
            if (f.exists()) {
                BufferedImage img = ImageIO.read(f);
                imageCache.put(fullPath, img);
            }
        } catch (Exception e) {
        }
    }

    private BufferedImage getCachedImage(String fullPath) {
        return imageCache.get(fullPath);
    }

    private void setupTextFields() {
        farmField = new JTextField();
        farmField.setFont(new Font("Arial", Font.PLAIN, 16));
        farmField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(farmField);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(nameField);
    }

    private void setupButtons(GameManager gm) {
        // --- [แก้ไข] ใช้ ImageButton สำหรับ OK ---
        // ขนาด 60x60
        Icon modernIcon = new Modernradio();
        okButton = new ImageButton(UiConfig.OKBUTTON_IMG, 60, 60);
        okButton.addActionListener(e -> handleOkClick(gm));
        add(okButton);
        JLabel text = new JLabel("SET COIN");
        text.setFont(fonts.getPixelFont(25f));
        text.setBounds(radiox, 250, 250, 50);
        add(text);
        ButtonGroup startmoney = new ButtonGroup();
        min = new JRadioButton("30G");
        min.setFont(fonts.getPixelFont(10f));
        min.setBounds(radiox, 300, 70, 30);
        min.setOpaque(false);
        min.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                gm.setStartmoney(30);
            }
        });
        min.setIcon(modernIcon);
        min.setSelectedIcon(modernIcon);
        mid = new JRadioButton("40G");
        mid.setFont(fonts.getPixelFont(10f));
        mid.setBounds(radiox, 330, 70, 30);
        mid.setOpaque(false);
        mid.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                gm.setStartmoney(40);
            }
        });
        mid.setIcon(modernIcon);
        mid.setSelectedIcon(modernIcon);
        max = new JRadioButton("50G");
        max.setFont(fonts.getPixelFont(10f));
        max.setBounds(radiox, 360, 70, 30);
        max.setOpaque(false);
        max.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                gm.setStartmoney(50);
            }
        });
        max.setIcon(modernIcon);
        max.setSelectedIcon(modernIcon);
        add(min);
        add(mid);
        add(max);
        startmoney.add(min);
        startmoney.add(mid);
        startmoney.add(max);
        min.setSelected(true);
        // --- Tabs ---
        hairTabBtn = createTabButton(0);
        shirtTabBtn = createTabButton(1);
        pantsTabBtn = createTabButton(2);
        add(hairTabBtn);
        add(shirtTabBtn);
        add(pantsTabBtn);

        // --- 5 Slot Buttons ---
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            boxButtons[i] = new JButton();
            setupInvisibleButton(boxButtons[i]);

            boxButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hoveredSlotIndex = idx;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredSlotIndex = -1;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressedSlotIndex = idx;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressedSlotIndex = -1;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentCategory == 0) selectedHair = idx;
                    else if (currentCategory == 1) selectedShirt = idx;
                    else selectedSkin = idx;
                    repaint();
                }
            });
            add(boxButtons[i]);
        }
    }

    private JButton createTabButton(int categoryIndex) {
        JButton btn = new JButton();
        setupInvisibleButton(btn);
        btn.addActionListener(e -> {
            currentCategory = categoryIndex;
            repaint();
        });
        return btn;
    }

    private void setupInvisibleButton(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // [สำคัญ] ตั้งค่า Pixel Art
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        if (popupImg == null) return;

        int panelW = getWidth();
        int panelH = getHeight();

        double scaleX = (double) panelW / POPUP_ORIG_SIZE;
        double scaleY = (double) panelH / POPUP_ORIG_SIZE;
        double scale = Math.min(scaleX, scaleY);
        int scaledSize = (int) Math.round(POPUP_ORIG_SIZE * scale);
        int popupX = (panelW - scaledSize) / 2;
        int popupY = (panelH - scaledSize) / 2;

        // วาดพื้นหลัง
        g.drawImage(popupImg, popupX, popupY, scaledSize, scaledSize, null);

        // วาดตัวละคร
        drawCharacterPreview(g, popupX, popupY, scale);

        // วาด Tab Bar
        Image barImg = (currentCategory == 0) ? hairBarImg :
                (currentCategory == 1) ? shirtBarImg : pantsBarImg;
        g.drawImage(barImg, popupX, popupY, scaledSize, scaledSize, null);

        // วาดของในช่อง
        drawSelectionItems(g, popupX, popupY, scale);

        // (ไม่ต้องวาดปุ่ม OK/Back เองแล้ว ImageButton จัดการ)

        // อัปเดตตำแหน่ง Hitbox
        updateLayout(popupX, popupY, scale);
    }

    private void drawCharacterPreview(Graphics g, int popupX, int popupY, double scale) {
        int x = popupX + (int) Math.round(ORIG_PREVIEW.x * scale);
        int y = popupY + (int) Math.round(ORIG_PREVIEW.y * scale);
        int w = (int) Math.round(ORIG_PREVIEW.width * scale);
        int h = (int) Math.round(ORIG_PREVIEW.height * scale);

        drawCachedLayer(g, UiConfig.PATH_BODY + "Front/Front.PNG", x, y, w, h);

        if (selectedHair < HAIR_FILES.length) {
            String hairName = HAIR_FILES[selectedHair];
            drawCachedLayer(g, UiConfig.PATH_HAIR + "Front/" + hairName + "back.png", x, y, w, h);
        }

        if (selectedHair < HAIR_FILES.length)
            drawCachedLayer(g, UiConfig.PATH_HAIR + "Front/" + HAIR_FILES[selectedHair] + ".PNG", x, y, w, h);

        if (selectedSkin < PANTS_FILES.length)
            drawCachedLayer(g, UiConfig.PATH_PANTS + "Front/" + PANTS_FILES[selectedSkin] + ".PNG", x, y, w, h);

        if (selectedShirt < SHIRT_FILES.length)
            drawCachedLayer(g, UiConfig.PATH_SHIRT + "Front/" + SHIRT_FILES[selectedShirt] + ".PNG", x, y, w, h);
    }

    private void drawCachedLayer(Graphics g, String path, int x, int y, int w, int h) {
        BufferedImage img = getCachedImage(path);
        if (img == null) {
            if (path.endsWith(".PNG")) img = getCachedImage(path.replace(".PNG", ".png"));
            else if (path.endsWith(".png")) img = getCachedImage(path.replace(".png", ".PNG"));
        }
        if (img != null) g.drawImage(img, x, y, w, h, null);
    }

    private void drawSelectionItems(Graphics g, int popupX, int popupY, double scale) {
        String[] currentFiles = (currentCategory == 0) ? HAIR_FILES :
                (currentCategory == 1) ? SHIRT_FILES : PANTS_FILES;
        String pathPrefix = (currentCategory == 0) ? UiConfig.PATH_HAIR :
                (currentCategory == 1) ? UiConfig.PATH_SHIRT : UiConfig.PATH_PANTS;
        int selectedIdx = (currentCategory == 0) ? selectedHair :
                (currentCategory == 1) ? selectedShirt : selectedSkin;

        for (int i = 0; i < 5; i++) {
            if (i >= currentFiles.length) break;

            int boxX = ORIG_BOX1_X + i * (ORIG_BOX_W + ORIG_BOX_GAP);
            int finalX = popupX + (int) Math.round(boxX * scale);

            int currentYBase = ORIG_BOX_Y;
            if (i == pressedSlotIndex) {
                currentYBase += 15;
            }

            int finalY = popupY + (int) Math.round(currentYBase * scale);
            int finalW = (int) Math.round(ORIG_BOX_W * scale);
            int finalH = (int) Math.round(ORIG_BOX_H * scale);

            BufferedImage icon = getCachedImage(pathPrefix + "Front/" + currentFiles[i] + ".PNG");
            if (icon != null) {
                g.drawImage(icon, finalX, finalY, finalW, finalH, null);
            }

            if (i == selectedIdx) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 215, 0));
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(finalX, finalY, finalW, finalH);
            }
        }
    }

    private void updateLayout(int popupX, int popupY, double scale) {
        setScaledBounds(farmField, ORIG_FARM_FIELD, popupX, popupY, scale);
        setScaledBounds(nameField, ORIG_NAME_FIELD, popupX, popupY, scale);

        // --- [จัดการปุ่ม OK] ---
        int okW = (int) Math.round(ORIG_OK.width * scale);
        int okH = (int) Math.round(ORIG_OK.height * scale);

        // เยื้องขวาตามต้องการ (ปรับค่าได้ตามชอบ)
        int xOffset = (int) (0 * scale);
        int yOffset = (int) (-150 * scale);

        int okX = popupX + (int) Math.round(ORIG_OK.x * scale) + xOffset;
        int okY = popupY + (int) Math.round(ORIG_OK.y * scale) + yOffset;

        // เผื่อที่ให้ปุ่มขยาย (+20)
        okButton.setBounds(okX + 50, okY + 50, okW + 20, okH + 20);

        // --- [จัดการปุ่ม Back] ---
        if (backButton != null) {
            // ขนาด Hitbox 100x100 เผื่อขยาย
            backButton.setBounds(45, 650, 100, 100);
        }

        setScaledBounds(hairTabBtn, ORIG_ICON_HAIR, popupX, popupY, scale);
        setScaledBounds(shirtTabBtn, ORIG_ICON_SHIRT, popupX, popupY, scale);
        setScaledBounds(pantsTabBtn, ORIG_ICON_PANTS, popupX, popupY, scale);

        for (int i = 0; i < 5; i++) {
            Rectangle origBox = new Rectangle(
                    ORIG_BOX1_X + i * (ORIG_BOX_W + ORIG_BOX_GAP),
                    ORIG_BOX_Y,
                    ORIG_BOX_W,
                    ORIG_BOX_H
            );
            setScaledBounds(boxButtons[i], origBox, popupX, popupY, scale);
        }
    }

    private void setScaledBounds(JComponent c, Rectangle orig, int popupX, int popupY, double scale) {
        int x = popupX + (int) Math.round(orig.x * scale);
        int y = popupY + (int) Math.round(orig.y * scale);
        int w = (int) Math.round(orig.width * scale);
        int h = (int) Math.round(orig.height * scale);
        c.setBounds(x, y, w, h);
    }

    private void setupBackButton(GameManager gm) {
        // [แก้ไข] ใช้ ImageButton
        backButton = new ImageButton(UiConfig.Back, 70, 70);
        backButton.addActionListener(e -> gm.switchScene("MAIN_MENU"));
        add(backButton);
        setComponentZOrder(backButton, 0);
    }

    private void handleOkClick(GameManager gm) {
        String farmName = farmField.getText().trim();
        String playerName = nameField.getText().trim();

        if (farmName.isEmpty() || playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] slots = {"slot1", "slot2", "slot3"};
        String selectedSlot = (String) JOptionPane.showInputDialog(this, "Choose a save slot:", "Select Slot", JOptionPane.QUESTION_MESSAGE, null, slots, slots[0]);

        if (selectedSlot != null) {
            String hName = (selectedHair < HAIR_FILES.length) ? HAIR_FILES[selectedHair] : HAIR_FILES[0];
            String sName = (selectedShirt < SHIRT_FILES.length) ? SHIRT_FILES[selectedShirt] : SHIRT_FILES[0];
            String pName = (selectedSkin < PANTS_FILES.length) ? PANTS_FILES[selectedSkin] : PANTS_FILES[0];
            gm.startNewGame(selectedSlot, playerName, hName, sName, pName);
        }
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            resetUI();
        }
    }

    private void resetUI() {
        farmField.setText("");
        nameField.setText("");
        currentCategory = 0;
        selectedHair = 0;
        selectedShirt = 0;
        selectedSkin = 0;
        pressedSlotIndex = -1;
        hoveredSlotIndex = -1;
        repaint();
    }
}