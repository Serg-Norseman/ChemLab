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
 * @since 0.1.0
 */
public enum ValencyId
{
    V1("1", 1),
    V2("2", 2),
    V3("3", 3),
    V4("4", 4),
    V5("5", 5),
    V6("6", 6),
    V7("7", 7),
    V8("8", 8);

    public final String Sign;
    public final byte Value;

    private ValencyId(String sign, int value)
    {
        this.Sign = sign;
        this.Value = (byte)value;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static ValencyId forValue(int value)
    {
        return values()[value];
    }
}
