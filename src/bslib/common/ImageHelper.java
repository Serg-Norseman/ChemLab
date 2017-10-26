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
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Common color and image utilities.
 *
 * @author Sergey V. Zhdanovskih
 */
public class ImageHelper
{
    public static ImageIcon loadIcon(Class baseClass, String resName)
    {
        try {
            Bitmap img = loadBitmap(baseClass, resName, true);
            return new ImageIcon(img);
        } catch (Exception ex) {
            Logger.write("ImageHelper.loadIcon(): " + ex.getMessage());
        }
        return null;
    }

    public static ImageIcon loadIcon(Class baseClass, String resName, Color transparentColor)
    {
        try {
            Bitmap img = loadBitmap(baseClass, resName, transparentColor);
            return new ImageIcon(img);
        } catch (Exception ex) {
            Logger.write("ImageHelper.loadIcon(): " + ex.getMessage());
        }
        return null;
    }

    /**
     * Attention: For use in ChemLab, this method automatically inserted the
     * path `/resources/res/`, and baseClass = AuxUtils.class.
     *
     * @param baseClass
     * @param resName
     * @return
     */
    public static BufferedImage loadImage(Class baseClass, String resName)
    {
        try {
            URL url = baseClass.getResource(resName);
            //BufferedImage img = (BufferedImage) Toolkit.getDefaultToolkit().getImage(url);
            BufferedImage img = ImageIO.read(url);
            return img;
        } catch (Exception ex) {
            Logger.write("ImageHelper.loadBitmap(): " + ex.getMessage());
        }
        return null;
    }

    public static Bitmap loadBitmap(Class baseClass, String resName, Color transparentColor)
    {
        try {
            BufferedImage img = loadImage(baseClass, resName);

            Bitmap transparentImage = Bitmap.makeTransparent(img, transparentColor);

            return transparentImage;
        } catch (Exception ex) {
            Logger.write("ImageHelper.loadBitmap.transparent(): " + ex.getMessage());
        }
        return null;
    }

    public static Bitmap loadBitmap(Class baseClass, String resName, boolean transparent)
    {
        try {
            BufferedImage img = loadImage(baseClass, resName);

            Bitmap transparentImage;
            if (transparent) {
                transparentImage = Bitmap.makeTransparent(img);
            } else {
                transparentImage = Bitmap.imageToBufferedImage(img);
            }

            return transparentImage;
        } catch (Exception ex) {
            Logger.write("ImageHelper.loadBitmap.transparent(): " + ex.getMessage());
        }
        return null;
    }
}
