/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.inventory;

import Project3_6713120.resource.fonts;
import Project3_6713120.ui.UiConfig;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HotbarPanel extends JPanel {

    private Inventory inventory;
    private int selectedSlotIndex = 0;

    private Image[] barImages = new Image[6];

    private Map<String, Image> iconCache = new HashMap<>();

    // ขนาดรวมของแถบ Hotbar
    private int barWidth = 468;
    private int barHeight = 88;

    // --- [จุดกำหนดขนาดและตำแหน่งช่อง] ---
    private int startX = 24;   // ระยะห่างจากขอบซ้าย
    private int startY = 24;   // ระยะห่างจากขอบบน

    // [แก้ไข] คอมเมนต์ระบุขนาดช่อง
    private int slotSize = 40; // <--- ขนาดความกว้าง/สูง ของไอเทมใน 1 ช่อง (48x48 px)

    private int stride = 77;   // <--- ระยะห่างจากจุดเริ่มช่องหนึ่ง ไปยังจุดเริ่มช่องถัดไป (SlotSize + Gap)

    public HotbarPanel(Inventory inventory) {
        this.inventory = inventory;
        setOpaque(false);

        for (int i = 0; i < 6; i++) {
            String path = UiConfig.Path + "Inventory/Bar/AllSelected" + (i + 1) + ".PNG";
            barImages[i] = ItemIconLoader.load(path);
        }

        setPreferredSize(new Dimension(barWidth, barHeight));
    }

    private Image getCachedIcon(String itemName) {
        if (itemName == null) return null;

        if (iconCache.containsKey(itemName)) {
            return iconCache.get(itemName);
        }

        String path = getIconPath(itemName);
        Image img = new ImageIcon(ItemIconLoader.loadScaled(path, slotSize)).getImage();

        iconCache.put(itemName, img);
        return img;
    }

    public void setSelectedSlot(int index) {
        if (index < 0) index = 0;
        if (index > 5) index = 5;
        this.selectedSlotIndex = index;
        repaint();
    }

    public void scroll(int direction) {
        selectedSlotIndex += direction;
        if (selectedSlotIndex > 5) selectedSlotIndex = 0;
        if (selectedSlotIndex < 0) selectedSlotIndex = 5;
        repaint();
    }

    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }

    public ItemStack getHeldItem() {
        Slot slot = inventory.getSlot(0, selectedSlotIndex);
        if (!slot.isEmpty()) return slot.stack;
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // วาดพื้นหลัง
        if (barImages[selectedSlotIndex] != null) {
            g.drawImage(barImages[selectedSlotIndex], 0, 0, barWidth, barHeight, null);
        }

        // วาดไอเทม
        for (int i = 0; i < 6; i++) {
            Slot slot = inventory.getSlot(0, i);

            if (!slot.isEmpty()) {
                int x = startX + (stride * i);
                int y = startY;

                Image icon = getCachedIcon(slot.stack.item.getName());
                if (icon != null) {
                    g.drawImage(icon, x, y, null);
                }

                // --- [แก้ไข] วาดตัวเลข (สีดำ + โชว์เสมอแม้มี 1 ชิ้น) ---
                // ลบ if (slot.stack.count > 1) ออก เพื่อให้โชว์เลข 1 ด้วย

                g.setColor(Color.BLACK); // สีดำ
                g.setFont(fonts.getPixelFont(16f));

                // วาดเลขที่มุมขวาล่าง (ปรับพิกัด x+30, y+42 ได้ถ้าเลขไม่ตรง)
                g.drawString(String.valueOf(slot.stack.count), x + 36, y + 48);
            }
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
}