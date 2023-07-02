package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Effect;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.SoundManager;
import uectd.gameSystem.util.Vector2;

public class TurretAttackToEnemyEffect extends Effect {
    private final static int X_NUM = 5;
    private final static int Y_NUM = 1;
    private final static int X_SIZE = 240;
    private final static int Y_SIZE = 240;

    public TurretAttackToEnemyEffect(GameObject root, GameObject parent) {
        super(root, parent, ImageManager.getInstance().getDivImage(ResourcePathDefines.TURRET_ATTACK_TO_ENEMY_IMAGES,
                X_NUM, Y_NUM, X_SIZE, Y_SIZE), false, 0.03f, 0);
        setDrawSize(new Vector2(80, 80));
        SoundManager.getInstance().play(ResourcePathDefines.CANNON_TURRET_SOUND_PATH);
    }
}
