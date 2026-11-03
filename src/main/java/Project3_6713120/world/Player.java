/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.world;

import Project3_6713120.inventory.Inventory;
import Project3_6713120.items.Item;
import Project3_6713120.ui.UiConfig;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private int x = 300;
    private int y = 300;
    private Direction direction = Direction.RIGHT;
    private String currentPlayableScene;
    private String name;
    private int money;
    private int speed = 10;

    // --- ระบบกระเป๋าใหม่ (Grid) ---
    private transient Inventory inventorySystem;

    // --- ระบบกระเป๋าเก่า (List) ---
    // เอากลับมาตามที่ขอ เพื่อให้โค้ดส่วนอื่นไม่ Error
    private final List<Item> inventory;

    private int selectedIndex;

    // Animation & Outfit
    private boolean isMoving = false;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private String hairName = "BlackHair";
    private String shirtName = "BlackTankTop";
    private String pantsName = "BlackShorts";

    public Player(String name) {
        this.name = name;
        this.money = 100;

        // [แก้ไข] คืนค่าให้ระบบเก่า (กัน NullPointerException)
        this.inventory = new ArrayList<>();

        this.selectedIndex = 0;
        currentPlayableScene = "MAIN_SCENE";
    }

    // ============================================================
    // จัดการ Inventory System (ระบบใหม่ Grid)
    // ============================================================
    public void setInventory(Inventory inventory) {
        this.inventorySystem = inventory;
    }

    // [แก้ไข] เปลี่ยนชื่อเพื่อไม่ให้ซ้ำกับ getInventory() ของ List
    public Inventory getInventorySystem() {
        return inventorySystem;
    }

    // ============================================================
    // จัดการ Item (Add/Remove)
    // ============================================================
    public void addItem(Item item) {
        // ให้ความสำคัญกับระบบใหม่ก่อน
        if (inventorySystem != null) {
            inventorySystem.add(item, 1);
        } else {
            // ถ้าไม่มีระบบใหม่ ให้ใส่ระบบเก่ากันเหนียว
            inventory.add(item);
        }
    }

    public boolean removeItem(Item item) {
        // ให้ความสำคัญกับระบบใหม่
        if (inventorySystem != null) {
            // เรียก remove(Item, amount) ที่เราเพิ่งเพิ่มใน Inventory.java
            return inventorySystem.remove(item, 1);
        }
        // ระบบเก่า (List)
        return inventory.remove(item);
    }

    public boolean removeItem(Item item, int amount) {
        // 1. ลองลบจากระบบ Inventory ใหม่ (Grid) ก่อน
        if (inventorySystem != null) {
            return inventorySystem.remove(item, amount);
        }

        // 2. (กันเหนียว) ถ้าไม่มีระบบใหม่ ให้ลบวนลูปจาก List เก่า
        // แต่ปกติควรจะมี inventorySystem เสมอถ้าแก้ GameManager ครบแล้ว
        boolean result = true;
        for (int i = 0; i < amount; i++) {
            // พยายามลบทีละชิ้น
            if (!inventory.remove(item)) {
                result = false; // ถ้าของไม่พอให้ลบ
            }
        }
        return result;
    }


    // ============================================================
    // จัดการ Inventory List (ระบบเก่า - เก็บไว้ตามคำขอ)
    // ============================================================
    public List<Item> getInventory() {
        return inventory;
    }

    public Item getSelectedItem() {
        // [Logic พิเศษ] พยายามดึงจากระบบใหม่ (แถวแรก/Hotbar) ก่อน
        if (inventorySystem != null) {
            // ตรวจสอบว่า index อยู่ในขอบเขตไหม
            if (selectedIndex >= 0 && selectedIndex < inventorySystem.cols) {
                var slot = inventorySystem.getSlot(0, selectedIndex);
                if (!slot.isEmpty()) {
                    return slot.stack.item;
                }
                return null;
            }
        }

        // ถ้าไม่มีระบบใหม่ ให้ใช้ระบบเก่า
        if (inventory.isEmpty()) return null;
        if (selectedIndex >= 0 && selectedIndex < inventory.size()) {
            return inventory.get(selectedIndex);
        }
        return null;
    }

    public void setSelectedIndex(int index) {
        // อัปเดต index ให้รองรับทั้งสองระบบ
        this.selectedIndex = index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void printInventory() {
        System.out.println("Inventory System Active: " + (inventorySystem != null));
        // ปริ้นระบบเก่าออกมาดู (ถ้ามีการใช้งาน)
        if (!inventory.isEmpty()) {
            System.out.println("Legacy List Inventory:");
            for (int i = 0; i < inventory.size(); i++) {
                Item item = inventory.get(i);
                String marker = (i == selectedIndex) ? " <==" : "";
                System.out.println((i + 1) + ". " + item + marker);
            }
        }
    }

    // ============================================================
    // Animation Logic
    // ============================================================
    public void updateAnimation() {
        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > 1) {
                spriteNum++;
                if (spriteNum > 4) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public void moveUp() {
        y -= speed;
        direction = Direction.UP;
        isMoving = true;
    }

    public void moveDown() {
        y += speed;
        direction = Direction.DOWN;
        isMoving = true;
    }

    public void moveLeft() {
        x -= speed;
        direction = Direction.LEFT;
        isMoving = true;
    }

    public void moveRight() {
        x += speed;
        direction = Direction.RIGHT;
        isMoving = true;
    }


    // ============================================================
    // Getters / Setters ทั่วไป
    // ============================================================
    public boolean isMoving() {
        return isMoving;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public String getHairName() {
        return hairName;
    }

    public String getShirtName() {
        return shirtName;
    }

    public String getPantsName() {
        return pantsName;
    }

    public void setOutfit(String hair, String shirt, String pants) {
        this.hairName = hair;
        this.shirtName = shirt;
        this.pantsName = pants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScene() {
        return currentPlayableScene;
    }

    public void setScene(String scene) {
        currentPlayableScene = scene;
    }

    public Direction getDirection() {
        return direction;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            return true;
        }
        return false;
    }

    public Point getFacingPoint() {
        int width = UiConfig.PERSON_WIDTH;
        int height = UiConfig.PERSON_HEIGHT;
        return switch (direction) {
            case UP -> new Point(x + width / 2, y + height / 3);
            case DOWN -> new Point(x + width / 2, y + height * 2 / 3);
            case LEFT -> new Point(x + width / 3, y + height / 2);
            case RIGHT -> new Point(x + width * 2 / 3, y + height / 2);
        };
    }

    public Rectangle getFutureHitbox(String moveDir) {
        int nextX = x;
        int nextY = y;

        // ขนาด Hitbox ของตัวละคร (เอาแค่ช่วงเท้า)
        // สมมติรูป 150x150 : ให้ Hitbox กว้าง 60 สูง 40 อยู่ตรงกลางล่าง
        int hitW = 60;
        int hitH = 40;
        int offsetX = (UiConfig.PERSON_WIDTH - hitW) / 2; // จัดกึ่งกลางแนวนอน
        int offsetY = UiConfig.PERSON_HEIGHT - hitH;      // จัดชิดล่าง (เท้า)

        switch (moveDir) {
            case "UP":
                nextY -= speed;
                break;
            case "DOWN":
                nextY += speed;
                break;
            case "LEFT":
                nextX -= speed;
                break;
            case "RIGHT":
                nextX += speed;
                break;
        }

        return new Rectangle(nextX + offsetX, nextY + offsetY, hitW, hitH);
    }

    // [เพิ่ม] Getter speed
    public int getSpeed() {
        return speed;
    }
}
