package Project3_6713120.ui;

import javax.swing.*;
import java.awt.*;

public class Modernradio implements Icon {
    private int size = 18;

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        JRadioButton b = (JRadioButton) c;

        // วงกลมนอกตรงๆ ไม่ใส่เงา
        g2.setColor(new Color(140, 110, 80)); // น้ำตาลอ่อน
        g2.fillOval(x, y, size, size);

        g2.setColor(new Color(100, 70, 50)); // น้ำตาลเข้ม (ขอบ)
        g2.drawOval(x, y, size, size);

        // วงกลมข้างใน (ไม่มีเงา)
        if (b.isSelected()) {
            g2.setColor(new Color(255, 255, 255)); // สีน้ำตาลอีกเฉด
            g2.fillOval(x + 4, y + 4, size - 8, size - 8);
        }
        g2.dispose();
    }
}

