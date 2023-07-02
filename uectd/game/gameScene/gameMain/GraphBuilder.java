package uectd.game.gameScene.gameMain;

import java.io.*;
import java.util.*;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Vector2;

public class GraphBuilder {
    public static Graph build(String graphPath) {
        try {
            File graphFile = new File(graphPath);
            Graph graph = null;
            if (graphFile.exists() && graphFile.isFile() && graphFile.canRead()) {
                Scanner sc = new Scanner(graphFile);
                int v = sc.nextInt();
                graph = new Graph(v);
                int halfWidth = ImageManager.getInstance().getImage(ResourcePathDefines.BACKGROUND_IMAGE).getWidth(null)
                        / 2;
                int halfHeight = ImageManager.getInstance().getImage(ResourcePathDefines.BACKGROUND_IMAGE)
                        .getHeight(null) / 2;
                for (int i = 0; i < v; i++) {
                    int x = sc.nextInt() - halfWidth, y = sc.nextInt() - halfHeight;
                    int idx = sc.nextInt();
                    graph.addVertex(new Vector2(x, y), idx);
                }

                int e = sc.nextInt();
                for (int i = 0; i < e; i++) {
                    int s = sc.nextInt(), t = sc.nextInt();
                    graph.addEdge(s, t);
                }
                sc.close();
                return graph;
            } else {
                FatalError.quit("マップデータファイルが見つからないか開けません");
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FatalError.quit("マップデータファイルが存在しません");
        }
        return null;
    }
}
