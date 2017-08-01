/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-1998, 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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
package chemlab.core;

import bslib.common.Bitmap;
import bslib.common.ImageHelper;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class CLUtils
{
    public static ImageIcon loadIcon(String resName)
    {
        return ImageHelper.loadIcon(CLUtils.class, "/resources/res/" + resName);
    }

    public static ImageIcon loadIcon(String resName, Color transparentColor)
    {
        return ImageHelper.loadIcon(CLUtils.class, "/resources/res/" + resName, transparentColor);
    }

    public static BufferedImage loadImage(String resName)
    {
        return ImageHelper.loadImage(CLUtils.class, "/resources/res/" + resName);
    }

    public static Bitmap loadBitmap(String resName, Color transparentColor)
    {
        return ImageHelper.loadBitmap(CLUtils.class, "/resources/res/" + resName, transparentColor);
    }

    public static Bitmap loadBitmap(String resName, boolean transparent)
    {
        return ImageHelper.loadBitmap(CLUtils.class, "/resources/res/" + resName, transparent);
    }
}
