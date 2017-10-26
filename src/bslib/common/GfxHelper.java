/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2017 by Sergey V. Zhdanovskih.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bslib.common;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public final class GfxHelper
{
    private static final double FULL_CIRCLE_RADIANS = Math.toRadians(360d);

    private GfxHelper()
    {
    }

    /**
     * Normalize the input radians in the range 360° > x >= 0°.
     *
     * @param radians The angle to normalize (in radians).
     *
     * @return The angle normalized in the range 360° > x >= 0°.
     */
    public static double normalizeRadians(double radians)
    {
        radians %= FULL_CIRCLE_RADIANS;
        if (radians < 0d) {
            radians += FULL_CIRCLE_RADIANS;
        }
        if (radians == FULL_CIRCLE_RADIANS) {
            radians = 0d;
        }
        return radians;
    }

    /**
     * Returns the point of a given angle (in radians) on a circle.
     *
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     * @param angleRadians The angle (in radians).
     *
     * @return The point of the given angle on the specified circle.
     */
    public static Point2D.Float pointFromAngleRadians(Point2D.Float center,
            float radius, double angleRadians)
    {
        return new Point2D.Float((float) (center.x + radius * Math.cos(angleRadians)),
                (float) (center.y + radius * Math.sin(angleRadians)));
    }

    /**
     * Returns the point of a given angle (in degrees) on a circle.
     *
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     * @param angleDegrees The angle (in degrees).
     *
     * @return The point of the given angle on the specified circle.
     */
    public static Point2D.Float pointFromAngleDegrees(Point2D.Float center,
            float radius, float angleDegrees)
    {
        return pointFromAngleRadians(center, radius, Math.toRadians(angleDegrees));
    }

    /**
     * Adds a circular arc to the given path by approximating it through a cubic
     * Bézier curve.
     *
     * @param path The path to add the arc to.
     * @param center The center of the circle.
     * @param start The starting point of the arc on the circle.
     * @param end The ending point of the arc on the circle.
     * @param moveToStart If {@code true}, move to the starting point.
     */
    public static void addBezierArcToPath(
            GeneralPath path, float centerX, float centerY,
            float startX, float startY, float endX, float endY,
            boolean moveToStart)
    {
        if (moveToStart) {
            path.moveTo(startX, startY);
        }
        if (startX == endX && startY == endY) {
            return;
        }

        final double ax = startX - centerX;
        final double ay = startY - centerY;
        final double bx = endX - centerX;
        final double by = endY - centerY;
        final double q1 = ax * ax + ay * ay;
        final double q2 = q1 + ax * bx + ay * by;
        final double k2 = 4d / 3d * (Math.sqrt(2d * q1 * q2) - q2) / (ax * by - ay * bx);
        final float x2 = (float) (centerX + ax - k2 * ay);
        final float y2 = (float) (centerY + ay + k2 * ax);
        final float x3 = (float) (centerX + bx + k2 * by);
        final float y3 = (float) (centerY + by - k2 * bx);

        path.curveTo(x2, y2, x3, y3, endX, endY);
    }
}
