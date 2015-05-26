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

import bslib.common.AuxUtils;
import bslib.common.Point;
import java.util.ArrayList;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class ClingHelper
{
    private static final int CLING_OFFSET = 10;
    
    private enum SimpleCling { Above, Below, Left, Right };
    
    private static boolean isClingCompatibility(LabDevice dev1, LabDevice dev2, SimpleCling cling)
    {
        DeviceClingSet testClings = new DeviceClingSet();
        
        switch (dev1.getID().Type) {
            case Container:
                if (cling == SimpleCling.Above) testClings.include(DeviceCling.ContainerAbove);
                if (cling == SimpleCling.Below) testClings.include(DeviceCling.ContainerBelow);
                if (cling == SimpleCling.Left) testClings.include(DeviceCling.ContainerLeft);
                if (cling == SimpleCling.Right) testClings.include(DeviceCling.ContainerRight);
                break;
            
            case Heater:
                if (cling == SimpleCling.Below) testClings.include(DeviceCling.HeaterBelow);
                break;
            
            case Meter:
                if (cling == SimpleCling.Below) testClings.include(DeviceCling.MeterBelow);
                break;

            case Connector:
                break;
        }
        
        return (testClings.hasIntersect(dev2.getID().Cling));
    }
    
    private static SimpleCling isNear(LabDevice dev1, int nX, int nY, LabDevice dev2)
    {
        SimpleCling result = null;
        
        int d1right = nX + dev1.getWidth() - 1;
        int d1bottom = nY + dev1.getHeight() - 1;
        int cX = (nX + d1right) / 2;
        int cY = (nY + d1bottom) / 2;
        
        int offset = Math.abs(d1bottom - dev2.getTop());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cX, dev2.getLeft(), dev2.getRight(), true)) {
            result = SimpleCling.Above;
        }
        
        offset = Math.abs(nY - dev2.getBottom());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cX, dev2.getLeft(), dev2.getRight(), true)) {
            result = SimpleCling.Below;
        }
        
        offset = Math.abs(d1right - dev2.getLeft());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cY, dev2.getTop(), dev2.getBottom(), true)) {
            result = SimpleCling.Left;
        }
        
        offset = Math.abs(nX - dev2.getRight());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cY, dev2.getTop(), dev2.getBottom(), true)) {
            result = SimpleCling.Right;
        }
        
        if (!isClingCompatibility(dev1, dev2, result)) {
            return null;
        }
        
        return result;
    }

    public static Point canCling(LabDevice dev, int nX, int nY, LabDevice dev2)
    {
        DeviceClingSet itClings = dev.getID().Cling;
        if (itClings.isEmpty()) {
            return null;
        }
        
        SimpleCling sc = isNear(dev, nX, nY, dev2);
        if (sc != null) {
            int dw = (dev.getWidth() - dev2.getWidth()) / 2;
            int dh = (dev.getHeight() - dev2.getHeight()) / 2;

            switch (sc) {
                case Above:
                    return new Point(dev2.getLeft() - dw, dev2.getTop() - dev.getHeight());

                case Below:
                    return new Point(dev2.getLeft() - dw, dev2.getBottom() + 1);

                case Left:
                    return new Point(dev2.getLeft() - dev.getWidth(), dev2.getTop() - dh);

                case Right:
                    return new Point(dev2.getRight() + 1, dev2.getTop() - dh);
            }
        }
        
        return null;
    }

    public static Point canCling(ArrayList<LabDevice> devices, LabDevice dev, int nX, int nY)
    {
        DeviceClingSet itClings = dev.getID().Cling;
        if (itClings.isEmpty()) {
            return null;
        }
        
        for (LabDevice device : devices) {
            if (!device.equals(dev)) {
                Point res = canCling(dev, nX, nY, device);
                
                if (res != null) {
                    return res;
                }
            }
        }
        
        return null;
    }    
}
