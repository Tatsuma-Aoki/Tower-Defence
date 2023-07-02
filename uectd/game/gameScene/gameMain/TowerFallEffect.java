package uectd.game.gameScene.gameMain;

import java.awt.event.*;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Effect;
import uectd.gameSystem.util.GameTimer;
import uectd.gameSystem.util.Vector2;

public class TowerFallEffect extends Effect implements ActionListener {

    private final static int DELAY = 250;

    private int bombCount;
    private GameTimer timer;

    public TowerFallEffect(GameObject root, GameObject parent) {
        super(root, parent, null, true, 0.0f, 0);
        timer = new GameTimer(DELAY, this);
        timer.start();
    }

    @Override
    protected void onSummoned() {
        super.onSummoned();
        bombCount = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bombCount++;
        if (bombCount == 10) {
            timer.stop();
            destroy();
        } else {
            var sub = new TowerFallEffectSub(root, parent);
            sub.position = Vector2.sum(position, new Vector2(Math.random() * 200 - 100, Math.random() * 200 - 100));
            this.addChild(sub);
        }
    }

}
