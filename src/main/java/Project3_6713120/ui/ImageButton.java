/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImageButton extends JButton {
    private Image img;
    private int baseW, baseH;
    private boolean isHover = false;
    private boolean isPress = false;
    private MySoundEffect click;
    private String clickpath = UiConfig.clicks;

    public ImageButton(String imgPath, int width, int height) {
        this.baseW = width;
        this.baseH = height;
        click = new MySoundEffect(clickpath);
        click.setVolume(0.5f);
        // ตั้งค่าพื้นฐานปุ่มให้โปร่งใส
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // โหลดรูป
        try {
            ImageIcon icon = new ImageIcon(imgPath);
            this.img = icon.getImage();
        } catch (Exception e) {
            setText("?"); // ถ้าโหลดรูปไม่ได้ ให้ขึ้น ?
        }
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // เมื่อปุ่มสูญเสียโฟกัส (เช่น มี Dialog ขึ้นมา)
                if (isPress) {
                    isPress = false;
                    repaint();
                }
            }
        });
        // ใส่ Logic การขยับ
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHover = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPress = true;
                click.playOnce();
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPress = false;
                repaint();
            }
        });
    }

    public void setnewbg(Image image) {
        this.img = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (img == null) {
            super.paintComponent(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        // ตั้งค่า Pixel Art ให้คมชัด (ไม่เบลอ)
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        int drawW = baseW;
        int drawH = baseH;

        // Hover: ขยาย 10%
        if (isHover) {
            drawW = (int) (baseW * 1.1);
            drawH = (int) (baseH * 1.1);
        }

        // จัดกึ่งกลาง
        int x = (getWidth() - drawW) / 2;
        int y = (getHeight() - drawH) / 2;

        // Press: ขยับลง 10px
        if (isPress) {
            y += 10;
        }

        g2.drawImage(img, x, y, drawW, drawH, null);
    }
}