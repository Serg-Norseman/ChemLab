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

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
public class ThermodynamicSolver
{
    private final ReactionSolver fReaction;
    
    private double fTotalEntropy;
    private double fTotalEnthalpy;
    private double fGibbsFreeEnergy;
    private double flg_K;
    private double fStdBalanceConstant;
    private double fdN;
    private double fFBalanceConstant;
    private double fSBalanceConstant;
    private double fTemperature;
    
    public ThermodynamicSolver(ReactionSolver reaction)
    {
        this.fReaction = reaction;

        this.fTemperature = ChemConsts.T0;
    }

    public final double getTotalEntropy()
    {
        return this.fTotalEntropy;
    }

    /**
     * HeatOfFormation/Enthalpy
     * @return 
     */
    public final double getTotalEnthalpy()
    {
        return this.fTotalEnthalpy;
    }

    public final double getSM_Gibbs_Energy()
    {
        return this.fGibbsFreeEnergy;
    }

    public final double getlg_K()
    {
        return this.flg_K;
    }

    public final double getStdBalanceConstant()
    {
        return this.fStdBalanceConstant;
    }

    public final double getdN()
    {
        return this.fdN;
    }

    public final double getFBalanceConstant()
    {
        return this.fFBalanceConstant;
    }

    public final double getSBalanceConstant()
    {
        return this.fSBalanceConstant;
    }

    /**
     * Get a current reaction's environment temperature.
     * @return temperature in K
     */
    public final double getTemperature()
    {
        return this.fTemperature;
    }

    /**
     * Set the current reaction's environment temperature.
     * @param value temperature in K
     */
    public final void setTemperature(float value)
    {
        this.fTemperature = value;
    }

    public void calculate()
    {
        try {
            double T = this.fTemperature;
            double dT = (T - ChemConsts.T0); // StdConds

            this.fTotalEntropy = 0.0;
            this.fTotalEnthalpy = 0.0; // calc checked
            this.fdN = 0.0;

            double FIntegral = 0.0;
            double SIntegral = 0.0;

            for (int i = 0; i < this.fReaction.getSubstanceCount(); i++) {
                Substance subst = this.fReaction.getSubstance(i);

                switch (subst.Type) {
                    case Reactant:
                        this.fTotalEntropy -= (subst.Factor * subst.getStandardEntropy());
                        this.fTotalEnthalpy -= (subst.Factor * subst.getHeatOfFormation());
                        this.fdN -= (subst.Factor);

                        FIntegral -= (((subst.getSMHC_A() * dT) + subst.getSMHC_B() * (dT * dT) / 2.0) + (subst.getSMHC_C() * dT) * (dT * dT) / 3.0);
                        SIntegral -= ((subst.getSMHC_A() * Math.log10((Math.abs(dT))) + subst.getSMHC_B() * dT) + subst.getSMHC_C() * (dT * dT) / 2.0);
                        break;

                    case Product:
                        this.fTotalEntropy += (subst.Factor * subst.getStandardEntropy());
                        this.fTotalEnthalpy += (subst.Factor * subst.getHeatOfFormation());
                        this.fdN += (subst.Factor);

                        FIntegral += (((subst.getSMHC_A() * dT) + subst.getSMHC_B() * (dT * dT) / 2.0) + (subst.getSMHC_C() * dT) * (dT * dT) / 3.0);
                        SIntegral += ((subst.getSMHC_A() * Math.log10((Math.abs(dT))) + subst.getSMHC_B() * dT) + subst.getSMHC_C() * (dT * dT) / 2.0);
                        break;
                }
            }

            this.fGibbsFreeEnergy = this.fTotalEnthalpy - (ChemConsts.T0 * this.fTotalEntropy * 0.001);

            this.flg_K = (-(this.fGibbsFreeEnergy * 1000.0 / (2.3 * ChemConsts.GAS_CONST_R * ChemConsts.T0)));
            this.fStdBalanceConstant = Math.pow(10.0, this.flg_K);
            this.fFBalanceConstant = (this.fStdBalanceConstant * Math.pow(0.1013, this.fdN));
            this.flg_K = (-(this.fTotalEnthalpy / (ChemConsts.GAS_CONST_R * T)) + this.fTotalEntropy / ChemConsts.GAS_CONST_R - 1.0 / (ChemConsts.GAS_CONST_R * T) * FIntegral + 1.0 / ChemConsts.GAS_CONST_R * SIntegral);
            this.fStdBalanceConstant = Math.pow(10.0, this.flg_K);
            this.fSBalanceConstant = (this.fStdBalanceConstant * Math.pow(0.1013, this.fdN));
        } catch (Exception ex) {

        }
    }

    public final boolean checkInput()
    {
        return true;
    }
}
