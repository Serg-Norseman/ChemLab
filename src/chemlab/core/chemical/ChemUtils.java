package chemlab.core.chemical;

import bslib.common.AuxUtils;
import bslib.common.RefObject;

public class ChemUtils
{
    public static final String sECompoundUnknown = "Соединение \"%s\" неизвестно.";
    public static final String sEFormatInvalid = "Формат данных неверен";
    public static final String rs_EVCalcImpossible = "Невозможно рассчитать валентности.";
    public static final String rs_EODCalcImpossible = "Невозможно рассчитать степени окисления";
    public static final String rs_EMMCalcImpossible = "Невозможно рассчитать молекулярную массу";

    public static final String rs_AllComponents = "Все компоненты";
    public static final String rs_SubstReagent = "реагент";
    public static final String rs_SubstProduct = "продукт";
    public static final String rs_ReagentsMass = "Масса реагентов";
    public static final String rs_ProductsMass = "Масса продуктов";
    public static final String rs_MolsInc = "Приращение числа молей";
    public static final String rs_BCFirstApproximation = "K(реакц., первое прибл.)";
    public static final String rs_BCSecondApproximation = "K(реакц., второе прибл.)";

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
