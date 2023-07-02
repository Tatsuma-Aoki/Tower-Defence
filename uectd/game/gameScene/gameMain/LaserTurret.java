package uectd.game.gameScene.gameMain;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.GameTimer;
import uectd.gameSystem.util.ImageManager;
import uectd.gameSystem.util.SoundManager;
import uectd.game.ResourcePathDefines;

import java.util.ArrayList;
import java.util.HashSet;

import java.awt.event.*;

public class LaserTurret extends BaseTurret implements ActionListener {
    private static final double TURRET_RADIUS = 50; // 見た目の大きさ
    private int attackPowerLevelupPrice;
    private int turretCollisionRadiusLevelupPrice;
    private int rofLevelupPrice;
    private int totalPurchasePrice = 0;
    ArrayList<GameObject> nearEnemies;
    private final static double LEVELUP_ATTACK_POWER = 0.2;
    private final static double LEVELUP_COLLISION_RADIUS = 10;
    private final static double INTERCEPT_COLLISION_RADIUS = 150;
    private final static int LEVELUP_LANDING_TIME = 50;
    private final static int LEVELUP_PRICE = 10;
    private final static int SALE_PRICE = 10;
    private static final double INTERCEPT_ATTACK_POWER = 1;
    private SearchEnemyField searchEnemyField;
    private GameTimer timer;

    public LaserTurret(GameObject root, GameObject parent, IMoneyTransfer iMoneyTransfer) {
        super(root, parent, "Laser", INTERCEPT_COLLISION_RADIUS + LEVELUP_COLLISION_RADIUS,
                INTERCEPT_ATTACK_POWER + LEVELUP_ATTACK_POWER, 1000, 10, 100,
                ImageManager.getInstance().getDivImage(ResourcePathDefines.LASER_TURRET_IMAGE, 1, 1, 64, 64),
                iMoneyTransfer);
        depth = 1;
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        timer.stop();
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
        this.collider = new CircleCollider(this, TURRET_RADIUS);
        searchEnemyField = new SearchEnemyField(root, this);
        searchEnemyField.setRadius(getTurretCollisionRadius());
        this.addChild(searchEnemyField);
        searchEnemyField.position = this.position;
        this.timer = new GameTimer(getRof(), this);
        this.timer.start();
    }

    @Override
    public void upAttackLevel() {
        super.upAttackLevel();
        attackPower = INTERCEPT_ATTACK_POWER + LEVELUP_ATTACK_POWER * getAttackLevel();
        for (GameObject child : children) {
            if (child instanceof Laser) {
                ((Laser) child).attackPower = attackPower;
            }
        }
    }

    @Override
    public void upRangeLevel() {
        super.upRangeLevel();
        turretCollisionRadius = INTERCEPT_COLLISION_RADIUS + LEVELUP_COLLISION_RADIUS * getRangeLevel();
        searchEnemyField.setRadius(turretCollisionRadius);
        for (GameObject child : children) {
            if (child instanceof Laser) {
                ((Laser) child).radius = turretCollisionRadius;
            }
        }
    }

    @Override
    public void upRofLevel() {
        int prevRofLevel = getRofLevel();
        super.upRofLevel();
        if (prevRofLevel != getRofLevel()) {
            rof = (int) (1000 - LEVELUP_LANDING_TIME * getRofLevel());
            timer.pause();
            int nextInitialDelay = timer.getInitialDelay();
            timer.stop();
            timer = new GameTimer(rof, this);
            timer.setInitialDelay(nextInitialDelay);
            timer.start();
        }
    }

    @Override
    public int getAttackPowerLevelupPrice() {
        attackPowerLevelupPrice = 20 + LEVELUP_PRICE * getAttackLevel();
        totalPurchasePrice += attackPowerLevelupPrice;
        return attackPowerLevelupPrice;
    }

    @Override
    public int getTurretCollisionRadiusLevelupPrice() {
        turretCollisionRadiusLevelupPrice = 20 + LEVELUP_PRICE * getRangeLevel();
        totalPurchasePrice += turretCollisionRadiusLevelupPrice;
        return turretCollisionRadiusLevelupPrice;
    }

    @Override
    public int getRofLevelupPrice() {
        rofLevelupPrice = 20 + LEVELUP_PRICE * getRofLevel();
        totalPurchasePrice += rofLevelupPrice;
        return rofLevelupPrice;
    }

    @Override
    public int getSalePrice() {
        return (purchasePrice + totalPurchasePrice) * 8 / 10;
    }

    public void hitLaser() {
        nearEnemies = searchEnemyField.searchNearEnemies();
        var enemySetInAttack = new HashSet<BaseEnemy>();
        for (var child : children) {
            if (child instanceof Laser && child.getEnabled()) {
                enemySetInAttack.add(((Laser) child).nearEnemy);
            }
        }
        for (GameObject nearEnemy : nearEnemies) {
            if (nearEnemy != null && !enemySetInAttack.contains(nearEnemy)) {
                Laser laser = new Laser(root, parent, attackPower, getTurretCollisionRadius(), this,
                        (BaseEnemy) nearEnemy);
                laser.position = this.position.clone();
                SoundManager.getInstance().play(ResourcePathDefines.LASER_SOUND_PATH);
                this.addChild(laser);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer)
            this.hitLaser();
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
    public void onSelected() {
        super.onSelected();
        searchEnemyField.setDisplayFlag(true);
    }

    @Override
    public void onUnSelected() {
        super.onUnSelected();
        searchEnemyField.setDisplayFlag(false);
    }
}
