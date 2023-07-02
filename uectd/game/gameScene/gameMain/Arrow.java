package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.game.gameScene.Direction;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Sprite;
import uectd.gameSystem.util.Vector2;

import java.awt.*;

public class Arrow extends Sprite implements IAttackable {
    private double attackPower;
    private double radius;
    private double cannonballSpeed;
    private IAttackable attacker;
    private GameObject enemyParentObject;
    private BaseEnemy nearestEnemy;
    private Image[] images;

    public Arrow(GameObject root, GameObject parent, double attackPower, int landingTime, BallistaTurret BallistaTurret,
            BaseEnemy nearestEnemy) {
        super(root, parent);
        this.attackPower = attackPower;
        this.radius = 20;
        this.cannonballSpeed = 400;
        this.collider = new CircleCollider(this, radius);
        this.attacker = BallistaTurret;
        this.nearestEnemy = nearestEnemy;
        this.images = ImageManager.getInstance().getDivImage(ResourcePathDefines.ARROW_IMAGES, 8, 1, 64, 64);
        setImage(this.images[0]);
        this.depth = 2;
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
    }

    @Override
    public void calc(float deltaTime) {
        super.calc(deltaTime);
        if (nearestEnemy.getEnabled()) {
            Vector2 diff = Vector2.diff(nearestEnemy.position, this.position);
            setImage(images[Direction.convertAngleToConstant(
                    Math.atan2(nearestEnemy.position.y - position.y, nearestEnemy.position.x - position.x))]);
            if (diff.norm() < radius * radius) {
                nearestEnemy.applyDamage(new Damage(attackPower, this));
                // TODO 被弾エフェクト
                this.destroy();
                return;
            }
            Vector2 direction = diff.normalized();
            position.add(Vector2.scale(direction, cannonballSpeed * deltaTime));
        } else {
            destroy();
        }
    }

}
