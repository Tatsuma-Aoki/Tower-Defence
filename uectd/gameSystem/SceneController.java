package uectd.gameSystem;

import java.awt.event.*;
import java.util.Stack;
import java.awt.*;

import javax.swing.Timer;

import uectd.gameSystem.util.IClickable;
import uectd.gameSystem.util.Intent;
import uectd.gameSystem.util.Vector2;

public abstract class SceneController implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    protected SceneModel model;
    protected SceneView view;
    protected javax.swing.Timer timer;

    private long prevTime = 0; // フレーム時間計算用、前のフレームの時刻

    public SceneController(SceneModel model, SceneView view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer(1, this);
        this.timer.start();

        view.getPanel().addMouseListener(this);
        view.getPanel().addMouseMotionListener(this);
        view.getPanel().addKeyListener(this);
        view.getPanel().setFocusable(true);
        view.getPanel().requestFocus();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();

        prevTime = System.nanoTime();
    }

    public void start(Intent intent) {
        model.start(intent);
        model.rootGameObject._start();
        view.start();
    }

    public void stop() {
        view.getPanel().setFocusable(false);
        timer.stop();
        model.stop();
        view.stop();
    }

    public void pause() {
        view.getPanel().setFocusable(false);
        timer.stop();
        model.pause();
        model.rootGameObject._pause();
        view.pause();
    }

    public void unpause(Intent intent) {
        view.getPanel().setFocusable(true);
        timer.restart();
        prevTime = System.nanoTime();
        model.unpause();
        model.rootGameObject._unpause();
        view.unpause();
    }

    public void actionPerformed(ActionEvent e) {
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != view.getPanel()) {
            // フォーカスが外れていたらリクエスト
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            view.getPanel().requestFocus();
        }
        if (e.getSource() == timer) {
            long time = System.nanoTime();
            float deltaTime = (float) (time - prevTime) / 1000000000f;
            prevTime = time;

            model.calc(deltaTime);
            model.rootGameObject._calc(deltaTime);
            view.repaint();
        }
    }

    protected IClickable findClickedObject(Vector2 position) {
        Stack<GameObject> stack = new Stack<>();
        IClickable clickedObject = null;
        stack.push(model.rootGameObject);
        int minDepth = Integer.MAX_VALUE;
        while (!stack.empty()) {
            var currentGameObject = stack.pop();
            if (currentGameObject.collider != null && currentGameObject instanceof IClickable
                    && currentGameObject.depth < minDepth && currentGameObject.collider.isPointIn(position)) {
                clickedObject = (IClickable) currentGameObject;
                minDepth = currentGameObject.depth;
            }
            if (currentGameObject.getEnabled()) {
                for (var nextGameObject : currentGameObject.children) {
                    stack.push(nextGameObject);
                }
            }
        }
        return clickedObject;
    }
}
