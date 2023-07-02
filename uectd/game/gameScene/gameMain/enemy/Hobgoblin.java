package uectd.game.gameScene.gameMain.enemy;

import java.util.ArrayList;

import uectd.game.ResourcePathDefines;
import uectd.game.gameScene.Direction;
import uectd.game.gameScene.gameMain.BaseEnemy;
import uectd.game.gameScene.gameMain.Tower;
import uectd.game.gameScene.gameMain.GameLevel;
import uectd.game.gameScene.gameMain.Graph;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.ImageManager;

public class Hobgoblin extends BaseEnemy {

    private final static float MOVE_SPEED = 75;
    private final static int TIME_PER_FRAME = 500;
    private static final double ATTACK_POWER = 30;
    private static final double DEFAULT_HP = 65;

    private final static int[][] IMAGE_INDEXES = { { 0, 1, 2, 1 }, { 3, 4, 5, 4 }, { 6, 7, 8, 7 }, { 9, 10, 11, 10 },
            { 12, 13, 14, 13 }, { 15, 16, 17, 16 }, { 18, 19, 20, 19 }, { 21, 22, 23, 22 } };

    private int currentAngle;

    public Hobgoblin(GameObject root, GameObject parent, GameLevel level, ArrayList<Tower> targets,
            Graph.Vertex initialVertex) {
        super(root, parent, level, targets, initialVertex);
        this.mover = new SearchAndDestroyStrategy(this, level, targets, MOVE_SPEED, 1000);
        this.setImages(ImageManager.getInstance().getDivImage(ResourcePathDefines.ENEMY2_IMAGES, 6, 4, 48, 48),
                TIME_PER_FRAME);
        this.hp = DEFAULT_HP;
        this.attackPower = ATTACK_POWER;
        this.setImageIndexes(IMAGE_INDEXES[0]);
        this.speed = MOVE_SPEED;
        this.startAnimation();
        this.collider = new CircleCollider(this, 10);
        this.depth = 3;
        this.dropValue = 70;
        this.dropScore = 200;
    }

    @Override
    public void start() {
        super.start();
        this.startAnimation();
    }

    @Override
    public void calc(float deltaTime) {
        super.calc(deltaTime);
        int angle = Direction.convertAngleToConstant(mover.getAngle());
        if (angle != currentAngle) {
            currentAngle = angle;
            this.setImageIndexes(IMAGE_INDEXES[angle]);
        }
    }

}
