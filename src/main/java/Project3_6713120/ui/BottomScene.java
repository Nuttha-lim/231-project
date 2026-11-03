/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul

package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.data.SaveData;

import javax.swing.*;
import java.awt.*;

import static Project3_6713120.ui.UiConfig.TABLE_SET_IMG;

public class BottomScene extends PlayableScene {
    SaveData save;
    private Image fenceForeground;
    private Image tableImage;
    private TransitionObject transUp, transRight;
    private LockObject lockRight;

    @Override
    protected void paintComponent(Graphics g) {
        MyImageIcon bgImage = new MyImageIcon(UiConfig.bgimg).resize(UiConfig.framew, UiConfig.frameh);
        super.paintComponent(g);
        g.drawImage(bgImage.getImage(), 0, 0, null);
    }

    public BottomScene(GameManager gameManager) {
        super(gameManager, 2, 570, 190);
        setLayout(null);

        fenceForeground = new MyImageIcon(UiConfig.fenceBottomImg).resize(UiConfig.framew, UiConfig.frameh).getImage();
        tableImage = new MyImageIcon(TABLE_SET_IMG).resize(250, 250).getImage();

        // --- HITBOXES ---
        addSolidObject(520, 155, 624, 20);
        addSolidObject(520, 465, 624, 20);
        addSolidObject(1124, 156, 20, 60);
        addSolidObject(1124, 310, 20, 150);
        addSolidObject(500, 155, 20, 307);
        addSolidObject(225, 480, 120, 170);
        addTriangleHitbox(0, 0, 470, 0, 0, 605);
        addSolidObject(380, 0, 105, 105);
        addSolidObject(282, 308, 50, 50);
        addSolidObject(180, 250, 55, 130);
        addSolidObject(156, 320, 55, 130);

        // --- OBJECTS ---
        JLabel treeGroup = new JLabel(new MyImageIcon(UiConfig.treeGroupBottomImg).resize(UiConfig.framew, UiConfig.frameh));
        treeGroup.setBounds(0, 0, UiConfig.framew, UiConfig.frameh);
        add(treeGroup);

        JLabel fence = new JLabel(new MyImageIcon(UiConfig.fenceBottomImg).resize(UiConfig.framew, UiConfig.frameh));
        fence.setBounds(0, 0, UiConfig.framew, UiConfig.frameh);
        add(fence);

        JLabel table = new JLabel(new MyImageIcon(TABLE_SET_IMG).resize(250, 250));
        table.setBounds(150, 400, 250, 250);
        add(table);

        updateLocks();
        setExits("MAIN_SCENE", null, null, "BOTTOM_RIGHT_SCENE");
        save = gameManager.getCurrentSave();
    }

    public void updateLocks() {
        if (gm.getCurrentSave() == null) return;

        if (transUp != null) {
            remove(transUp);
            interactables.remove(transUp);
            transUp = null;
        }
        if (lockRight != null) {
            remove(lockRight);
            interactables.remove(lockRight);
            lockRight = null;
        }
        if (transRight != null) {
            remove(transRight);
            interactables.remove(transRight);
            transRight = null;
        }

        // 1. ปุ่มขึ้นบน (กลับ MainScene)
        // [แก้ไข] จุดเกิดที่ Main = (1196, 683) ตรงกับปุ่มล่างของ Main
        transUp = new TransitionObject(gm, UiConfig.arrowUpImg, 640, 0, "MAIN_SCENE", 302, 304);
        add(transUp);
        interactables.add(transUp);

        // 2. ปุ่มไปขวา (BottomRightScene)
        if (!gm.getCurrentSave().isBottomRightSceneUnlocked()) {
            lockRight = new LockObject(gm, UiConfig.lockRightImg, 1280, 342, 1500, () -> {
                gm.getCurrentSave().setBottomRightSceneUnlocked(true);
                updateLocks();
            });
            add(lockRight);
            interactables.add(lockRight);
        } else {
            // [แก้ไข] จุดเกิดที่ BottomRight = (0, 342) ตรงกับปุ่มซ้ายของ BottomRight
            transRight = new TransitionObject(gm, UiConfig.arrowRightImg, 1280, 342, "BOTTOM_RIGHT_SCENE", 1, 190);
            add(transRight);
            interactables.add(transRight);
        }
        repaint();
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2 = (Graphics2D) g;
        Shape originalClip = g2.getClip();
        int playerY = (playerLabel != null) ? (playerLabel.getY() + playerLabel.getHeight()) : 0;

        if (playerY < 580) g2.drawImage(tableImage, 150, 400, null);

        if (fenceForeground != null) {
            if (playerY < 175) {
                g2.setClip(0, 100, UiConfig.framew, 150);
                g2.drawImage(fenceForeground, 0, 0, null);
                g2.setClip(originalClip);
            }
            if (playerY < 465) {
                g2.setClip(0, 425, UiConfig.framew, 150);
                g2.drawImage(fenceForeground, 0, 0, null);
                g2.setClip(originalClip);
            }
        }

        if (transUp != null && transUp.isVisible()) paintButtonOnTop(g, transUp);
        if (lockRight != null && lockRight.isVisible()) paintButtonOnTop(g, lockRight);
        if (transRight != null && transRight.isVisible()) paintButtonOnTop(g, transRight);
    }

    private void paintButtonOnTop(Graphics g, JComponent c) {
        Graphics gComp = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
        c.paint(gComp);
        gComp.dispose();
    }
}