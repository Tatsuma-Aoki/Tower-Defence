package uectd.game.gameScene.gameMain;

import java.io.*;
import java.util.*;

import uectd.gameSystem.FatalError;
import uectd.gameSystem.GameObject;

public class TowerArranger {
    private static final int TOWER_NUM = 2;

    public static ArrayList<Tower> arrange(String towerCandidateFilePath, GameObject rootGameObject, TowerParent parent,
            Graph graph) {
        try {
            File candidateVertexFile = new File(towerCandidateFilePath);
            if (candidateVertexFile.exists() && candidateVertexFile.isFile() && candidateVertexFile.canRead()) {
                Scanner sc = new Scanner(candidateVertexFile, "UTF-8");
                int n = sc.nextInt(); // 入力行数。
                var candidateIndexes = new ArrayList<Integer>();
                for (int i = 0; i < n; i++) {
                    candidateIndexes.add(i);
                }
                Collections.shuffle(candidateIndexes);
                var arrangeIndexes = candidateIndexes.subList(0, Math.min(TOWER_NUM, n));
                var res = new ArrayList<Tower>();
                for (int i = 0; i < n; i++) {
                    int idx = sc.nextInt();
                    String name = sc.next();
                    if (arrangeIndexes.contains(i)) {
                        Tower tower = new Tower(rootGameObject, (GameObject) parent, graph.vertexes.get(idx));
                        tower.setName(name);
                        parent.addChild(tower);
                        res.add(tower);
                    }
                }
                sc.close();
                return res;
            } else {
                FatalError.quit("タワー位置情報ファイルが見つからないか開けません");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FatalError.quit("タワー位置情報ファイルが存在しません");
        }
        return null;
    }
}
