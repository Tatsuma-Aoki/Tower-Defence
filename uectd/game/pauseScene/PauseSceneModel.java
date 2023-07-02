package uectd.game.pauseScene;

import uectd.game.titleScene.TitleScene;
import uectd.gameSystem.SceneChangeListener;
import uectd.gameSystem.SceneModel;

public class PauseSceneModel extends SceneModel {

    public PauseSceneModel(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
    }

    @Override
    public void calc(float deltaTime) {
    }

    public void unpause() {
        sceneChangeListener.scenePopped(null);
    }

    public void exit() {
        sceneChangeListener.sceneChanged(new TitleScene(sceneChangeListener), true, null);
    }

}
