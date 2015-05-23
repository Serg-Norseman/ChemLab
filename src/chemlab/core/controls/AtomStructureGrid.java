package chemlab.core.controls;

import chemlab.core.chemical.AtomSolver;
import chemlab.core.chemical.OrbitalId;
import chemlab.core.chemical.ShellId;
import chemlab.vtable.VirtualTable;

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
