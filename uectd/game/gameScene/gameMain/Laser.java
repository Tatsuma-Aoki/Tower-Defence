package uectd.game.gameScene.gameMain;

import uectd.game.ResourcePathDefines;
import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.Drawable;
import uectd.gameSystem.util.GameTimer;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.Vector2;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import java.awt.*;
import java.awt.event.*;

public class Laser extends Drawable implements IAttackable, EnemyDieListener, ActionListener { // Spriteだと回転が面倒なのでDrawableをそのまま使う
    protected double attackPower;
    protected double radius;
    private IAttackable attacker;
    private GameObject enemyParentObject;
    protected BaseEnemy nearEnemy;
    private GameTimer timer;

    private Image image;

    public Laser(GameObject root, GameObject parent, double attackPower, double radius, LaserTurret laserTurret,
            BaseEnemy nearEnemy) {
        super(root, parent);
        this.attackPower = attackPower;
        this.radius = radius;
        this.collider = new CircleCollider(this, radius);
        this.attacker = laserTurret;
        this.nearEnemy = nearEnemy;
        this.image = ImageManager.getInstance().getImage(ResourcePathDefines.LASER_IMAGE_PATH);
        this.depth = 2;
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
        timer = new GameTimer(300, this);
        timer.start();
    }

    @Override
    public void pause() {
        super.pause();
        timer.pause();
    }

    @Override
    public void unpause() {
        super.unpause();
        timer.unpause();
    }

    @Override
    public void calc(float deltaTime) {
        super.calc(deltaTime);
        if (nearEnemy.getEnabled()) {
            Vector2 diff = Vector2.diff(nearEnemy.position, this.position);
            if (diff.norm() > radius * radius) {
                this.destroy();
            }
        } else {
            this.destroy();
        }
    }

    @Override
    public void draw(Graphics g, Vector2 screenPosition, float ratio) {
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalAffineTransform = g2.getTransform();
        AffineTransform affineTransform = (AffineTransform) originalAffineTransform.clone();
        double dist = Math.sqrt((nearEnemy.position.y - this.position.y) * (nearEnemy.position.y - this.position.y)
                + (nearEnemy.position.x - this.position.x) * (nearEnemy.position.x - this.position.x));
        affineTransform
                .rotate(Math.atan2(nearEnemy.position.y - this.position.y, nearEnemy.position.x - this.position.x)
                        - Math.toRadians(90), screenPosition.x, screenPosition.y);
        g2.setTransform(affineTransform);
        g2.drawImage(this.image, (int) (screenPosition.x - 200 * ratio * 0.5), (int) (screenPosition.y),
                (int) (200 * ratio), (int) (dist * ratio), null);
        g2.setTransform(originalAffineTransform); // アフィン変換行列を元に戻す
    }

    @Override
    public void onEnemyDead(BaseEnemy enemy) {
        this.destroy();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            nearEnemy.applyDamage(new Damage(attackPower, this));
        }
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        timer.stop();
    }
}
