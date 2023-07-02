package uectd.game.gameScene.gameMain;

public interface IMoneyTransfer {
    void moneyAdd(int amount);

    boolean tryMoneyPay(int amount);

    int getBalance();

}