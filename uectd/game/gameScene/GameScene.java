package uectd.game.gameScene;

import uectd.gameSystem.*;

public class GameScene extends Scene {

    public GameScene(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
        this.model = new GameSceneModel(sceneChangeListener);
        this.view = new GameSceneView(model);
        this.controller = new GameSceneController(model, view);
    }

}
