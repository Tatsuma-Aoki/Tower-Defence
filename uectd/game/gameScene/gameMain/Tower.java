package uectd.game.gameScene.gameMain;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Sprite;
import uectd.gameSystem.util.Vector2;

import java.awt.*;

public class Tower extends Sprite implements IDamageApplicable {

    private static final double DEFAULT_HP = 1000;
    protected double hp;
    protected String name;
    protected ArrayList<TowerFallListener> towerFallListeners, addList, removeList;
    protected Graph.Vertex vertex;

    public Tower(GameObject root, GameObject parent, Graph.Vertex vertex) {
        super(root, parent);
        this.vertex = vertex;
        this.towerFallListeners = new ArrayList<>();
        this.addList = new ArrayList<>();
        this.removeList = new ArrayList<>();
        this.position = vertex.position;
        this.hp = DEFAULT_HP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHp() {
        return this.hp;
    }

    public double getMaxHp() {
        return DEFAULT_HP;
    }

    public Graph.Vertex getVertex() {
        return vertex;
    }

    @Override
    public void calc(float deltaTime) {
        super.calc(deltaTime);
        if (!addList.isEmpty()) {
            for (var towerFallListener : addList) {
                towerFallListeners.add(towerFallListener);
            }
            addList.clear();
        }
        if (!removeList.isEmpty()) {
            for (var towerFallListener : removeList) {
                towerFallListeners.remove(towerFallListener);
            }
            removeList.clear();
        }
    }

    @Override
    public void applyDamage(Damage d) {
        if (getEnabled()) {
            hp -= d.attack;
            if (hp <= 0) {
                hp = 0;
                for (TowerFallListener towerFallListener : towerFallListeners) {
                    towerFallListener.onTowerFall(this);
                }
                addList.clear();
                removeList.clear();
                towerFallListeners.clear();
                this.destroy();

                var effect = new TowerFallEffect(root, root);
                effect.position = this.position.clone();
                root.addChild(effect);
            }
        }
    }

    public void addTowerFallListener(TowerFallListener towerFallListener) {
        addList.add(towerFallListener);
    }

    public void removeTowerFallListener(TowerFallListener towerFallListener) {
        removeList.add(towerFallListener);
    }

    @Override
    public void draw(Graphics g, Vector2 screenPosition, float ratio) {
        super.draw(g, screenPosition, ratio);
        g.setColor(new Color(50, 50, 240, 120));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setStroke(new BasicStroke((int) (20 * ratio), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g2d.setColor(Color.GRAY);
        g2d.drawOval((int) screenPosition.x, (int) screenPosition.y - (int) (80 * ratio), (int) (90 * ratio),
                (int) (90 * ratio));
        g2d.setStroke(new BasicStroke((int) (13 * ratio), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        if (hp > DEFAULT_HP * 0.5) {
            g.setColor(Color.GREEN);
        } else if (hp > DEFAULT_HP * 0.3) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g2d.drawArc((int) screenPosition.x, (int) screenPosition.y - (int) (80 * ratio), (int) (90 * ratio),
                (int) (90 * ratio), 90, (int) (360 * (getHp() / getMaxHp())));
    }
}
