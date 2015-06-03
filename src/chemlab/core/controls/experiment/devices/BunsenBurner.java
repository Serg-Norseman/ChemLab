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
import chemlab.core.measure.ChemUnits;
import chemlab.core.measure.CombustionHeat;
import java.util.List;
import javax.measure.Measure;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class BunsenBurner extends LabDevice
{
    private static final Measure<Double, CombustionHeat> METHANE_HC = Measure.valueOf(50.1, ChemUnits.MEGAJOULE_PER_KILOGRAM);
    private static final Measure<Double, ?> METHANE_HC_JpG = ChemUnits.convert(METHANE_HC, ChemUnits.JOULE_PER_GRAM);
    
    private long fPrevTick;
    
    public BunsenBurner(ExperimentBench owner, int x, int y, DeviceId deviceId)
    {
        super(owner, x, y, deviceId);
    }

    @Override
    protected void updateState(long time)
    {
        // every 50 milliseconds
        long dTime = time - this.fPrevTick;
        if (dTime >= 1000) {
            if (this.fActive) {
                // Methane, t=2043 °С, E=50,1 MJ/kg
                
                double energy = METHANE_HC_JpG.getValue(); // 1 gram/sec
                energy *= 0.01; // 1/100 of gram per sec
                
                List<LabDevice> devs = this.fBench.findConnections(this);
                if (devs.size() > 0) {
                    devs.get(0).addHeatEnergy(energy);
                }
            }
            
            this.fPrevTick = time;
        }
    }

    @Override
    protected void activate()
    {
        this.fPrevTick = this.fPrevTime;
    }
    
    @Override
    protected void deactivate()
    {
        this.fPrevTick = 0;
    }
}
