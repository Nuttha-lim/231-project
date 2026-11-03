/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul


package Project3_6713120.ui;

import Project3_6713120.GameManager;

import javax.swing.*;
import java.awt.*;

public class MainScene extends PlayableScene {

    private Image foregroundImage; // รูปสำหรับรั้ว
    private Image shopImage;       // รูปสำหรับร้านค้า
    private Image houseImage;      // รูปบ้าน (ใช้สำหรับตู้จดหมาย)
    private Image treeGroupForegroundImage; // รูปกลุ่มต้นไม้
    private groundforbean bean;
    // ประกาศตัวแปรปุ่ม (ต้องประกาศให้ครบ ทั้ง Lock และ Transition)
    private LockObject lockRight, lockDown;
    private TransitionObject transRight, transDown;

    public MainScene(GameManager gameManager) {
        super(gameManager, 0, 590, 390);
        setLayout(null);

        // --- Load Images & Hitboxes (เหมือนเดิม) ---
        foregroundImage = new MyImageIcon(UiConfig.fenceimg).resize(UiConfig.framew, UiConfig.frameh).getImage();
        shopImage = new MyImageIcon(UiConfig.VEG_STORE_IMG).resize(UiConfig.SHOP_WIDTH, UiConfig.SHOP_HEIGHT).getImage();
        treeGroupForegroundImage = new MyImageIcon(UiConfig.treeGroupImg).resize(UiConfig.framew, UiConfig.frameh).getImage();
        houseImage = new MyImageIcon(UiConfig.housemain).resize(1366, 768).getImage();

        addSolidObject(145, 86, 182, 128);
        addSolidObject(326, 86, 150, 175);
        addSolidObject(103, 150, 230, 162);
        addSolidObject(467, 237, 12, 31);  // ตู้จดหมาย

        addSolidObject(535, 360, 624, 20);  // รั้วบน
        addSolidObject(535, 670, 624, 20);  // รั้วล่าง
        addSolidObject(535, 370, 20, 60);   // รั้วซ้ายบน
        addSolidObject(535, 520, 20, 150);  // รั้วซ้ายล่าง
        addSolidObject(1170, 370, 20, 307); // รั้วขวา

        addSolidObject(690, 150, 180, 100); // ร้านค้า
        addSolidObject(1190, 140, 40, 30);  // โคนต้นไม้
        addTriangleHitbox(0, 253, 0, 768, 430, 768); // ป่ามุมซ้าย

        //บล็อกจำเพาะ
        bean = new groundforbean(gameManager);
        interactables.add(bean);
        add(bean);
        //

        // =================================================================
        // การวาง Object (Layering - Background)
        // =================================================================

        // 1. ของตกแต่ง
        addDecoration(UiConfig.treeimg, 1150, 20, 170, 170);
        addDecoration(UiConfig.treeimg, 1300, 60, 170, 170);

        ShopLabel shop = new ShopLabel(gameManager);
        shop.setBounds(680, 76, UiConfig.SHOP_WIDTH, UiConfig.SHOP_HEIGHT);
        add(shop);
        interactables.add(shop);

        int fenceW = 1366;
        int fenceH = 768;
        JLabel fence = new JLabel(new MyImageIcon(UiConfig.fenceimg).resize(fenceW, fenceH));
        fence.setBounds(0, 0, fenceW, fenceH);
        add(fence);

        int houseW = 1366;
        int houseH = 768;
        JLabel house = new JLabel(new MyImageIcon(UiConfig.housemain).resize(houseW, houseH));
        house.setBounds(0, 0, houseW, houseH);
        add(house);

        updateLocks();
        super.setExits(null, "BOTTOM_SCENE", null, "RIGHT_SCENE");
    }

    public groundforbean getBean() {
        return bean;
    }

    public void updateLocks() {
        if (gm.getCurrentSave() == null) return;

        if (lockRight != null) {
            remove(lockRight);
            interactables.remove(lockRight);
            lockRight = null;
        }
        if (lockDown != null) {
            remove(lockDown);
            interactables.remove(lockDown);
            lockDown = null;
        }
        if (transRight != null) {
            remove(transRight);
            interactables.remove(transRight);
            transRight = null;
        }
        if (transDown != null) {
            remove(transDown);
            interactables.remove(transDown);
            transDown = null;
        }

        // 1. ทางขวา (ไป RightScene)
        if (!gm.getCurrentSave().isRightSceneUnlocked()) {
            lockRight = new LockObject(gm, UiConfig.lockRightImg, 1281, 598, 500, () -> {
                gm.getCurrentSave().setRightSceneUnlocked(true);
                updateLocks();
            });
            add(lockRight);
            interactables.add(lockRight);
        } else {
            // [แก้ไข] จุดเกิดที่ RightScene = (0, 598) ตรงกับปุ่มซ้ายของฉากนั้น
            transRight = new TransitionObject(gm, UiConfig.arrowRightImg, 1281, 598, "RIGHT_SCENE", 0, 405);
            add(transRight);
            interactables.add(transRight);
        }

        // 2. ทางล่าง (ไป BottomScene)
        if (!gm.getCurrentSave().isBottomSceneUnlocked()) {
            lockDown = new LockObject(gm, UiConfig.lockDownImg, 1196, 683, 1000, () -> {
                gm.getCurrentSave().setBottomSceneUnlocked(true);
                updateLocks();
            });
            add(lockDown);
            interactables.add(lockDown);
        } else {
            // [แก้ไข] จุดเกิดที่ BottomScene = (640, 0) ตรงกับปุ่มบนของฉากนั้น
            transDown = new TransitionObject(gm, UiConfig.arrowDownImg, 1196, 683, "BOTTOM_SCENE", 1190, 211);
            add(transDown);
            interactables.add(transDown);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        MyImageIcon bgImage = new MyImageIcon(UiConfig.bgimg).resize(UiConfig.framew, UiConfig.frameh);
        super.paintComponent(g);
        g.drawImage(bgImage.getImage(), 0, 0, null);
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        Graphics2D g2 = (Graphics2D) g;
        Shape originalClip = g2.getClip();
        int playerY = (playerLabel != null) ? (playerLabel.getY() + playerLabel.getHeight()) : 0;

        if (playerY < 260) g2.drawImage(shopImage, 680, 76, null);

        if (playerY < 268) {
            g2.setClip(383, 50, 111, 215);
            g2.drawImage(houseImage, 0, 0, null);
            g2.setClip(originalClip);
        }

        if (foregroundImage != null) {
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
        }

        if (treeGroupForegroundImage != null) {
            g2.drawImage(treeGroupForegroundImage, 0, 0, null);
        }

        // วาดปุ่มทับ
        if (lockRight != null && lockRight.isVisible()) paintButtonOnTop(g, lockRight);
        if (transRight != null && transRight.isVisible()) paintButtonOnTop(g, transRight);
        if (lockDown != null && lockDown.isVisible()) paintButtonOnTop(g, lockDown);
        if (transDown != null && transDown.isVisible()) paintButtonOnTop(g, transDown);
    }

    private void paintButtonOnTop(Graphics g, JComponent c) {
        Graphics gComp = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
        c.paint(gComp);
        gComp.dispose();
    }

    private void addDecoration(String imgPath, int x, int y, int w, int h) {
        JLabel label = new JLabel(new MyImageIcon(imgPath).resize(w, h));
        label.setBounds(x, y, w, h);
        add(label);
    }
}