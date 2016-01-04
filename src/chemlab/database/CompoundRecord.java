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
package chemlab.database;

import chemlab.core.chemical.SubstanceState;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
@DatabaseTable(tableName = "compounds")
public final class CompoundRecord extends BaseDaoEnabled<CompoundRecord, String>
{
    @DatabaseField(columnName = "formula", id = true)
    private String fFormula;

    @DatabaseField(columnName = "mass")
    private double fMolecularMass;

    @ForeignCollectionField
    private ForeignCollection<PhysicalState> fPhysicalStates;

    @ForeignCollectionField
    private ForeignCollection<CompoundName> fCompoundNames;

    //public final HashMap<String, String> Names;
    public CompoundRecord()
    {
        //this.fPhysicalStates = new PhysicalState[3];
        //this.Names = new HashMap<>();
    }

    public CompoundRecord(Dao<CompoundRecord, String> dao)
    {
        this.setDao(dao);
    }

    public String getFormula()
    {
        return this.fFormula;
    }

    public void setFormula(String value)
    {
        this.fFormula = value;
    }

    public double getMolecularMass()
    {
        return this.fMolecularMass;
    }

    public void setMolecularMass(double value)
    {
        this.fMolecularMass = value;
    }

    public ForeignCollection<PhysicalState> getStates()
    {
        return this.fPhysicalStates;
    }

    public void setStates(ForeignCollection<PhysicalState> states)
    {
        this.fPhysicalStates = states;
    }

    public ForeignCollection<CompoundName> getNames()
    {
        return this.fCompoundNames;
    }

    public void setNames(ForeignCollection<CompoundName> names)
    {
        this.fCompoundNames = names;
    }

    public final PhysicalState getPhysicalState(SubstanceState state)
    {
        if (state == null || state == SubstanceState.Ion) {
            return null;
        }

        ForeignCollection<PhysicalState> states = this.getStates();
        for (PhysicalState st : states) {
            if (st.getState() == state) {
                return st;
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "Compound{"
                + "Formula=" + this.fFormula
                + ", MolecularMass=" + this.fMolecularMass
                + '}';
    }
}
