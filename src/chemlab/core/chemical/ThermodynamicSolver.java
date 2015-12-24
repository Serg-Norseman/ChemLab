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
    
    private double fSM_Entropy;
    private double fSM_Enthalpy;
    private double fSM_Gibbs_Energy;
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

    public final double getSM_Entropy()
    {
        return this.fSM_Entropy;
    }

    public final double getSM_Enthalpy()
    {
        return this.fSM_Enthalpy;
    }

    public final double getSM_Gibbs_Energy()
    {
        return this.fSM_Gibbs_Energy;
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

    public final double getTemperature()
    {
        return this.fTemperature;
    }

    public final void setTemperature(float value)
    {
        this.fTemperature = value;
    }

    public void calculate()
    {
        try {
            this.fSM_Entropy = 0.0;
            this.fSM_Enthalpy = 0.0;
            this.fdN = 0.0;
            double FIntegral = 0.0;
            double SIntegral = 0.0;
            double T = this.fTemperature;
            double dT = (T - ChemConsts.T0);

            for (int i = 0; i < this.fReaction.getSubstanceCount(); i++) {
                Substance subst = this.fReaction.getSubstance(i);

                switch (subst.Type) {
                    case Reactant:
                        this.fSM_Entropy -= (subst.Factor * subst.getStandardEntropy());
                        this.fSM_Enthalpy -= (subst.Factor * subst.getHeatOfFormation());
                        this.fdN -= (subst.Factor);
                        FIntegral -= (((subst.getSMHC_A() * dT) + subst.getSMHC_B() * (dT * dT) / 2.0) + (subst.getSMHC_C() * dT) * (dT * dT) / 3.0);
                        SIntegral -= ((subst.getSMHC_A() * Math.log10((Math.abs(dT))) + subst.getSMHC_B() * dT) + subst.getSMHC_C() * (dT * dT) / 2.0);
                        break;

                    case Product:
                        this.fSM_Entropy += (subst.Factor * subst.getStandardEntropy());
                        this.fSM_Enthalpy += (subst.Factor * subst.getHeatOfFormation());
                        this.fdN += (subst.Factor);
                        FIntegral += (((subst.getSMHC_A() * dT) + subst.getSMHC_B() * (dT * dT) / 2.0) + (subst.getSMHC_C() * dT) * (dT * dT) / 3.0);
                        SIntegral += ((subst.getSMHC_A() * Math.log10((Math.abs(dT))) + subst.getSMHC_B() * dT) + subst.getSMHC_C() * (dT * dT) / 2.0);
                        break;
                }
            }

            this.fSM_Gibbs_Energy = (this.fSM_Enthalpy - ChemConsts.T0 * this.fSM_Entropy * 0.001);
            this.flg_K = (-(this.fSM_Gibbs_Energy * 1000.0 / (2.3 * ChemConsts.GAS_CONST_R * ChemConsts.T0)));
            this.fStdBalanceConstant = Math.pow(10.0, this.flg_K);
            this.fFBalanceConstant = (this.fStdBalanceConstant * Math.pow(0.1013, this.fdN));
            this.flg_K = (-(this.fSM_Enthalpy / (ChemConsts.GAS_CONST_R * T)) + this.fSM_Entropy / ChemConsts.GAS_CONST_R - 1.0 / (ChemConsts.GAS_CONST_R * T) * FIntegral + 1.0 / ChemConsts.GAS_CONST_R * SIntegral);
            this.fStdBalanceConstant = Math.pow(10.0, this.flg_K);
            this.fSBalanceConstant = (this.fStdBalanceConstant * Math.pow(0.1013, this.fdN));
        } catch (Exception ex) {

        }
    }
}
