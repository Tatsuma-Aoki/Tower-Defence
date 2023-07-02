package uectd.game.gameScene.gameMain;

public class Damage {

    public double attack;
    public IAttackable attacker;

    public Damage(double attack, IAttackable attacker) {
        this.attack = attack;
        this.attacker = attacker;
    }
}