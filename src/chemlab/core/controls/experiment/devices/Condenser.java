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
package chemlab.core.controls.experiment.devices;

import chemlab.core.controls.experiment.DeviceId;
import chemlab.core.controls.experiment.ExperimentBench;
import chemlab.core.controls.experiment.LabDevice;
import java.util.List;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
public class Condenser extends LabDevice
{
    private long fPrevTick;
    
    public Condenser(ExperimentBench owner, int x, int y, DeviceId deviceId)
    {
        super(owner, x, y, deviceId);
    }

    @Override
    protected void updateState(long time)
    {
        // every 50 milliseconds
        long dTime = time - this.fPrevTick;
        if (dTime >= 1000) {
            List<LabDevice> devs = this.findConnections();
            if (devs.size() > 0) {

            }
            
            this.fPrevTick = time;
        }
    }

    private void condenseSteam()
    {
        
    }

    private void equalizePressure()
    {
        
    }
}
