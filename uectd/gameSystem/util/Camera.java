package uectd.gameSystem.util;

import uectd.gameSystem.GameObject;

public class Camera extends GameObject {
    public float ratio;

    public Camera(GameObject root, GameObject parent, float ratio) {
        super(root, parent);
        this.ratio = ratio;
    }

}
