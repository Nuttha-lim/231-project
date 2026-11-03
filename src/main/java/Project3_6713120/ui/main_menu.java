/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;


import Project3_6713120.GameManager;
import Project3_6713120.MainApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

public class main_menu extends ScenePanel {
    private MyImageIcon bg, titlename;
    private MainApplication mainApplication;
    private GameManager man;
    private JFrame mainFrame;
    private SettingFrame settingFrame;
    private main_menu menu;
    private int startmoney = 30;
    private JLabel draw, titlefield;
    private int Framew = UiConfig.framew;
    private int Frameh = UiConfig.frameh;
    private int borderxbut = UiConfig.borderxbut;
    private int borderybut = UiConfig.borderybut;
    private ImageButton Newbutton, loadButton, creditButton, hopbutton, settingbutton, quitbutton;
    private ImageButton MusicButton;
    private String backimg = UiConfig.bgimg;
    private String[] buttonpath = {
            UiConfig.newbutimg, UiConfig.loadbutimg, UiConfig.creditbutimg,
            UiConfig.musicbutimg, UiConfig.settingbutimg, UiConfig.hopimg, UiConfig.quitimg, UiConfig.ofmusicbut
    };
    private JRadioButton on, off;
    private MySoundEffect theamsound;
    private String iconimg = UiConfig.iconimg;
    private int widthbut = UiConfig.widthbut;
    private int heightbut = UiConfig.heightbut;
    private int cubebut = 90;

    public main_menu(MainApplication parent, GameManager manager) {
        mainApplication = parent;
        mainFrame = parent.getframe();
        menu = this;
        man = manager;
        man.setStartmoney(startmoney);
        theamsound = mainApplication.getTheamsound();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Framew, Frameh));
        Image icon = Toolkit.getDefaultToolkit().getImage(iconimg);
        mainFrame.setTitle("CO Valley");
        mainFrame.setIconImage(icon);

        AddComponents();
        settingFrame = new SettingFrame(menu, man);
        settingFrame.setIconImage(new ImageIcon(iconimg).getImage());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC_PRESSED");

        getActionMap().put("ESC_PRESSED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingFrame.setVisible(true);
                settingFrame.setFocusable(true);
                mainFrame.setEnabled(false);
            }
        });
        on = settingFrame.geton();
        off = settingFrame.getoff();
    }

    public JFrame getsetting() {
        return settingFrame;
    }

    public main_menu getMenu() {
        return this;
    }

    public String[] getButtonpath() {
        return buttonpath;
    }

    public ImageButton getMusicButton() {
        return MusicButton;
    }

    public MySoundEffect getTheamsound() {
        return theamsound;
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public MainApplication getMain() {
        return mainApplication;
    }

    public void AddComponents() {
        bg = new MyImageIcon(backimg).resize(Framew, Frameh);
        titlename = new MyImageIcon(iconimg).resize(500, 250);
        draw = new JLabel();
        draw.setIcon(bg);
        titlefield = new JLabel();
        titlefield.setIcon(titlename);
        titlefield.setBounds(90, 0, 540, 280);
        draw.add(titlefield);
        draw.setLayout(null);
        draw.add(new ObjectLabel(UiConfig.bushimg, 69, 69, 830, 427));
        draw.add(new ObjectLabel(UiConfig.bushimg, 86, 86, 766, 389));
        draw.add(new ObjectLabel(UiConfig.houseimg, 606, 606, 744, 134));
        draw.add(new ObjectLabel(UiConfig.fenceimgmenu, 311, 291, 719, 326));
        draw.add(new ObjectLabel(UiConfig.treeimg, 250, 250, 410, 226));
        draw.add(new ObjectLabel(UiConfig.treeimg, 250, 250, 560, 114));

        MusicButton = new ImageButton(buttonpath[3], cubebut, cubebut);
        MusicButton.setBounds(Framew - 150, 50, cubebut + 30, cubebut + 30);
        MusicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!theamsound.isOnn()) {
                    MusicButton.setnewbg(new MyImageIcon(buttonpath[3]).getImage());
                    theamsound.playLoop();
                    on.setSelected(true);
                } else {
                    MusicButton.setnewbg(new MyImageIcon(buttonpath[7]).getImage());
                    theamsound.stop();
                    off.setSelected(true);
                }
            }
        });
        Newbutton = new ImageButton(buttonpath[0], widthbut, heightbut);
        Newbutton.setBounds(borderxbut, borderybut, widthbut + 30, 30 + heightbut);
        Newbutton.addActionListener(e -> {
            man.switchScene("NEW_GAME");
            man.setStartmoney(startmoney);
        });


        loadButton = new ImageButton(buttonpath[1], widthbut, heightbut);
        loadButton.setBounds(borderxbut, borderybut + 100, 30 + widthbut, 30 + heightbut);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //load Frame set name load

                man.switchScene("LOAD_GAME");
            }
        });

        creditButton = new ImageButton(buttonpath[2], widthbut, heightbut);
        creditButton.setBounds(borderxbut, borderybut + 200, widthbut + 30, 30 + heightbut);
        creditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //load Frame set name load

                man.switchScene("CREDIT_GAME");
            }
        });

        settingbutton = new ImageButton(buttonpath[4], cubebut, cubebut);
        settingbutton.setBounds(borderxbut + 110, borderybut + 300, cubebut + 30, 30 + cubebut);
        settingbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingFrame.setVisible(true);
                settingFrame.setFocusable(true);
                mainFrame.setEnabled(false);
            }
        });
        hopbutton = new ImageButton(buttonpath[5], cubebut, cubebut);
        hopbutton.setBounds(borderxbut, borderybut + 300, cubebut + 30, 30 + cubebut);
        hopbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //man.switchScene();
                HowToPlay how = new HowToPlay(menu);
                how.setIconImage(new ImageIcon(iconimg).getImage());
                how.setVisible(true);
                how.setFocusable(true);

                // ปิดการกด main frame ชั่วคราว เหมือน setting frame
                mainFrame.setEnabled(false);

            }
        });
        quitbutton = new ImageButton(buttonpath[6], cubebut, cubebut);
        quitbutton.setBounds(borderxbut + 220, borderybut + 300, 30 + cubebut, 30 + cubebut);
        quitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //load Frame set name load
                System.exit(0);
            }
        });

        draw.add(Newbutton);
        draw.add(loadButton);
        draw.add(creditButton);
        draw.add(MusicButton);
        draw.add(settingbutton);
        draw.add(hopbutton);
        draw.add(quitbutton);

        add(draw, BorderLayout.CENTER);
    }
}

class ImageRoundedButton extends JButton {
    private Image backgroundImage;
    private int radius = 25; // ความโค้งของขอบ

    public ImageRoundedButton(String text, Image image) {
        super(text);
        this.backgroundImage = image;
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public void setnewbg(Image image) {
        this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // สร้าง mask โค้งมน
        Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setClip(clip);

        // วาดรูปพื้นหลังพอดีกับปุ่ม
        if (backgroundImage != null) {
            int imgW = backgroundImage.getWidth(null);
            int imgH = backgroundImage.getHeight(null);
            int btnW = getWidth();
            int btnH = getHeight();

            // 🔸 คำนวณสเกลให้ภาพ "เต็มปุ่ม" โดยรักษาสัดส่วน
            double scale = Math.max((double) btnW / imgW, (double) btnH / imgH);

            int newW = (int) (imgW * scale);
            int newH = (int) (imgH * scale);

            // 🔸 ตัดส่วนเกินตรงกลางให้อยู่ในกรอบปุ่ม
            int x = (btnW - newW) / 2;
            int y = (btnH - newH) / 2;

            g2.drawImage(backgroundImage, x, y, newW, newH, this);
        }

        // ถ้ามี hover ให้ใส่ overlay โปร่งใส
        if (getModel().isRollover()) {
            g2.setColor(new Color(255, 255, 255, 60)); // สีขาวโปร่งใส
            g2.fill(clip);
        }

        // วาดขอบบาง ๆ ถ้าต้องการ
        g2.setColor(new Color(255, 255, 255, 80));
        g2.setStroke(new BasicStroke(2));
        g2.draw(clip);

        // วาดข้อความตรงกลาง
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() + fm.getAscent()) / 2 - 3;
        g2.setColor(Color.WHITE);
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}