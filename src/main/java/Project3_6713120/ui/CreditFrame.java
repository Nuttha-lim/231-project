/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.resource.fonts;

import javax.swing.*;
import java.awt.*;

public class CreditFrame extends ScenePanel {

    private GameManager manager;

    public CreditFrame(GameManager manager) {
        this.manager = manager;

        setLayout(null);
        setPreferredSize(new Dimension(UiConfig.framew, UiConfig.frameh));

        addComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        MyImageIcon bgImage = new MyImageIcon(UiConfig.creditbg).resize(UiConfig.framew, UiConfig.frameh);
        g.drawImage(bgImage.getImage(), 0, 0, null);
    }

    private void addComponents() {
        setLayout(null);
        setDoubleBuffered(true);
        setBounds(0, 0, UiConfig.framew, UiConfig.frameh);

        MyImageIcon creditGamyuiPic = new MyImageIcon(UiConfig.creditGamyui).resize(200, 230);
        JLabel creditGamyuiLabel = new JLabel(creditGamyuiPic);
        creditGamyuiLabel.setBounds(227, 164, 180, 180);
        creditGamyuiLabel.setVerticalAlignment(SwingConstants.TOP);
        add(creditGamyuiLabel);

        MyImageIcon creditYokPic = new MyImageIcon(UiConfig.creditYok).resize(170, 200);
        JLabel creditYokLabel = new JLabel(creditYokPic);
        creditYokLabel.setBounds(583, 164, 180, 180);
        creditYokLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(creditYokLabel);

        MyImageIcon creditRyanPic = new MyImageIcon(UiConfig.creditRyan).resize(200, 220);
        JLabel creditRyanLabel = new JLabel(creditRyanPic);
        creditRyanLabel.setBounds(940, 164, 180, 180);
        creditRyanLabel.setVerticalAlignment(SwingConstants.TOP);
        creditRyanLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(creditRyanLabel);

        MyImageIcon creditChelseaPic = new MyImageIcon(UiConfig.creditChelsea).resize(300, 400);
        JLabel creditChelseaLabel = new JLabel(creditChelseaPic);
        creditChelseaLabel.setBounds(405, 456, 180, 180);
        add(creditChelseaLabel);

        MyImageIcon creditMixPic = new MyImageIcon(UiConfig.creditMix).resize(250, 290);
        JLabel creditMixLabel = new JLabel(creditMixPic);
        creditMixLabel.setBounds(760, 456, 180, 180);
        creditMixLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        creditMixLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(creditMixLabel);

        JLabel creditGamyuiText = new JLabel("<html><div style='text-align:center;'>6713115<br>Kornchanok<br>Phutrakul</html>", SwingConstants.CENTER);
        creditGamyuiText.setFont(fonts.getPixelFont(18f));
        creditGamyuiText.setForeground(Color.decode("#61422f"));
        creditGamyuiText.setBounds(160, 164 + 180 + 20, 300, 120);
        creditGamyuiText.setVerticalAlignment(SwingConstants.TOP);
        add(creditGamyuiText);

        JLabel creditYokText = new JLabel("<html><div style='text-align:center;'>6713117<br>Nuttha Limkhunthammo</html>", SwingConstants.CENTER);
        creditYokText.setFont(fonts.getPixelFont(18f));
        creditYokText.setForeground(Color.decode("#61422f"));
        creditYokText.setBounds(520, 164 + 180 + 20, 300, 120);
        creditYokText.setVerticalAlignment(SwingConstants.TOP);
        add(creditYokText);

        JLabel creditRyanText = new JLabel("<html><div style='text-align:center;'>6713120<br>Ryan<br>Gleeson</html>", SwingConstants.CENTER);
        creditRyanText.setFont(fonts.getPixelFont(18f));
        creditRyanText.setForeground(Color.decode("#61422f"));
        creditRyanText.setBounds(880, 164 + 180 + 20, 300, 120);
        creditRyanText.setVerticalAlignment(SwingConstants.TOP);
        add(creditRyanText);

        JLabel creditChelseaText = new JLabel("<html><div style='text-align:center;'>6713121<br>Supanattha Supapipatt</html>", SwingConstants.CENTER);
        creditChelseaText.setFont(fonts.getPixelFont(18f));
        creditChelseaText.setForeground(Color.decode("#61422f"));
        creditChelseaText.setBounds(340, 456 + 180 + 20, 300, 120);
        creditChelseaText.setVerticalAlignment(SwingConstants.TOP);
        add(creditChelseaText);

        JLabel creditMixText = new JLabel("<html><div style='text-align:center;'>6713221<br>Jakkarin Roemtangsakul</html>", SwingConstants.CENTER);
        creditMixText.setFont(fonts.getPixelFont(18f));
        creditMixText.setForeground(Color.decode("#61422f"));
        creditMixText.setBounds(700, 456 + 180 + 20, 300, 120);
        creditMixText.setVerticalAlignment(SwingConstants.TOP);
        add(creditMixText);

        // Back Button
        ImageButton backButton = new ImageButton(UiConfig.backimg, 120, 120);
        backButton.setBounds(UiConfig.framew - 100 - 60, UiConfig.frameh - 100 - 60,
                140, 140);
        backButton.addActionListener(e -> manager.switchScene("MAIN_MENU"));
        add(backButton);
    }
}
