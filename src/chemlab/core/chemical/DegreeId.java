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

import java.awt.Color;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public enum DegreeId
{
    N7("-7", -7, Color.blue),
    N6("-6", -6, Color.blue),
    N5("-5", -5, Color.blue),
    N4("-4", -4, Color.blue),
    N3("-3", -3, Color.blue),
    N2("-2", -2, Color.blue),
    N1("-1", -1, Color.blue),
    Zero("0", 0, Color.black),
    P1("+1", +1, Color.red),
    P2("+2", +2, Color.red),
    P3("+3", +3, Color.red),
    P4("+4", +4, Color.red),
    P5("+5", +5, Color.red),
    P6("+6", +6, Color.red),
    P7("+7", +7, Color.red);

    public final String Sign;
    public final byte Value;
    public final Color SColor;

    private DegreeId(String sign, int value, Color color)
    {
        this.Sign = sign;
        this.Value = (byte)value;
        this.SColor = color;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static DegreeId forValue(int value)
    {
        return values()[value];
    }
}
