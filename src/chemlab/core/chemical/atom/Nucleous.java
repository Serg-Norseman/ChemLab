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
package chemlab.core.chemical.atom;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class Nucleous
{
    private final int fProtons;
    private final int fNeutrons;
    private final double fAMU;
    private final int fChargeSign;

    public Nucleous(int protons, int neutrons)
    {
        this.fProtons = protons;
        this.fNeutrons = neutrons;
        this.fAMU = INeutron.AMU * neutrons + IProton.AMU * protons;
        this.fChargeSign = IProton.CHARGE_SIGN * protons;
    }
}
