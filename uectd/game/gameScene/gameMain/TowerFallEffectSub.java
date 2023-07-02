package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Effect;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.SoundManager;

public class TowerFallEffectSub extends Effect {
    private final static int X_NUM = 7;
    private final static int Y_NUM = 1;
    private final static int X_SIZE = 120;
    private final static int Y_SIZE = 120;

    public TowerFallEffectSub(GameObject root, GameObject parent) {
        super(root, parent, ImageManager.getInstance().getDivImage(ResourcePathDefines.EFFECT_BOMB_IMAGES, X_NUM, Y_NUM,
                X_SIZE, Y_SIZE), false, 0.07f, 0);
        SoundManager.getInstance().play(ResourcePathDefines.TOWER_COLLAPSE_SOUND_PATH);
    }
}