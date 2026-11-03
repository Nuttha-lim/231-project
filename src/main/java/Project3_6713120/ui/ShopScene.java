/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.data.SaveData;
import Project3_6713120.inventory.Inventory;
import Project3_6713120.inventory.InventoryPanel;
import Project3_6713120.items.Item;
import Project3_6713120.items.SeedItem;
import Project3_6713120.items.VegetableItem;
import Project3_6713120.resource.fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static Project3_6713120.data.GameData.ITEMS;

public class ShopScene extends ScenePanel {
    private GameManager man;
    private ScenePanel shop;
    private String[] img = {UiConfig.shoppimg, UiConfig.buybutimg, UiConfig.sellbutimg, UiConfig.coin, UiConfig.coinslot};
    private String[] sellboximg = {UiConfig.carrotimg, UiConfig.Cornimg, UiConfig.Potatoimg, UiConfig.pumknimg, UiConfig.blueberryimg, UiConfig.cherryimg, UiConfig.melonimg, UiConfig.strawberryimg, UiConfig.Beanimg};
    private String[] buyboximg = {UiConfig.carrotseed, UiConfig.cornseeds, UiConfig.potatoseed, UiConfig.pumkinseed, UiConfig.blueberryseed, UiConfig.cherryseed, UiConfig.melonseed, UiConfig.strawberryseed, UiConfig.Beanseed};
    private ImageButton addsell, addbuy;
    private JButton accept;
    private JPanel buysec, sellsec, resultsec;
    private JLabel carrottext, corntext, pottext, pumtext, blueberrytext, cherrytext, melontext, strawberrytext;
    private JLabel carrotseedtext, cornseedtext, potatoseedtext, pumkinseedtext, blueberryseedtext, cherryseedtext, melonseedtext, strawberryseedtext;
    private JLabel showbuy, showsell;
    private JLabel coinimg, buycointext, sellcointext, sumcointext;
    private int width = UiConfig.framew;
    private int height = UiConfig.frameh;
    private int sellsidex = width - 339;
    private int buysidex = 130;
    private int bothsidey = 180;
    private SaveData backup;
    private JLabel remainselltext;
    private Inventory inventory;
    private InventoryPanel panelInven;
    private JComboBox<VegetableItem> sell;
    private JComboBox<SeedItem> buy;
    private ArrayList<VegetableItem> sellList = new ArrayList();
    private ArrayList<SeedItem> buyList = new ArrayList();
    private ArrayList<greenlabel> allbuysection = new ArrayList();
    private ArrayList<greenlabel> allsellsection = new ArrayList();
    private Timer holdTimer;
    private int imgshowW = 200;
    private int imgshowH = 200;
    private int secx = 480;
    private int secW = 400;
    private int boxsize = 45;
    private int index1, index2;
    private coins coin;
    private boolean[] buyUnlocked;

    public ShopScene(GameManager gameManager) {
        // Set the preferred size (optional)
        shop = this;
        inventory = gameManager.getInventory();
        panelInven = gameManager.getInventoryPanel();
        for (Item item : ITEMS)
            if (item instanceof VegetableItem)
                sellList.add((VegetableItem) item);
        for (Item item : ITEMS)
            if (item instanceof SeedItem)
                buyList.add((SeedItem) item);
        setPreferredSize(new Dimension(UiConfig.framew, UiConfig.frameh));
        man = gameManager;
        coin = new coins();
        remainselltext = new JLabel();
        buycointext = new JLabel();
        sellcointext = new JLabel();
        sumcointext = new JLabel();
        coin.setbuysellside(buycointext, sellcointext, sumcointext);
        // Make sure it’s visible and focusable if needed
        setVisible(true);
        setFocusable(true);
        // Optionally, set a layout manager if you want to add buttons/labels later
        creategreenlabel();
        addcomponent();
        getInputMap(this.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC_PRESSED");

        getActionMap().put("ESC_PRESSED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                man.switchScene("MAIN_SCENE");
                reset();
                for (greenlabel g : allbuysection)
                    if (g.getvalid()) g.setvalid(false);
                for (greenlabel g : allsellsection)
                    if (g.getvalid()) g.setvalid(false);
            }
        });
    }


    public void creategreenlabel() {
        for (int i = 0; i < 9; i++) {
            allsellsection.add(new greenlabel(new MyImageIcon(sellboximg[i]).resize(boxsize, boxsize)));
            allbuysection.add(new greenlabel(new MyImageIcon(buyboximg[i]).resize(boxsize, boxsize)));
        }
        for (greenlabel i : allbuysection)
            i.setcoin(coin, true);
        for (greenlabel i : allsellsection) {
            i.setcoin(coin, false);
            i.setresult(remainselltext);
        }
        }

    public void updateremain() {
        remainselltext.setText("Remain: " + inventory.getTotalCount(sellList.get(index1)));
    }

    public void addcomponent() {
        accept = new JButton("accept");
        remainselltext.setText("Remain: " + inventory.getTotalCount(sellList.get(0)));
        remainselltext.setFont(fonts.getPixelFont(15f));
        remainselltext.setBounds(sellsidex, 450, 209, 50);
        showbuy = new JLabel(new MyImageIcon(buyboximg[0]).resize(imgshowW, imgshowH));
        showbuy.setBounds(buysidex, bothsidey, imgshowW, imgshowH);
        showsell = new JLabel(new MyImageIcon(sellboximg[0]).resize(imgshowW, imgshowH));
        showsell.setBounds(sellsidex, bothsidey, imgshowW, imgshowH);
        buysec = new JPanel();
        buysec.setOpaque(false);
        buysec.setBounds(secx, 382, secW, 220);
        sellsec = new JPanel();
        sellsec.setOpaque(false);
        sellsec.setBounds(secx, 160, secW, 210);
        resultsec = new JPanel();
        resultsec.setOpaque(false);
        resultsec.setLayout(null);
        resultsec.setBounds(secx, 610, secW, 130);
        sell = new JComboBox();

        buyUnlocked = new boolean[buyList.size()];
        for (VegetableItem a : sellList)
            sell.addItem(a);
        sell.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                index1 = sell.getSelectedIndex();
                remainselltext.setText("Remain: " + inventory.getTotalCount(sellList.get(index1)));
                showsell.setIcon(new MyImageIcon(sellboximg[index1]).resize(imgshowW, imgshowH));
            }
        });
        buy = new JComboBox();
        for (SeedItem a : buyList)
            buy.addItem(a);
        buy.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int idx = buy.getSelectedIndex();
                if (!buyUnlocked[idx])
                    buy.setSelectedIndex(0);
                index2 = buy.getSelectedIndex();
                showbuy.setIcon(new MyImageIcon(buyboximg[index2]).resize(imgshowW, imgshowH));
            }
        });
        buy.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (index >= 0 && index < buyUnlocked.length) {
                    c.setEnabled(buyUnlocked[index]);
                }
                return c;
            }
        });
        sell.setBounds(sellsidex, 400, 209, 50);
        buy.setBounds(buysidex, 400, 209, 50);
        addbuy = new ImageButton(img[1], 205, 70);
        addbuy.setBounds(buysidex - 20, height - 150, 240, 150);
        addbuy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (holdTimer != null && holdTimer.isRunning()) {
                    holdTimer.stop();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                addbuysec(index2);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (holdTimer != null && holdTimer.isRunning()) {
                    holdTimer.stop();
                }
                // Timer สำหรับยิงรัว ๆ ทุก 100 ms
                holdTimer = new Timer(100, ev -> {
                    addbuysec(index2);
                });
                holdTimer.start();
                buysec.revalidate();
                buysec.repaint();
            }
        });
        addsell = new ImageButton(img[2], 205, 70);
        addsell.setBounds(sellsidex - 20, height - 150, 240, 150);
        addsell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (holdTimer != null && holdTimer.isRunning()) {
                    holdTimer.stop();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                addsellsec(index1);
                sellsec.revalidate();
                sellsec.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (holdTimer != null && holdTimer.isRunning()) {
                    holdTimer.stop();
                }

                // Timer สำหรับยิงรัว ๆ ทุก 100 ms
                holdTimer = new Timer(100, ev -> {
                    addsellsec(index1);
                });
                holdTimer.start();
                sellsec.revalidate();
                sellsec.repaint();
            }
        });
        coinimg = new JLabel(new MyImageIcon(img[3]).resize(boxsize, boxsize));
        coinimg.setBounds(300, 50, boxsize, boxsize);
        buycointext.setText("Buy value: 0");
        buycointext.setFont(new Font("Arial", Font.PLAIN, 30));
        buycointext.setBounds(buysidex, height - 200, 300, 100);
        sellcointext.setText("Sell value: 0");
        sellcointext.setFont(new Font("Arial", Font.PLAIN, 30));
        sellcointext.setBounds(sellsidex, height - 200, 300, 100);
        sumcointext.setText("Get: 0");
        sumcointext.setFont(new Font("Arial", Font.PLAIN, 30));
        sumcointext.setBounds(100, 50, 200, 50);
        accept.setBounds(100, 100, 200, 20);
        accept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (coin.getispay()) {
                    if (coin.getResult() <= backup.getMoney()) {
                        backup.removemoney(coin.getResult());
                        JOptionPane.showMessageDialog(null, "Success Trade");
                        summary();
                        reset();
                    } else {
                        JOptionPane.showMessageDialog(null, "NO ENOUGH MONEY");
                    }
                } else {
                    backup.addmoney(coin.getResult());
                    JOptionPane.showMessageDialog(null, "Success Trade");
                    summary();
                    reset();
                    updateremain();
                }
                man.updateCoinUI();
            }
        });
        resultsec.add(coinimg);
        resultsec.add(sumcointext);
        resultsec.add(accept);
        shop.add(remainselltext);
        shop.add(buycointext);
        shop.add(sellcointext);
        shop.add(showbuy);
        shop.add(showsell);
        shop.add(buy);
        shop.add(sell);
        shop.add(sellsec);
        shop.add(buysec);
        shop.add(resultsec);
        shop.add(addsell);
        shop.add(addbuy);
        validate();
    }

    public void reset() {
        index1=0;
        buy.setSelectedIndex(index1);
        sell.setSelectedIndex(index1);
        sellsec.removeAll();
        sellsec.revalidate();
        sellsec.repaint();
        buysec.removeAll();
        buysec.revalidate();
        buysec.repaint();
        coin.setinitial();
        sumcointext.setText("Get: 0");
        buycointext.setText("Buy value: 0");
        sellcointext.setText("Sell value: 0");
    }

    public void summary() {
        if (buysec.getComponentCount() != 0)
            for (greenlabel g : allbuysection) {
                for (SeedItem v : buyList)
                    if (g.getvalid()) {
                        if (g.getname() == v.getName()) {
                            inventory.add(v, g.getCount());
                            panelInven.refreshInventory();
                            if (g.getname() == "Bean Seeds")
                                g.setOnlyone();
                            g.setvalid(false);
                        }
                    }
            }
        if (sellsec.getComponentCount() != 0)
            for (greenlabel g : allsellsection)
                for (VegetableItem v : sellList)
                    if (g.getvalid()) {
                        if (g.getname() == v.getName()) {
                            inventory.remove(v, g.getCount());
                            // ถ้าของที่ถูกขายคือ sellList index = 0
                            switch (v.getName()) {
                                case "Carrot":
                                    buyUnlocked[1] = true;
                                    backup.setBuyindex(1);
                                    break;
                                case "Corn":
                                    buyUnlocked[2] = true;
                                    backup.setBuyindex(2);
                                    break;
                                case "Potato":
                                    buyUnlocked[3] = true;
                                    backup.setBuyindex(3);
                                    break;
                                case "Pumpkin":
                                    buyUnlocked[4] = true;
                                    backup.setBuyindex(4);
                                    break;
                                case "Blueberry":
                                    buyUnlocked[5] = true;
                                    backup.setBuyindex(5);
                                    break;
                                case "Cherry":
                                    buyUnlocked[6] = true;
                                    backup.setBuyindex(6);
                                    break;
                                case "Melon":
                                    buyUnlocked[7] = true;
                                    backup.setBuyindex(7);
                                    break;
                                case "Strawberry":
                                    buyUnlocked[8] = true;
                                    backup.setBuyindex(8);
                                    break;

                            }
                            panelInven.refreshInventory();
                            g.setvalid(false);
                        }
                    }
    }

    public void setbackup(GameManager man) {
        reset();
        for (int i = 0; i <= 8; i++)
            buyUnlocked[i] = false;
        backup = man.getCurrentSave();
        for (int i = 0; i <= backup.getBuyindex(); i++)
            buyUnlocked[i] = true;
    }

    public void addbuysec(int select) {
        switch (select) {
            case 0:
                if (!allbuysection.get(select).getvalid())//carrot
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    carrotseedtext = new JLabel();
                    allbuysection.get(select).settexts(carrotseedtext);
                    carrotseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(carrotseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    carrotseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 1:
                if (!allbuysection.get(select).getvalid())//corn
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    cornseedtext = new JLabel();
                    allbuysection.get(select).settexts(cornseedtext);
                    cornseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(cornseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    cornseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 2:
                if (!allbuysection.get(select).getvalid())//potato
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    potatoseedtext = new JLabel();
                    allbuysection.get(select).settexts(potatoseedtext);
                    potatoseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(potatoseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    potatoseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 3:
                if (!allbuysection.get(select).getvalid())//pumkin
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    pumkinseedtext = new JLabel();
                    allbuysection.get(select).settexts(pumkinseedtext);
                    pumkinseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(pumkinseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    pumkinseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 4:
                if (!allbuysection.get(select).getvalid())//blueberry
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    blueberryseedtext = new JLabel();
                    allbuysection.get(select).settexts(blueberryseedtext);
                    blueberryseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(blueberryseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    blueberryseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 5:
                if (!allbuysection.get(select).getvalid())//cherry
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    cherryseedtext = new JLabel();
                    allbuysection.get(select).settexts(cherryseedtext);
                    cherryseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(cherryseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    cherryseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 6:
                if (!allbuysection.get(select).getvalid())//melon
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    melonseedtext = new JLabel();
                    allbuysection.get(select).settexts(melonseedtext);
                    melonseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(melonseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    melonseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 7:
                if (!allbuysection.get(select).getvalid())//strawberry
                {
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    strawberryseedtext = new JLabel();
                    allbuysection.get(select).settexts(strawberryseedtext);
                    strawberryseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(strawberryseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    allbuysection.get(select).up();
                    strawberryseedtext.setText("" + allbuysection.get(select).getCount());
                }
                coin.addbuycoin(buyList.get(index2).getPrice());
                break;
            case 8:
                if (!allbuysection.get(select).getvalid() && !allbuysection.get(select).isOnlyone())//bean
                {
                    coin.addbuycoin(buyList.get(index2).getPrice());
                    buysec.add(allbuysection.get(select));
                    allbuysection.get(select).setvalid(true);
                    allbuysection.get(select).setname(buyList.get(select).getName());
                    allbuysection.get(select).up();
                    strawberryseedtext = new JLabel();
                    allbuysection.get(select).settexts(strawberryseedtext);
                    strawberryseedtext.setText("" + allbuysection.get(select).getCount());
                    buysec.add(strawberryseedtext);
                    allbuysection.get(select).setPlace(buysec);
                    allbuysection.get(select).setprice(buyList.get(select).getPrice());
                } else {
                    if (holdTimer != null && holdTimer.isRunning()) {
                        holdTimer.stop();
                    }
                    JOptionPane.showMessageDialog(null, "CAN BUY ONLY ONE");
                }
                break;

        }
    }

    public void addsellsec(int select) {
        if (allsellsection.get(select).getCount() + 1 > inventory.getTotalCount(sellList.get(select))) {
            if (holdTimer != null && holdTimer.isRunning()) {
                holdTimer.stop();
            }
            JOptionPane.showMessageDialog(null, "Don't have enough " + sellList.get(select).getName() + " To sell");
            return;
        }
        coin.addsellcoin(sellList.get(index1).getPrice());
        switch (select) {
            case 0:
                if (!allsellsection.get(select).getvalid())//carrot
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    carrottext = new JLabel();
                    allsellsection.get(select).settexts(carrottext);
                    carrottext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(carrottext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    carrottext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 1:
                if (!allsellsection.get(select).getvalid())//corn
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    corntext = new JLabel();
                    allsellsection.get(select).settexts(corntext);
                    corntext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(corntext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    corntext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 2:
                if (!allsellsection.get(select).getvalid())//potato
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    pottext = new JLabel();
                    allsellsection.get(select).settexts(pottext);
                    pottext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(pottext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    pottext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 3:
                if (!allsellsection.get(select).getvalid())//pumkin
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    pumtext = new JLabel();
                    allsellsection.get(select).settexts(pumtext);
                    pumtext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(pumtext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    pumtext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 4:
                if (!allsellsection.get(select).getvalid())//blueberry
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    blueberrytext = new JLabel();
                    allsellsection.get(select).settexts(blueberrytext);
                    blueberrytext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(blueberrytext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    blueberrytext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 5:
                if (!allsellsection.get(select).getvalid())//cherry
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    cherrytext = new JLabel();
                    allsellsection.get(select).settexts(cherrytext);
                    cherrytext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(cherrytext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    cherrytext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 6:
                if (!allsellsection.get(select).getvalid())//cherry
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    melontext = new JLabel();
                    allsellsection.get(select).settexts(melontext);
                    melontext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(melontext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    melontext.setText("" + allsellsection.get(select).getCount());
                }
                break;
            case 7:
                if (!allsellsection.get(select).getvalid())//strawberry
                {
                    sellsec.add(allsellsection.get(select));
                    allsellsection.get(select).setname(sellList.get(select).getName());
                    allsellsection.get(select).setvalid(true);
                    allsellsection.get(select).up();
                    strawberrytext = new JLabel();
                    allsellsection.get(select).settexts(strawberrytext);
                    strawberrytext.setText("" + allsellsection.get(select).getCount());
                    sellsec.add(strawberrytext);
                    allsellsection.get(select).setPlace(sellsec);
                    allsellsection.get(select).setprice(sellList.get(select).getPrice());
                } else {
                    allsellsection.get(select).up();
                    strawberrytext.setText("" + allsellsection.get(select).getCount());
                }
                break;
        }
        allsellsection.get(select).setstock(inventory.getTotalCount(sellList.get(select)));
        remainselltext.setText("Remain: "+(inventory.getTotalCount(sellList.get(select))-allsellsection.get(select).getCount()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new MyImageIcon(img[0]).resize(width, height).getImage(), 0, 0, null);
    }
}

class greenlabel extends JLabel {
    private boolean added = false;
    private boolean onlyone = false;
    private int count;
    private coins coin;
    private Timer hold;
    private JLabel section;
    private JPanel place;
    private JLabel remainselltext;
    private int remain;
    private boolean isbuysec = true;
    private int price;
    private String name;

    public greenlabel(ImageIcon g) {
        super(g);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (hold != null && hold.isRunning()) {
                    hold.stop();
                }
                delete();
                section.setText("" + getCount());
                // Timer สำหรับยิงรัว ๆ ทุก 100 ms
                hold = new Timer(150, ev ->
                {
                    delete();
                    section.setText("" + getCount());

                });
                hold.start();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hold != null && hold.isRunning()) {
                    hold.stop();
                }
            }
        });
    }

    public boolean isOnlyone() {
        return onlyone;
    }

    public void setOnlyone() {
        onlyone = true;
    }

    public String getname() {
        return name;
    }

    public void setname(String n) {
        name = n;
    }

    public void setprice(int p) {
        price = p;
    }


    public void setcoin(coins p, boolean t) {
        coin = p;
        isbuysec = t;
    }

    public void setPlace(JPanel n) {
        place = n;
    }

    public void settexts(JLabel n) {
        section = n;
    }

    public int getCount() {
        return count;
    }

    public boolean getvalid() {
        return added;
    }

    public void setvalid(boolean t) {
        added = t;
        count = 0;
    }

    public void up() {
        count++;
    }
    public void setresult(JLabel m) {
        remainselltext = m;
    }
    public void setstock(int re){
        remain = re;
    }
    // โค้ดที่แก้ไขใน greenlabel.delete()
    public void delete() {
        if (count > 0) {
            // 2. ลบคอยน์ตามราคา
            if (isbuysec) coin.deletebuycoin(price);
            else coin.deletesellcoin(price);
            // 3. ลดค่า count (ต้องทำหลังจากลบคอยน์และอยู่ในเงื่อนไข)
            count--;

            // 4. อัปเดต remainselltext
            // Note: remainselltext ควรแสดงจำนวนคงเหลือใน Inventory (remain)
            // หลังจากการลบรายการนี้ (count ที่ลดลงแล้ว)
            remainselltext.setText("Remain: " + (remain - getCount()));

            // 5. หากจำนวนในตะกร้าขายเป็น 0 หรือน้อยกว่า
            if (count <= 0) {
                if (hold != null && hold.isRunning()) {
                    hold.stop();
                }
                // ลบองค์ประกอบ GUI ออก
                place.remove(this);
                place.remove(section);
                added = false;
                place.revalidate();
                place.repaint();
            }
        }
    }
}

class coins {
    private int buycoin, sellcoin, result;
    private JLabel buyside, sellside, resultside;
    private boolean isbuy = false;
    private boolean issell = false;
    private boolean ispay;
    private boolean sellmorebuy;

    public coins() {
    }

    public boolean getispay() {
        return ispay;
    }

    public void setbuysellside(JLabel b, JLabel s, JLabel res) {
        buyside = b;
        sellside = s;
        resultside = res;
    }

    public int getResult() {
        return result;
    }

    public void setinitial() {
        buycoin = 0;
        sellcoin = 0;
        result = 0;
    }

    public void checkbuy() {
        if (buycoin > 0) isbuy = true;
        else isbuy = false;
    }

    public void checksell() {
        if (sellcoin > 0) issell = true;
        else issell = false;
    }

    public int getBuycoin() {
        return buycoin;
    }

    public int getSellcoin() {
        return sellcoin;
    }

    public void checkResult() {
        if (buycoin > sellcoin) {
            result = buycoin - sellcoin;
            sellmorebuy = false;
        } else {
            result = sellcoin - buycoin;
            sellmorebuy = true;
        }
    }

    public void addbuycoin(int n) {
        buycoin += n;
        buyside.setText("Buy value: " + buycoin);
        checkbuy();
        changeresult();
    }

    public void addsellcoin(int n) {
        sellcoin += n;
        sellside.setText("Sell value: " + sellcoin);
        checksell();
        changeresult();
    }

    public void deletebuycoin(int n) {
        buycoin -= n;
        buyside.setText("Buy value: " + buycoin);
        checkbuy();
        changeresult();
    }

    public void deletesellcoin(int n) {
        sellcoin -= n;
        sellside.setText("Sell value: " + sellcoin);
        checkbuy();
        changeresult();
    }

    public void changeresult() {
        checkResult();
        if (isbuy && issell) {
            if (sellmorebuy) {
                resultside.setText("Get: " + result);
                ispay = false;
            } else {
                resultside.setText("Pay: " + result);
                ispay = true;
            }
        } else if (issell) {
            resultside.setText("Get: " + getSellcoin());
            ispay = false;
        } else if (isbuy) {
            resultside.setText("Pay: " + getBuycoin());
            ispay = true;
        } else resultside.setText("Pay: 0");
    }
}