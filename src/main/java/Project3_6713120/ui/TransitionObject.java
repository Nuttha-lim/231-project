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

// [1] เปลี่ยนจาก extends JLabel เป็น extends ImageButton
// เพื่อให้ได้คุณสมบัติ Hover, Press, Sound มาฟรีๆ
public class TransitionObject extends ImageButton implements Interactable {
    private GameManager gm;
    private String targetScene;
    private int spawnX, spawnY;

    public TransitionObject(GameManager gm, String imgPath, int x, int y, String targetScene, int spawnX, int spawnY) {
        // [2] ส่งรูปและขนาดพื้นฐาน (84x84) ไปให้ ImageButton
        super(imgPath, 84, 84);

        this.gm = gm;
        this.targetScene = targetScene;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        setBounds(x - 5, y - 5, 120, 120);

        // [4] ใช้ addActionListener แทน MouseListener แบบเดิม
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interact(gm);
            }
        });
    }

    @Override
    public void interact(GameManager gm) {
        // ย้ายตำแหน่งและเปลี่ยนฉาก
        gm.getCurrentSave().getPlayer().setPosition(spawnX, spawnY);
        gm.switchScene(targetScene);
    }
}