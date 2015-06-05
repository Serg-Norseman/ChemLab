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

import chemlab.core.chemical.AtomSolver;
import chemlab.core.chemical.OrbitalId;
import chemlab.core.chemical.ShellId;
import chemlab.vtable.VirtualTable;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.3.0
 */
public class AtomStructureGrid extends VirtualTable
{
    private AtomSolver fAtomMaster;

    public AtomStructureGrid()
    {
        super(null);

        super.addColumn(".", 100);
        for (OrbitalId or : OrbitalId.values()) {
            super.addColumn("" + or.Sym, 100);
        }

        for (ShellId sh : ShellId.values()) {
            int idx = sh.getValue();
            Object[] rowData = new Object[6];
            rowData[0] = sh.Sym + "-" + String.valueOf(idx + 1);
            this.addRow(rowData);
        }
        
        this.packColumns(10);
    }

    public final AtomSolver getAtomMaster()
    {
        return this.fAtomMaster;
    }

    public final void setAtomMaster(AtomSolver value)
    {
        this.fAtomMaster = value;
    }

    public void refresh()
    {
        if (this.fAtomMaster != null) {
            super.clear();

            for (ShellId sh : ShellId.values()) {
                int shIdx = sh.getValue();
                Object[] rowData = new Object[6];
                rowData[0] = sh.Sym + "-" + String.valueOf(shIdx + 1);
                
                for (OrbitalId or : OrbitalId.values()) {
                    int orIdx = or.getValue();
                    int value = (int) this.fAtomMaster.getElectronCount(sh, or);
                    
                    rowData[orIdx + 1] = String.valueOf(value);
                }
                
                this.addRow(rowData);
            }

            this.packColumns(10);
        }
    }
}
