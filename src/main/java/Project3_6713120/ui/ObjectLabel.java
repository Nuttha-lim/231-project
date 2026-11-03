/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;


import javax.swing.*;

public class ObjectLabel extends JLabel {
    private MyImageIcon img;
    private int cury;
    private int curx;
    private int width;
    private int height;

    public ObjectLabel(String pngpath, int pigwidth, int pigheight, int x, int y) {
        width = pigwidth;
        height = pigheight;
        curx = x;
        cury = y;
        img = new MyImageIcon(pngpath).resize(width, height);
        setIcon(img);
        setposition();
    }

    public void setposition() {
        setBounds(curx, cury, width, height);
    }
}
