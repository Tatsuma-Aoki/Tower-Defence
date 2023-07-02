package uectd.gameSystem;

import uectd.gameSystem.util.*;
import java.util.Observable;

public abstract class SceneModel extends Observable {
    protected SceneChangeListener sceneChangeListener;
    protected GameObject rootGameObject;

    public void start(Intent intent) {
    }

    public void stop() {
        rootGameObject.destroy();
    }

    public void pause() {
    }

    public void unpause() {
    }

    public void calc(float deltaTime) {
    }

    public SceneModel(SceneChangeListener sceneChangeListener) {
        rootGameObject = new GameObject(null, null);
        rootGameObject.setEnabled(true);
        this.sceneChangeListener = sceneChangeListener;
    }

    public void addGameObject(GameObject gameObject) {
        rootGameObject.addChild(gameObject);
    }

}
