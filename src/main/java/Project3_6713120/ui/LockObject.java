/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul

package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.world.Interactable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// [1] เปลี่ยน extends จาก JLabel เป็น ImageButton
public class LockObject extends ImageButton implements Interactable {
    private int price;
    private Runnable unlockAction;
    private GameManager gm;

    public LockObject(GameManager gm, String imgPath, int x, int y, int price, Runnable unlockAction) {
        // [2] ส่งรูปและขนาด (84x84) ไปให้ ImageButton จัดการ
        super(imgPath, 84, 84);

        this.gm = gm;
        this.price = price;
        this.unlockAction = unlockAction;

        // [3] ตั้งค่าตำแหน่งและเผื่อขนาดปุ่มสำหรับการขยาย (Hover)
        // ขยับตำแหน่งถอยหลังนิดหน่อย (-5) และขยายกรอบ (+10)
        setBounds(x - 5, y - 5, 120, 120);

        // [4] ใช้ ActionListner แทน MouseListener
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interact(gm);
            }
        });
    }

    @Override
    public void interact(GameManager gm) {
        // เปิด Dialog เหมือนเดิม
        new dialogborder(gm, price, () -> {
            unlockAction.run();
            this.setVisible(false);
            gm.getCurrentScene().repaint();
        }).setVisible(true);
    }
}