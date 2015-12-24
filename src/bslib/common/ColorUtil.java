/*
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program; if not, write to the Free 
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */
package bslib.common;

import java.awt.Color;

/**
 * Common color utilities.
 *
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */
public class ColorUtil
{
    public static Color BGRToRGB(int bgrColor)
    {
        int r = (bgrColor >> 0) & 0xFF;
        int g = (bgrColor >> 8) & 0xFF;
        int b = (bgrColor >> 16) & 0xFF;
        return new Color(r, g, b);
    }

    public static Color newColor(int r, int g, int b)
    {
        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = 255;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = 255;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = 255;
        }

        return new Color(r, g, b);
    }
    
    /**
     * Blend two colors.
     *
     * @param color First color to blend.
     * @param backColor Second color to blend.
     * @param ratio Blend ratio. 0.5 will give even blend, 1.0 will return
     * color1, 0.0 will return color2 and so on.
     * @return Blended color.
     */
    public static Color blend(Color color, Color backColor, double ratio)
    {
        double invRatio = (1.0 - ratio);
        
        int col = color.getRGB();
        int r1 = (col >> 16) & 0xFF;
        int g1 = (col >> 8) & 0xFF;
        int b1 = (col) & 0xFF;

        int backCol = color.getRGB();
        int r2 = (backCol >> 16) & 0xFF;
        int g2 = (backCol >> 8) & 0xFF;
        int b2 = (backCol) & 0xFF;

        int r = (int) ((r1 * ratio) + r2 * invRatio);
        int g = (int) ((g1 * ratio) + g2 * invRatio);
        int b = (int) ((b1 * ratio) + b2 * invRatio);

        return newColor(r, g, b);
    }

    /**
     * Make an even blend between two colors.
     *
     * @param c1 First color to blend.
     * @param c2 Second color to blend.
     * @return Blended color.
     */
    public static Color blend(Color color1, Color color2)
    {
        return ColorUtil.blend(color1, color2, 0.5);
    }

    /**
     * Make a color darker.
     *
     * @param color Color to make darker.
     * @param fraction Darkness fraction.
     * @return Darker color.
     */
    public static Color darker(Color color, float fraction)
    {
        float factor = (1.0f - fraction);
        int rgb = color.getRGB();

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb >> 0) & 0xFF;
        int alpha = (rgb >> 24) & 0xFF;

        red = (int) (red * factor);
        green = (int) (green * factor);
        blue = (int) (blue * factor);

        red = (red < 0) ? 0 : red;
        green = (green < 0) ? 0 : green;
        blue = (blue < 0) ? 0 : blue;

        return new Color(red, green, blue, alpha);
    }

    /**
     * Make a color lighter.
     *
     * @param color Color to make lighter.
     * @param fraction Darkness fraction.
     * @return Lighter color.
     */
    public static Color lighter(Color color, double fraction)
    {
        int red = (int) Math.round(color.getRed() * (1.0 + fraction));
        int green = (int) Math.round(color.getGreen() * (1.0 + fraction));
        int blue = (int) Math.round(color.getBlue() * (1.0 + fraction));

        if (red < 0) {
            red = 0;
        } else if (red > 255) {
            red = 255;
        }
        if (green < 0) {
            green = 0;
        } else if (green > 255) {
            green = 255;
        }
        if (blue < 0) {
            blue = 0;
        } else if (blue > 255) {
            blue = 255;
        }

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);
    }

    /**
     * Return the hex name of a specified color.
     *
     * @param color Color to get hex name of.
     * @return Hex name of color: "rrggbb".
     */
    public static String getHexName(Color color)
    {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        String rHex = Integer.toString(r, 16);
        String gHex = Integer.toString(g, 16);
        String bHex = Integer.toString(b, 16);

        return (rHex.length() == 2 ? "" + rHex : "0" + rHex)
                + (gHex.length() == 2 ? "" + gHex : "0" + gHex)
                + (bHex.length() == 2 ? "" + bHex : "0" + bHex);
    }

    /**
     * Return the "distance" between two colors. The rgb entries are taken to be
     * coordinates in a 3D space [0.0-1.0], and this method returnes the
     * distance between the coordinates for the first and second color.
     *
     * @param r1, g1, b1 First color.
     * @param r2, g2, b2 Second color.
     * @return Distance bwetween colors.
     */
    public static double colorDistance(double r1, double g1, double b1,
            double r2, double g2, double b2)
    {
        double a = r2 - r1;
        double b = g2 - g1;
        double c = b2 - b1;

        return Math.sqrt(a * a + b * b + c * c);
    }

    /**
     * Return the "distance" between two colors.
     *
     * @param color1 First color [r,g,b].
     * @param color2 Second color [r,g,b].
     * @return Distance bwetween colors.
     */
    public static double colorDistance(double[] color1, double[] color2)
    {
        return ColorUtil.colorDistance(color1[0], color1[1], color1[2],
                color2[0], color2[1], color2[2]);
    }

    /**
     * Return the "distance" between two colors.
     *
     * @param color1 First color.
     * @param color2 Second color.
     * @return Distance between colors.
     */
    public static double colorDistance(Color color1, Color color2)
    {
        float rgb1[] = new float[3];
        float rgb2[] = new float[3];

        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);

        return ColorUtil.colorDistance(rgb1[0], rgb1[1], rgb1[2],
                rgb2[0], rgb2[1], rgb2[2]);
    }

    /**
     * Check if a color is more dark than light. Useful if an entity of this
     * color is to be labeled: Use white label on a "dark" color and black label
     * on a "light" color.
     *
     * @param r,g,b Color to check.
     * @return True if this is a "dark" color, false otherwise.
     */
    public static boolean isDark(double r, double g, double b)
    {
        // Measure distance to white and black respectively
        double dWhite = ColorUtil.colorDistance(r, g, b, 1.0, 1.0, 1.0);
        double dBlack = ColorUtil.colorDistance(r, g, b, 0.0, 0.0, 0.0);

        return dBlack < dWhite;
    }

    /**
     * Check if a color is more dark than light. Useful if an entity of this
     * color is to be labeled: Use white label on a "dark" color and black label
     * on a "light" color.
     *
     * @param color Color to check.
     * @return True if this is a "dark" color, false otherwise.
     */
    public static boolean isDark(Color color)
    {
        float r = color.getRed() / 255.0f;
        float g = color.getGreen() / 255.0f;
        float b = color.getBlue() / 255.0f;

        return isDark(r, g, b);
    }
}
