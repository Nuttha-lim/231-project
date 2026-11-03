/// ///////////
/// / 6713115 Kornchanok Phutrakul
/// 6713117 Nuttha Limkhunthammo
/// 6713120 Ryan Gleeson
/// 6713121 Supanattha Supapipatt
/// 6713221 Jakkarin Roemtangsakul
package Project3_6713120.ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MySoundEffect {
    private Clip clip;
    private FloatControl gainControl;
    private boolean on;
    private float desi;

    public MySoundEffect(String filename) {
        try {
            java.io.File file = new java.io.File(filename);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float getDesi() {
        return desi;
    }

    public boolean isOnn() {
        return on;
    }

    public void playOnce() {
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    public void playLoop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        on = true;
    }

    public void stop() {
        clip.stop();
        on = false;
    }

    public void setVolume(float gain) {
        if (gain < 0.0f) gain = 0.0f;
        if (gain > 1.0f) gain = 1.0f;
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        desi = gain;
        gainControl.setValue(dB);
    }
}