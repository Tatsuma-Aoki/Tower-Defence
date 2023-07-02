package uectd.game.gameScene.gameMain.enemy;

import uectd.gameSystem.util.Vector2;

public class EnemyAnimation {
    private long startTime;
    private int period, moveStartTime;
    private double moveLength;

    public EnemyAnimation(int period, int moveStartTime, double moveLength) {
        this.period = period;
        this.moveStartTime = moveStartTime;
        this.moveLength = moveLength;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public Vector2 getOffset(double angle) {
        int time = (int) ((System.currentTimeMillis() - startTime) % period);
        if (time < moveStartTime) {
            return new Vector2();
        }
        double r = moveLength * Math.sin((time - moveStartTime) * Math.PI / (period - moveStartTime));
        return new Vector2(r * Math.cos(angle), r * Math.sin(angle));
    }
}
