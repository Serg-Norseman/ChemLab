/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-2001 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.measure.Measure;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.AmountOfSubstance;
import javax.measure.quantity.Area;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Force;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.NonSI;
import javax.measure.unit.ProductUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public final class ChemUnits
{
    private static final ChemUnits INSTANCE = new ChemUnits();
    private static final List<Unit<?>> UNITS = new ArrayList<>();

    private ChemUnits()
    {
    }

    public static ChemUnits getInstance()
    {
        return INSTANCE;
    }

    public List<Unit<?>> getUnits()
    {
        return Collections.unmodifiableList(UNITS);
    }

    private static <U extends Unit<?>> U add(U unit)
    {
        UNITS.add(unit);
        return unit;
    }

    public static final Unit<Temperature> KELVIN = add(SI.KELVIN);
    public static final Unit<Temperature> CELSIUS = add(SI.CELSIUS);
    public static final Unit<Temperature> RANKINE = add(NonSI.RANKINE);
    public static final Unit<Temperature> FAHRENHEIT = add(NonSI.FAHRENHEIT);

    public static final Unit<Mass> KILOGRAM = add(SI.KILOGRAM);
    public static final Unit<Mass> GRAM = add(SI.GRAM);
    public static final Unit<Mass> METRIC_TON = add(NonSI.METRIC_TON);

    public static final Unit<Length> METER = add(SI.METRE);
    public static final Unit<Length> KILOMETER = add(SI.KILOMETRE);
    public static final Unit<Length> CENTIMETER = add(SI.CENTIMETRE);
    public static final Unit<Length> MILLIMETER = add(SI.MILLIMETRE);

    public static final Unit<AmountOfSubstance> MOLE = add(SI.MOLE);

    public static final Unit<Duration> SECOND = add(SI.SECOND);

    public static final Unit<Force> NEWTON = add(SI.NEWTON);

    public static final Unit<Pressure> PASCAL = add(SI.PASCAL);
    public static final Unit<Pressure> KILOPASCAL = add(PASCAL.times(1000));
    public static final Unit<Pressure> ATMOSPHERE = add(NonSI.ATMOSPHERE);
    public static final Unit<Pressure> BAR = add(NonSI.BAR);
    public static final Unit<Pressure> MILLIMETER_OF_MERCURY = add(NonSI.MILLIMETER_OF_MERCURY);
    public static final Unit<Pressure> INCH_OF_MERCURY = add(NonSI.INCH_OF_MERCURY);

    public static final Unit<Area> SQUARE_METRE = add(SI.SQUARE_METRE);
    public static final Unit<Volume> CUBIC_METRE = add(SI.CUBIC_METRE);
    public static final Unit<Volume> LITER = add(NonSI.LITRE);

    public static final Unit<VolumetricDensity> KG_M3 = add(VolumetricDensity.UNIT);
    public static final Unit<VolumetricDensity> G_CM3 = add(new ProductUnit<VolumetricDensity>(
            SI.GRAM.divide(SI.CENTIMETRE.pow(3))));
    public static final Unit<VolumetricDensity> G_LITER = add(new ProductUnit<VolumetricDensity>(
            SI.GRAM.divide(NonSI.LITER)));

    public static Measure<Double, ?> convert(Measure<Double, ?> src, Unit<?> targetUnit)
    {
        Unit<?> sourceUnit = src.getUnit();
        UnitConverter conv = sourceUnit.getConverterTo(targetUnit);
        double val = conv.convert(src.getValue());
        return Measure.valueOf(val, targetUnit);
    }
}
