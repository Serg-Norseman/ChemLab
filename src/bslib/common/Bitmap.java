/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Sergey V. Zhdanovskih.
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class Bitmap extends BufferedImage
{
    public Bitmap(int width, int height)
    {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public static Bitmap makeTransparent(BufferedImage source)
    {
        Color transparentColor = new Color(source.getRGB(0, 0));
        final Image imageWithTransparency = makeColorTransparent(source, transparentColor);
        final Bitmap transparentImage = imageToBufferedImage(imageWithTransparency);
        return transparentImage;
    }

    public static Bitmap makeTransparent(BufferedImage source, Color transparentColor)
    {
        final Image imageWithTransparency = makeColorTransparent(source, transparentColor);
        final Bitmap transparentImage = imageToBufferedImage(imageWithTransparency);
        return transparentImage;
    }

    /**
     * Convert Image to BufferedImage.
     *
     * @param image Image to be converted to BufferedImage.
     * @return BufferedImage corresponding to provided Image.
     */
    public static Bitmap imageToBufferedImage(final Image image)
    {
        final Bitmap bufferedImage = new Bitmap(image.getWidth(null), image.getHeight(null));
        final Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    /**
     * Make provided image transparent wherever color matches the provided
     * color.
     *
     * @param im BufferedImage whose color will be made transparent.
     * @param color Color in provided image which will be made transparent.
     * @return Image with transparency applied.
     */
    private static Image makeColorTransparent(final BufferedImage im, final Color color)
    {
        int rgb = color.getRGB();
        
        final ImageFilter filter = new RGBImageFilter()
        {
            // the color we are looking for (white)... Alpha bits are set to opaque
            public int markerRGB = rgb/* | 0xFFFFFFFF*/;

            @Override
            public final int filterRGB(final int x, final int y, final int rgb)
            {
                if ((rgb/* | 0xFF000000*/) == markerRGB) {
                    // Mark the alpha bits as zero - transparent  
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do  
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}
