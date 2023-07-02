package uectd.game.pauseScene;

import uectd.gameSystem.*;

public class PauseScene extends Scene {

    public PauseScene(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
        this.model = new PauseSceneModel(sceneChangeListener);
        this.view = new PauseSceneView(model);
        this.controller = new PauseSceneController(model, view);
    }

}
