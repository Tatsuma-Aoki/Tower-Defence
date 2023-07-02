package uectd.game.gameScene.gameMain.enemy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.awt.event.*;

import uectd.game.gameScene.gameMain.BaseEnemyActStrategy;
import uectd.game.gameScene.gameMain.Tower;
import uectd.game.gameScene.gameMain.Damage;
import uectd.game.gameScene.gameMain.GameLevel;
import uectd.game.gameScene.gameMain.Graph;
import uectd.game.gameScene.gameMain.TowerFallListener;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.GameTimer;
import uectd.gameSystem.util.Vector2;

public class SearchAndDestroyStrategy extends BaseEnemyActStrategy implements TowerFallListener, ActionListener {

    private enum Mode {
        search, destroy,
    };

    private static final double THRESHOLD_RADIUS = 10;
    private Mode currentMode;
    private Stack<Graph.Vertex> route;
    private int attackInterval;
    private ArrayList<Tower> targets;
    private Tower target;
    private GameTimer attackTimer;
    private EnemyAnimation animation;
    private double angle;

    @Override
    public SearchAndDestroyStrategy clone() {
        SearchAndDestroyStrategy sdStrategy = null;
        sdStrategy = (SearchAndDestroyStrategy) super.clone();
        sdStrategy.attackTimer = new GameTimer(attackInterval, sdStrategy);
        sdStrategy.targets = new ArrayList<>(targets);
        sdStrategy.animation = new EnemyAnimation(attackInterval, attackInterval * 7 / 10, 30);
        return sdStrategy;
    }

    @Override
    public void onSummoned() {
        Collections.shuffle(targets);
        if (!targets.isEmpty())
            target = targets.get(0);
        animation.start();

        for (var tower : targets) {
            tower.addTowerFallListener(this);
        }
        enemyGameObject.start();
    }

    @Override
    public void pause() {
        super.pause();
        attackTimer.pause();
    }

    @Override
    public void unpause() {
        super.unpause();
        attackTimer.unpause();
    }

    public SearchAndDestroyStrategy(GameObject enemyGameObject, GameLevel level, ArrayList<Tower> targets, float speed,
            int attackInterval) {
        super(enemyGameObject, level, targets);
        this.currentMode = Mode.search;
        this.route = new Stack<>();
        this.targets = new ArrayList<>(targets);
        this.attackInterval = attackInterval;
        if (!this.targets.isEmpty())
            this.target = this.targets.get(0);
        this.attackTimer = new GameTimer(attackInterval, this);
        this.animation = new EnemyAnimation(attackInterval, attackInterval * 7 / 10, 30);
    }

    @Override
    public void calc(float deltaTime) {
        if (target == null)
            return;
        if (currentMode == Mode.search) {
            if (route.isEmpty()) {
                route = level.graph.shortestPath(level.graph.nearestVertex(enemyGameObject.position),
                        target.getVertex());
            }
            var nextDst = route.peek();
            var vector = Vector2.diff(nextDst.position, enemyGameObject.position);
            if (vector.magnitude() < THRESHOLD_RADIUS) {
                route.pop();
                if (route.isEmpty()) {
                    currentMode = Mode.destroy;
                    attackTimer.start();
                    animation.start();
                    vector = Vector2.diff(nextDst.position, enemyGameObject.position);
                    return;
                }
                nextDst = route.peek();
                vector = Vector2.diff(enemyGameObject.position, nextDst.position);
            }
            angle = Math.atan2(vector.y, vector.x);
            enemyGameObject.position.add(Vector2.scale(vector.normalized(), deltaTime * enemyGameObject.getSpeed()));
        } else if (currentMode == Mode.destroy) {
            enemyGameObject.offset = animation.getOffset(angle);
        }
    }

    @Override
    public void onTowerFall(Tower tower) {
        attackTimer.stop();
        route.clear();
        currentMode = Mode.search;
        target.removeTowerFallListener(this);
        targets.remove(target);
        if (tower == target) {
            if (!targets.isEmpty())
                target = targets.get(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        animation.start();
        target.applyDamage(new Damage(enemyGameObject.getAttackPower(), enemyGameObject));
    }

    public double getAngle() {
        return angle;
    }

    @Override
    public void onDestroyed() {
        this.attackTimer.stop();
        for (var target : targets) {
            target.removeTowerFallListener(this);
        }
    }
}
