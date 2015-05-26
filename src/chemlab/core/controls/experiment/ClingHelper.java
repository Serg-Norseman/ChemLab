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

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class ClingHelper
{
    private static boolean isClingCompatibility(LabDevice dev1, LabDevice dev2)
    {
        DeviceType type1 = dev1.getID().Type;
        DeviceClingSet clings1 = dev1.getID().Cling;
        DeviceType type2 = dev2.getID().Type;
        DeviceClingSet clings2 = dev2.getID().Cling;
        
        switch (type1) {
            case Container:
                break;
            
            case Heater:
                break;
            
            case Meter:
                break;

            case Connector:
                break;
        }
        
        return false;
    }
    
    private static final int CLING_OFFSET = 10;
    
    public enum SimpleCling { Above, Below, Left, Right };
    
    public static SimpleCling isNear(LabDevice dev1, int nX, int nY, LabDevice dev2)
    {
        int d1right = nX + dev1.getWidth() - 1;
        int d1bottom = nY + dev1.getHeight() - 1;
        int cX = (nX + d1right) / 2;
        int cY = (nY + d1bottom) / 2;
        
        int offset = Math.abs(d1bottom - dev2.getTop());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cX, dev2.getLeft(), dev2.getRight(), true)) {
            return SimpleCling.Above;
        }
        
        offset = Math.abs(nY - dev2.getBottom());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cX, dev2.getLeft(), dev2.getRight(), true)) {
            return SimpleCling.Below;
        }
        
        offset = Math.abs(d1right - dev2.getLeft());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cY, dev2.getTop(), dev2.getBottom(), true)) {
            return SimpleCling.Left;
        }
        
        offset = Math.abs(nX - dev2.getRight());
        if (offset < CLING_OFFSET && AuxUtils.isValueBetween(cY, dev2.getTop(), dev2.getBottom(), true)) {
            return SimpleCling.Right;
        }
        
        return null;
    }
    
}
