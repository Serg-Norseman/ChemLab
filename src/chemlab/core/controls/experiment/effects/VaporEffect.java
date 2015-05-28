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
package chemlab.core.controls.experiment.effects;

import chemlab.core.controls.experiment.LabDevice;
import java.awt.Graphics2D;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class VaporEffect implements IDeviceEffect
{
    private LabDevice fDevice;

    public VaporEffect()
    {
        
    }

    @Override
    public void init(LabDevice device)
    {
        this.fDevice = device;
    }

    @Override
    public void doStep()
    {
    }

    @Override
    public void draw(Graphics2D g)
    {
    }
}
