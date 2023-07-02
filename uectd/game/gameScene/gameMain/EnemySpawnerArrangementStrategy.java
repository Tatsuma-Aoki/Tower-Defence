package uectd.game.gameScene.gameMain;

import java.util.ArrayList;

import uectd.game.gameScene.gameMain.Graph.Vertex;
import uectd.gameSystem.GameObject;

public abstract class EnemySpawnerArrangementStrategy {
    protected ArrayList<Vertex> initialVertexList;
    protected GameObject rootGameObject;
    protected GameObject parentGameObject;
    protected EnemySpawnerParent enemySpawnerParent;
    protected EnemyParent enemyParent;
    protected GameLevel level;
    protected ArrayList<Tower> targets;

    public abstract void arrange(int wave);
}
