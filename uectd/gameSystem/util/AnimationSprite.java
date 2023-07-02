package uectd.gameSystem.util;

import uectd.gameSystem.GameObject;
import java.awt.event.*;
import java.awt.*;

// アニメーションするオブジェクト
public class AnimationSprite extends Sprite implements ActionListener {

    private int timePerFrame;
    private Image[] images;
    private int[] imageIndexes;
    private int currentImageIndex;
    private int currentImagesNum;
    private GameTimer timer;

    @Override
    public AnimationSprite clone() {
        AnimationSprite animationSprite = null;

        animationSprite = (AnimationSprite) super.clone();
        animationSprite.images = this.images.clone();
        animationSprite.imageIndexes = this.imageIndexes.clone();
        animationSprite.timer = new GameTimer(this.timer.getDelay(), animationSprite);
        animationSprite.timer.setInitialDelay(this.timer.getInitialDelay());
        return animationSprite;
    }

    public AnimationSprite(GameObject root, GameObject parent) {
        super(root, parent);
    }

    protected void setImages(Image[] images, int timePerFrame) {
        this.images = images;
        this.timePerFrame = timePerFrame;
    }

    protected void setImageIndexes(int[] imageIndexes) {
        this.imageIndexes = imageIndexes;
        this.currentImagesNum = imageIndexes.length;
        this.currentImageIndex = 0;
        this.setImage(images[imageIndexes[0]]);
    }

    protected void startAnimation() {
        timer = new GameTimer(timePerFrame, this);
        timer.start();
    }

    protected void stopAnimation() {
        timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentImageIndex++;
        if (currentImageIndex >= currentImagesNum) {
            currentImageIndex = 0;
        }
        setImage(images[imageIndexes[currentImageIndex]], false);
    }

    @Override
    public void pause() {
        super.pause();
        timer.pause();
    }

    @Override
    public void unpause() {
        super.unpause();
        timer.unpause();
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        timer.stop();
    }

}
