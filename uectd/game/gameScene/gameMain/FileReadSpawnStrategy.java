package uectd.game.gameScene.gameMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import uectd.game.ResourcePathDefines;
import uectd.game.gameScene.gameMain.enemy.Zombie;
import uectd.gameSystem.FatalError;
import uectd.game.gameScene.gameMain.enemy.Goblin;
import uectd.game.gameScene.gameMain.enemy.Hobgoblin;
import uectd.game.gameScene.gameMain.enemy.Cyclopes;

public class FileReadSpawnStrategy extends EnemySpawnerArrangementStrategy {
    private EnemyDieListener enemyDieListener;

    public FileReadSpawnStrategy(EnemyDieListener enemyDieListener) {
        this.enemyDieListener = enemyDieListener;
    }

    @Override
    public void arrange(int wave) { // W数に対応するspawnerの設置を行う。

        try {
            File spawnerDetailFile = new File(ResourcePathDefines.ENEMY_SPAWNER_DEFINES_PATH);
            if (spawnerDetailFile.exists() && spawnerDetailFile.isFile() && spawnerDetailFile.canRead()) {
                Scanner sc = new Scanner(spawnerDetailFile);
                try {
                    int loadedWave = sc.nextInt(), n = sc.nextInt(); // W数とそのWでの入力行数
                    while (wave != loadedWave) {
                        for (int i = 0; i < n; i++)
                            for (int j = 0; j < 5; j++)
                                sc.nextInt();
                        loadedWave = sc.nextInt();
                        n = sc.nextInt();
                    }
                    // この時点で、loadedWave==wave、nはそのWでの入力行数 になっている
                    for (int i = 0; i < n; i++) {
                        EnemySpawner spawner = generateSpawner(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(),
                                sc.nextInt());
                        enemySpawnerParent.addChild(spawner);
                    }

                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    FatalError.quit("該当するウェーブデータが存在しません");
                }
                sc.close();
                // ファイル閉じてここから頂点を決定
            } else {
                FatalError.quit("ウェーブデータファイルが見つからないか開けません");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FatalError.quit("ウェーブデータファイルが存在しません");
        }
    }
    // [csvのフォーマット]
    //
    // Wave数 今回Waveの入力行数
    // initialVertexNum initialDelay spawnDelay spawnCount spawnEnemy

    // Wごとのspawnerの定義を記述したファイル(EnemySpawnerDefines.dat)の、連続した5つの値を読み込んで、spawnerを生成する
    private EnemySpawner generateSpawner(int initialVertexNum, int initialDelay, int spawnDelay, int spawnCount,
            int spawnEnemyNum) {
        BaseEnemy spawnEnemy;
        switch (spawnEnemyNum) { // ここで、enemyの種類を番号から識別する。enemy追加等で書き換えが必要となる箇所なので注意！
            case 0:
                spawnEnemy = new Zombie(rootGameObject, enemyParent, level, targets,
                        initialVertexList.get(Math.min(initialVertexNum, initialVertexList.size())));
                break;
            case 1:
                spawnEnemy = new Goblin(rootGameObject, enemyParent, level, targets,
                        initialVertexList.get(Math.min(initialVertexNum, initialVertexList.size())));
                break;
            case 2:
                spawnEnemy = new Hobgoblin(rootGameObject, enemyParent, level, targets,
                        initialVertexList.get(Math.min(initialVertexNum, initialVertexList.size())));
                break;
            case 3:
                spawnEnemy = new Cyclopes(rootGameObject, enemyParent, level, targets,
                        initialVertexList.get(Math.min(initialVertexNum, initialVertexList.size())));
                break;
            default:
                spawnEnemy = new Zombie(rootGameObject, enemyParent, level, targets,
                        initialVertexList.get(Math.min(initialVertexNum, initialVertexList.size())));
                break;
        }

        EnemySpawner spawner = new EnemySpawner(rootGameObject, enemySpawnerParent, initialDelay, spawnDelay,
                spawnCount, spawnEnemy, initialVertexList.get(initialVertexNum), enemyParent, enemyDieListener);
        spawner.position = initialVertexList.get(initialVertexNum).position.clone();
        spawner.setEnabled(true);
        return spawner;
    }
}
