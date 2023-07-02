package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Effect;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.SoundManager;
import uectd.gameSystem.util.Vector2;

public class EnemySpawnerEffect extends Effect {
    private final static int X_NUM = 10;
    private final static int Y_NUM = 1;
    private final static int X_SIZE = 240;
    private final static int Y_SIZE = 240;

    public EnemySpawnerEffect(GameObject root, GameObject parent) {
        super(root, parent, ImageManager.getInstance().getDivImage(ResourcePathDefines.ENEMY_SUMMON_IMAGES, X_NUM,
                Y_NUM, X_SIZE, Y_SIZE), false, 0.03f, 0);
        setDrawSize(new Vector2(80, 80));
        this.offset.y = -25;
        SoundManager.getInstance().play(ResourcePathDefines.SUMMON_SOUND_PATH);
        this.depth = 1;
    }
}
