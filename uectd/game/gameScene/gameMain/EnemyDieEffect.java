package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Effect;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.SoundManager;
import uectd.gameSystem.util.Vector2;

public class EnemyDieEffect extends Effect {

    private final static int X_NUM = 5;
    private final static int Y_NUM = 2;
    private final static int X_SIZE = 192;
    private final static int Y_SIZE = 192;

    public EnemyDieEffect(GameObject root, GameObject parent) {
        super(root, parent, ImageManager.getInstance().getDivImage(ResourcePathDefines.ENEMY_DIE_EFFECT_IMAGES, X_NUM,
                Y_NUM, X_SIZE, Y_SIZE), false, 0.03f, 0);
        setDrawSize(new Vector2(80, 80));
        SoundManager.getInstance().play(ResourcePathDefines.ENEMY_DIE_SOUND_PATH);
    }

}