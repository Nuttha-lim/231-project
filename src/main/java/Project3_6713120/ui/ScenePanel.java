/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.world.Interactable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class ScenePanel extends JPanel {
    protected ArrayList<Interactable> interactables = new ArrayList<>();
    protected String topExit;
    protected String bottomExit;
    protected String leftExit;
    protected String rightExit;

    public ScenePanel() {
        setLayout(null);
        setDoubleBuffered(true);
    }

    public void addObject(JComponent comp) {
        add(comp);
        if (comp instanceof Interactable) {
            interactables.add((Interactable) comp);
        }
    }

    public void escape(main_menu menu, GameManager man) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        MyImageIcon bgImage = new MyImageIcon(UiConfig.bgimg).resize(UiConfig.framew, UiConfig.frameh);
        g.drawImage(bgImage.getImage(), 0, 0, null);
    }

    public void escape() {

    }
}