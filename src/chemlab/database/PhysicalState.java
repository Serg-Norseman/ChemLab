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
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;
import java.awt.Color;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
@DatabaseTable(tableName = "compound_states")
public class PhysicalState extends BaseDaoEnabled<PhysicalState, String>
{
    @DatabaseField(columnName = "id", generatedId = true)
    private int fId;

    @DatabaseField(columnName = "formula", foreign = true)
    private CompoundRecord fCompound;

    @DatabaseField(columnName = "state")
    private int fState;

    @DatabaseField(columnName = "density")
    private double fDensity; // g/cm³!

    @DatabaseField(columnName = "color")
    private int fColor;

    @DatabaseField(columnName = "heat_formation")
    private double fHeatFormation; // Enthalpy, KJ

    @DatabaseField(columnName = "gibbs_free_energy")
    private double fGibbsFreeEnergy; // KJ

    @DatabaseField(columnName = "std_entropy")
    private double fStdEntropy; // J/K

    @DatabaseField(columnName = "specific_heat")
    private double fMolarHeatCapacity; // J / (mole * K)
    
    public PhysicalState()
    {
        
    }

    public PhysicalState(Dao<PhysicalState, String> dao)
    {
        this.setDao(dao);
    }
    
    public CompoundRecord getCompound()
    {
        return this.fCompound;
    }
    
    public void setCompound(CompoundRecord value)
    {
        this.fCompound = value;
    }
    
    public SubstanceState getState()
    {
        return SubstanceState.forValue(this.fState);
    }
    
    public void setState(SubstanceState value)
    {
        this.fState = value.getValue();
    }
    
    public double getDensity()
    {
        return this.fDensity;
    }
    
    public void setDensity(double value)
    {
        this.fDensity = value;
    }
    
    public Color getColor()
    {
        return new Color(this.fColor);
    }
    
    public void setColor(Color value)
    {
        this.fColor = value.getRGB();
    }
    
    public double getHeatFormation()
    {
        return this.fHeatFormation;
    }
    
    public void setHeatFormation(double value)
    {
        this.fHeatFormation = value;
    }
    
    public double getGibbsFreeEnergy()
    {
        return this.fGibbsFreeEnergy;
    }
    
    public void setGibbsFreeEnergy(double value)
    {
        this.fGibbsFreeEnergy = value;
    }
    
    public double getStdEntropy()
    {
        return this.fStdEntropy;
    }
    
    public void setStdEntropy(double value)
    {
        this.fStdEntropy = value;
    }
    
    public double getMolarHeatCapacity()
    {
        return this.fMolarHeatCapacity;
    }
    
    public void setMolarHeatCapacity(double value)
    {
        this.fMolarHeatCapacity = value;
    }
    
    @Override
    public String toString() {
        return "PhysState{" +
                "State=" + this.fState +
                ", Density=" + this.fDensity +
                '}';
    }
}
