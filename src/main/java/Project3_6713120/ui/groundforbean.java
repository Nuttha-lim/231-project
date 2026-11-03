/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.inventory.ItemStack;
import Project3_6713120.world.Interactable;

import javax.swing.*;
import java.awt.*;

public class groundforbean extends JLabel implements Interactable {
    private GameManager man;
    private final int width = 95;
    private final int height = 76;
    private ImageIcon img;
    private ImageIcon bean;
    private Rectangle x = new Rectangle(900, 0, 217, 289);
    private Rectangle y = new Rectangle(900, 215, 96, 77);
    private String cupimg = UiConfig.cup;
    private String beangif = UiConfig.fullbeangif;

    public groundforbean(GameManager mane) {
        man = mane;
        img = new MyImageIcon(cupimg).resize(width, height);
        bean = new MyImageIcon(beangif);
        setIcon(img);
        setSize(width, height);
    }

    public void setbeannstate(GameManager man) {
        if (man.getCurrentSave().isBeanGroundPlanted()) {
            man.getMainScene().removeSolid(y);
            man.getMainScene().addSolidObject(900, 0, 217, 289);
            Image scaled = bean.getImage().getScaledInstance(216, 288, Image.SCALE_DEFAULT);
            ImageIcon scaledGif = new ImageIcon(scaled);
            setBounds(900, 0, 216, 288);
            setSize(216, 288);
            setIcon(scaledGif);
        } else {
            man.getMainScene().removeSolid(x);
            man.getMainScene().addSolidObject(960, 215, 96, 77);
            setBounds(960, 215, 95, 76);
            setIcon(img);
            setSize(width, height);
        }
    }

    @Override
    public void interact(GameManager gm) {
        ItemStack selected = gm.getHeldItem();
        if (selected != null) {
            if (selected.item.getName().equals("Bean Seeds")) {
                gm.getMainScene().removeSolid(y);
                Image scaled = bean.getImage().getScaledInstance(216, 288, Image.SCALE_DEFAULT);
                ImageIcon scaledGif = new ImageIcon(scaled);
                setBounds(900, 0, 216, 288);
                setSize(216, 288);
                setIcon(scaledGif);
                repaint();
                gm.getInventory().remove(selected.item);
                gm.getCurrentSave().setBeanGroundPlanted(true);
                JOptionPane.showMessageDialog(null, "   CONGRATS!!  \nYOU REACH THE REAL END");
                gm.getMainScene().addSolidObject(900, 0, 217, 289);
                gm.getPlayer().setPosition(302, 304);
            }
        }
    }
}

