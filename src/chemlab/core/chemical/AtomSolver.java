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
import bslib.common.BaseObject;
import bslib.common.StringHelper;
import java.util.Arrays;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public class AtomSolver extends BaseObject
{
    private final byte[][] fStructure = new byte[8][5];

    public AtomSolver()
    {
    }

    private void clear()
    {
        for (int s = 0, sLast = ShellId.s_Last.getValue(); s <= sLast; s++)
        {
            Arrays.fill(this.fStructure[s], (byte)0);
        }
    }

    public final byte getElectronCount(ShellId shell, OrbitalId orbital)
    {
        return this.fStructure[shell.getValue()][orbital.getValue()];
    }

    public final void setElectronCount(ShellId shell, OrbitalId orbital, byte value)
    {
        this.fStructure[shell.getValue()][orbital.getValue()] = value;
    }

    public final byte[][] getStructure()
    {
        return (byte[][]) fStructure.clone();
    }

    public final String getStructureStr()
    {
        String result = "";

        for (int s = 0, sLast = ShellId.s_Last.getValue(); s <= sLast; s++)
        {
            for (int o = 0, oLast = OrbitalId.o_Last.getValue(); o <= oLast; o++)
            {
                byte val = this.fStructure[s][o];
                if (val != 0) {
                    String text = String.valueOf(val);
                    result = StringHelper.concat(result, String.valueOf(s + 1), String.valueOf(o).substring(2, 3), text, " ");
                }
            }
        }

        return result;
    }

    public final void setStructureStr(String value)
    {
        this.clear();
        if (StringHelper.isNullOrEmpty(value)) {
            return;
        }

        String s = value.trim();
        String[] parts = s.split("[ ]", -1);

        for (String prt : parts) {
            ShellId shell = ShellId.forValue(AuxUtils.parseInt("" + prt.charAt(0), 0) - 1);

            String prtOrb = prt.substring(1, 2);

            for (OrbitalId orbital : OrbitalId.values()) {
                if (StringHelper.equals(orbital.toString().substring(2, 3), prtOrb)) {
                    int cnt = AuxUtils.parseInt(prt.substring(2, prt.length()), 0);
                    this.fStructure[shell.getValue()][orbital.getValue()] = (byte) cnt;
                    break;
                }
            }
        }
    }
}
