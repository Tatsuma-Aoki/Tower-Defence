package uectd.gameSystem.util;

import uectd.gameSystem.FatalError;

public class Vector2 implements Cloneable {
    private static final double EPS = 1e-9;

    public static final int COUNTER_CLOCKWISE = 1;
    public static final int CLOCKWISE = -1;
    public static final int ONLINE_BACK = 2;
    public static final int ONLINE_FRONT = -2;
    public static final int ON_SEGMENT = 0;

    public double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this(0., 0.);
    }

    public String toString() {
        return String.format("[%f, %f]", x, y);
    }

    public void add(Vector2 vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public static Vector2 sum(Vector2 vector1, Vector2 vector2) {
        return new Vector2(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    public void sub(Vector2 vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    public static Vector2 diff(Vector2 a, Vector2 b) {
        return new Vector2(a.x - b.x, a.y - b.y);
    }

    public double norm() {
        return x * x + y * y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalized() { // 正規化
        double m = this.magnitude();
        if (Math.abs(m) < EPS) {
            return new Vector2();
        }
        return new Vector2(this.x / m, this.y / m);
    }

    public static Vector2 scale(Vector2 v, double s) {
        return new Vector2(v.x * s, v.y * s);
    }

    public static double dot(Vector2 a, Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double cross(Vector2 a, Vector2 b) {
        return a.x * b.y - a.y * b.x;
    }

    public static int ccw(Vector2 p0, Vector2 p1, Vector2 p2) {
        Vector2 v1 = new Vector2(p1.x - p0.x, p1.y - p0.y);
        Vector2 v2 = new Vector2(p2.x - p0.x, p2.y - p0.y);
        double c = cross(v1, v2);
        if (c > EPS)
            return COUNTER_CLOCKWISE;
        if (c < -EPS)
            return CLOCKWISE;
        if (dot(v1, v2) < -EPS)
            return ONLINE_BACK;
        if (v1.norm() < v2.norm())
            return ONLINE_FRONT;
        return ON_SEGMENT;
    }

    @Override
    public Vector2 clone() {
        Vector2 v = null;
        try {
            v = (Vector2) super.clone();
        } catch (CloneNotSupportedException ce) {
            ce.printStackTrace();
            FatalError.quit("オブジェクトのクローンに失敗しました");
        }
        return v;
    }
}
