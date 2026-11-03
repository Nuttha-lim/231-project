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

public class ShopLabel extends JLabel implements Interactable {
    private final MyImageIcon image;
    private final int width = UiConfig.SHOP_WIDTH;
    private final int height = UiConfig.SHOP_HEIGHT;
    private final int curX = 600;
    private final int curY = 100;
    private final GameManager gm;
    private ShopScene shopScene;

    public ShopLabel(GameManager gm) {
        image = new MyImageIcon(UiConfig.VEG_STORE_IMG).resize(width, height);
        setIcon(image);
        setBounds(curX, curY, width, height);
        this.gm = gm;
        shopScene = gm.getShopScene();
    }

    @Override
    public void interact(GameManager gm) {
        System.out.println("Welcome to the shop!");
        shopScene.updateremain();
        gm.switchScene("SHOP_SCENE");
    }
}

