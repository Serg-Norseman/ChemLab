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

import chemlab.core.measure.ChemUnits;
import javax.measure.quantity.Mass;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.Unit;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class ChemConsts
{
    public static final Unit<Mass> STD_MASS_UNIT = ChemUnits.GRAM;
    public static final Unit<VolumetricDensity> STD_REF_DENSITY_UNIT = ChemUnits.G_CM3; // for PhysicalState.Density

    public static final double AVOGADRO = 6.0221413 * Math.pow(10, 23);
    public static final double GAS_CONST_R = 8.3144621; // gas constant
    public static final double BOLTZMAN_CONSTANT = GAS_CONST_R / AVOGADRO;
    public static final double T0 = 298.15; // +25 °С (298,15 К)

    public static final double ATMOSPHERIC_PRESSURE = 100_000; // Pa
    public static final double ROOM_TEMP = 273.15; // K, 0 °С

    // others
    public static final double GRAVITATIONAL_ACCELERATION = 9.8;

    /**
     * Spectrum constants.
     */
    public static final double WavelengthMinimum = 380.0;
    public static final double WavelengthMaximum = 780.0;
    public static final double BlueWavelength = WavelengthMinimum;
    public static final double RedWavelength = WavelengthMaximum;
}
