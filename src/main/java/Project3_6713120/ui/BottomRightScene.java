package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.data.SaveData;

import javax.swing.*;
import java.awt.*;

public class BottomRightScene extends PlayableScene {
    SaveData save;
    private Image fenceForeground;
    private Image sandCastleImage;

    // ประกาศปุ่มทั้งแบบล็อคและไม่ล็อค
    private TransitionObject transLeft, transUp;
    private LockObject lockLeft, lockUp;

    @Override
    protected void paintComponent(Graphics g) {
        MyImageIcon bgImage = new MyImageIcon(UiConfig.bgimg).resize(UiConfig.framew, UiConfig.frameh);
        super.paintComponent(g);
        g.drawImage(bgImage.getImage(), 0, 0, null);
    }

    public BottomRightScene(GameManager gameManager) {
        super(gameManager, 3, 225, 190);
        setLayout(null);

        fenceForeground = new MyImageIcon(UiConfig.fenceBottomRightImg).resize(UiConfig.framew, UiConfig.frameh).getImage();
        sandCastleImage = new MyImageIcon(UiConfig.sandCastleImg).resize(UiConfig.framew, UiConfig.frameh).getImage();

        // --- HITBOXES ---
        addSolidObject(162, 175, 624, 20);
        addSolidObject(162, 465, 624, 20);
        addSolidObject(790, 175, 20, 310);
        addSolidObject(162, 175, 20, 40);
        addSolidObject(162, 310, 20, 175);
        addSolidObject(1085, 0, 281, 768);
        // Hitbox ปราสาททราย
        addSolidObject(1050, 350, 100, 60);

        // --- OBJECTS ---
        JLabel sandCastle = new JLabel(new MyImageIcon(UiConfig.sandCastleImg).resize(UiConfig.framew, UiConfig.frameh));
        sandCastle.setBounds(0, 0, UiConfig.framew, UiConfig.frameh);
        add(sandCastle);

        JLabel fence = new JLabel(new MyImageIcon(UiConfig.fenceBottomRightImg).resize(UiConfig.framew, UiConfig.frameh));
        fence.setBounds(0, 0, UiConfig.framew, UiConfig.frameh);
        add(fence);

        MyImageIcon beachImage = new MyImageIcon(UiConfig.BOTTOM_BEACH).resize(300, UiConfig.frameh);
        JLabel beach = new JLabel(beachImage);
        beach.setBounds(UiConfig.framew - 300, 0, 300, UiConfig.frameh);
        add(beach);

        updateLocks();
        setExits("RIGHT_SCENE", null, "BOTTOM_SCENE", null);
        save = gameManager.getCurrentSave();
    }

    public void updateLocks() {
        if (gm.getCurrentSave() == null) return;

        // ล้างปุ่มเก่าทิ้ง
        if (transLeft != null) {
            remove(transLeft);
            interactables.remove(transLeft);
            transLeft = null;
        }
        if (lockLeft != null) {
            remove(lockLeft);
            interactables.remove(lockLeft);
            lockLeft = null;
        }
        if (transUp != null) {
            remove(transUp);
            interactables.remove(transUp);
            transUp = null;
        }
        if (lockUp != null) {
            remove(lockUp);
            interactables.remove(lockUp);
            lockUp = null;
        }

        // 1. ทางซ้าย (ไป BottomScene)
        if (!gm.getCurrentSave().isBottomSceneUnlocked()) {
            // ถ้ายังไม่ปลด -> สร้างแม่กุญแจ (ราคา 1000 เท่ากับตอนซื้อจาก Main)
            lockLeft = new LockObject(gm, UiConfig.lockLeftImg, 0, 342, 1000, () -> {
                gm.getCurrentSave().setBottomSceneUnlocked(true);
                updateLocks();
            });
            add(lockLeft);
            interactables.add(lockLeft);
        } else {
            // ปลดแล้ว -> ลูกศรซ้าย
            // จุดเกิดที่ BottomScene = (1280, 342)
            transLeft = new TransitionObject(gm, UiConfig.arrowLeftImg, 0, 342, "BOTTOM_SCENE", 1190, 211);
            add(transLeft);
            interactables.add(transLeft);
        }

        // 2. ทางบน (ไป RightScene)
        if (!gm.getCurrentSave().isRightSceneUnlocked()) {
            // ถ้ายังไม่ปลด -> สร้างแม่กุญแจ (ราคา 500 เท่ากับตอนซื้อจาก Main)
            lockUp = new LockObject(gm, UiConfig.lockUpImg, 640, 0, 500, () -> {
                gm.getCurrentSave().setRightSceneUnlocked(true);
                updateLocks();
            });
            add(lockUp);
            interactables.add(lockUp);
        } else {
            // ปลดแล้ว -> ลูกศรบน
            // จุดเกิดที่ RightScene = (85, 683)
            transUp = new TransitionObject(gm, UiConfig.arrowUpImg, 640, 0, "RIGHT_SCENE", 0, 405);
            add(transUp);
            interactables.add(transUp);
        }

        repaint();
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2 = (Graphics2D) g;
        Shape originalClip = g2.getClip();
        int playerY = (playerLabel != null) ? (playerLabel.getY() + playerLabel.getHeight()) : 0;

        // ปราสาททรายบังคน
        if (sandCastleImage != null) {
            if (playerY < 380) {
                g2.setClip(1000, 250, 200, 200);
                g2.drawImage(sandCastleImage, 0, 0, null);
                g2.setClip(originalClip);
            }
        }

        // รั้วบังคน
        if (fenceForeground != null) {
            if (playerY < 175) {
                g2.setClip(0, 100, UiConfig.framew, 150);
                g2.drawImage(fenceForeground, 0, 0, null);
                g2.setClip(originalClip);
            }
            if (playerY < 465) {
                g2.setClip(0, 430, UiConfig.framew, 150);
                g2.drawImage(fenceForeground, 0, 0, null);
                g2.setClip(originalClip);
            }
        }

        // วาดปุ่มทับ (สำคัญมาก: ต้องสั่งวาดทุกปุ่มที่มีโอกาสแสดงผล)
        if (transLeft != null && transLeft.isVisible()) paintButtonOnTop(g, transLeft);
        if (lockLeft != null && lockLeft.isVisible()) paintButtonOnTop(g, lockLeft);

        if (transUp != null && transUp.isVisible()) paintButtonOnTop(g, transUp);
        if (lockUp != null && lockUp.isVisible()) paintButtonOnTop(g, lockUp);
    }

    private void paintButtonOnTop(Graphics g, JComponent c) {
        Graphics gComp = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
        c.paint(gComp);
        gComp.dispose();
    }
}