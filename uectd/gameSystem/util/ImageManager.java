package uectd.gameSystem.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import uectd.gameSystem.FatalError;

public class ImageManager {
    // Singletonパターンを使用
    private static ImageManager instance;
    // パス -> 適当なユニークの番号と
    // 番号 -> 画像の対応関係がある
    // エフェクトなど1つの画像に複数の画像をまとめることがよくあるのでパスと画像の対応は1対1でない
    private Map<Integer, Image> images;
    private Map<String, Integer> ids;

    // 番号との対応付けのためのカウンタ
    private static int counter = 0;

    private ImageManager() {
        images = new HashMap<>();
        ids = new HashMap<>();
    }

    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    private int _loadImage(String absolutePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(absolutePath));
        int id = counter++;
        ids.put(absolutePath, id);
        images.put(id, image);
        return id;
    }

    public int loadImage(String filePath) {
        try {
            String absolutePath = Paths.get(filePath).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            return _loadImage(absolutePath);
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("画像ファイル入出力エラー");
            return -1;
        }
    }

    // 一つの画像ファイルから複数の画像を切り出したものを取得
    public Image[] getDivImage(String filePath, int xNum, int yNum, int xSize, int ySize) {
        Image[] res = new Image[xNum * yNum];
        try {
            String absolutePath = Paths.get(filePath).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            if (ids.containsKey(absolutePath)) {
                int id = ids.get(absolutePath);
                for (int i = 0; i < res.length; i++) {
                    res[i] = images.get(id + i);
                }
            } else {
                ids.put(absolutePath, counter);
                BufferedImage image = ImageIO.read(new File(absolutePath));
                for (int y = 0; y < yNum; y++) {
                    for (int x = 0; x < xNum; x++) {
                        images.put(counter, image.getSubimage(xSize * x, ySize * y, xSize, ySize));
                        res[y * xNum + x] = images.get(counter++);
                    }
                }
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("画像ファイル入出力エラー");
            return null;
        }
    }

    public Image getImage(String filePath) {
        try {
            String absolutePath = Paths.get(filePath).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            if (!ids.containsKey(absolutePath)) {
                _loadImage(absolutePath);
            }
            return images.get(ids.get(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
            FatalError.quit("画像ファイル入出力エラー");
            return null;
        }
    }

    public Image getImage(int id) {
        return images.get(id);
    }
}
