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
package chemlab.core.chemical;

import bslib.common.AuxUtils;
import bslib.common.RefObject;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public class ChemUtils
{
    public static double extractNumber(RefObject<String> str, int pos)
    {
        double result;
        try {
            String val = "";
            while (pos < str.argValue.length() && CLData.DecimNumbers.indexOf(str.argValue.charAt(pos)) >= 0) {
                val += (str.argValue.charAt(pos));
                str.argValue = str.argValue.substring(0, pos) + str.argValue.substring(pos + 1);
            }

            result = AuxUtils.parseFloat(val, 1.0);
        } catch (Exception ex) {
            result = 1.0;
        }

        return result;
    }
}
