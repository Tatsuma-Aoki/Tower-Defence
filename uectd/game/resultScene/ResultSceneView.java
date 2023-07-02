package uectd.game.resultScene;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import java.awt.*;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.SceneView;
import uectd.gameSystem.util.GameSound;
import uectd.gameSystem.util.ImageManager;

public class ResultSceneView extends SceneView {

    private Font font;
    private Image backgroundImage;
    public static GameSound gameSound;

    public ResultSceneView(SceneModel model) {
        super(model);
        this.setBackground(Color.BLACK);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(ResourcePathDefines.FONT_PATH));
        } catch (FontFormatException e) {
            e.printStackTrace();
            FatalError.quit("フォント形式エラー");
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("フォント読み込みエラー");
        }
    }

    @Override
    public void start() {
        if (((ResultSceneModel) model).cleared) {
            backgroundImage = ImageManager.getInstance().getImage(ResourcePathDefines.CLEAR_IMAGE);

            JLabel gameOverLabel = new JLabel("Game Clear!");
            gameOverLabel.setForeground(Color.RED);
            gameOverLabel.setFont(font.deriveFont(100f));
            gameOverLabel.setVerticalAlignment(JLabel.CENTER);
            this.add(gameOverLabel);

            JLabel messageLabel = new JLabel("あなたは電通大をゾンビから守り切った!");
            messageLabel.setForeground(Color.RED);
            messageLabel.setFont(font.deriveFont(50f));
            messageLabel.setVerticalAlignment(JLabel.CENTER);
            this.add(messageLabel);

            JLabel scoreLabel = new JLabel("Score: " + ((ResultSceneModel) model).score);
            scoreLabel.setForeground(Color.RED);
            scoreLabel.setFont(font.deriveFont(70f));
            scoreLabel.setVerticalAlignment(JLabel.CENTER);
            this.add(scoreLabel);

            JLabel messageLabel2 = new JLabel("エスケープキーでタイトルメニューへ");
            messageLabel2.setForeground(Color.RED);
            messageLabel2.setFont(font.deriveFont(70f));
            messageLabel2.setVerticalAlignment(JLabel.CENTER);
            this.add(messageLabel2);

            gameSound = new GameSound(ResourcePathDefines.GAME_CLEAR_SOUND_PATH);
            gameSound.init();
            gameSound.start();
            gameSound.volume(3);
        } else {
            backgroundImage = ImageManager.getInstance().getImage(ResourcePathDefines.GAME_OVER_IMAGE);
            JLabel gameOverLabel = new JLabel("Game Over...");
            gameOverLabel.setForeground(Color.RED);
            gameOverLabel.setFont(font.deriveFont(100f));
            gameOverLabel.setVerticalAlignment(JLabel.CENTER);
            this.add(gameOverLabel);
            JLabel messageLabel = new JLabel("電通大はゾンビたちの手に落ちた...");
            messageLabel.setForeground(Color.RED);
            messageLabel.setFont(font.deriveFont(70f));
            messageLabel.setVerticalAlignment(JLabel.CENTER);
            this.add(messageLabel);
            JLabel scoreLabel = new JLabel("Score: " + ((ResultSceneModel) model).score);
            scoreLabel.setForeground(Color.RED);
            scoreLabel.setFont(font.deriveFont(70f));
            scoreLabel.setVerticalAlignment(JLabel.CENTER);
            this.add(scoreLabel);
            JLabel messageLabel2 = new JLabel("エスケープキーでタイトルメニューへ");
            messageLabel2.setForeground(Color.RED);
            messageLabel2.setFont(font.deriveFont(70f));
            messageLabel2.setVerticalAlignment(JLabel.CENTER);
            this.add(messageLabel2);

            gameSound = new GameSound(ResourcePathDefines.GAME_OVER_SOUND_PATH);
            gameSound.init();
            gameSound.start();
            gameSound.volume(3);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        super.draw(g);
    }

}
