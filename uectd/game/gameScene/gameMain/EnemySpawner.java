package uectd.game.gameScene.gameMain;

import java.awt.event.*;

import uectd.game.gameScene.gameMain.Graph.Vertex;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.*;

public class EnemySpawner extends GameObject implements ActionListener {
    private int spawnCount; // 敵出現数。
    private int initialDelay; // Wave開始から敵出現開始までの遅延(ms)
    private int spawnDelay; // 敵出現間隔(ms)
    private GameTimer timer; // 出現待ちに用いるゲーム内タイマー
    private BaseEnemy spawnEnemy; // 出現敵種
    private Vertex initialVertex; // 敵出現地点
    private EnemyParent enemyParent; // 出現した敵が属するゲーム内オブジェクトのヒエラルキーの親
    private EnemyDieListener enemyDieListener; // エネミー死亡リスナー

    public EnemySpawner(GameObject root, GameObject parent, int initialDelay, int spawnDelay, int spawnCount,
            BaseEnemy spawnEnemy, Vertex initialVertex, EnemyParent enemyParent, EnemyDieListener enemyDieListener) {
        super(root, parent);
        this.initialDelay = initialDelay;
        this.spawnDelay = spawnDelay;
        this.spawnCount = spawnCount;
        this.spawnEnemy = spawnEnemy;
        this.initialVertex = initialVertex;
        this.enemyParent = enemyParent;
        this.enemyDieListener = enemyDieListener;
    }

    public void setSpawnCount(int spawnCount) {
        this.spawnCount = spawnCount;
    }

    public void setInitialDelay(int initialDelay) {
        this.initialDelay = initialDelay;
    }

    public void setSpawnDelay(int spawnDelay) {
        this.spawnDelay = spawnDelay;
    }

    @Override
    public void onEnabled() {
        // スポナーがenableされた場合の処理
        this.timer = new GameTimer(this.spawnDelay, this);
        this.timer.setInitialDelay(this.initialDelay);
        this.timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (spawnCount > 0) {
            // 一体ごと出現
            BaseEnemy enemy = spawnEnemy.clone();
            enemy.position = this.position.clone();
            enemy.initialVertex = this.initialVertex;
            enemy.addEnemyDieListener(enemyDieListener);
            enemyParent.addChild(enemy);
            var effect = new EnemySpawnerEffect(root, root);
            effect.position = this.position.clone();
            root.addChild(effect);
            spawnCount--;
        } else {
            this.setEnabled(false);
            timer.stop();
        }
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
