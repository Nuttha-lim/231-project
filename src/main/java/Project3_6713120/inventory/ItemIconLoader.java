/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.inventory;

import Project3_6713120.ui.MyImageIcon;

import java.awt.*;

public class ItemIconLoader {

    public static Image load(String path) {
        return new MyImageIcon(path).getImage();
    }

    public static Image loadScaled(String path, int size) {
        Image img = load(path);
        return img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
    }
}