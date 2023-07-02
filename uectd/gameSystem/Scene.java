package uectd.gameSystem;

public abstract class Scene {
    protected SceneModel model;
    protected SceneView view;
    protected SceneController controller;
    protected SceneChangeListener sceneChangeListener;

    public Scene(SceneChangeListener sceneChangeListener) {
        this.sceneChangeListener = sceneChangeListener;
    }
}
