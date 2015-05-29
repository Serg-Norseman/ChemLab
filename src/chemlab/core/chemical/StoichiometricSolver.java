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

import bslib.common.Logger;
import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.unit.Unit;

/**
 * Stoichiometric equations processing.
 * 
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class StoichiometricSolver
{
    private final ReactionSolver fReaction;

    public StoichiometricSolver(ReactionSolver reaction)
    {
        this.fReaction = reaction;
    }
    
    public final double calculate()
    {
        double result = -1;

        try {
            int inputs = 0;
            int outputs = 0;
            double minMoles = Double.MAX_VALUE;
            for (int i = 0; i < fReaction.getSubstanceCount(); i++) {
                Substance substance = fReaction.getSubstance(i);
                StoicParams params = substance.getStoicParams();
                
                switch (params.Type) {
                    case Input:
                        inputs++;
                        double moles = calculateSource(substance);
                        minMoles = Math.min(minMoles, moles);
                        break;

                    case Output:
                        outputs++;
                        break;
                }
            }
            
            if (inputs <= 0 || outputs <= 0) {
                return -1;
            }

            for (int i = 0; i < fReaction.getSubstanceCount(); i++) {
                Substance targetSubst = fReaction.getSubstance(i);
                result = calculateTarget(minMoles, targetSubst);
            }
        } catch (Exception ex) {
            Logger.write("StoichiometricSolver.calculate(): " + ex.getMessage());
        }

        return result;
    }

    private double calculateSource(Substance sourceSubst)
    {
        double moles = 0;
        StoicParams sourceParams = sourceSubst.getStoicParams();

        SubstanceState state = sourceSubst.State;
        switch (state) {
            case Solid: {
                Measure<Double, Mass> mass;
                if (sourceParams.Mode == StoicParams.InputMode.imSolid_M) {
                    mass = sourceParams.Mass;
                } else {
                    mass = ChemFuncs.volumeToMass(sourceParams.Volume, sourceParams.Density);
                }
                moles = massToMoles(mass, sourceSubst);
                break;
            }

            case Liquid: {
                double molarity;
                if (sourceParams.Mode == StoicParams.InputMode.imLiquid_M) {
                    molarity = sourceParams.ConcM;
                } else {
                    molarity = StoichiometricSolver.percentToConcentration(sourceParams.MassPercent, sourceParams.Density.getValue(), sourceSubst);
                }

                moles = ChemFuncs.volumeToMoles(sourceParams.Volume, molarity);
                break;
            }

            case Gas: {
                if (sourceParams.isSTP) {
                    moles = ChemFuncs.stpToMoles(sourceParams.Volume);
                } else {
                    moles = ChemFuncs.PVnRT(sourceParams.Pressure, sourceParams.Volume, 0, sourceParams.Temperature, 'n').getValue();
                }
                break;
            }
        }
        
        sourceParams.Moles = moles;
        return moles;
    }

    private int calculateTarget(double moles, Substance targetSubst)
    {
        int result = -1;

        try {
            StoicParams targetParams = targetSubst.getStoicParams();

            Unit<?> tgtUnit = targetParams.ResultUnit;
            boolean resVolume = tgtUnit.isCompatible(ChemUnits.LITER);

            if (!resVolume) {
                // calc to mass
                Measure<Double, Mass> mass = molesToMass(moles, targetSubst);
                targetParams.Result = mass;
            } else {
                // calc to volume
                switch (targetSubst.State) {
                    case Solid: {
                        Measure<Double, Mass> mass = molesToMass(moles, targetSubst);
                        targetParams.Result = ChemFuncs.massToVolume(mass, targetParams.Density);
                        break;
                    }

                    case Liquid: {
                        targetParams.Result = ChemFuncs.molesToVolume(moles, targetParams.ConcM);
                        break;
                    }

                    case Gas: {
                        targetParams.Result = ChemFuncs.PVnRT(targetParams.Pressure, null, moles, targetParams.Temperature, 'V');
                        break;
                    }
                }
            }
            
            result = 0;
        } catch (Exception ex) {
            Logger.write("StoichiometricSolver.calculateTarget(): " + ex.getMessage());
        }
        
        return result;
    }

    public static double massToMoles(Measure<Double, Mass> mMass, Substance subst)
    {
        double mass = (mMass == null) ? 0 : ChemUnits.convert(mMass, ChemUnits.GRAM).getValue();
        double molarMass = subst.getMolecularMass(true);
        return (mass / molarMass);
    }

    public static Measure<Double, Mass> molesToMass(double moles, Substance subst)
    {
        double molarMass = subst.getMolecularMass(true);
        double mass = (moles * molarMass);
        return Measure.valueOf(mass, ChemUnits.GRAM);
    }

    public static double percentToConcentration(double percent, double density, Substance subst)
    {
        double molarMass = subst.getMolecularMass(true);
        return ChemFuncs.percentToConcentration(percent, density, molarMass);
    }

    public static double concentrationToPercent(double concentration, double density, Substance subst)
    {
        double molarMass = subst.getMolecularMass(true);
        return ChemFuncs.concentrationToPercent(concentration, density, molarMass);
    }

    public static SubstanceState getState(String phase)
    {
        switch (phase) {
            case "gas":
                return SubstanceState.Gas;
                
            case "liquid":
                return SubstanceState.Liquid;

            case "solid":
                return SubstanceState.Solid;
        }

        return null;
    }
}
