package uectd.game.gameScene.gameMain;

import java.util.ArrayList;
import java.awt.*;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.Sprite;
import uectd.gameSystem.util.Vector2;

public class SearchEnemyField extends Sprite {
    private boolean isDisplaying;
    private EnemyParent enemyParent;

    public SearchEnemyField(GameObject root, GameObject parent) {
        super(root, parent);
        collider = new CircleCollider(this, 0);
        depth = 4;
    }

    @Override
    public void calc(float deltaTime) {
        super.calc(deltaTime);
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
        this.enemyParent = (EnemyParent) root.findChild(EnemyParent.class);
    }

    public void setRadius(double radius) {
        ((CircleCollider) collider).radius = radius;
        this.setDrawSize(new Vector2(radius * 2, radius * 2));
    }

    public void setDisplayFlag(boolean isDisplaying) {
        this.isDisplaying = isDisplaying;
        if (isDisplaying) {
            setImage(ResourcePathDefines.ENEMY_SEARCH_FIELD_IMAGE, false);
        } else {
            setImage((Image) null, false);
        }
    }

    public ArrayList<GameObject> searchNearEnemies() {
        return this.collider.findCollidedChildren(this.enemyParent);
    }

    public BaseEnemy searchNearestEnemy() {
        BaseEnemy nearestEnemy = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        ArrayList<GameObject> collisionEnemies = searchNearEnemies();
        for (GameObject gameObject : collisionEnemies) {
            if (gameObject instanceof BaseEnemy) {
                // range内でTurretから最も近いenemyを選択
                double distance = Vector2.diff(this.position, gameObject.position).norm();
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestEnemy = (BaseEnemy) gameObject;
                }
            }
        }
        return nearestEnemy;
    }
}
