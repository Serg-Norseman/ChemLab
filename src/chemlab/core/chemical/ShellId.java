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
public enum ShellId
{
    s_K('K', 2),
    s_L('L', 8),
    s_M('M', 18),
    s_N('N', 32),
    s_O('O', 32),
    s_P('P', 32),
    s_Q('Q', 32),
    s_R('R', 32);

    public final char Sym;
    public final int Capacity;

    private ShellId(char sym, int capacity)
    {
        this.Sym = sym;
        this.Capacity = capacity;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static ShellId forValue(int value)
    {
        return values()[value];
    }
}
