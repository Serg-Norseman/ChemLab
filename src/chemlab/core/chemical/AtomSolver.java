package chemlab.core.chemical;

import bslib.common.AuxUtils;
import bslib.common.BaseObject;
import bslib.common.StringHelper;

public class AtomSolver extends BaseObject
{
    private final byte[][] fStructure = new byte[8][5];

    public AtomSolver()
    {
        this.clear();
    }

    private void clear()
    {
        int s = 0;
        do {
            int o = 0;
            do {
                this.fStructure[s][o] = 0;
                o++;
            } while (o != 5);
            s++;
        } while (s != 8);
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

        int s = 0;
        do {
            int o = 0;
            do {
                if (this.fStructure[s][o] != 0) {
                    int value = (int) this.fStructure[s][o];
                    String text = String.valueOf(value);
                    result = StringHelper.concat(result, String.valueOf(s + 1), String.valueOf(o).substring(2, 3), text, " ");
                }
                o++;
            } while (o != 5);
            s++;
        } while (s != 8);

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
            ShellId shell = ShellId.forValue(AuxUtils.ParseInt("" + prt.charAt(0), 0) - 1);

            String prtOrb = prt.substring(1, 2);

            for (OrbitalId orbital : OrbitalId.values()) {
                if (StringHelper.equals(orbital.toString().substring(2, 3), prtOrb)) {
                    int cnt = AuxUtils.ParseInt(prt.substring(2, prt.length()), 0);
                    this.fStructure[shell.getValue()][orbital.getValue()] = (byte) cnt;
                    break;
                }
            }
        }
    }
}
