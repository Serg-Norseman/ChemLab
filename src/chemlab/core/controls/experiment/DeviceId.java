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
package chemlab.core.controls.experiment;

import chemlab.core.controls.experiment.misc.DeviceCling;
import chemlab.core.controls.experiment.misc.DeviceClingSet;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public enum DeviceId
{
    dev_Beaker_100(DeviceType.Container, 100, 50, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_Beaker_250(DeviceType.Container, 250, 75, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_Beaker_600(DeviceType.Container, 600, 85, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_Conical_Flask_100(DeviceType.Container, 100, 50, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_Conical_Flask_250(DeviceType.Container, 250, 75, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_Roundbottom_Flask_100(DeviceType.Container, 100, 50, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_TestTube_50(DeviceType.Container, 50, 20, 1, new DeviceClingSet(DeviceCling.HeaterBelow, DeviceCling.MeterBelow, DeviceCling.ContainerAbove)),
    dev_Bunsen_Burner(DeviceType.Heater, 0, 0, 5, new DeviceClingSet(DeviceCling.ContainerAbove)),
    dev_Buret_10(DeviceType.Container, 10, 20, 1, new DeviceClingSet(DeviceCling.ContainerBelow, DeviceCling.ContainerAbove)),
    dev_Buret_50(DeviceType.Container, 50, 50, 1, new DeviceClingSet(DeviceCling.ContainerBelow, DeviceCling.ContainerAbove)),
    dev_Electronic_Balance_250(DeviceType.Meter, 250, 0, 1, new DeviceClingSet(DeviceCling.ContainerAbove)),
    dev_Graduated_Cylinder_10(DeviceType.Container, 10, 20, 1, new DeviceClingSet(DeviceCling.ContainerAbove)),
    dev_Graduated_Cylinder_100(DeviceType.Container, 100, 50, 1, new DeviceClingSet(DeviceCling.ContainerAbove)),
    dev_Heater(DeviceType.Heater, 0, 0, 1, new DeviceClingSet(DeviceCling.ContainerAbove));

    public final DeviceType Type;
    public final short RealVolume;
    public final short RealMass;
    public final int Frames;
    public final DeviceClingSet Cling;
    
    private DeviceId(DeviceType type, int realVolume, int realMass, int frames, DeviceClingSet cling)
    {
        this.Type = type;
        this.RealVolume = (short) realVolume;
        this.RealMass = (short) realMass;
        this.Frames = frames;
        this.Cling = cling;
    }
    
    public int getValue()
    {
        return this.ordinal();
    }

    public static DeviceId forValue(int value)
    {
        return values()[value];
    }
}
