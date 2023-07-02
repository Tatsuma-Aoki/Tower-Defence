package uectd.gameSystem.util;

import uectd.gameSystem.GameObject;

public class CircleCollider extends Collider {
    public double radius;

    public CircleCollider(GameObject gameObject, double radius) {
        super(gameObject);
        this.radius = radius;
    }

    // 座標がコライダーの中にあるか
    @Override
    public boolean isPointIn(Vector2 point) {
        return ((gameObject.position.x + gameObject.offset.x - point.x)
                * (gameObject.position.x + gameObject.offset.x - point.x)
                + (gameObject.position.y + gameObject.offset.y - point.y)
                        * (gameObject.position.y + gameObject.offset.y - point.y)) < radius * radius;
    }

    // コライダー同士の衝突判定
    @Override
    public boolean isColliderOn(Collider other) {
        if (other instanceof CircleCollider) {
            CircleCollider otherCirc = (CircleCollider) other;
            return Vector2.diff(other.getPosition(), this.getPosition()).norm() < (this.radius + otherCirc.radius)
                    * (this.radius + otherCirc.radius);
        }
        return false;
    }

    @Override
    public CircleCollider clone() {
        CircleCollider collider = null;
        collider = (CircleCollider) super.clone();
        return collider;
    }
}
