package uectd.gameSystem;

import uectd.gameSystem.util.Intent;

public interface SceneChangeListener {
    void sceneChanged(Scene scene, boolean stackClearFlag, Intent intent);

    void sceneChanged(Scene scene, Intent intent);

    void scenePopped(Intent intent);

    void scenePushed(Scene scene, Intent intent);
}
