package uectd.game.titleScene;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.Define;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;
import uectd.gameSystem.util.GameSound;
import uectd.gameSystem.util.ImageManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TitleSceneView extends SceneView {

    public JButton startButton, exitButton;
    private Image image;
    public static GameSound gameSound;
    private Font font;

    public TitleSceneView(SceneModel model) {
        super(model);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(ResourcePathDefines.FONT_PATH));
        } catch (FontFormatException e) {
            e.printStackTrace();
            FatalError.quit("フォント形式エラー");
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("フォント読み込みエラー");
        }
        font = font.deriveFont(30f);

        this.setLayout(null);
        startButton = new JButton("Start");
        startButton.setBounds(Define.HALF_WINDOW_WIDTH - 200, 400, 400, 80);
        startButton.setBackground(new Color(100, 100, 100));
        startButton.setFont(font);
        this.add(startButton);
        exitButton = new JButton("Exit");
        exitButton.setBounds(Define.HALF_WINDOW_WIDTH - 200, 550, 400, 80);
        exitButton.setBackground(new Color(100, 100, 100));
        exitButton.setFont(font);
        this.add(exitButton);
        // BGM
        gameSound = new GameSound(ResourcePathDefines.START_SCREEN_SOUND_PATH);
        gameSound.init();
        gameSound.loop();
        gameSound.volume(3);
        image = ImageManager.getInstance().getImage(ResourcePathDefines.TITLE_IMAGE);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    @Override
    public void start() {
    }
}
