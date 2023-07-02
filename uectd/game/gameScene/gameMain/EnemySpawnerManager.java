package uectd.game.gameScene.gameMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import uectd.game.gameScene.gameMain.Graph.Vertex;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.GameObject;

// GameObjectではない
public class EnemySpawnerManager {
    private EnemySpawnerArrangementStrategy spawnerStrategy;
    private static final int MAX_VERTEX_NUM = 3;

    public EnemySpawnerManager(Graph graph, EnemySpawnerArrangementStrategy spawnerStrategy,
            String candidateVertexFilePath, GameObject rootGameObject, GameObject parentGameObject,
            EnemySpawnerParent enemySpawnerParent, EnemyParent enemyParent, GameLevel level, ArrayList<Tower> targets) {
        this.spawnerStrategy = spawnerStrategy;
        spawnerStrategy.rootGameObject = rootGameObject;
        spawnerStrategy.parentGameObject = parentGameObject;
        spawnerStrategy.enemySpawnerParent = enemySpawnerParent;
        spawnerStrategy.enemyParent = enemyParent;
        spawnerStrategy.level = level;
        spawnerStrategy.targets = targets;

        // 3つくらい頂点を選出
        // 頂点の候補(どの頂点番号が敵出現位置になり得るか)は外部ファイル

        // 敵出現場所の候補が入ったファイル -> candidateVertexListに最大3つまで頂点を入れる
        // ファイルに書いてある頂点の個数が3つより少ない場合は、1 or 2個しか入らない
        try {
            File candidateVertexFile = new File(candidateVertexFilePath);
            if (candidateVertexFile.exists() && candidateVertexFile.isFile() && candidateVertexFile.canRead()) {
                Scanner sc = new Scanner(candidateVertexFile);
                int n = sc.nextInt(); // 入力行数。
                var candidateVertexList = new ArrayList<Vertex>();
                for (int i = 0; i < n; i++) {
                    int idx = sc.nextInt();
                    candidateVertexList.add(graph.vertexes.get(idx));
                }
                sc.close();
                // ファイル閉じてここから頂点を決定
                Collections.shuffle(candidateVertexList);
                var vertexList = new ArrayList<>(
                        candidateVertexList.subList(0, Math.min(MAX_VERTEX_NUM, candidateVertexList.size()))); // 先頭3要素のリストを作成(もしcandidateが少なければ3つより少なくなる)
                spawnerStrategy.initialVertexList = vertexList; // strategyに初期スポ位置を渡す
                for (Vertex vertex : vertexList) {
                    var spawnerMark = new EnemySpawnerMark(rootGameObject, rootGameObject);
                    spawnerMark.position = vertex.position;
                    rootGameObject.addChild(spawnerMark);
                }
            } else {
                FatalError.quit("敵情報ファイルが見つからないか開けません");
            }
        } catch (FileNotFoundException e) {
            FatalError.quit("敵情報ファイルが存在しません");
        }

    }
    // wave終了時にスポナーは全消し → spawnerParentのchildをまとめて殺せばおｋ
    // wav開始時に、Model側で、スポナー生成関数を呼び出す。

    public void arrange(int wave) {
        spawnerStrategy.arrange(wave);
    }

}
