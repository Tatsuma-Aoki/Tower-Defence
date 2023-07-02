package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Sprite;

public class BackGround extends Sprite {

    public BackGround(GameObject root, GameObject parent) {
        super(root, parent);
        setImage(ImageManager.getInstance().getImage(ResourcePathDefines.BACKGROUND_IMAGE));
        this.depth = 10;
    }

}
