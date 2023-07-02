package uectd.game.resultScene;

import uectd.game.titleScene.TitleScene;
import uectd.gameSystem.SceneChangeListener;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.util.Intent;

public class ResultSceneModel extends SceneModel {
    public int score;
    public boolean cleared;

    public ResultSceneModel(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
    }

    @Override
    public void start(Intent intent) {
        super.start(intent);
        cleared = intent.getBooleanValue("Cleared");
        score = intent.getIntegerValue("Score");
    }

    public void toTitle() {
        sceneChangeListener.sceneChanged(new TitleScene(sceneChangeListener), null);
    }

}
