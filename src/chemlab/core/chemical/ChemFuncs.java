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

import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.Unit;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public final class ChemFuncs
{
    public static double moleculesToMoles(double molecules)
    {
        return molecules / ChemConsts.Avogadro;
    }

    public static double molesToMolecules(double moles)
    {
        return moles * ChemConsts.Avogadro;
    }

    public static double volumeToMoles(Measure<Double, Volume> volume, double molarity)
    {
        double vol = (volume == null) ? 0 : ChemUnits.convert(volume, ChemUnits.LITER).getValue();
        return vol * molarity;
    }

    public static Measure<Double, Volume> molesToVolume(double moles, double molarity)
    {
        double result = moles / molarity;
        return Measure.valueOf(result, ChemUnits.LITER);
    }

    /**
     * Calculate the mass by volume and density [checked].
     * @param volume
     * @param density
     * @return 
     */
    public static Measure<Double, Mass> volumeToMass(Measure<Double, Volume> volume, Measure<Double, VolumetricDensity> density)
    {
        double vol = (volume == null) ? 0 : ChemUnits.convert(volume, ChemUnits.LITER).getValue();
        double den = (density == null) ? 0 : ChemUnits.convert(density, ChemUnits.G_LITER).getValue();

        double result = volumeToMass(vol, den);
        
        return Measure.valueOf(result, ChemUnits.GRAM);
    }

    private static double volumeToMass(double volume, double density)
    {
        return volume * density; // L * g/L
    }

    /**
     * Calculate the volume by mass and density [checked].
     * @param mass
     * @param density
     * @return 
     */
    public static Measure<Double, Volume> massToVolume(Measure<Double, Mass> mass, Measure<Double, VolumetricDensity> density)
    {
        double mas = (mass == null) ? 0 : ChemUnits.convert(mass, ChemUnits.GRAM).getValue();
        double den = (density == null) ? 0 : ChemUnits.convert(density, ChemUnits.G_LITER).getValue();
        double result = massToVolume(mas, den);
        
        return Measure.valueOf(result, ChemUnits.LITER);
    }

    private static double massToVolume(double mass, double density)
    {
        return mass / density; // grams / g/L
    }

    public static double stpToMoles(Measure<Double, Volume> volume)
    {
        double vol = (volume == null) ? 0 : ChemUnits.convert(volume, ChemUnits.LITER).getValue();
        return stpToMoles(vol);
    }
    
    private static double stpToMoles(double volume)
    {
        return volume / 22.4 /* litres */;
    }

    public static double molesToSTP(double moles)
    {
        return moles * 22.4 /* litres */;
    }

    public static double percentToConcentration(double percent, double density, double molarMass)
    {
        return (percent * density) / molarMass;
    }

    public static double concentrationToPercent(double concentration, double density, double molarMass)
    {
        return (concentration * molarMass * 100) / density;
    }

    public static Measure<Double, ?> PVnRT(Measure<Double, Pressure> pressure, Measure<Double, Volume> volume, 
            double moles, Measure<Double, Temperature> temperature, char want)
    {
        double pres = (pressure == null) ? 0 : ChemUnits.convert(pressure, ChemUnits.KILOPASCAL).getValue();
        double temp = (temperature == null) ? 0 : ChemUnits.convert(temperature, ChemUnits.KELVIN).getValue();
        double vol = (volume == null) ? 0 : ChemUnits.convert(volume, ChemUnits.LITER).getValue();
        
        double res = 0;
        Unit<?> unit = Unit.ONE;

        if (want == 'P') {
            res = (moles * ChemConsts.R * temp) / vol;
            unit = ChemUnits.KILOPASCAL;
        } else if (want == 'V') {
            res = (moles * ChemConsts.R * temp) / pres;
            unit = ChemUnits.LITER;
        } else if (want == 'n') {
            res = (pres * vol) / (ChemConsts.R * temp);
            unit = ChemUnits.MOLE;
        } else if (want == 'T') {
            res = (pres * vol) / (ChemConsts.R * moles);
            unit = ChemUnits.KELVIN;
        }
        
        return Measure.valueOf(res, unit);
    }

    public static double getPressure(double temperature, double volume)
    {
        return ChemConsts.R * temperature / volume;
    }

    public static double getVolume(double pressure, double temperature)
    {
        return ChemConsts.R * temperature / pressure;
    }

    public static double getTemperature(double pressure, double volume)
    {
        return pressure * volume / ChemConsts.R;
    }

    public static double DPmRT(Measure<Double, VolumetricDensity> density, Measure<Double, Pressure> pressure, 
            double molarMass, Measure<Double, Temperature> temperature, char want)
    {
        double pres = (pressure == null) ? 0 : ChemUnits.convert(pressure, ChemUnits.KILOPASCAL).getValue();
        double temp = (temperature == null) ? 0 : ChemUnits.convert(temperature, ChemUnits.KELVIN).getValue();
        double dens = (density == null) ? 0 : ChemUnits.convert(density, ChemUnits.G_LITER).getValue();
        
        if (want == 'D') {
            return (pres * molarMass) / (ChemConsts.R * temp);
        } else if (want == 'P') {
            return (ChemConsts.R * temp * pres) / (molarMass);
        } else if (want == 'T') {
            return (pres * molarMass) / (ChemConsts.R * dens);
        } else {
            return -1;
        }
    }
}
