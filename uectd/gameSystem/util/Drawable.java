package uectd.gameSystem.util;

import java.awt.*;

import uectd.gameSystem.GameObject;

public abstract class Drawable extends GameObject {
    public Drawable(GameObject root, GameObject parent) {
        super(root, parent);
    }

    public abstract void draw(Graphics g, Vector2 screenPosition, float ratio);

}
