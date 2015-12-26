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

import chemlab.core.chemical.SubstanceState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public final class CompoundRecord
{
    private final PhysicalState[] fPhysicalStates;

    public String Formula;
    public double MolecularMass;

    public final HashMap<String, String> Names;
    public final List<RadicalRecord> Radicals;

    public CompoundRecord()
    {
        this.fPhysicalStates = new PhysicalState[3];
        
        this.Names = new HashMap<>();
        this.Radicals = new ArrayList<>();
    }
    
    public final PhysicalState getPhysicalState(SubstanceState state, boolean canCreate)
    {
        if (state == null || state == SubstanceState.Ion) {
            return null;
        }

        int idx = state.getValue();
        PhysicalState physState = this.fPhysicalStates[idx];
        if (physState == null && canCreate) {
            physState = new PhysicalState();
            physState.State = state;
            this.fPhysicalStates[idx] = physState;
        }

        return physState;
    }
}
