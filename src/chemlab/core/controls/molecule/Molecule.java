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

import chemlab.core.chemical.BondKind;
import chemlab.core.chemical.CompoundSolver;
import bslib.common.BaseObject;
import bslib.common.StringHelper;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * 
 * @author Serg V. Zhdanovskih
 * @since 0.4.0
 */
public class Molecule extends BaseObject
{
    private static class ElementX
    {
        public String Symbol;
        public int Count;

        public ElementX(String symbol, int count)
        {
            this.Symbol = symbol;
            this.Count = count;
        }
    }

    private final MoleculeMaster fMaster;
    private String fName;

    public ArrayList<MolAtom> fAtoms;
    public ArrayList<MolBond> fBonds;

    public Matrix3D axmmat;
    public Matrix3D axmat;
    public Matrix3D tmat;

    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    private double zmin;
    private double zmax;

    public final String getName()
    {
        return this.fName;
    }

    public final void setName(String value)
    {
        this.fName = value;
        this.fMaster.changeContent();
    }

    public final MoleculeMaster getMaster()
    {
        return this.fMaster;
    }

    public final String getFormula()
    {
        String result = "";

        ArrayList<ElementX> elements = new ArrayList<>();
        for (int i = 0; i < this.getAtomCount(); i++) {
            elements.add(new ElementX(this.getAtom(i).getSign(), 1));
        }

        for (int i = 0; i < elements.size(); i++) {
            int j = i + 1;
            while (j <= elements.size() - 1) {
                if (StringHelper.equals(elements.get(j).Symbol, elements.get(i).Symbol)) {
                    elements.get(i).Count++;
                    elements.remove(j);
                } else {
                    j++;
                }
            }
        }

        for (int i = 0; i < elements.size(); i++) {
            int count = elements.get(i).Count;
            String str = String.valueOf(count);
            result = result + elements.get(i).Symbol + str;
            if (i != elements.size() - 1) {
                result += " ";
            }
        }

        elements.clear();

        return result;
    }

    public final double getMolecularMass()
    {
        double result = 0.0;

        String sFormula = this.getFormula();
        
        CompoundSolver compound = this.fMaster.getCompoundMaster();
        if (compound != null && !StringHelper.equals(sFormula, "")) {
            compound.Formula = sFormula;
            compound.Charge = 0;
            compound.analyse();
            compound.loadData();
            compound.calculateMolecularMass();
            result = compound.getMolecularMass();
        }

        return result;
    }

    @Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.fBonds = null;
            this.fAtoms = null;
            this.axmmat = null;
            this.axmat = null;
            this.tmat = null;
        }
        super.dispose(disposing);
    }

    private static float calc(int x1, int y1, int x2, int y2, int X)
    {
        float b;
        float a;
        try {
            b = (((y2 - x2 * y1 / (float) x1) / (1.0f - x2 / (float) x1)));
            a = (((y1 - b) / x1));
        } catch (Exception ex) {
            b = 0f;
            a = 0f;
        }

        return ((a * X + b));
    }

    public final MolAtom getAtom(int index)
    {
        MolAtom result = null;
        if (index >= 0 && index <= this.fAtoms.size() - 1) {
            result = this.fAtoms.get(index);
        }
        return result;
    }

    public final MolBond getBond(int index)
    {
        MolBond result = null;
        if (index >= 0 && index <= this.fBonds.size() - 1) {
            result = this.fBonds.get(index);
        }
        return result;
    }

    public final int getAtomCount()
    {
        return this.fAtoms.size();
    }

    public final int getBondCount()
    {
        return this.fBonds.size();
    }

    public final int getSelectedAtomsCount()
    {
        int result = 0;

        for (int i = 0; i < this.fAtoms.size(); i++) {
            if (this.getAtom(i).getSelected()) {
                result++;
            }
        }
        return result;
    }

    public final MolAtom getSelectedAtom(int index)
    {
        int selectIndex = -1;
        for (MolAtom atom : this.fAtoms) {
            if (atom.getSelected()) {
                selectIndex++;
                if (selectIndex == index) {
                    return atom;
                }
            }
        }

        return null;
    }

    public final MolBond getSelectedBond(int index)
    {
        int selectIndex = -1;
        for (MolBond bond : this.fBonds) {
            if (bond.getSelected()) {
                selectIndex++;
                if (selectIndex == index) {
                    return bond;
                }
            }
        }

        return null;
    }

    public final int getSelectedBondsCount()
    {
        int result = 0;
        for (MolBond bond : this.fBonds) {
            if (bond.getSelected()) {
                result++;
            }
        }
        return result;
    }

    public Molecule(MoleculeMaster master)
    {
        this.fMaster = master;
        this.tmat = new Matrix3D();
        this.axmat = new Matrix3D();
        this.axmmat = new Matrix3D();
        this.fAtoms = new ArrayList<>();
        this.fBonds = new ArrayList<>();
    }

    public final void draw(Graphics2D canvas)
    {
        int aCnt = this.fAtoms.size();

        ArrayList<Integer> ats = new ArrayList<>();

        for (int i = 0; i < aCnt; i++) {
            MolAtom tAtom = this.getAtom(i);
            tAtom.mat.init();
            tAtom.mat.mult(tAtom.amat);
            tAtom.mat.translate(((double) this.fMaster.getWidth() / 2.0), ((double) this.fMaster.getHeight() / 2.0), 0.0);
            tAtom.transform();
            ats.add(0);
        }

        for (int i = 0; i < aCnt; i++) {
            ats.set(i, i);
        }

        for (int i = 0; i < aCnt; i++) {
            for (int j = i + 1; j < aCnt; j++) {
                int a = ats.get(i);
                int b = ats.get(j);

                if (this.getAtom(i).getTZ() > this.getAtom(j).getTZ()) {
                    ats.set(i, b);
                    ats.set(j, a);
                }
            }
        }

        if (this.fMaster.getViewOptions().contains(ViewOption.mvoSticks)) {
            for (int i = 0; i < this.fBonds.size(); i++) {
                this.getBond(i).draw(canvas);
            }
        }

        if (this.fMaster.getViewOptions().contains(ViewOption.mvoBalls)) {
            for (int i = 0; i < aCnt; i++) {
                this.getAtom(ats.get(i)).draw(canvas);
            }
        }
    }

    public final void allocateAtoms(int number)
    {
        number = number - this.getAtomCount();
        for (int i = 1; i <= number; i++) {
            this.addAtom();
        }
    }

    public final void freeUnusedAtoms()
    {
        int i = 0;
        while (i < this.getAtomCount()) {
            if (StringHelper.equals(this.getAtom(i).getSign(), "")) {
                this.deleteAtom(i);
            } else {
                i++;
            }
        }
    }

    public final void clearSelected()
    {
        this.fMaster.beginUpdate();

        while (this.getSelectedAtomsCount() > 0) {
            this.getSelectedAtom(0).setSelected(false);
        }

        while (this.getSelectedBondsCount() > 0) {
            this.getSelectedBond(0).setSelected(false);
        }

        this.fMaster.endUpdate();
    }

    public final MolAtom addAtom()
    {
        MolAtom result = new MolAtom(this);
        this.fAtoms.add(result);
        return result;
    }

    public final void deleteAtom(int index)
    {
        if (index >= 0 && index <= this.fAtoms.size() - 1) {
            MolAtom atom = this.fAtoms.get(index);
            int i = 0;
            while (i <= this.fBonds.size() - 1) {
                if (this.getBond(i).getF_Atom().equals(atom) || this.getBond(i).getT_Atom().equals(atom)) {
                    this.deleteBond(i);
                } else {
                    i++;
                }
            }
            this.getAtom(index).dispose();
            this.fAtoms.remove(index);
        }
    }

    public final MolAtom findAtomByCoord(int aX, int aY)
    {
        for (MolAtom atom : this.fAtoms) {
            int r = atom.getTZ();

            if (r < 0) {
                r = 0;
            }
            if (r > 15) {
                r = 15;
            }
            if (aX >= atom.getTX() - r && aX <= atom.getTX() + r && aY >= atom.getTY() - r && aY <= atom.getTY() + r) {
                return atom;
            }
        }
        return null;
    }

    public final int indexOfAtom(MolAtom atom)
    {
        return this.fAtoms.indexOf(atom);
    }

    public final MolBond addBond(MolAtom fa, MolAtom ta, BondKind bk)
    {
        MolBond result = new MolBond(this);
        result.setF_Atom(fa);
        result.setT_Atom(ta);
        result.setKind(bk);
        this.fBonds.add(result);
        return result;
    }

    public final void deleteBond(int index)
    {
        this.fBonds.get(index).dispose();
        this.fBonds.remove(index);
    }

    public final MolBond findBondByCoord(int aX, int aY)
    {
        for (int i = 0; i < this.fBonds.size(); i++) {
            MolBond bond = this.getBond(i);

            int x = bond.getF_Atom().getTX();
            int y = bond.getF_Atom().getTY();
            int x2 = bond.getT_Atom().getTX();
            int y2 = bond.getT_Atom().getTY();

            int dX = Math.abs(x - x2);
            int dY = Math.abs(y - y2);

            float d;
            try {
                d = ((dX / (float) dY));
            } catch (Exception ex) {
                d = 1f;
            }

            if (d >= 1f) {
                float yy = calc(x, y, x2, y2, aX);
                int min;
                int max;
                if (x2 > x) {
                    min = x;
                    max = x2;
                } else {
                    min = x2;
                    max = x;
                }

                if (yy >= (aY - 5) && yy <= (aY + 5) && aX >= min && aX <= max) {
                    return bond;
                }
            } else {
                float xx = calc(y, x, y2, x2, aY);
                int min;
                int max;
                if (y2 > y) {
                    min = y;
                    max = y2;
                } else {
                    min = y2;
                    max = y;
                }

                if (xx >= (aX - 5) && xx <= (aX + 5) && aY >= min && aY <= max) {
                    return bond;
                }
            }
        }

        return null;
    }

    public final int indexOfBond(MolBond bond)
    {
        return this.fBonds.indexOf(bond);
    }

    public final boolean hasBond(MolAtom atom1, MolAtom atom2)
    {
        for (MolBond bond : this.fBonds) {
            if (((bond.getF_Atom() == atom1) && (bond.getT_Atom() == atom2)) || ((bond.getF_Atom() == atom2) && (bond.getT_Atom() == atom1))) {
                return true;
            }
        }
        return false;
    }

    public final void clear()
    {
        for (int i = 0; i < this.fBonds.size(); i++) {
            this.getBond(i).dispose();
        }
        this.fBonds.clear();

        for (int i = 0; i < this.fAtoms.size(); i++) {
            this.getAtom(i).dispose();
        }
        this.fAtoms.clear();

        this.axmat.init();
        this.fMaster.DoChange();
    }

    public final void selectAll()
    {
        this.fMaster.beginUpdate();

        for (int i = 0; i < this.fAtoms.size(); i++) {
            this.getAtom(i).setSelected(true);
        }

        for (int i = 0; i < this.fBonds.size(); i++) {
            this.getBond(i).setSelected(true);
        }

        this.fMaster.endUpdate();
    }

    public final void deleteSelected()
    {
        this.fMaster.beginUpdate();
        while (this.getSelectedAtomsCount() > 0) {
            int ind = this.fAtoms.indexOf(this.getSelectedAtom(0));
            this.deleteAtom(ind);
        }
        this.fMaster.endUpdate();
    }

    public final void getMolecularBounds()
    {
        if (this.fAtoms.size() <= 0) return;
        
        MolAtom atom = this.getAtom(0);
        this.xmin = atom.getX();
        this.xmax = this.xmin;
        this.ymin = atom.getY();
        this.ymax = this.ymin;
        this.zmin = atom.getZ();
        this.zmax = this.zmin;

        for (int i = 0; i < this.fAtoms.size(); i++) {
            atom = this.getAtom(i);
            
            double x = atom.getX();
            if (x < xmin) {
                xmin = x;
            }
            if (x > xmax) {
                xmax = x;
            }
            double y = atom.getY();
            if (y < ymin) {
                ymin = y;
            }
            if (y > ymax) {
                ymax = y;
            }
            double z = atom.getZ();
            if (z < zmin) {
                zmin = z;
            }
            if (z > zmax) {
                zmax = z;
            }
        }
    }

    public final void fitToWnd(int width, int height)
    {
        this.getMolecularBounds();

        double xw = (this.xmax - this.xmin);
        double yw = (this.ymax - this.ymin);
        double zw = (this.zmax - this.zmin);
        if (yw > xw) {
            xw = yw;
        }
        if (zw > xw) {
            xw = zw;
        }
        
        double f = (width / xw);
        double f2 = (height / xw);
        double xfac;
        if (f < f2) {
            xfac = f;
        } else {
            xfac = f2;
        }

        this.tmat.init();
        this.tmat.translate((-(this.xmin + this.xmax) / 2.0), (-(this.ymin + this.ymax) / 2.0), (-(this.zmin + this.zmax) / 2.0));
        this.tmat.scale(xfac);

        for (int i = 0; i < this.fAtoms.size(); i++) {
            MolAtom atom = this.getAtom(i);
            atom.amat.mult(this.tmat);
        }

        this.fMaster.Repaint();
    }

    public final void properties()
    {
        if (this.fMaster.fMasterListener != null) {
            this.fMaster.fMasterListener.moleculeProperties(this.fMaster, this);
        }
    }
}
