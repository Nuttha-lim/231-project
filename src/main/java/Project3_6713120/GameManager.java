/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120;

import Project3_6713120.data.GameData;
import Project3_6713120.data.GameSaveSystem;
import Project3_6713120.data.SaveData;
import Project3_6713120.inventory.HotbarPanel;
import Project3_6713120.inventory.Inventory;
import Project3_6713120.inventory.InventoryPanel;
import Project3_6713120.inventory.ItemStack;
import Project3_6713120.resource.fonts;
import Project3_6713120.ui.*;
import Project3_6713120.world.Interactable;
import Project3_6713120.world.Player;
import Project3_6713120.world.Plot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GameManager extends JPanel implements Runnable {

    public static final int SCREEN_WIDTH = 1366;
    public static final int SCREEN_HEIGHT = 768;

    private SaveData currentSave;
    private String currentSlot;
    private Thread gameThread;
    private final int FPS = 60;

    // --- ส่วนประกอบ UI แบบ Layer ---
    private JLayeredPane layeredPane; // ตัวจัดการชั้น
    private JPanel sceneContainer;    // ชั้นล่าง (เก็บฉากเกม CardLayout)
    private CardLayout cardLayout;
    private ShopScene shopScene;
    private Inventory inventory;
    private InventoryPanel inventoryPanel; // ชั้นบน
    private int startmoney; //เงินเริมต้น
    private MainScene MainScene;
    private HotbarPanel hotbarPanel;
    private main_menu mainMenu;
    private GameManager man;
    private JLabel coinSlot, coinText;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private Player player;
    private java.util.Timer autoSaveTimer;

    private boolean isGameplayActive() {
        ScenePanel current = getCurrentScene();

        // ถ้าฉากปัจจุบันไม่ใช่ PlayableScene (เช่น เป็น MainMenu, ShopScene, Credit) -> ห้ามยุ่งกับ Inventory
        if (!(current instanceof PlayableScene)) {
            return false;
        }

        // ถ้าเป็น PlayableScene แต่เปิด Dialog (ESC) ค้างไว้ -> ห้ามยุ่ง
        if (((PlayableScene) current).isDialogActive()) {
            return false;
        }

        return true;
    }


    public GameManager(MainApplication mainapp) {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        man = this;
        mainMenu = new main_menu(mainapp, this);
        // [สำคัญ 1] ปิดการย้าย Focus ด้วยปุ่ม TAB
        setFocusTraversalKeysEnabled(false);

        setLayout(new BorderLayout()); // ใช้ BorderLayout เพื่อวาง LayeredPane เต็มจอ

        // 1. สร้าง JLayeredPane
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // 2. สร้าง Scene Container (ชั้นล่างสุด - Gameplay)
        cardLayout = new CardLayout();
        sceneContainer = new JPanel(cardLayout);
        sceneContainer.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // 3. สร้าง Inventory (ชั้นบน - Popup)
        inventory = new Inventory(4, 6);
        inventoryPanel = new InventoryPanel(inventory, this);

        // จัดตำแหน่ง Inventory กึ่งกลางจอ (ขนาด 800x700 เพื่อให้เห็นครบทุกแถว)
        int invW = 800;
        int invH = 700;
        int invX = (SCREEN_WIDTH - invW) / 2;
        int invY = (SCREEN_HEIGHT - invH) / 2;
        inventoryPanel.setBounds(invX, invY, invW, invH);
        inventoryPanel.setVisible(false); // ซ่อนไว้ก่อน

        hotbarPanel = new HotbarPanel(inventory);
        int barW = 468;
        int barH = 88;
        int barX = (SCREEN_WIDTH - barW) / 2;
        int barY = SCREEN_HEIGHT - barH - 20;
        hotbarPanel.setBounds(barX, barY, barW, barH);
        hotbarPanel.setVisible(false);

        // 4. ประกอบร่าง Layer
        layeredPane.add(sceneContainer, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(hotbarPanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(inventoryPanel, JLayeredPane.POPUP_LAYER);
        shopScene = new ShopScene(this);
        //add(shopScene,BorderLayout.CENTER);
        add(layeredPane, BorderLayout.CENTER);
        MainScene = new MainScene(this);
        // --- Add Scenes ---
        sceneContainer.add(mainMenu, "MAIN_MENU");
        sceneContainer.add(new NewGameUI(this), "NEW_GAME");
        sceneContainer.add(new LoadGameUI(this), "LOAD_GAME");
        sceneContainer.add(new CreditFrame(this), "CREDIT_GAME");
        sceneContainer.add(MainScene, "MAIN_SCENE");
        sceneContainer.add(shopScene, "SHOP_SCENE");
        sceneContainer.add(new RightScene(this), "RIGHT_SCENE");
        sceneContainer.add(new BottomScene(this), "BOTTOM_SCENE");
        sceneContainer.add(new BottomRightScene(this), "BOTTOM_RIGHT_SCENE");

        // [สำคัญ 2] เรียกใช้ KeyBinding (TAB)
        setupGlobalKeys();
        setupCoinSlot();
        // --- Controls (KeyListener) ---
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // ถ้า Inventory เปิดอยู่ ไม่ต้องรับปุ่มเดิน/Hotbar ของ GameManager
                // ให้ InventoryPanel จัดการเรื่องของมันเอง (เว้นแต่ปุ่ม P/ESC ที่อยากให้ Global)
                if (inventoryPanel.isVisible()) {
                    // อนุญาตให้กด ESC เพื่อปิด Inventory ได้
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        toggleInventory();
                    }
                    return;
                }

                // [สำคัญ] เช็คว่าอยู่ใน Gameplay Mode หรือไม่
                // ถ้าไม่ได้เล่นอยู่ (เช่น อยู่ร้านค้า หรือ อยู่หน้า Setting) ห้ามกดเลข 1-6 หรือ เดิน
                if (!isGameplayActive()) {
                    // ยอมให้กด ESC เพื่อออกจาก Shop หรือ ปิด Dialog ได้
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        ScenePanel s = getCurrentScene();
                        if (s != null) s.escape(mainMenu, man);
                    }
                    return;
                }

                // --- พื้นที่ปลอดภัย: ตอนนี้อยู่ในฉากเล่นเกมปกติแล้ว ---

                // 1. Hotbar Selection
                if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_6) {
                    int slot = e.getKeyCode() - KeyEvent.VK_1;
                    hotbarPanel.setSelectedSlot(slot);
                }

                // 2. Movement & Interaction
                upPressed = downPressed = leftPressed = rightPressed = false;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        upPressed = true;
                        break;
                    case KeyEvent.VK_S:
                        downPressed = true;
                        break;
                    case KeyEvent.VK_A:
                        leftPressed = true;
                        break;
                    case KeyEvent.VK_D:
                        rightPressed = true;
                        break;
                    case KeyEvent.VK_E:
                        tryInteract();
                        break; // กด E เพื่อคุย/เปิด Shop

                    // Cheat
                  /*  case KeyEvent.VK_P:
                        if (currentSave != null) {
                            currentSave.addmoney(1000);
                            updateCoinUI();
                            System.out.println("Cheat: +1000 G");
                        }
                        break;*/

                    case KeyEvent.VK_ESCAPE:
                        ScenePanel s = getCurrentScene();
                        if (s != null) s.escape(mainMenu, man);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // ต้องเช็คด้วย ไม่งั้นเดินค้างถ้ากดปุ่มตอนเปิดเมนู
                if (!isGameplayActive()) {
                    upPressed = downPressed = leftPressed = rightPressed = false;
                    return;
                }

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        upPressed = false;
                        break;
                    case KeyEvent.VK_S:
                        downPressed = false;
                        break;
                    case KeyEvent.VK_A:
                        leftPressed = false;
                        break;
                    case KeyEvent.VK_D:
                        rightPressed = false;
                        break;
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // เพิ่มเงื่อนไข: ต้องไม่อยู่ใน Shop/Dialog ถึงจะเลื่อนเมาส์เปลี่ยนของได้
                if (!inventoryPanel.isVisible() && isGameplayActive()) {
                    hotbarPanel.scroll(e.getWheelRotation());
                }
            }
        });
    }

    public MainScene getMainScene() {
        return MainScene;
    }

    public void resetInputs() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        if (player != null) {
            player.setMoving(false);
        }
    }

    public ShopScene getShopScene() {
        return shopScene;
    }

    public void setupCoinSlot() {
        coinSlot = new JLabel(new MyImageIcon(UiConfig.coinslot).resize(230, 200));
        coinSlot.setBounds(1085, 15, 230, 200);
        coinSlot.setLayout(null);

        coinText = new JLabel("0");
        coinText.setFont(fonts.getPixelFont(15f));
        coinText.setBounds(100, 50, 150, 100);
        coinSlot.add(coinText);

        // ลอยบนสุดเหนือทุกซีน
        layeredPane.add(coinSlot, JLayeredPane.PALETTE_LAYER);

        coinSlot.setVisible(false);  // ซ่อนก่อน จะแสดงเมื่อเข้าเกม
    }

    public Player getPlayer() {
        return player;
    }

    public InventoryPanel getInventoryPanel() {
        return inventoryPanel;
    }

    public JLabel getCoinSlot() {
        return coinSlot;
    }

    public void updateCoinUI() {
        if (currentSave == null) return;
        coinText.setText("" + currentSave.getMoney());
    }

    public void setStartmoney(int c) {
        startmoney = c;
    }

    // --- ฟังก์ชันเปิด/ปิด Inventory ---
    public void toggleInventory() {
        boolean visible = !inventoryPanel.isVisible();
        inventoryPanel.setVisible(visible);
        hotbarPanel.setVisible(!visible);

        if (visible) {
            inventoryPanel.refreshInventory();
            inventoryPanel.requestFocusInWindow();
        } else {
            hotbarPanel.repaint();
            upPressed = downPressed = leftPressed = rightPressed = false;
            this.requestFocusInWindow();
        }
        repaint();
    }

    private void setupGlobalKeys() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "toggleInv");

        am.put("toggleInv", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // เงื่อนไข:
                // 1. ถ้า Inventory เปิดอยู่ -> ยอมให้กด Tab เพื่อปิดได้เสมอ
                // 2. ถ้า Inventory ปิดอยู่ -> ต้องอยู่ใน Gameplay Mode เท่านั้นถึงจะเปิดได้
                if (inventoryPanel.isVisible()) {
                    toggleInventory();
                } else if (isGameplayActive()) {
                    toggleInventory();
                }
            }
        });
    }

    public ItemStack getHeldItem() {
        return hotbarPanel.getHeldItem();
    }


    public void switchScene(String sceneName) {
        // แค่สั่งสลับหน้าจอ เดี๋ยว UI มันจะรู้ตัวและรีเฟรชตัวเองผ่าน setVisible
        cardLayout.show(sceneContainer, sceneName);

        ScenePanel scene = getCurrentScene();
        if (scene instanceof PlayableScene) {
            player.setScene(sceneName);
            ((PlayableScene) scene).addPlayerLabel(player);
            ((PlayableScene) scene).addTileLabel(this);
            ((PlayableScene) scene).refreshPlots();

            if (scene instanceof MainScene) {
                ((MainScene) scene).updateLocks();
            }
            if (scene instanceof RightScene) ((RightScene) scene).updateLocks();
            if (scene instanceof BottomScene) ((BottomScene) scene).updateLocks();
            if (scene instanceof BottomRightScene) ((BottomRightScene) scene).updateLocks();

        }

        if (sceneName.equals("MAIN_SCENE") || sceneName.equals("BOTTOM_SCENE") || sceneName.equals("RIGHT_SCENE") || sceneName.equals("BOTTOM_RIGHT_SCENE")) {
            if (!inventoryPanel.isVisible()) {
                hotbarPanel.setVisible(true);
            }
        } else {
            hotbarPanel.setVisible(false);
        }
    }

    public void tryInteract() {
        if (player == null) return;
        ScenePanel scene = getCurrentScene();
        if (scene == null) return;
        if (scene instanceof PlayableScene) {
            Point target = player.getFacingPoint();
            for (Interactable obj : ((PlayableScene) scene).getInteractables()) {
                if (obj instanceof JComponent comp && comp.getBounds().contains(target)) {
                    obj.interact(this);
                    break;
                }
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public SaveData getCurrentSave() {
        return currentSave;
    }

    public ScenePanel getCurrentScene() {
        for (Component comp : sceneContainer.getComponents()) {
            if (comp.isVisible() && comp instanceof ScenePanel scene) {
                return scene;
            }
        }
        return null;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        growthThread = new GrowthThread(this);
        gameThread.start();
        growthThread.start();
    }

    @Override
    public void run() {
        startGameLoop();
    }

    private Timer gameTimer;
    private GrowthThread growthThread;

    public void startGameLoop() {
        int drawInterval = 1000 / FPS; // milliseconds per frame
        gameTimer = new Timer(drawInterval, e -> {
            update();
            ScenePanel sc = getCurrentScene();
            if (sc != null) sc.repaint();
        });
        gameTimer.start();
    }

    public void startNewGame(String slotName, String playerName, String hairName, String shirtName, String pantsName) {
        currentSave = new SaveData(slotName, playerName);
        currentSlot = slotName;
        player = currentSave.getPlayer();
        player = currentSave.getPlayer();
        player.setOutfit(hairName, shirtName, pantsName);
        player.setInventory(inventory);
        clearInventory();
        inventory.add(GameData.HOE, 1);
        inventory.add(GameData.WATERING_CAN, 1);
        inventory.add(GameData.CARROT_SEEDS, 5);

        //inventory.add(GameData.BEAN_SEEDS,1);

        currentSave.setBeanGroundPlanted(false);
        currentSave.setBuyindex(0);
        getMainScene().getBean().setbeannstate(this);
        currentSave.setMoney(startmoney);
        shopScene.setbackup(this);

        // default 4 plots
        getCurrentSave().getPlots().add(new Plot(true, this));
        getCurrentSave().getPlots().add(new Plot(false, this));
        getCurrentSave().getPlots().add(new Plot(false, this));
        getCurrentSave().getPlots().add(new Plot(false, this));

        if (inventoryPanel != null) inventoryPanel.refreshInventory();
        coinSlot.setVisible(true);
        updateCoinUI();
        currentSave.setPlayer(player);

        GameSaveSystem.saveGame(slotName, currentSave);

        switchScene(player.getScene());
        ScenePanel scene = getCurrentScene();
        if (scene instanceof PlayableScene) {
            ((PlayableScene) scene).initializePlots();
        }
        startAutoSaveTimer();
        startGameThread();
    }

    public void loadGame(String slotName) {
        stopGameThread(); // stop old timers/threads

        SaveData loaded = GameSaveSystem.loadGame(slotName);
        if (loaded == null) {
            JOptionPane.showMessageDialog(this, "No save found for " + slotName);
            return;
        }

        currentSave = loaded;
        currentSlot = slotName;
        player = currentSave.getPlayer();
        player.setInventory(inventory);
        clearInventory();
        getMainScene().getBean().setbeannstate(this);
        ItemStack[][] savedItems = currentSave.getInventoryItems();
        if (savedItems != null) {
            for (int r = 0; r < inventory.rows; r++) {
                for (int c = 0; c < inventory.cols; c++) {
                    inventory.getSlot(r, c).stack = savedItems[r][c];
                }
            }
        }

        if (inventoryPanel != null) inventoryPanel.refreshInventory();
        shopScene.setbackup(this);
        coinSlot.setVisible(true);
        updateCoinUI();
        switchScene(player.getScene());

        ScenePanel scene = getCurrentScene();
        if (scene instanceof PlayableScene) {
            ((PlayableScene) scene).initializePlots();
        }


        startAutoSaveTimer();
        startGameThread();
    }

    public void deleteSlot(String slotName) {
        GameSaveSystem.deleteGame(slotName);
    }

    public void startAutoSaveTimer() {
        if (autoSaveTimer != null) autoSaveTimer.cancel();
        autoSaveTimer = new java.util.Timer(true);
        autoSaveTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                autoSave();
            }
        }, 30000, 30000);
    }

    public void autoSave() {
        saveGameToSlot();
    }

    private void saveGameToSlot() {
        if (currentSlot == null || currentSave == null || player == null) return;

        currentSave.setPlayer(player);
        currentSave.updateTimestamp();
        ItemStack[][] itemsToSave = new ItemStack[inventory.rows][inventory.cols];
        for (int r = 0; r < inventory.rows; r++) {
            for (int c = 0; c < inventory.cols; c++) {
                itemsToSave[r][c] = inventory.getSlot(r, c).stack;
            }
        }
        currentSave.setInventoryItems(itemsToSave);
        GameSaveSystem.saveGame(currentSlot, currentSave);
    }

    private void clearInventory() {
        if (inventory == null) return;
        for (int r = 0; r < inventory.rows; r++) {
            for (int c = 0; c < inventory.cols; c++) {
                inventory.getSlot(r, c).stack = null;
            }
        }
    }

    // ใน GameManager.java

    public void update() {
        if (player == null) return;

        player.setMoving(false);

        // --- ส่วนที่ 1: คำนวณการเดินและการชน (Collision) แบบแยกแกน (Sliding) ---
        if (!inventoryPanel.isVisible()) {
            ScenePanel currentScene = getCurrentScene();
            PlayableScene pScene = (currentScene instanceof PlayableScene) ? (PlayableScene) currentScene : null;

            // 1. เช็คแกนตั้ง (Y-Axis) : UP / DOWN
            if (upPressed || downPressed) {
                String dirY = upPressed ? "UP" : "DOWN";
                Rectangle futureY = player.getFutureHitbox(dirY);
                boolean collideY = false;

                if (pScene != null) {
                    // [แก้จุดที่ 1] เปลี่ยน Rectangle เป็น Shape
                    for (Shape wall : pScene.getSolidObjects()) {
                        // [แก้จุดที่ 2] ใช้ wall.intersects(futureY) แทน
                        // เพราะ Shape มีฟังก์ชันเช็คชนกับ Rectangle แต่ Rectangle ไม่มีฟังก์ชันเช็คชนกับ Shape
                        if (wall.intersects(futureY)) {
                            collideY = true;
                            break;
                        }
                    }
                }

                if (!collideY) {
                    if (upPressed) player.moveUp();
                    else player.moveDown();
                }
            }

            // 2. เช็คแกนนอน (X-Axis) : LEFT / RIGHT
            if (leftPressed || rightPressed) {
                String dirX = leftPressed ? "LEFT" : "RIGHT";
                Rectangle futureX = player.getFutureHitbox(dirX);
                boolean collideX = false;

                if (pScene != null) {
                    // [แก้จุดที่ 1] เปลี่ยน Rectangle เป็น Shape
                    for (Shape wall : pScene.getSolidObjects()) {
                        // [แก้จุดที่ 2] สลับเป็น wall.intersects(...)
                        if (wall.intersects(futureX)) {
                            collideX = true;
                            break;
                        }
                    }
                }

                if (!collideX) {
                    if (leftPressed) player.moveLeft();
                    else player.moveRight();
                }
            }
        }

        // --- ส่วนที่ 2: อัปเดตภาพและ Interaction ---
        ScenePanel scene = getCurrentScene();
        if (scene instanceof PlayableScene) {
            PlayableScene playableScene = (PlayableScene) scene;
            Point target = player.getFacingPoint();

            playableScene.checkBounds(player, this);

            for (Interactable obj : playableScene.getInteractables()) {
                if (obj instanceof JComponent comp) {
                    if (comp.getBounds().contains(target)) {
                        comp.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                    } else {
                        comp.setBorder(null);
                    }
                }
            }
            playableScene.refreshPlayer();
        }
    }

    public void stopGameThread() {
        if (gameThread != null) {
            gameThread.interrupt(); // or set a flag
            gameThread = null;
        }
        if (gameTimer != null) {
            gameTimer.stop();
            gameTimer = null;
        }
        if (growthThread != null) {
            growthThread.stopThread(); // safe stop
            growthThread = null;
        }
        if (autoSaveTimer != null) {
            autoSaveTimer.cancel();
            autoSaveTimer = null;
        }
    }
}

class GrowthThread extends Thread {
    private volatile boolean running = true;
    private final int interval = 1000; // ms
    private final GameManager gm;

    public GrowthThread(GameManager gm) {
        this.gm = gm;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                running = false;
                break;
            }

            // heavy logic off the EDT
            for (Plot plot : gm.getCurrentSave().getPlots()) {
                plot.growAll(gm);
            }

            // UI updates must go back to EDT
            SwingUtilities.invokeLater(() -> {
                ScenePanel scene = gm.getCurrentScene();
                if (scene instanceof PlayableScene) {
                    ((PlayableScene) scene).refreshPlots();
                }
            });
        }

    }


    public void stopThread() {
        running = false;
        this.interrupt();
    }
}
