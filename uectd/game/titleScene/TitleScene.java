package uectd.game.titleScene;

import uectd.gameSystem.*;

public class TitleScene extends Scene {

    public TitleScene(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
        this.model = new TitleSceneModel(sceneChangeListener);
        this.view = new TitleSceneView(model);
        this.controller = new TitleSceneController(model, view);
    }

}
