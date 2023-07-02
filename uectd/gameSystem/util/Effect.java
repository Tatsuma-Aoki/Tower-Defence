package uectd.gameSystem.util;

import uectd.gameSystem.GameObject;
import java.awt.*;

public class Effect extends Sprite {

    protected Image[] images;
    public boolean loopFlag;
    public float frameTime, elapsedTime;
    public int currentFrame;
    private int frameNumber;

    public Effect(GameObject root, GameObject parent, Image[] images, boolean loopFlag, float frameTime, int depth) {
        super(root, parent);
        this.loopFlag = loopFlag;
        if (images != null) {
            this.frameNumber = images.length;
            if (images.length > 0)
                this.images = new Image[images.length];
            setImage(images[0]);
        }
        this.images = images;
        this.depth = depth;
        this.frameTime = frameTime;
        this.elapsedTime = 0;
        this.currentFrame = 0;
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
        if (images != null)
            setImage(images[0]);

        this.elapsedTime = 0;
        this.currentFrame = 0;
    }

    @Override
    public void calc(float deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime >= frameTime) {
            currentFrame++;
            if (currentFrame >= frameNumber) {
                if (loopFlag) {
                    currentFrame = 0;
                } else {
                    this.destroy();
                    return;
                }
            }
            if (images != null)
                setImage(images[currentFrame], false);
            elapsedTime = 0;
        }
        super.calc(deltaTime);
    }

    @Override
    public void draw(Graphics g, Vector2 screenPosition, float ratio) {
        super.draw(g, screenPosition, ratio);
    }

}
