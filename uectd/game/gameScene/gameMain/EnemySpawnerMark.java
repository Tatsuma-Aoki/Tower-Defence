package uectd.game.gameScene.gameMain;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Drawable;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Vector2;

import java.awt.*;

public class EnemySpawnerMark extends Drawable {
    Image image;
    int width, height;

    public EnemySpawnerMark(GameObject root, GameObject parent) {
        super(root, parent);
        image = ImageManager.getInstance().getImage(ResourcePathDefines.ENEMY_SPAWNER_MARK_IMAGE);
        width = image.getWidth(null);
        height = image.getHeight(null);
        this.depth = 9;
    }

    @Override
    public void draw(Graphics g, Vector2 screenPosition, float ratio) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalAffineTransform = g2.getTransform();
        AffineTransform affineTransform = (AffineTransform) originalAffineTransform.clone();
        affineTransform.rotate(System.currentTimeMillis() / 2000.0, screenPosition.x, screenPosition.y);
        g2.setTransform(affineTransform);
        g2.drawImage(this.image, (int) (screenPosition.x - width * ratio * 0.5),
                (int) (screenPosition.y - height * ratio * 0.5), (int) (width * ratio), (int) (height * ratio), null);
        g2.setTransform(originalAffineTransform); // アフィン変換行列を元に戻す

    }
}
