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
package chemlab.refbooks;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public final class RadicalRecord
{
    public final String Formula;
    public final int Charge;
    
    public RadicalRecord(String formula, int charge)
    {
        this.Formula = formula;
        this.Charge = charge;
    }
    
    @Override
    public final RadicalRecord clone()
    {
        RadicalRecord copy = new RadicalRecord(this.Formula, this.Charge);
        return copy;
    }
}
