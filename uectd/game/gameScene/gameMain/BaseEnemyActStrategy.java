package uectd.game.gameScene.gameMain;

import java.util.ArrayList;

import uectd.gameSystem.FatalError;
import uectd.gameSystem.GameObject;

public abstract class BaseEnemyActStrategy implements Cloneable {
    protected BaseEnemy enemyGameObject;
    protected GameLevel level;
    protected ArrayList<Tower> targets;

    abstract public void calc(float deltaTime);

    public BaseEnemyActStrategy(GameObject enemyGameObject, GameLevel level, ArrayList<Tower> targets) {
        this.enemyGameObject = (BaseEnemy) enemyGameObject;
        this.level = level;
        this.targets = targets;
    }

    public void pause() {
    }

    public void unpause() {
    }

    @Override
    public BaseEnemyActStrategy clone() {
        BaseEnemyActStrategy baseEnemyActStrategy = null;

        try {
            baseEnemyActStrategy = (BaseEnemyActStrategy) super.clone();
        } catch (CloneNotSupportedException ce) {
            ce.printStackTrace();
            FatalError.quit("オブジェクトのクローンに失敗しました");
        }
        return baseEnemyActStrategy;
    }

    public abstract double getAngle();

    public void onSummoned() {
    }

    public void onDestroyed() {
    }
}
