package uectd.gameSystem.util;

import java.util.ArrayList;

import uectd.gameSystem.FatalError;
import uectd.gameSystem.GameObject;

public abstract class Collider implements Cloneable {

    public GameObject gameObject;

    public Collider(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public abstract boolean isPointIn(Vector2 point);

    public abstract boolean isColliderOn(Collider collider);

    public Vector2 getPosition() {
        return new Vector2(gameObject.position.x + gameObject.offset.x, gameObject.position.y + gameObject.offset.y);
    }

    public ArrayList<GameObject> findCollidedChildren(GameObject parent) {
        var res = new ArrayList<GameObject>();
        for (var gameObject : parent.children) {
            if (gameObject.collider != null && this.isColliderOn(gameObject.collider)) {
                res.add(gameObject);
            }
        }
        return res;
    }

    @Override
    public Collider clone() {
        Collider collider = null;
        try {
            collider = (Collider) super.clone();
        } catch (CloneNotSupportedException ce) {
            ce.printStackTrace();
            FatalError.quit("オブジェクトのクローンに失敗しました");
        }
        return collider;
    }

}
