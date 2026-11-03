/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.world.Direction;
import Project3_6713120.world.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerLabel extends JLabel {
    private final Player player;
    private final int width = UiConfig.PERSON_WIDTH; // 100
    private final int height = UiConfig.PERSON_HEIGHT; // 100

    // Cache เก็บรูปภาพเพื่อไม่ให้โหลดซ้ำจนแลค
    private static Map<String, BufferedImage> spriteCache = new HashMap<>();

    // เก็บสถานะล่าสุดเพื่อเช็คว่าต้องวาดใหม่ไหม
    private int lastSpriteNum = -1;
    private boolean lastIsMoving = false;
    private Direction lastDirection = null;
    private String lastHair = "", lastShirt = "", lastPants = "";

    public PlayerLabel(Player player) {
        this.player = player;
        setLayout(null);
        setSize(width, height);
        updatePlayerIcon();
    }

    public void refresh() {
        // 1. อัปเดตตำแหน่ง
        Point position = player.getPosition();
        setLocation(position.x, position.y);

        // 2. คำนวณ Animation
        player.updateAnimation();

        // 3. เช็คว่ามีการเปลี่ยนแปลงไหม (ถ้าไม่มีอะไรเปลี่ยน ไม่ต้องวาดใหม่ให้เปลืองเครื่อง)
        if (isStateChanged()) {
            updatePlayerIcon();
            saveLastState();
        }
        repaint();
    }

    private boolean isStateChanged() {
        return player.getSpriteNum() != lastSpriteNum ||
                player.isMoving() != lastIsMoving ||
                player.getDirection() != lastDirection ||
                !player.getHairName().equals(lastHair) ||
                !player.getShirtName().equals(lastShirt) ||
                !player.getPantsName().equals(lastPants);
    }

    private void saveLastState() {
        lastSpriteNum = player.getSpriteNum();
        lastIsMoving = player.isMoving();
        lastDirection = player.getDirection();
        lastHair = player.getHairName();
        lastShirt = player.getShirtName();
        lastPants = player.getPantsName();
    }

    // ============================================================
    // ระบบวาดตัวละคร (Composite System)
    // ============================================================
    private void updatePlayerIcon() {
        try {
            // สร้างภาพเปล่าๆ
            BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = combined.createGraphics();

            drawHairBack(g2, player.getHairName());
            drawBody(g2);

            if (player.getDirection() == Direction.UP) {
                // -------------------------------------------------
                // กรณี: หันหลัง (UP)
                // ลำดับ: ชุด -> ผม (เพื่อให้ผมบังลายเสื้อด้านหลัง)
                // -------------------------------------------------
                drawClothing(g2, UiConfig.PATH_PANTS, player.getPantsName());
                drawClothing(g2, UiConfig.PATH_SHIRT, player.getShirtName());
                drawHair(g2, player.getHairName()); // ผมวาดทีหลังสุด

            } else if (player.getDirection() == Direction.DOWN) {
                // -------------------------------------------------
                // กรณี: หันหน้า (DOWN) [ที่คุณต้องการแก้]
                // ลำดับ: ผม -> ชุด (เพื่อให้เสื้อ/กระโปรง ทับปลายผมที่ยาวลงมา)
                // -------------------------------------------------
                // วาดผมก่อน (หน้าม้าจะอยู่บนหัว แต่ปลายผมจะโดนชุดทับ)
                drawHair(g2, player.getHairName());

                // วาดชุดทีหลัง (จะทับปลายผม)
                drawClothing(g2, UiConfig.PATH_PANTS, player.getPantsName());
                drawClothing(g2, UiConfig.PATH_SHIRT, player.getShirtName());

            } else {
                // -------------------------------------------------
                // กรณี: หันข้าง (LEFT, RIGHT)
                // ปกติหันข้าง ผมควรจะบังไหล่เสื้อ -> ใช้แบบหันหลัง
                // -------------------------------------------------
                drawClothing(g2, UiConfig.PATH_PANTS, player.getPantsName());
                drawClothing(g2, UiConfig.PATH_SHIRT, player.getShirtName());
                drawHair(g2, player.getHairName());
            }

            g2.dispose();
            setIcon(new ImageIcon(combined));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawHairBack(Graphics2D g2, String hairName) {
        if (hairName == null || hairName.isEmpty()) return;

        // เช็คว่ามีไฟล์ ...back.png สำหรับทรงผมนี้หรือไม่
        // ไฟล์เหล่านี้อยู่ในโฟลเดอร์ Front เท่านั้นตามภาพที่คุณส่งมา
        String dirFolder = "Front";
        // สร้างชื่อไฟล์ เช่น "LightBrownHair" -> "LightBrownHairback"
        String backFileName = hairName + "back";

        // เราจะวาด HairBack เฉพาะตอนหันหน้า (DOWN) เท่านั้น
        if (player.getDirection() == Direction.DOWN) {
            // ลองโหลดไฟล์อย่างอัจฉริยะ (เช็คทั้ง .png และ .PNG)
            BufferedImage img = loadSmartImage(UiConfig.PATH_HAIR + dirFolder + "/", backFileName);

            if (img != null) {
                // ถ้ามีไฟล์ ก็วาดลงไปเลย เป็นเลเยอร์แรกสุด
                g2.drawImage(img, 0, 0, width, height, null);
            }
            // ถ้าไม่มีไฟล์ (img == null) ก็แค่วาดผ่านไป ไม่ต้องทำอะไร
        }
    }

    // --- Logic วาด Body (ไฟล์ Body ชื่อไม่เหมือนชาวบ้าน) ---
    private void drawBody(Graphics2D g2) {
        String dirFolder = "Front";
        String fileName = "Front";

        switch (player.getDirection()) {
            case UP:
                dirFolder = "Back";
                fileName = "Back";
                break;
            case DOWN:
                dirFolder = "Front";
                fileName = "Front";
                break;
            case LEFT:
                dirFolder = "Lside";
                fileName = "Left";
                break;
            case RIGHT:
                dirFolder = "Rside";
                fileName = "Right";
                break;
        }

        if (player.isMoving()) {
            fileName += "Walking" + player.getSpriteNum();
        }

        String fullPath = UiConfig.PATH_BODY + dirFolder + "/" + fileName;
        drawImage(g2, fullPath);
    }

    // --- Logic วาด เสื้อ/กางเกง (ซับซ้อนสุด เพราะมี LSide/RSide prefix) ---
    private void drawClothing(Graphics2D g2, String basePath, String itemName) {
        if (itemName == null || itemName.isEmpty()) return;

        String dirFolder = "Front";
        String filePrefix = "";   // คำนำหน้าไฟล์ เช่น LSide...
        String moveSuffix = "";   // คำลงท้ายตอนเดิน

        // 1. ตรวจสอบทิศทางและสร้างชื่อไฟล์
        switch (player.getDirection()) {
            case UP:
                dirFolder = "Back";
                if (player.isMoving()) {
                    moveSuffix = "_BackWalking" + player.getSpriteNum();
                } else {
                    // ถ้าหยุดเดิน ให้ลองใช้ Walking1 แทน (ถ้าไม่มีรูปท่ายืน)
                    moveSuffix = "_BackWalking1";
                }
                break;

            case DOWN:
                dirFolder = "Front";
                if (player.isMoving()) {
                    moveSuffix = "_FrontWalking" + player.getSpriteNum();
                } else {
                    // ด้านหน้ามักจะมีรูปยืนปกติอยู่แล้ว แต่กันเหนียวไว้
                    moveSuffix = "";
                }
                break;

            case LEFT:
                dirFolder = "LSide";
                filePrefix = "LSide";
                if (player.isMoving()) {
                    moveSuffix = "_LeftWalking" + player.getSpriteNum();
                } else {
                    // [จุดสำคัญ] ถ้าหยุดเดิน ให้ใช้ Walking1 แทนท่ายืน
                    moveSuffix = "_LeftWalking1";
                }
                break;

            case RIGHT:
                dirFolder = "Rside";
                filePrefix = "RSide";
                if (player.isMoving()) {
                    moveSuffix = "_RightWalking" + player.getSpriteNum();
                } else {
                    // [จุดสำคัญ] ถ้าหยุดเดิน ให้ใช้ Walking1 แทนท่ายืน
                    moveSuffix = "_RightWalking1";
                }
                break;
        }

        // 2. ลองโหลดรูป (แบบมี Suffix)
        String targetName = filePrefix + itemName + moveSuffix;
        BufferedImage img = loadSmartImage(basePath + dirFolder + "/", targetName);

        // 3. (Fallback Level 1) ถ้ายังไม่เจอ ลองตัด Suffix ออก (หาท่ายืนปกติ)
        // กรณีที่บางชุดอาจจะมีท่ายืนจริงๆ ก็ให้กลับมาใช้ท่ายืน
        if (img == null && !moveSuffix.isEmpty()) {
            String fallbackName = filePrefix + itemName;
            img = loadSmartImage(basePath + dirFolder + "/", fallbackName);
        }

        // 4. (Fallback Level 2) ถ้าเป็นด้านหน้า (DOWN) แล้วยังไม่เจอ
        // ให้ลองใช้ Walking1 แทน (บางทีชุดด้านหน้าก็ไม่มีท่ายืน)
        if (img == null && player.getDirection() == Direction.DOWN) {
            img = loadSmartImage(basePath + dirFolder + "/", itemName + "_FrontWalking1");
        }

        // 5. วาดรูป
        if (img != null) {
            g2.drawImage(img, 0, 0, width, height, null);
        }
    }

    // --- Logic วาดผม (Hair ไม่มี Walking Suffix ในรูปที่ให้มา) ---
    private void drawHair(Graphics2D g2, String hairName) {
        if (hairName == null || hairName.isEmpty()) return;

        String dirFolder = "Front";
        String finalName = hairName;
        boolean needsFlip = false; // ตัวแปรเช็คว่าต้องกลับด้านไหม

        switch (player.getDirection()) {
            case UP:
                dirFolder = "Back";
                finalName = "Back" + hairName;
                break;
            case DOWN:
                dirFolder = "Front";
                finalName = hairName;
                break;
            case LEFT:
                dirFolder = "Side";
                finalName = "Side" + hairName;
                break;
            case RIGHT:
                dirFolder = "Side";
                finalName = "Side" + hairName;
                needsFlip = true; // หันขวา ให้กลับด้านรูป
                break;
        }

        // โหลดรูปผม
        BufferedImage hairImg = loadSmartImage(UiConfig.PATH_HAIR + dirFolder + "/", finalName);

        if (hairImg != null) {
            if (needsFlip) {
                // ถ้าต้องกลับด้าน ให้ใช้ฟังก์ชัน flipImageHorizontally
                hairImg = flipImageHorizontally(hairImg);
            }
            // วาดรูปลงไป
            g2.drawImage(hairImg, 0, 0, width, height, null);
        }
    }

    // --- Helper Method: กลับด้านรูปแนวนอน ---
    private BufferedImage flipImageHorizontally(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    // ฟังก์ชันวาดรูปแบบง่าย (รู้ path เต็มๆ)
    private void drawImage(Graphics2D g2, String pathNoExt) {
        BufferedImage img = loadSmartImage("", pathNoExt);
        if (img != null) {
            g2.drawImage(img, 0, 0, width, height, null);
        }
    }

    // ฟังก์ชันโหลดรูปอัจฉริยะ (ลอง .PNG, .png และเช็ค Cache)
    private BufferedImage loadSmartImage(String folder, String fileName) {
        String fullPath = folder + fileName;

        // 1. เช็คใน Cache ก่อน
        if (spriteCache.containsKey(fullPath)) {
            return spriteCache.get(fullPath);
        }

        // 2. ลองโหลดไฟล์ (ลองทั้ง .PNG และ .png)
        BufferedImage img = tryLoad(fullPath + ".PNG");
        if (img == null) {
            img = tryLoad(fullPath + ".png");
        }

        // 3. ถ้าเจอ เก็บเข้า Cache
        if (img != null) {
            spriteCache.put(fullPath, img);
        }

        return img;
    }

    private BufferedImage tryLoad(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                return ImageIO.read(f);
            }
        } catch (Exception e) {
            // ไม่ต้องปริ้น Error ถ้าหาไม่เจอ เพราะเรามีระบบ Fallback รองรับ
        }
        return null;
    }
}