package Project3_6713120.resource;

import java.awt.*;
import java.io.File;

public class fonts {

    public static Font pixelFont;

    static {
        try {
            // ใช้ path แบบ relative จาก working directory
            File fontFile = new File("src/main/java/Project3_6713120/fonts/PressStart2P-Regular.ttf");
            if (!fontFile.exists()) {
                throw new RuntimeException("Font file not found: " + fontFile.getAbsolutePath());
            }

            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load font! Using fallback Arial.");
            pixelFont = new Font("Arial", Font.PLAIN, 20); // fallback
        }
    }

    public static Font getPixelFont(float size) {
        return pixelFont.deriveFont(size);
    }
}
