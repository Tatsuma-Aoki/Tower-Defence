package uectd.gameSystem.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameSound {
    private String filePath;
    private Clip clip;

    public GameSound(String filePath) {
        this.filePath = filePath;
    }

    public void init() {
        clip = createClip(new File(filePath));
    }

    private static Clip createClip(File path) {
        // 指定されたURLのオーディオ入力ストリームを取得
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)) {

            // ファイルの形式取得
            AudioFormat af = ais.getFormat();

            // 単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
            DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

            // 指定された Line.Info オブジェクトの記述に一致するラインを取得
            Clip c = (Clip) AudioSystem.getLine(dataLine);

            // 再生準備完了
            c.open(ais);

            return c;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 再生開始
    public void start() {
        clip.setFramePosition(0);
        clip.start();
    }

    // ループ再生
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // 一時停止
    public void pause() {
        clip.stop();
    }

    // 停止
    public void stop() {
        clip.stop();
        clip.close();
    }

    public boolean isPlaying() {
        return clip.isRunning();
    }

    // 音量調整
    public void volume(int soundVolume) {
        FloatControl ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue((float) Math.log10((float) soundVolume / 100) * 20);
    }
}
