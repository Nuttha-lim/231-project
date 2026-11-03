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
import java.awt.event.*;

public class dialogborder extends JDialog {
    private JButton okButton;
    private JButton backButton;
    private JLabel titleLabel;
    private JLabel priceLabel;
    private JLabel errorLabel;

    private int dW = 450;
    private int dH = 300;

    private GameManager man;

    public dialogborder(GameManager man, int m, Runnable onSuccess) {
        super(UiConfig.window, "Unlock Area", ModalityType.APPLICATION_MODAL);
        this.man = man;

        // --- เพิ่มส่วนนี้เพื่อตั้งค่า Icon ---
        setIconImage(new ImageIcon(UiConfig.iconimg).getImage());
        // --------------------------------

        setResizable(false);
        man.resetInputs();

        setSize(dW, dH);
        setLocationRelativeTo(null);
        setLayout(null);

        // สีพื้นหลัง #fff3d7
        getContentPane().setBackground(Color.decode("#fff3d7"));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                man.requestFocusInWindow();
            }
        });

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC_PRESSED");
        getRootPane().getActionMap().put("ESC_PRESSED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // --- UI Components ---

        // 1. หัวข้อ
        titleLabel = new JLabel("Unlock this area?", SwingConstants.CENTER);
        titleLabel.setFont(fonts.getPixelFont(24f));
        titleLabel.setBounds(0, 30, dW, 40);
        add(titleLabel);

        // 2. ราคา (เริ่มต้นสีดำ)
        priceLabel = new JLabel("Cost: " + m + " G", SwingConstants.CENTER);
        priceLabel.setFont(fonts.getPixelFont(20f));
        priceLabel.setForeground(Color.BLACK);
        priceLabel.setBounds(0, 70, dW, 30);
        add(priceLabel);

        // 3. Error Message (เตรียมไว้ แต่ยังว่างเปล่า)
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(fonts.getPixelFont(16f));
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(0, 100, dW, 30);
        add(errorLabel);

        // --- Buttons ---
        int buttonSize = 75;
        int gap = 40;
        int startY = 150;
        int totalWidth = (buttonSize * 2) + gap;
        int startX = (dW - totalWidth) / 2;

        // ปุ่ม Back
        backButton = new ImageButton(UiConfig.Back, buttonSize, buttonSize);
        backButton.setBounds(startX, startY, buttonSize + 20, buttonSize + 30);
        backButton.addActionListener(e -> dispose());
        add(backButton);

        // ปุ่ม OK
        okButton = new ImageButton(UiConfig.OKBUTTON_IMG, buttonSize, buttonSize);
        okButton.setBounds(startX + buttonSize + gap, startY, buttonSize + 20, buttonSize + 30);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // เช็คเงินตอนกดปุ่ม
                if (man.getCurrentSave().getMoney() < m) {
                    // เงินไม่พอ -> เปลี่ยนสีแดง และขึ้นข้อความเตือนข้างล่าง
                    priceLabel.setForeground(Color.RED);
                    errorLabel.setText("Not Enough Money!");
                } else {
                    // เงินพอ -> ตัดเงินและปลดล็อค
                    man.getCurrentSave().removemoney(m);
                    man.updateCoinUI();
                    onSuccess.run();
                    JOptionPane.showMessageDialog(null, "UNLOCKED!");
                    dispose();
                }
            }
        });
        add(okButton);
    }
}