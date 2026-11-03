/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.MainApplication;
import Project3_6713120.resource.fonts;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class SettingFrame extends JFrame {
    private SettingFrame here;
    private int width = UiConfig.framew / 2;
    private int height = UiConfig.frameh / 2;
    private JLabel setting, voice, headvolume;
    private JRadioButton on, off;
    private JPanel set;
    private JSlider volume;
    private main_menu menu;
    private MainApplication mainframe;
    private GameManager manage;
    private String[] butimgpath;
    private MySoundEffect theamsound;
    private ImageButton Musicbut;
    private MyImageIcon bgimg;
    private String bgpath = UiConfig.settingframe;

    public SettingFrame(main_menu previous, GameManager man) {
        menu = previous;
        here = this;
        setting = new JLabel();
        set = new JPanel();
        manage = man;
        mainframe = (MainApplication) previous.getMainFrame();
        theamsound = previous.getTheamsound();
        Musicbut = previous.getMusicButton();
        butimgpath = previous.getButtonpath();
        setResizable(false);
        setTitle("setting");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(mainframe);


        set.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC_PRESSED");

        set.getActionMap().put("ESC_PRESSED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.setEnabled(true);
                setVisible(false);
            }
        });
        add(set);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainframe.setEnabled(true);
            }
        });
        addcomponent();
        validate();
    }

    public JRadioButton geton() {
        return on;
    }

    public JRadioButton getoff() {
        return off;
    }

    public void addcomponent() {
        bgimg = new MyImageIcon(bgpath).resize(width - 13, height - 30);
        setting.setLayout(null);
        volume = new JSlider(1, 100, 50);
        voice = new JLabel("Value: " + volume.getValue());
        voice.setFont(fonts.getPixelFont(15f));
        // When the user drags or releases the knob
        volume.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = volume.getValue();
                voice.setText("Value: " + value);
                float state = value / 100f;
                float currentde = theamsound.getDesi();
                float change = state - currentde;
                float now = currentde + change;
                theamsound.setVolume(now);
            }
        });
        volume.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int sliderWidth = volume.getWidth();
                int value = (int) ((double) mouseX / sliderWidth *
                        (volume.getMaximum() - volume.getMinimum()));
                volume.setValue(volume.getMinimum() + value);
            }
        });
        Icon modernIcon = new Modernradio();
        volume.setOpaque(false);
        on = new JRadioButton("ON");
        on.setFont(fonts.getPixelFont(15f));
        on.setOpaque(false);
        on.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                theamsound.playLoop();
                Musicbut.setnewbg(new MyImageIcon(butimgpath[3]).getImage());
            }
        });
        on.setIcon(modernIcon);
        on.setSelectedIcon(modernIcon);
        off = new JRadioButton("OFF");
        off.setFont(fonts.getPixelFont(15f));
        off.setOpaque(false);
        off.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                theamsound.stop();
                Musicbut.setnewbg(new MyImageIcon(butimgpath[7]).getImage());
            }
        });
        off.setIcon(modernIcon);
        off.setSelectedIcon(modernIcon);
        ButtonGroup radio = new ButtonGroup();
        radio.add(on);
        radio.add(off);
        on.setSelected(true);
        headvolume = new JLabel("VOLUME:");

        headvolume.setFont(fonts.getPixelFont(36f));
        headvolume.setBounds(75, height - 240, 300, 50);
        volume.setBounds(width - 330, height - 220, 250, 10);
        voice.setBounds(width - 320, height - 220, 200, 50);
        on.setBounds(width - 320, height - 275, 100, 50);
        off.setBounds(width - 220, height - 275, 100, 50);
        setting.setIcon(bgimg);

        setting.add(headvolume);
        setting.add(on);
        setting.add(off);
        setting.add(volume);
        setting.add(voice);
        add(setting);
    }
}
