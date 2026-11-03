/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120;

import Project3_6713120.ui.MySoundEffect;
import Project3_6713120.ui.UiConfig;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApplication extends JFrame {
    public static void main(String[] args) {
        new MainApplication();
    }

    private GameManager gameManager;
    private MySoundEffect theamsound;
    private String soundpath = UiConfig.soundtheam;

    public MainApplication() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // 1. Create the GameManager
        theamsound = new MySoundEffect(soundpath);
        GameManager gameManager = new GameManager(this);

        // 2. Add it to the window
        add(gameManager);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameManager.autoSave();
                ;   // auto-save ก่อนปิดเกม
            }
        });
        // 3. Finalize window settings
        pack();
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
        theamsound.playLoop();
        theamsound.setVolume(0.2f);
    }

    public MySoundEffect getTheamsound() {
        return theamsound;
    }

    public JFrame getframe() {
        return this;
    }
}