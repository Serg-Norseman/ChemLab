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
package chemlab.core.controls;

import bslib.common.Bitmap;
import chemlab.core.CLUtils;
import chemlab.core.chemical.CrystalKind;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.3.0
 */
public final class CrystalViewer extends JPanel
{
    private CrystalKind fCrystal;
    private final Bitmap[] fImages;

    public CrystalViewer()
    {
        super();
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setSize(40, 40);

        this.fImages = new Bitmap[8];
        this.fCrystal = CrystalKind.Cubic;

        for (CrystalKind i : CrystalKind.values()) {
            String name = "cs_" + i.toString() + ".bmp";
            Bitmap bmp = (Bitmap)CLUtils.loadBitmap(name.toLowerCase(), true);
            this.fImages[i.getValue()] = bmp;
        }
    }

    public final void setCrystal(CrystalKind value)
    {
        if (this.fCrystal != value) {
            this.fCrystal = value;
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Bitmap image = this.fImages[this.fCrystal.getValue()];
        int dx = (this.getWidth() - image.getWidth()) / 2;
        int dy = (this.getHeight() - image.getHeight()) / 2;

        g.drawImage(image, dx, dy, null);
    }
}
