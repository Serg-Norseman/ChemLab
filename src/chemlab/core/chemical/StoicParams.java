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
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.Unit;

/**
 * Stoichiometric parameters of a substances.
 * 
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class StoicParams
{
    public enum InputMode
    {
        imSolid_M,      // Solid, Mass (Grams)
        imSolid_V_D,    // Solid, Volume + Density
        imLiquid_M,     // Liquid, Volume + Concentration (M)
        imLiquid_MP_D,  // Liquid, Volume + Mass% + Density
        imGas_In,       // Gas input, 
        imGas_Out       // Gas output, 
    }

    public enum ParamType
    {
        None, Input, Output
    }
    
    public ParamType Type;
    public InputMode Mode;
    public Unit<?> ResultUnit;
    
    public Measure<Double, Mass> Mass;
    public Measure<Double, Volume> Volume;
    public Measure<Double, VolumetricDensity> Density;
    public Measure<Double, Pressure> Pressure;
    public Measure<Double, Temperature> Temperature;
    
    public double ConcM;
    public double MassPercent;
    public boolean isSTP;
    
    // fields only for solver!
    public double Moles;
    public Measure<Double, ?> Result;
    
    public StoicParams()
    {
        this.Type = ParamType.None;
        this.Mode = InputMode.imSolid_M;
    }
    
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();

        try {
            switch (Mode) {
                case imSolid_M:
                    str.append("Mass: ").append(Mass);
                    break;

                case imSolid_V_D:
                    str.append("Volume: ").append(Volume).append(", Density: ").append(Density);
                    break;

                case imLiquid_M:
                    str.append("ConcM: ").append(ConcM);
                    break;

                case imLiquid_MP_D:
                    str.append("MassPercent: ").append(MassPercent).append(", Density: ").append(Density);
                    break;

                case imGas_In:
                    if (isSTP) {
                        str.append("STP").append(", Volume: ").append(Volume);
                    } else {
                        str.append("Pressure: ").append(Pressure).append(", Volume: ").append(Volume).append(", Temperature: ").append(Temperature);
                    }
                    break;

                case imGas_Out:
                    break;

                default:
                    throw new AssertionError(Mode.name());

            }
        } catch (Exception ex) {
            Logger.write("InputParams.toString(): " + ex.getMessage());
        }
        
        return str.toString();
    }
}
