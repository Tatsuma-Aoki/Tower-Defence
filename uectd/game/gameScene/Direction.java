package uectd.game.gameScene;

import uectd.gameSystem.util.Vector2;

public class Direction {
    public static final int NORTH = 0;
    public static final int NORTH_WEST = 1;
    public static final int WEST = 2;
    public static final int NORTH_EAST = 3;
    public static final int EAST = 4;
    public static final int SOUTH_WEST = 5;
    public static final int SOUTH = 6;
    public static final int SOUTH_EAST = 7;

    private static final double PI_1_8 = Math.PI * 1 / 8;
    private static final double PI_3_8 = Math.PI * 3 / 8;
    private static final double PI_5_8 = Math.PI * 5 / 8;
    private static final double PI_7_8 = Math.PI * 7 / 8;

    public static int convertVectorToConstant(Vector2 vector) {
        return convertAngleToConstant(Math.atan2(vector.y, vector.x));
    }

    public static int convertAngleToConstant(double angle) {
        if (0 <= angle) {
            if (angle <= PI_5_8) {
                if (angle <= PI_3_8) {
                    if (angle <= PI_1_8) {
                        return EAST;
                    }
                    return NORTH_EAST;
                }
                return NORTH;
            }
            if (angle <= PI_7_8) {
                return NORTH_WEST;
            }
        } else {
            if (-PI_5_8 <= angle) {
                if (-PI_3_8 <= angle) {
                    if (-PI_1_8 <= angle) {
                        return EAST;
                    }
                    return SOUTH_EAST;
                }
                return SOUTH;
            }
            if (-PI_7_8 <= angle) {
                return SOUTH_WEST;
            }
        }
        return WEST;
    }
}
