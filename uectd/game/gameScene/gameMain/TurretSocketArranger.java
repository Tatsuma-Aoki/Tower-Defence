package uectd.game.gameScene.gameMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.FatalError;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Vector2;

public class TurretSocketArranger {
    public static void arrangeTurrets(String turretSocketsPath, GameObject root, TurretSocketParent turretSocketParent,
            TurretParent turretParent) {
        try {
            File turretsFile = new File(turretSocketsPath);
            if (turretsFile.exists() && turretsFile.isFile() && turretsFile.canRead()) {
                Scanner sc = new Scanner(turretsFile);
                int n = sc.nextInt(); // 入力行数。
                int halfWidth = ImageManager.getInstance().getImage(ResourcePathDefines.BACKGROUND_IMAGE).getWidth(null)
                        / 2;
                int halfHeight = ImageManager.getInstance().getImage(ResourcePathDefines.BACKGROUND_IMAGE)
                        .getHeight(null) / 2;
                for (int i = 0; i < n; i++) {
                    int x = sc.nextInt() - halfWidth, y = sc.nextInt() - halfHeight;
                    var turretSocket = new TurretSocket(root, turretSocketParent, turretParent);
                    turretSocket.position = new Vector2(x, y);
                    turretSocketParent.addChild(turretSocket);
                }
                sc.close();
            } else {
                FatalError.quit("タレット配置情報ファイルが見つからないか開けません");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FatalError.quit("タレット配置情報ファイルが存在しません");
        }

    }
}
