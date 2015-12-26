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
package chemlab.core.chemical;

import chemlab.refbooks.CompoundRecord;
import chemlab.refbooks.PhysicalState;
import java.awt.Color;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class Substance extends CompoundSolver
{
    public SubstanceRedoxType RedoxType = SubstanceRedoxType.Oxidant;
    public SubstanceType Type = SubstanceType.Reactant;

    private CompoundRecord fRecord;
    private SubstanceState fState = SubstanceState.Solid;
    private StoicParams fStoicParams;

    public Substance()
    {
        super();

        this.fRecord = null;
        this.fState = SubstanceState.Solid;

        this.RedoxType = SubstanceRedoxType.Oxidant;
        this.Type = SubstanceType.Reactant;
    }

    public final SubstanceState getState()
    {
        return this.fState;
    }

    public final void setState(SubstanceState value)
    {
        this.fState = value;
    }

    public final StoicParams getStoicParams()
    {
        if (fStoicParams == null) {
            this.fStoicParams = new StoicParams();
        }
        return this.fStoicParams;
    }

    public final double getMolecularMass(boolean withFactor)
    {
        double molMass = super.getMolecularMass();
        if (withFactor) {
            molMass *= this.Factor;
        }
        return molMass;
    }

    protected final CompoundRecord getRecord()
    {
        if (this.fRecord == null) {
            this.fRecord = CLData.CompoundsBook.checkCompound(this.Formula);
        }
        
        return this.fRecord;
    }

    public PhysicalState getPhysicalState(SubstanceState state)
    {
        CompoundRecord record = this.getRecord();
        return (record == null) ? null : record.getPhysicalState(state, false);
    }

    /**
     *
     * @return J / (mole * K)
     */
    public final double getMolarHeatCapacity()
    {
        return this.getMolarHeatCapacity(this.getState());
    }

    public final double getMolarHeatCapacity(SubstanceState state)
    {
        PhysicalState physState = this.getPhysicalState(state);
        return (physState == null) ? Double.NaN : physState.MolarHeatCapacity;
    }

    /*public final double getDensity()
    {
        // don't switch on! conflict code
        return this.getDensity(this.getState());
    }*/

    public final double getDensity(SubstanceState state)
    {
        PhysicalState physState = this.getPhysicalState(state);
        return (physState == null) ? Double.NaN : physState.Density;
    }
    
    public final Color getColor(SubstanceState state)
    {
        PhysicalState physState = this.getPhysicalState(state);
        
        Color color;
        if (physState != null) {
            color = physState.Color;
        } else {
            switch (state) {
                case Solid:
                    color = Color.gray;
                    break;

                case Liquid:
                    color = Color.cyan;
                    break;

                case Gas:
                    color = Color.cyan.brighter();
                    break;

                case Ion:
                    color = Color.yellow;
                    break;

                default:
                    color = Color.black;
            }
        }
        
        return color;
    }

    /**
     * HeatOfFormation/Enthalpy
     * @return 
     */
    public final double getHeatOfFormation()
    {
        return this.getHeatOfFormation(this.getState());
    }

    public final double getHeatOfFormation(SubstanceState state)
    {
        PhysicalState physState = this.getPhysicalState(state);
        return (physState == null) ? Double.NaN : physState.HeatFormation;
    }

    public final double getGibbsFreeEnergy()
    {
        return this.getGibbsFreeEnergy(this.getState());
    }

    public final double getGibbsFreeEnergy(SubstanceState state)
    {
        PhysicalState physState = this.getPhysicalState(state);
        return (physState == null) ? Double.NaN : physState.GibbsFreeEnergy;
    }

    public final double getStandardEntropy()
    {
        return this.getStandardEntropy(this.getState());
    }

    public final double getStandardEntropy(SubstanceState state)
    {
        PhysicalState physState = this.getPhysicalState(state);
        return (physState == null) ? Double.NaN : physState.StdEntropy;
    }

    public final double SolubilityAt(double temperature)
    {
        return Double.NaN;
    }

    public final double getSMHC_A()
    {
        return Double.NaN;
    }

    public final double getSMHC_B()
    {
        return Double.NaN;
    }

    public final double getSMHC_C()
    {
        return Double.NaN;
    }

    public final double getMeltingPoint()
    {
        return Double.NaN;
    }

    public final double getBoilingPoint()
    {
        return Double.NaN;
    }
}
