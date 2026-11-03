/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.MainApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HowToPlay extends JFrame {

    private HowToPlay here;
    private JLabel bgpanel;
    private ImageIcon howtoplay;
    private String bgpath = UiConfig.HTP;
    private JTextArea textArea;
    private MainApplication mainframe;

    public HowToPlay(main_menu previous) {
        here = this;
        mainframe = (MainApplication) previous.getMainFrame();
        setResizable(false);
        setTitle("How To Play");
        setIconImage(new ImageIcon(UiConfig.iconimg).getImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Key Listener (ESC)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    mainframe.setEnabled(true);
                    dispose();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainframe.setEnabled(true);
            }
        });

        addComponent();

        pack();

        setLocationRelativeTo(mainframe);
    }

    private void addComponent() {
        setLayout(new BorderLayout());

        howtoplay = new ImageIcon(bgpath);

        bgpanel = new JLabel();
        bgpanel.setIcon(howtoplay);

        add(bgpanel, BorderLayout.CENTER);
    }
}