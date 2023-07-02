package uectd.gameSystem.util;

import javax.swing.Timer;
import java.awt.event.*;

public class GameTimer extends Timer implements ActionListener {
    private long offsetTime;// ポーズ時に次回の初期遅延を計算するためのオフセット時間
    private int initialDelay;
    private boolean isRunning;

    public GameTimer(int delay, ActionListener listener) {
        super(delay, listener);
        this.addActionListener(this);
        this.initialDelay = delay;
    }

    public void start() {
        this.offsetTime = System.currentTimeMillis();
        this.initialDelay = getDelay();
        this.isRunning = true;
        super.start();
    }

    public void stop() {
        this.isRunning = false;
        super.stop();
    }

    public void pause() {
        long nowTime = System.currentTimeMillis();
        initialDelay -= nowTime - offsetTime;
        super.stop();
        super.setInitialDelay(Math.max(0, initialDelay));
        this.offsetTime = nowTime;
    }

    public void unpause() {
        this.offsetTime = System.currentTimeMillis();
        if (isRunning)
            super.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.offsetTime = System.currentTimeMillis();
        this.initialDelay = getDelay();
    }

    @Override
    public void setInitialDelay(int initialDelay) {
        super.setInitialDelay(initialDelay);
        this.initialDelay = initialDelay;
    }

    @Override
    public int getInitialDelay() {
        return super.getInitialDelay();
    }
}
