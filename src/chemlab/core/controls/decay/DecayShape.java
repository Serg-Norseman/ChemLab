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
package chemlab.core.controls.decay;

import bslib.common.AuxUtils;
import bslib.common.Bitmap;
import bslib.common.ImageHelper;
import chemlab.core.chemical.DecayMode;
import chemlab.core.chemical.CLData;
import chemlab.forms.CommonUtils;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public final class DecayShape extends JComponent
{
    public enum ShapeType
    {
        dskDown,
        dskRite,
        dskDecayMode,
        dskNuclide;
    }

    private Bitmap fImage;
    private ShapeType fType = ShapeType.dskDown;

    public ShapeId Id = ShapeId.DownHead;
    public DecayMode Mode = DecayMode.dmStable;
    public NuclideNode NuclideNode;

    public DecayShape(JComponent parent, int left, int top)
    {
        super();
        this.setOpaque(false);
        this.fImage = new Bitmap(32, 32);
        this.setLocation(left, top);
        this.setSize(32, 32);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
    }

    /*@Override
     protected void Dispose(boolean disposing)
     {
     if (disposing) {
     this.fImage.Dispose();
     }
     super.Dispose(disposing);
     }*/
    public ShapeType getType()
    {
        return this.fType;
    }

    public void setType(ShapeType value)
    {
        this.fType = value;

        String resName = "";
        switch (this.fType) {
            case dskDown:
                resName = this.Id.toString();
                break;

            case dskRite:
                resName = this.Id.toString();
                break;

            case dskDecayMode:
                resName = this.Mode.toString();
                StringBuilder sb = new StringBuilder(resName);
                sb.insert(2, "_");
                resName = sb.toString();
                break;

            case dskNuclide: {
                String sValue = this.NuclideNode.Nuclide;

                int i = 0;
                StringBuilder sbv = new StringBuilder(sValue);
                while (i < sbv.length()) {
                    if (CLData.Numbers.indexOf(sbv.charAt(i)) < 0) {
                        sbv.deleteCharAt(i);
                    } else {
                        i++;
                    }
                }
                sValue = sbv.toString();
                
                int iValue = (int) (AuxUtils.ParseInt(sValue, 0) / 4.0f + 2.75f);

                for (NuclideId nid : NuclideId.values()) {
                    sValue = nid.toString().substring(3);
                    int iNID = AuxUtils.ParseInt(sValue, 0);

                    if (iValue == iNID || (iNID >= iValue - 2 && iNID <= iValue + 2)) {
                        break;
                    }
                }

                resName = "N" + sValue;
            }
            break;
        }

        String lpBitmapName = resName.toLowerCase() + ".bmp";
        this.fImage = ImageHelper.loadBitmap(lpBitmapName, true);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(this.fImage, 0, 0, null);

        g.setColor(Color.black);
        switch (this.fType) {
            case dskNuclide: {
                String sValue = this.NuclideNode.Nuclide;
                g.drawString(sValue, 0, 0);
                break;
            }
        }
    }
}
