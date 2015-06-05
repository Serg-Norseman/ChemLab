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
public enum OrbitalId
{
    o_s('s', 2),
    o_p('p', 6),
    o_d('d', 10),
    o_f('f', 14),
    o_g('g', 18);

    public final char Sym;
    public final int Capacity;

    private OrbitalId(char sym, int capacity)
    {
        this.Sym = sym;
        this.Capacity = capacity;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static OrbitalId forValue(int value)
    {
        return values()[value];
    }
}
