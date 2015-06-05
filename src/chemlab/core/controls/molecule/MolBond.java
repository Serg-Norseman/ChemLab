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
package chemlab.core.controls.molecule;

import chemlab.core.chemical.CLData;
import chemlab.core.chemical.BondKind;
import bslib.common.AuxUtils;
import bslib.common.BaseObject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * 
 * @author Serg V. Zhdanovskih
 * @since 0.4.0
 */
public class MolBond extends BaseObject
{
    private final Molecule fOwner;

    private MolAtom FF_Atom;
    private MolAtom FT_Atom;
    private BondKind fKind;
    private boolean fSelected;

    public MolBond(Molecule owner)
    {
        this.fOwner = owner;
        this.fKind = BondKind.bkSingle;
    }

    public final MolAtom getF_Atom()
    {
        return this.FF_Atom;
    }

    public final void setF_Atom(MolAtom value)
    {
        this.FF_Atom = value;
    }

    public final MolAtom getT_Atom()
    {
        return this.FT_Atom;
    }

    public final void setT_Atom(MolAtom value)
    {
        this.FT_Atom = value;
    }

    public final BondKind getKind()
    {
        return this.fKind;
    }

    public final void setKind(BondKind value)
    {
        if (this.fKind == value) {
            this.fKind = value;
            this.fOwner.getMaster().Repaint();
        }
    }

    public final boolean getSelected()
    {
        return this.fSelected;
    }

    public final void setSelected(boolean value)
    {
        this.fSelected = value;
        this.fOwner.getMaster().Repaint();
        this.fOwner.getMaster().DoChange();
    }

    public final Molecule getOwner()
    {
        return this.fOwner;
    }

    private static void drawLine(Graphics2D canvas, int x1, int y1, int x2, int y2, Color c1, Color c2)
    {
        //Pen pen;
        int x3 = x1 + (x2 - x1) / 2;
        int y3 = y1 + (y2 - y1) / 2;
        if (c1 != c2) {
            canvas.setColor(c1);
            canvas.drawLine(x1, y1, x3, y3);
            canvas.setColor(c2);
            canvas.drawLine(x3, y3, x2, y2);
        } else {
            canvas.setColor(c1);
            canvas.drawLine(x1, y1, x2, y2);
        }
    }

    private Stroke getDashStroke(int dash)
    {
        return new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{dash}, 0);
    }
    
    private void drawHydrogen(Graphics2D canvas, int x1, int y1, int x2, int y2, Color c1, Color c2, int w)
    {
        canvas.setColor(c1);
        //canvas.setStroke(getDashStroke(2);
        //pen.DashStyle = DashStyle.Dot;
        canvas.drawLine(x1, y1, x2, y2);
    }

    private void drawDonorAcceptor(Graphics2D canvas, int x1, int y1, int x2, int y2, Color c1, Color c2, int w)
    {
        canvas.setColor(c1);
        //pen.DashStyle = DashStyle.DashDot;
        canvas.drawLine(x1, y1, x2, y2);
    }

    private void drawSingle(Graphics2D canvas, int x1, int y1, int x2, int y2, Color c1, Color c2, int w)
    {
        canvas.setColor(c1);
        //pen.DashStyle = DashStyle.Solid;
        drawLine(canvas, x1, y1, x2, y2, c1, c2);
    }

    private void drawDouble(Graphics2D canvas, int x1, int y1, int x2, int y2, Color c1, Color c2, int w)
    {
        canvas.setColor(c1);
        //pen.DashStyle = DashStyle.Solid;

        int off;
        if (this.fKind == BondKind.bkDouble) {
            off = 1;
        } else {
            off = 2;
        }

        if ((x2 > x1 && y2 < y1) || (x2 < x1 && y2 > y1)) {
            drawLine(canvas, x1 - off, y1 - off, x2 - off, y2 - off, c1, c2);
            drawLine(canvas, x1 + off, y1 + off, x2 + off, y2 + off, c1, c2);
        } else {
            drawLine(canvas, x1 - off, y1 + off, x2 - off, y2 + off, c1, c2);
            drawLine(canvas, x1 + off, y1 - off, x2 + off, y2 - off, c1, c2);
        }
    }

    private void drawConjugated(Graphics2D canvas, int x1, int y1, int x2, int y2, Color c1, Color c2, int w)
    {
        int off;
        if (this.fKind == BondKind.bkDouble) {
            off = 1;
        } else {
            off = 2;
        }
        if ((x2 > x1 && y2 < y1) || (x2 < x1 && y2 > y1)) {
            canvas.setColor(c1);
            //pen.DashStyle = DashStyle.Solid;
            drawLine(canvas, x1 - off, y1 - off, x2 - off, y2 - off, c1, c2);
            canvas.setColor(c1);
            //pen.DashStyle = DashStyle.Dot;
            drawLine(canvas, x1 + off, y1 + off, x2 + off, y2 + off, c1, c2);
        } else {
            canvas.setColor(c1);
            //pen.DashStyle = DashStyle.Solid;
            drawLine(canvas, x1 - off, y1 + off, x2 - off, y2 + off, c1, c2);
            canvas.setColor(c1);
            //pen.DashStyle = DashStyle.Dot;
            drawLine(canvas, x1 + off, y1 - off, x2 + off, y2 - off, c1, c2);
        }
    }

    public final void draw(Graphics2D canvas)
    {
        if (this.getF_Atom() != null && this.getT_Atom() != null) {
            int X = (int) (Math.round((double) this.getF_Atom().getTX()));
            int Y = (int) (Math.round((double) this.getF_Atom().getTY()));
            int X2 = (int) (Math.round((double) this.getT_Atom().getTX()));
            int Y2 = (int) (Math.round((double) this.getT_Atom().getTY()));

            Color c;
            Color c2;
            int w = 1;
            if (this.fSelected) {
                c = AuxUtils.BGRToRGB(16776960);
                c2 = AuxUtils.BGRToRGB(16776960);
                w = 2;
            } else {
                c = this.getF_Atom().getColor();
                c2 = this.getT_Atom().getColor();
            }

            switch (this.fKind) {
                case bkSingle:
                    this.drawSingle(canvas, X, Y, X2, Y2, c, c2, w);
                    break;
                case bkDouble:
                    this.drawDouble(canvas, X, Y, X2, Y2, c, c2, w);
                    break;
                case bkTriple:
                    this.drawSingle(canvas, X, Y, X2, Y2, c, c2, w);
                    this.drawDouble(canvas, X, Y, X2, Y2, c, c2, w);
                    break;
                case bkConjugated:
                    this.drawConjugated(canvas, X, Y, X2, Y2, c, c2, w);
                    break;
            }
        }
    }

    public final double getLength()
    {
        double dx = this.FT_Atom.getX() - this.FF_Atom.getX();
        double dy = this.FT_Atom.getY() - this.FF_Atom.getY();
        double dz = this.FT_Atom.getZ() - this.FF_Atom.getZ();
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }

    public final void setLength(double newLength)
    {
        Point3D p0 = new Point3D();
        p0.X = this.FF_Atom.getX();
        p0.Y = this.FF_Atom.getY();
        p0.Z = this.FF_Atom.getZ();

        Point3D p = new Point3D();
        p.X = this.FT_Atom.getX();
        p.Y = this.FT_Atom.getY();
        p.Z = this.FT_Atom.getZ();

        double dX = (p.X - p0.X);
        double dY = (p.Y - p0.Y);
        double dZ = (p.Z - p0.Z);
        double len = this.getLength();
        double rate = (newLength / len);

        p.X = (p0.X + dX * rate);
        p.Y = (p0.Y + dY * rate);
        p.Z = (p0.Z + dZ * rate);

        this.FF_Atom.setX(p0.X);
        this.FF_Atom.setY(p0.Y);
        this.FF_Atom.setZ(p0.Z);
        this.FT_Atom.setX(p.X);
        this.FT_Atom.setY(p.Y);
        this.FT_Atom.setZ(p.Z);
    }

    public final void setKindByChar(char value)
    {
        for (BondKind bk : BondKind.values()) {
            if (CLData.BondKinds[bk.getValue()] == value) {
                this.setKind(bk);
                return;
            }
        }
    }
}
