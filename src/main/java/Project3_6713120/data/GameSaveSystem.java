/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.data;

import java.io.*;

public class GameSaveSystem {
    public static final String SAVE_DIR = "src/main/java/Project3_6713120/saves/";
    public static final String EXT = ".sav";

    public static boolean saveGame(String slotName, SaveData data) {
        try {
            new File(SAVE_DIR).mkdirs();

            data.updateTimestamp(); // อัปเดตเวลาล่าสุดก่อนเซฟ

            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(SAVE_DIR + slotName + EXT)
            );
            oos.writeObject(data);
            oos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static SaveData loadGame(String slotName) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(SAVE_DIR + slotName + EXT)
            );
            SaveData data = (SaveData) ois.readObject();
            ois.close();
            return data;

        } catch (Exception e) {
            return null;
        }
    }

    public static boolean deleteGame(String slotName) {
        File f = new File(SAVE_DIR + slotName + EXT);
        return f.exists() && f.delete();
    }
}
