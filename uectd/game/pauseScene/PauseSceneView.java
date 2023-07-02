package uectd.game.pauseScene;

import javax.swing.JButton;
import javax.swing.JLabel;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;
import uectd.gameSystem.util.ImageManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PauseSceneView extends SceneView {

    public JButton unpauseButton, exitButton;

    public Image backgroundImage;

    private Font font;

    public PauseSceneView(SceneModel model) {
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

        JLabel pauseLabel = new JLabel("Pause");
        pauseLabel.setFont(font);
        pauseLabel.setBounds(100, 40, 200, 60);
        this.add(pauseLabel);

        unpauseButton = new JButton("Continue");
        unpauseButton.setFont(font);
        unpauseButton.setBounds(100, 100, 200, 60);
        unpauseButton.setContentAreaFilled(false);
        this.add(unpauseButton);

        exitButton = new JButton("Quit");
        exitButton.setFont(font);
        exitButton.setBounds(100, 160, 200, 60);
        exitButton.setContentAreaFilled(false);
        this.add(exitButton);

    }

    @Override
    public void start() {
        backgroundImage = ImageManager.getInstance().getImage(ResourcePathDefines.BACKGROUND_IMAGE);

    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        g.drawImage(backgroundImage, 0, 0,
                backgroundImage.getWidth(null) * getHeight() / backgroundImage.getHeight(null), getHeight(), null);
    }

}
