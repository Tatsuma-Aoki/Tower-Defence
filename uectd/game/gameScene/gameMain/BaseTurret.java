package uectd.game.gameScene.gameMain;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.IClickable;
import uectd.gameSystem.util.Sprite;

import java.awt.*;

public abstract class BaseTurret extends Sprite implements IAttackable, IClickable {
    protected String name;
    protected double turretCollisionRadius;
    protected double attackPower;
    protected int rof;
    protected int attackLevel;
    protected int rangeLevel;
    protected int rofLevel;
    protected int maxLevel;
    protected int purchasePrice;
    protected int levelupPrice;
    protected final static int LEVELUP_PRICE = 10;
    protected Image[] images;
    protected IMoneyTransfer iMoneyTransfer;
    protected GameObject enemyParentObject;

    public BaseTurret(GameObject root, GameObject parent, String name, double turretCollisionRadius, double attackPower,
            int rof, int maxLevel, int purchasePrice, Image[] images, IMoneyTransfer iMoneyTransfer) {
        super(root, parent);
        this.name = name;
        this.turretCollisionRadius = turretCollisionRadius;
        this.attackPower = attackPower;
        this.rof = rof;
        this.attackLevel = 1;
        this.rangeLevel = 1;
        this.rofLevel = 1;
        this.maxLevel = maxLevel;
        this.purchasePrice = purchasePrice;
        this.images = images;
        this.iMoneyTransfer = iMoneyTransfer;
        this.setImage(images[0]);
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
        this.enemyParentObject = root.findChild(EnemyParent.class);
    }

    public void sell() {
        iMoneyTransfer.moneyAdd(this.getSalePrice());
        destroy();
    }

    public double getTurretCollisionRadius() {
        return turretCollisionRadius;
    }

    public double getAttackPower() {
        return attackPower;
    }

    public int getRof() {
        return rof;
    }

    public Image getImage() {
        return images[0];
    }

    public String getName() {
        return this.name;
    }

    public int getAttackLevel() {
        return this.attackLevel;
    }

    public int getRangeLevel() {
        return this.rangeLevel;
    }

    public int getRofLevel() {
        return this.rofLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public abstract int getSalePrice();

    public int getPurchasePrice() {
        return this.purchasePrice;
    }

    public abstract int getAttackPowerLevelupPrice();

    public abstract int getTurretCollisionRadiusLevelupPrice();

    public abstract int getRofLevelupPrice();

    public void onSelected() {
    }

    public void onUnSelected() {
    }

    public void upAttackLevel() {
        if (attackLevel < maxLevel && iMoneyTransfer.tryMoneyPay(getAttackPowerLevelupPrice())) {
            ++attackLevel;
        }
    }

    public void upRangeLevel() {
        if (rangeLevel < maxLevel && iMoneyTransfer.tryMoneyPay(getTurretCollisionRadiusLevelupPrice())) {
            ++rangeLevel;
        }
    }

    public void upRofLevel() {
        if (rofLevel < maxLevel && iMoneyTransfer.tryMoneyPay(getRofLevelupPrice())) {
            ++rofLevel;
        }
    }

    @Override
    public BaseTurret clone() {
        BaseTurret baseTurret = null;
        baseTurret = (BaseTurret) super.clone();
        baseTurret.name = new String(this.name);
        baseTurret.images = this.images.clone();
        return baseTurret;
    }
}
