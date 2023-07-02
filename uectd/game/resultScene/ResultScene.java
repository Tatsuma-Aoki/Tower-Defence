package uectd.game.resultScene;

import uectd.gameSystem.Scene;
import uectd.gameSystem.SceneChangeListener;

public class ResultScene extends Scene {

    public ResultScene(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
        this.model = new ResultSceneModel(sceneChangeListener);
        this.view = new ResultSceneView(model);
        this.controller = new ResultSceneController(model, view);
    }

}
