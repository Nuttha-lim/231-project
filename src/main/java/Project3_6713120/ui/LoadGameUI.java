/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import Project3_6713120.GameManager;
import Project3_6713120.data.GameSaveSystem;
import Project3_6713120.data.SaveData;
import Project3_6713120.resource.fonts;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class LoadGameUI extends JPanel {
    private GameManager gameManager;
    private JPanel slot1Panel, slot2Panel, slot3Panel;
    private Image backgroundImage;

    public LoadGameUI(GameManager gameManager) {
        this.gameManager = gameManager;
        loadBackground();

        setLayout(null);
        setPreferredSize(new Dimension(GameManager.SCREEN_WIDTH, GameManager.SCREEN_HEIGHT));

        // Save Slots
        createSaveSlots();

        // Back Button (ใช้ ImageButton)
        ImageButton backButton = new ImageButton(UiConfig.Back, 70, 70);
        backButton.setBounds(45, 650, 100, 100); // Hitbox ใหญ่กว่ารูปนิดหน่อย
        backButton.addActionListener(e -> gameManager.switchScene("MAIN_MENU"));
        add(backButton);
        setComponentZOrder(backButton, 0);
    }

    private void loadBackground() {
        try {
            ImageIcon icon = new ImageIcon(UiConfig.Load);
            backgroundImage = icon.getImage();
        } catch (Exception e) {
            System.err.println("Cannot load background: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // วาดพื้นหลังสีเขียว
        g.setColor(new Color(200, 220, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (backgroundImage != null) {
            int imgWidth = 1100;
            int imgHeight = 650;
            int imgX = (getWidth() - imgWidth) / 2;
            int imgY = (getHeight() - imgHeight) / 2 + 20;

            g.drawImage(backgroundImage, imgX, imgY, imgWidth, imgHeight, this);
        }
    }

    private void createSaveSlots() {
        // ปรับตำแหน่งให้อยู่กลางจอ
        int slotWidth = 1000;
        int slotHeight = 150;
        int startX = (GameManager.SCREEN_WIDTH - slotWidth) / 2 + 20;
        int startY = 200;
        int gap = 25;

        slot1Panel = createSlotPanel("Slot1", 1, startX, startY);
        slot2Panel = createSlotPanel("Slot2", 2, startX, startY + slotHeight + gap);
        slot3Panel = createSlotPanel("Slot3", 3, startX, startY + (slotHeight + gap) * 2);

        add(slot1Panel);
        add(slot2Panel);
        add(slot3Panel);
    }

    private JPanel createSlotPanel(String slotName, int slotNumber, int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, 1000, 150);
        panel.setOpaque(false);

        SaveData saveData = GameSaveSystem.loadGame(slotName);

        if (saveData != null) {
            // Player Name
            JLabel nameLabel = new JLabel(saveData.getSaveName());
            nameLabel.setFont(fonts.getPixelFont(46f));
            nameLabel.setForeground(new Color(101, 67, 33));
            nameLabel.setBounds(80, 25, 500, 45); // เยื้องซ้าย
            panel.add(nameLabel);

            // Last Played
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateStr = sdf.format(saveData.getTimestamp());
            JLabel dateLabel = new JLabel("Last played: " + dateStr);
            dateLabel.setFont(fonts.getPixelFont(12f));
            dateLabel.setForeground(new Color(101, 67, 33));
            dateLabel.setBounds(80, 75, 400, 30); // เยื้องซ้าย
            panel.add(dateLabel);

            // Load Button (ใช้ ImageButton)
            ImageButton loadBtn = new ImageButton(UiConfig.Play, 80, 80);
            loadBtn.setBounds(735, 20, 110, 110);
            loadBtn.addActionListener(e -> {
                gameManager.loadGame(slotName);
            });
            panel.add(loadBtn);

            // Delete Button (ใช้ ImageButton)
            ImageButton deleteBtn = new ImageButton(UiConfig.Delete, 80, 80);
            deleteBtn.setBounds(835, 20, 110, 110);
            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete this save file?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    gameManager.deleteSlot(slotName);
                    refreshSlots();
                }
            });
            panel.add(deleteBtn);

        } else {
            // Empty Slot - ปุ่ม Plus (ใช้ ImageButton)
            ImageButton plusBtn = new ImageButton(UiConfig.Plus, 100, 100);
            plusBtn.setBounds(435, 10, 130, 130);
            plusBtn.addActionListener(e -> {
                gameManager.switchScene("NEW_GAME");
            });
            panel.add(plusBtn);
        }

        return panel;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshSlots(); // โหลดข้อมูลจากไฟล์เซฟใหม่ทันที
        }
    }

    private void refreshSlots() {
        if (slot1Panel != null) remove(slot1Panel);
        if (slot2Panel != null) remove(slot2Panel);
        if (slot3Panel != null) remove(slot3Panel);
        createSaveSlots();
        revalidate();
        repaint();
    }
}