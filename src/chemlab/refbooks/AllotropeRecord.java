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
 * @since 0.2.0
 */
public final class AllotropeRecord
{
    public int FAllotrope_ID;
    public int ElementID;
    public String Name;
    public double FDensity;
    public String FDensity_Unit;
    public String FDensity_Condition;
    public double FTMelting;
    public String FTMelting_Condition;
    public double FTBoiling;
    public String FTBoiling_Condition;
    public double FTCrystal;
    public String FTCrystal_Condition;
    public double FElectric_Resistance;
    public double FElectric_Conductivity;
    public double FThermo_Expansion;
    public double FHeat_Conductivity;
    public double FHeat_Capacity;
}
