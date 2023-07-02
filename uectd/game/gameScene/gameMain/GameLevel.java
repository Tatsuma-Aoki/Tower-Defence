package uectd.game.gameScene.gameMain;

import java.awt.*;

import uectd.gameSystem.GameObject;
import uectd.gameSystem.util.Drawable;
import uectd.gameSystem.util.Vector2;

public class GameLevel extends Drawable {

    public Graph graph;

    public GameLevel(GameObject root, GameObject parent, Graph graph) {
        super(root, parent);
        this.graph = graph;
        this.depth = 9;
    }

    @Override
    public void draw(Graphics g, Vector2 screenPosition, float ratio) {
        Graphics2D g2 = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(6 * ratio);
        Color color = new Color(0, 255, 255, 20);
        g2.setStroke(bs);
        g2.setColor(color);
        for (var edges : graph.data) {
            for (var edge : edges) {
                int x1 = (int) ((screenPosition.x + graph.vertexes.get(edge.from).position.x * ratio));
                int y1 = (int) ((screenPosition.y + graph.vertexes.get(edge.from).position.y * ratio));
                int x2 = (int) ((screenPosition.x + graph.vertexes.get(edge.to).position.x * ratio));
                int y2 = (int) ((screenPosition.y + graph.vertexes.get(edge.to).position.y * ratio));
                g2.drawLine(x1, y1, x2, y2);
            }
        }
        g2.setColor(new Color(240, 240, 40, 80));
        for (var vertex : graph.vertexes) {
            int x = (int) (screenPosition.x + (vertex.position.x - 5) * ratio);
            int y = (int) (screenPosition.y + (vertex.position.y - 5) * ratio);
            int l = (int) (10 * ratio);
            g2.drawOval(x, y, l, l);
        }
    }

}
