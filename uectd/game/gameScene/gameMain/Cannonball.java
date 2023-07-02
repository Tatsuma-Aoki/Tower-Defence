package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Sprite;
import uectd.gameSystem.util.Vector2;

public class Cannonball extends Sprite implements IAttackable {
    private double attackPower;
    private double radius;
    private double cannonballSpeed;
    private IAttackable attacker;
    private GameObject enemyParentObject;
    private BaseEnemy nearestEnemy;

    public Cannonball(GameObject root, GameObject parent, double attackPower, int landingTime,
            CannonTurret CannonTurret, BaseEnemy nearestEnemy) {
        super(root, parent);
        this.attackPower = attackPower;
        this.radius = 10;
        this.cannonballSpeed = 200;
        this.collider = new CircleCollider(this, radius);
        this.attacker = CannonTurret;
        this.nearestEnemy = nearestEnemy;
        setImage(ImageManager.getInstance().getImage(ResourcePathDefines.CANNONBALL_IMAGE_PATH));
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
            if (diff.norm() < radius * radius) {
                nearestEnemy.applyDamage(new Damage(attackPower, this));
                var effect = new TurretAttackToEnemyEffect(root, root);
                effect.position = nearestEnemy.position.clone();
                root.addChild(effect);
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
