/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.world.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public abstract class PlayableScene extends ScenePanel {
    protected String topExit, bottomExit, leftExit, rightExit;
    public TileLabel[][] tileLabels = new TileLabel[4][4];
    int tileX, tileY;
    protected PlayerLabel playerLabel;
    protected int plotIndex;
    // ใช้ ImageButton แทน JButton ธรรมดา
    protected ImageButton quit, home, setting;
    protected JLabel bgimg;
    protected JDialog dia;
    protected boolean tilesAdded = false;
    protected GameManager gm;
    // protected dialogborder dias;
    protected int diawidth = UiConfig.framew / 2;
    protected int diaheight = UiConfig.frameh / 2;
    protected ArrayList<Shape> solidObjects = new ArrayList<>();

    public PlayableScene(GameManager gm, int plotIndex, int tileX, int tileY) {
        this.gm = gm;
        this.plotIndex = plotIndex;
        this.setFocusTraversalKeysEnabled(false);
        this.setLayout(null);
        this.tileX = tileX;
        this.tileY = tileY;
        setupKeyBindings();
    }

    public void addSolidObject(int x, int y, int w, int h) {
        solidObjects.add(new Rectangle(x, y, w, h));
    }

    public void removeSolid(Shape x) {
        solidObjects.remove(x);
    }

    public void addTriangleHitbox(int x1, int y1, int x2, int y2, int x3, int y3) {
        // สร้าง Polygon จากจุด 3 จุด
        int[] xPoints = {x1, x2, x3};
        int[] yPoints = {y1, y2, y3};
        solidObjects.add(new Polygon(xPoints, yPoints, 3));
    }

    public ArrayList<Shape> getSolidObjects() {
        return solidObjects;
    }

    // [เพิ่ม] เมธอดสำหรับดึงกำแพงไปเช็ค
    /*@Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);

        // ใช้ Graphics2D เพื่อให้วาด Shape ได้
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(255, 0, 0, 100)); // สีแดงโปร่งแสง

        for (Shape shape : solidObjects) {
            g2.fill(shape); // ใช้ fill แทน fillRect
        }
    }*/


    public String getExit(Direction direction) {
        return switch (direction) {
            case UP -> topExit;
            case DOWN -> bottomExit;
            case LEFT -> leftExit;
            case RIGHT -> rightExit;
        };
    }

    public boolean isDialogActive() {
        return dia != null && dia.isVisible();
    }

    @Override
    public void escape(main_menu menu, GameManager man) {
        // 1. สร้าง Dialog
        dia = new JDialog(menu.getMainFrame(), "Options");

        dia.setIconImage(new ImageIcon(UiConfig.iconimg).getImage());

        int dW = 400;
        int dH = 550;

        dia.setSize(dW, dH);
        dia.setLocationRelativeTo(null);
        dia.setResizable(false);

        // 2. พื้นหลัง
        int bgW = dW - 15;
        int bgH = dH - 40;
        bgimg = new JLabel(new MyImageIcon(UiConfig.optionframe).resize(bgW, bgH));
        bgimg.setLayout(null);
        dia.add(bgimg);

        // --- คำนวณตำแหน่ง (ปรับจูนใหม่) ---
        int btnWidth = 280;
        int btnHeight = 85;


        int xOffset = -20;
        int centerX = ((bgW - btnWidth) / 2) + xOffset;


        int startY = 100;
        int gap = 25;

        // 3. ปุ่ม Home
        ImageButton home = new ImageButton(UiConfig.hommebutop, btnWidth, btnHeight);
        home.setBounds(centerX, startY, btnWidth + 40, btnHeight + 40);
        home.addActionListener(e -> {
            man.autoSave();
            man.stopGameThread();
            man.switchScene("MAIN_MENU");
            dia.dispose();
            if (man.getCoinSlot() != null) man.getCoinSlot().setVisible(false);
        });
        bgimg.add(home);

        // 4. ปุ่ม Quit
        int quitY = startY + btnHeight + gap;
        ImageButton quit = new ImageButton(UiConfig.quitbutop, btnWidth, btnHeight);
        quit.setBounds(centerX, quitY, btnWidth + 40, btnHeight + 40);
        quit.addActionListener(e -> {
            man.autoSave();
            man.stopGameThread();
            dia.dispose();
            if (man.getCoinSlot() != null) man.getCoinSlot().setVisible(false);
            System.exit(0);
        });
        bgimg.add(quit);

        // 5. ปุ่ม Setting
        int iconSize = 70;
        // คำนวณกึ่งกลางสำหรับปุ่มเล็ก + offset เดียวกัน
        int settingX = ((bgW - iconSize) / 2) + xOffset;
        int settingY = quitY + btnHeight + gap + 10;

        ImageButton setting = new ImageButton(UiConfig.settingbutimg, iconSize, iconSize);
        setting.setBounds(settingX, settingY, iconSize + 40, iconSize + 40);

        setting.addActionListener(e -> {


            if (menu.getsetting() != null) {
                JFrame settingFrame = menu.getsetting();
                settingFrame.setIconImage(new ImageIcon(UiConfig.iconimg).getImage());
                dia.setVisible(false);
                settingFrame.setVisible(true);

                java.awt.event.WindowAdapter closeListener = new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        restoreOption();
                    }

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        restoreOption();
                    }

                    @Override
                    public void windowDeactivated(java.awt.event.WindowEvent e) {
                        if (!settingFrame.isVisible()) restoreOption();
                    }

                    private void restoreOption() {
                        if (dia != null && !dia.isVisible()) {
                            dia.setVisible(true);
                        }
                        settingFrame.removeWindowListener(this);
                    }
                };
                settingFrame.addWindowListener(closeListener);
            }
        });
        bgimg.add(setting);

        // ESC Key Binding
        JRootPane root = dia.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CLOSE_DIA");
        root.getActionMap().put("CLOSE_DIA", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dia.dispose();
            }
        });

        dia.setVisible(true);
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // กด Tab เพื่อเปิด/ปิด Inventory
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "toggleInventory");

        am.put("toggleInventory", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gm.toggleInventory();
            }
        });
    }

    public void setExits(String top, String bottom, String left, String right) {
        this.topExit = top;
        this.bottomExit = bottom;
        this.leftExit = left;
        this.rightExit = right;
    }

    public int getPlotIndex() {
        return plotIndex;
    }

    public ArrayList<Interactable> getInteractables() {
        return interactables;
    }

    public void refreshPlayer() {
        if (playerLabel != null) {
            playerLabel.refresh();
        }
    }

    public void refreshPlots() {
        for (int i = 0; i < tileLabels.length; i++) {
            for (int j = 0; j < tileLabels[i].length; j++) {
                TileLabel label = tileLabels[i][j];
                label.refresh();
            }
        }
    }

    public void initializePlots() {
        Plot plot = gm.getCurrentSave().getPlots().get(plotIndex);

        for (int i = 0; i < tileLabels.length; i++) {
            for (int j = 0; j < tileLabels[i].length; j++) {
                Tile tile = plot.getTile(i, j);
                TileLabel label = tileLabels[i][j];
                label.setTile(tile);
                label.refresh();
            }
        }
    }

    public void addTileLabel(GameManager gm) {
        if (!tilesAdded) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Tile myTile = gm.getCurrentSave().getPlots().get(plotIndex).getTile(i, j);

                    // [แก้ไขตรงนี้] เปลี่ยนจาก 590, 390 เป็น tileX, tileY
                    TileLabel tileLabel = new TileLabel(tileX, tileY, i, j, gm, myTile);

                    add(tileLabel);
                    interactables.add(tileLabel);
                    tileLabels[i][j] = tileLabel;
                }
            }
            tilesAdded = true;
        }
    }

    public void addPlayerLabel(Player player) {
        if (playerLabel != null) {
            remove(playerLabel);
        }
        playerLabel = new PlayerLabel(player);
        add(playerLabel);
        if (getComponentCount() > 0) {
            try {
                this.setComponentZOrder(playerLabel, 0);
            } catch (Exception e) {
            }
        }
        revalidate();
        repaint();
    }

    public void checkBounds(Player player, GameManager gm) {
        Point position = player.getPosition();
        int pW = UiConfig.PERSON_WIDTH;
        int pH = UiConfig.PERSON_HEIGHT;
        int bounce = 10; // ระยะเด้งกลับ

        // =========================================================
        // กฎการเช็ค: จะไปฉากไหน ต้องเช็คว่าฉากนั้นปลดล็อคหรือยัง
        // =========================================================

        // --- 1. เดินขึ้นบน (UP) ---
        if (position.y < 0) {
            String exit = getExit(Direction.UP);
            if (exit != null) {
                // เช็ค: ถ้าจะไป RIGHT_SCENE (จาก BottomRight)
                if (exit.equals("RIGHT_SCENE") && !gm.getCurrentSave().isRightSceneUnlocked()) {
                    player.setPosition(position.x, 0 + bounce);
                    return; // ห้ามผ่าน
                }
                // เช็ค: ถ้าจะไป MAIN_SCENE (จาก Bottom) -> ปกติ Main เปิดตลอด แต่ใส่ไว้กันเหนียว

                player.setPosition(position.x, UiConfig.frameh - pH);
                gm.switchScene(exit);
            } else {
                player.setPosition(position.x, 0);
            }
        }

        // --- 2. เดินลงล่าง (DOWN) ---
        else if (position.y > UiConfig.frameh - pH) {
            String exit = getExit(Direction.DOWN);
            if (exit != null) {
                // เช็ค: ถ้าจะไป BOTTOM_SCENE (จาก Main)
                if (exit.equals("BOTTOM_SCENE") && !gm.getCurrentSave().isBottomSceneUnlocked()) {
                    player.setPosition(position.x, UiConfig.frameh - pH - bounce);
                    return;
                }
                // เช็ค: ถ้าจะไป BOTTOM_RIGHT_SCENE (จาก Right)
                if (exit.equals("BOTTOM_RIGHT_SCENE") && !gm.getCurrentSave().isBottomRightSceneUnlocked()) {
                    player.setPosition(position.x, UiConfig.frameh - pH - bounce);
                    return;
                }

                player.setPosition(position.x, 0);
                gm.switchScene(exit);
            } else {
                player.setPosition(position.x, UiConfig.frameh - pH);
            }
        }

        // --- 3. เดินซ้าย (LEFT) ---
        else if (position.x < 0) {
            String exit = getExit(Direction.LEFT);
            if (exit != null) {
                // เช็ค: ถ้าจะไป MAIN_SCENE (จาก Right) -> ปกติเปิดตลอด

                // [สำคัญ] เช็ค: ถ้าจะไป BOTTOM_SCENE (จาก BottomRight)
                // ต้องเช็คว่า BottomScene ปลดล็อคหรือยัง! (นี่คือจุดที่คุณเจอ)
                if (exit.equals("BOTTOM_SCENE") && !gm.getCurrentSave().isBottomSceneUnlocked()) {
                    player.setPosition(0 + bounce, position.y);
                    return; // เด้งกลับ
                }

                player.setPosition(UiConfig.framew - pW, position.y);
                gm.switchScene(exit);
            } else {
                player.setPosition(0, position.y);
            }
        }

        // --- 4. เดินขวา (RIGHT) ---
        else if (position.x > UiConfig.framew - pW) {
            String exit = getExit(Direction.RIGHT);
            if (exit != null) {
                // เช็ค: ถ้าจะไป RIGHT_SCENE (จาก Main)
                if (exit.equals("RIGHT_SCENE") && !gm.getCurrentSave().isRightSceneUnlocked()) {
                    player.setPosition(UiConfig.framew - pW - bounce, position.y);
                    return;
                }
                // เช็ค: ถ้าจะไป BOTTOM_RIGHT_SCENE (จาก Bottom)
                if (exit.equals("BOTTOM_RIGHT_SCENE") && !gm.getCurrentSave().isBottomRightSceneUnlocked()) {
                    player.setPosition(UiConfig.framew - pW - bounce, position.y);
                    return;
                }

                player.setPosition(0, position.y);
                gm.switchScene(exit);
            } else {
                player.setPosition(UiConfig.framew - pW, position.y);
            }
        }
    }

}