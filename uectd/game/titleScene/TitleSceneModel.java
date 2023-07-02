package uectd.game.titleScene;

import uectd.game.gameScene.GameScene;
import uectd.gameSystem.SceneChangeListener;
import uectd.gameSystem.SceneModel;
import uectd.gameSystem.util.Intent;

public class TitleSceneModel extends SceneModel {

    public TitleSceneModel(SceneChangeListener sceneChangeListener) {
        super(sceneChangeListener);
    }

    @Override
    public void calc(float deltaTime) {

    }

    public void gameStart() {
        var intent = new Intent();
        intent.setBooleanValue("CannonTurret", true);
        intent.setBooleanValue("LaserTurret", true);
        intent.setBooleanValue("BallistaTurret", true);
        sceneChangeListener.sceneChanged(new GameScene(sceneChangeListener), intent);
    }

    public void exit() {
        System.exit(0);
    }

}
