package uectd.gameSystem;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import uectd.gameSystem.util.*;

public abstract class SceneView extends JPanel implements Observer {
    protected SceneModel model;

    public Camera camera; // カメラを表すGameObject。描画にこのオブジェクトの情報を用いることもできるが、用いないこともできる。

    public JPanel getPanel() {
        return this;
    }

    public SceneView(SceneModel model) {
        this.setBackground(Color.white);
        this.model = model;
        model.addObserver(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }

    public void start() {
        camera = (Camera) model.rootGameObject.findChild(Camera.class);
        if (camera == null) {
            System.err.println("カメラがツリー内に存在しません");
        }
    }

    public void stop() {
    }

    public void pause() {
    }

    public void unpause() {
    }

    public void draw(Graphics g) {
        // DFSして描画できるオブジェクトだけまとめる
        Stack<GameObject> stack = new Stack<>();
        ArrayList<Drawable> drawableList = new ArrayList<>();

        stack.push(model.rootGameObject);
        while (!stack.empty()) {
            var currentGameObject = stack.pop();
            if (currentGameObject.getEnabled()) {
                if (currentGameObject instanceof Drawable)
                    drawableList.add((Drawable) currentGameObject);
                for (var nextGameObject : currentGameObject.children) {
                    if (nextGameObject.getEnabled())
                        stack.push(nextGameObject);
                }
            }
        }

        // Depthでソートしてdraw
        drawableList.sort((a, b) -> b.depth - a.depth);

        for (Drawable iDrawable : drawableList) {
            if (camera == null) {
                iDrawable.draw(g, iDrawable.position, 1);
            } else {
                iDrawable.draw(g,
                        new Vector2(
                                (iDrawable.position.x + iDrawable.offset.x - camera.position.x) * camera.ratio
                                        + Define.HALF_WINDOW_WIDTH,
                                (iDrawable.position.y + iDrawable.offset.y - camera.position.y) * camera.ratio
                                        + Define.HALF_WINDOW_HEIGHT),
                        camera.ratio);
            }
        }
    }

    public Vector2 convertScreenPosToWorldPos(Vector2 screenPos) {
        return new Vector2((screenPos.x - Define.HALF_WINDOW_WIDTH) / camera.ratio + camera.position.x,
                (screenPos.y - Define.HALF_WINDOW_HEIGHT) / camera.ratio + camera.position.y);
    }

    public Vector2 convertWorldPosToScreenPos(Vector2 worldPos) {
        return new Vector2((worldPos.x - camera.position.x) * camera.ratio + Define.HALF_WINDOW_WIDTH,
                (worldPos.y - camera.position.y) * camera.ratio + Define.HALF_WINDOW_HEIGHT);
    }

}
