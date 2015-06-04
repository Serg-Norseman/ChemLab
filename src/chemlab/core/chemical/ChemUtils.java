package chemlab.core.chemical;

import bslib.common.AuxUtils;
import bslib.common.RefObject;

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

            result = AuxUtils.ParseFloat(val, 1.0);
        } catch (Exception ex) {
            result = 1.0;
        }

        return result;
    }
}
