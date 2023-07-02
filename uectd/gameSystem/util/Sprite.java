package uectd.gameSystem.util;

import uectd.gameSystem.Define;
import uectd.gameSystem.GameObject;

import java.awt.*;

public class Sprite extends Drawable {

    public Image image;

    protected int width, height; // 画像サイズ

    private Vector2 drawSize; // 描画サイズ（ゲームオブジェクト空間基準）

    @Override
    public Sprite clone() {
        Sprite sprite = null;

        sprite = (Sprite) super.clone();
        sprite.drawSize = drawSize.clone();
        return sprite;
    }

    public Sprite(GameObject root, GameObject parent) {
        super(root, parent);
        this.drawSize = new Vector2();
    }

    protected void setImage(String filePath, boolean setDrawSize) {
        this.image = ImageManager.getInstance().getImage(filePath);
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        if (setDrawSize) {
            this.drawSize.x = this.width;
            this.drawSize.y = this.height;
        }
    }

    protected void setImage(int id, boolean setDrawSize) {
        this.image = ImageManager.getInstance().getImage(id);
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        if (setDrawSize) {
            this.drawSize.x = this.width;
            this.drawSize.y = this.height;
        }
    }

    protected void setImage(Image image, boolean setDrawSize) {
        this.image = image;
        if (image != null) {
            this.width = image.getWidth(null);
            this.height = image.getHeight(null);
        }
        if (setDrawSize) {
            this.drawSize.x = this.width;
            this.drawSize.y = this.height;
        }
    }

    protected void setImage(String filePath) {
        this.setImage(filePath, true);
    }

    protected void setImage(int id) {
        this.setImage(id, true);
    }

    protected void setImage(Image image) {
        this.setImage(image, true);
    }

    protected void setDrawSize(Vector2 drawSize) {
        this.drawSize = drawSize;
    }

    @Override
    public void draw(Graphics g, Vector2 screenPosition, float ratio) {
        if (image != null) {
            float half_width = (float) drawSize.x * 0.5f * ratio;
            float half_height = (float) drawSize.y * 0.5f * ratio;

            int x = (int) (screenPosition.x - half_width);
            int y = (int) (screenPosition.y - half_height);
            int w = (int) (drawSize.x * ratio);
            int h = (int) (drawSize.y * ratio);
            if (x + w >= 0 && y + h >= 0 && x <= Define.WINDOW_WIDTH && y <= Define.WINDOW_HEIGHT) {
                g.drawImage(image, x, y, w, h, null);
            }
        }
    }

}
