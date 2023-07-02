package uectd.game.gameScene.gameMain;

import java.util.ArrayList;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.AnimationSprite;

public abstract class BaseEnemy extends AnimationSprite implements IDamageApplicable, IAttackable {

    // 耐久、攻撃、スピード、ドロップ金額、撃破時スコア
    protected double hp, attackPower, speed;
    protected int dropValue, dropScore;

    protected BaseEnemyActStrategy mover; // Strategyパターンを使用して実装、敵の行動パターンを表すクラス
    protected GameLevel level; // ゲームのマップデータのオブジェクト。経路探索に使う
    protected ArrayList<Tower> targets; // 襲撃対象のリスト
    protected Graph.Vertex initialVertex; // 最初に出現したマップ上の地点
    protected ArrayList<EnemyDieListener> enemyDieListeners; // この敵が撃破されたときの通知を受け取るリスナーの一覧

    @Override
    public BaseEnemy clone() {
        BaseEnemy enemy = null;
        enemy = (BaseEnemy) super.clone();
        enemy.mover = mover.clone();
        enemy.mover.enemyGameObject = enemy;
        enemy.enemyDieListeners = new ArrayList<>(enemy.enemyDieListeners);
        return enemy;
    }

    public BaseEnemy(GameObject root, GameObject parent, GameLevel level, ArrayList<Tower> targets,
            Graph.Vertex initialVertex) {
        super(root, parent);
        this.level = level;
        this.targets = targets;
        this.enemyDieListeners = new ArrayList<>();
        this.initialVertex = initialVertex;
    }

    @Override
    public void pause() {
        super.pause();
        mover.pause();
    }

    @Override
    public void unpause() {
        super.unpause();
        mover.unpause();
    }

    public double getHp() {
        return hp;
    }

    public double getAttackPower() {
        return attackPower;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDropValue() {
        return dropValue;
    }

    public int getDropScore() {
        return dropScore;
    }

    @Override
    public void calc(float deltaTime) {
        mover.calc(deltaTime);
    }

    @Override
    public void applyDamage(Damage d) {
        if (this.getEnabled()) {
            hp -= d.attack;
            if (hp < 0) {
                hp = 0;
                var effect = new EnemyDieEffect(root, root);
                effect.position = this.position.clone();
                root.addChild(effect);
                for (var enemyDieListener : enemyDieListeners) {
                    enemyDieListener.onEnemyDead(this);
                }
                destroy();
            }
        }
    }

    public void addEnemyDieListener(EnemyDieListener enemyDieListener) {
        enemyDieListeners.add(enemyDieListener);
    }

    public void removeEnemyDieListener(EnemyDieListener enemyDieListener) {
        enemyDieListeners.remove(enemyDieListener);
    }

    public Graph.Vertex getInitialVertex() {
        return initialVertex;
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
        mover.onSummoned();
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        mover.onDestroyed();
    }
}
