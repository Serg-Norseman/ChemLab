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
    
    private double FSM_Entropy;
    private double FSM_HeatCapacity;
    private double FSMF_Gibbs_Energy;
    private double FSMF_Enthalpy;
    private double FSMHC_A;
    private double FSMHC_C;
    private double FSMHC_B;

    private double fMass; // used in LabDevice
    protected double fTemperature;

    private StoicParams fStoicParams;

    public double Melting_Point;
    public double Boiling_Point;
    public double Solubility_at_0C;
    public double Solubility_at_100C;

    public Substance()
    {
        super();

        this.fRecord = null;
        this.fState = SubstanceState.Solid;

        this.RedoxType = SubstanceRedoxType.Oxidant;
        this.Type = SubstanceType.Reactant;
    }

    public SubstanceState getState()
    {
        return this.fState;
    }

    public void setState(SubstanceState value)
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

    public final double getMoles()
    {
        double moles = (this.fMass /*gram*/ / this.getMolecularMass());
        return moles;
    }
    
    public final double getMass()
    {
        return this.fMass;
    }
    
    public final void setMass(double value)
    {
        this.fMass = value;
    }
    
    public final void adjustMass(double value)
    {
        this.fMass += value;
    }

    public final double getTemperature()
    {
        return fTemperature;
    }

    public final void setTemperature(double value)
    {
        this.fTemperature = value;
    }
    
    protected final CompoundRecord getRecord()
    {
        if (this.fRecord == null) {
            this.fRecord = CLData.CompoundsBook.checkCompound(this.Formula);
        }
        
        return this.fRecord;
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
        CompoundRecord record = this.getRecord();
        double specHeat = (record == null) ? 0 : record.getPhysicalState(state, true).MolarHeatCapacity;
        return specHeat;
    }

    public final double getDensity(SubstanceState state)
    {
        CompoundRecord record = this.getRecord();
        double density = (record == null) ? 0 : record.getPhysicalState(state, true).Density;
        return density;
    }
    
    public final Color getColor(SubstanceState state)
    {
        CompoundRecord record = this.getRecord();
        
        Color color;
        if (record != null) {
            color = record.getPhysicalState(state, true).Color;
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

    public final double getSM_HeatCapacity()
    {
        return this.FSM_HeatCapacity;
    }

    public final double getSMHC_A()
    {
        return this.FSMHC_A;
    }

    public final double getSMHC_B()
    {
        return this.FSMHC_B;
    }

    public final double getSMHC_C()
    {
        return this.FSMHC_C;
    }

    public final double getSM_Entropy()
    {
        return this.FSM_Entropy;
    }

    public final double getSMF_Enthalpy()
    {
        return this.FSMF_Enthalpy;
    }

    public final double getSMF_Gibbs_Energy()
    {
        return this.FSMF_Gibbs_Energy;
    }
}
