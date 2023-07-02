package uectd.game.gameScene.gameMain;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.CircleCollider;
import uectd.gameSystem.util.GameTimer;
import uectd.gameSystem.util.ImageManager;
import uectd.game.ResourcePathDefines;
import uectd.game.gameScene.Direction;

import java.awt.event.*;

public class BallistaTurret extends BaseTurret implements ActionListener {
    private static final double TURRET_RADIUS = 50; // 見た目の大きさ
    private int attackPowerLevelupPrice;
    private int turretCollisionRadiusLevelupPrice;
    private int rofLevelupPrice;
    private int totalPurchasePrice = 0;
    private final static double LEVELUP_ATTACK_POWER = 0.5;
    private final static double LEVELUP_COLLISION_RADIUS = 20;
    private final static double INTERCEPT_ATTACK_POWER = 20;
    private final static double INTERCEPT_COLLISION_RADIUS = 400;
    private final static int LEVELUP_LANDING_TIME = 50;
    private final static int LEVELUP_PRICE = 10;
    private final static int SALE_PRICE = 10;
    private SearchEnemyField searchEnemyField;
    private GameTimer timer;

    public BallistaTurret(GameObject root, GameObject parent, IMoneyTransfer iMoneyTransfer) {
        super(root, parent, "Ballista", INTERCEPT_COLLISION_RADIUS + LEVELUP_COLLISION_RADIUS,
                INTERCEPT_ATTACK_POWER + LEVELUP_ATTACK_POWER, 2000, 10, 80,
                ImageManager.getInstance().getDivImage(ResourcePathDefines.BALLISTA_TURRET_IMAGES, 8, 1, 64, 64),
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
    }

    @Override
    public void upRangeLevel() {
        super.upRangeLevel();
        System.out.print(turretCollisionRadius + " ");
        turretCollisionRadius = INTERCEPT_COLLISION_RADIUS + LEVELUP_COLLISION_RADIUS * getRangeLevel();
        System.out.println(turretCollisionRadius);
        searchEnemyField.setRadius(turretCollisionRadius);
    }

    @Override
    public void upRofLevel() {
        int prevRofLevel = getRofLevel();
        super.upRofLevel();
        if (prevRofLevel != getRofLevel()) {
            rof = (int) (2000 - LEVELUP_LANDING_TIME * getRofLevel());
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

    public void fireCannonball() {
        BaseEnemy nearestEnemy = searchEnemyField.searchNearestEnemy();

        // enemyの座標にCannonballを置く
        if (nearestEnemy != null) {
            Arrow arrow = new Arrow(root, root, attackPower, rof, this, nearestEnemy);
            arrow.position = this.position.clone();
            root.addChild(arrow);
            setImage(images[Direction.convertAngleToConstant(
                    Math.atan2(nearestEnemy.position.y - position.y, nearestEnemy.position.x - position.x))]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer)
            this.fireCannonball();
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
