/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul

package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.data.SaveData;
import Project3_6713120.world.Interactable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RightScene extends PlayableScene {
    private ArrayList<Interactable> interactables = new ArrayList<>();
    SaveData save;
    private Image foregroundImage;
    private TransitionObject transLeft, transDown;
    private LockObject lockDown;

    @Override
    protected void paintComponent(Graphics g) {
        MyImageIcon bgImage = new MyImageIcon(UiConfig.bgimg).resize(UiConfig.framew, UiConfig.frameh);
        super.paintComponent(g);
        g.drawImage(bgImage.getImage(), 0, 0, null);
    }

    public RightScene(GameManager gameManager) {
        super(gameManager, 1, 205, 390);
        setLayout(null);

        foregroundImage = new MyImageIcon(UiConfig.fenceRightImg).resize(UiConfig.framew, UiConfig.frameh).getImage();

        // --- HITBOXES ---
        addSolidObject(162, 360, 624, 20);
        addSolidObject(162, 670, 624, 20);
        addSolidObject(162, 360, 20, 60);
        addSolidObject(162, 520, 20, 150);
        addSolidObject(772, 360, 20, 307);
        addSolidObject(890, 0, 560, 222);
        addSolidObject(1085, 280, 281, 490);

        // --- OBJECTS ---
        int fenceW = UiConfig.framew;
        int fenceH = UiConfig.frameh;
        JLabel fence = new JLabel(new MyImageIcon(UiConfig.fenceRightImg).resize(fenceW, fenceH));
        fence.setBounds(0, 0, fenceW, fenceH);
        add(fence);

        MyImageIcon fisherImg = new MyImageIcon(UiConfig.FISHER_HOUSE_IMG).resize(400, 400);
        JLabel fisherHouse = new JLabel(fisherImg);
        fisherHouse.setBounds(900, -30, 400, 400);
        add(fisherHouse);

        MyImageIcon swimImg = new MyImageIcon(UiConfig.SWIMTUBE_IMG).resize(50, 50);
        JLabel swim = new JLabel(swimImg);
        swim.setBounds(UiConfig.framew - 225, 150, 50, 50);
        add(swim);

        MyImageIcon shellImage = new MyImageIcon(UiConfig.SHELL_IMG).resize(25, 25);
        JLabel shell1 = new JLabel(shellImage);
        shell1.setBounds(UiConfig.framew - 200, 300, 25, 25);
        add(shell1);

        JLabel shell2 = new JLabel(shellImage);
        shell2.setBounds(UiConfig.framew - 250, 600, 25, 25);
        add(shell2);

        MyImageIcon beachImage = new MyImageIcon(UiConfig.TOP_BEACH).resize(300, UiConfig.frameh);
        JLabel beach = new JLabel(beachImage);
        beach.setBounds(UiConfig.framew - 300, 0, 300, UiConfig.frameh);
        add(beach);

        updateLocks();
        setExits(null, "BOTTOM_RIGHT_SCENE", "MAIN_SCENE", null);
        save = gameManager.getCurrentSave();
    }

    public void updateLocks() {
        if (gm.getCurrentSave() == null) return;

        if (transLeft != null) {
            remove(transLeft);
            interactables.remove(transLeft);
            transLeft = null;
        }
        if (lockDown != null) {
            remove(lockDown);
            interactables.remove(lockDown);
            lockDown = null;
        }
        if (transDown != null) {
            remove(transDown);
            interactables.remove(transDown);
            transDown = null;
        }

        // 1. กลับ MainScene (ปุ่มซ้าย)
        // [แก้ไข] จุดเกิดที่ Main = (1281, 598) ตรงกับปุ่มขวาของ Main
        transLeft = new TransitionObject(gm, UiConfig.arrowLeftImg, 0, 598, "MAIN_SCENE", 302, 304);
        add(transLeft);
        interactables.add(transLeft);

        // 2. ไป BottomRightScene (ปุ่มล่าง)
        if (!gm.getCurrentSave().isBottomRightSceneUnlocked()) {
            lockDown = new LockObject(gm, UiConfig.lockDownImg, 85, 683, 1500, () -> {
                gm.getCurrentSave().setBottomRightSceneUnlocked(true);
                updateLocks();
            });
            add(lockDown);
            interactables.add(lockDown);
        } else {
            // [แก้ไข] จุดเกิดที่ BottomRight = (640, 0) ตรงกับปุ่มบนของ BottomRight
            transDown = new TransitionObject(gm, UiConfig.arrowDownImg, 85, 683, "BOTTOM_RIGHT_SCENE", 1, 190);
            add(transDown);
            interactables.add(transDown);
        }
        repaint();
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (foregroundImage == null) return;

        Graphics2D g2 = (Graphics2D) g;
        Shape originalClip = g2.getClip();
        int playerY = (playerLabel != null) ? (playerLabel.getY() + playerLabel.getHeight()) : 0;

        if (playerY < 380) {
            g2.setClip(0, 300, UiConfig.framew, 180);
            g2.drawImage(foregroundImage, 0, 0, null);
            g2.setClip(originalClip);
        }
        if (playerY < 690) {
            g2.setClip(0, 640, UiConfig.framew, 140);
            g2.drawImage(foregroundImage, 0, 0, null);
            g2.setClip(originalClip);
        }

        if (transLeft != null && transLeft.isVisible()) paintButtonOnTop(g, transLeft);
        if (lockDown != null && lockDown.isVisible()) paintButtonOnTop(g, lockDown);
        if (transDown != null && transDown.isVisible()) paintButtonOnTop(g, transDown);
    }

    private void paintButtonOnTop(Graphics g, JComponent c) {
        Graphics gComp = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
        c.paint(gComp);
        gComp.dispose();
    }
}