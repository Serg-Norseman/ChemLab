package chemlab.core.chemical;

import bslib.common.BaseObject;
import bslib.common.RefObject;
import bslib.common.StringHelper;
import chemlab.refbooks.CompoundRecord;
import chemlab.refbooks.ElementRecord;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public class CompoundSolver extends BaseObject
{
    private static final String OpenBr = "([";
    private static final String CloseBr = ")]";
    private static final String PossibleChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789()[]*,.";

    private ArrayList<CompoundSolver> fCompounds;
    private ArrayList<CompoundElement> fElements;

    private double fMolecularMass;
    private double fTotalAtoms;

    public int Charge;
    public String Formula;
    public double Factor;

    public CompoundSolver()
    {
        super();
        this.fCompounds = new ArrayList<>();
        this.fElements = new ArrayList<>();
    }

    @Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.clear();
            this.fElements = null;
            this.fCompounds = null;
        }
        super.dispose(disposing);
    }

    public final double getMolecularMass()
    {
        return this.fMolecularMass;
    }

    public final double getTotalAtoms()
    {
        return this.fTotalAtoms;
    }

    private boolean checkDegrees()
    {
        double sum = 0.0;

        int iMaxENeg = -1;
        int iMinDeg = -1;

        double eNeg = 0f;
        int deg = 7;

        for (int i = 0; i < this.fElements.size(); i++) {
            CompoundElement element = this.fElements.get(i);
            DegreeId did = element.DegreeID;
            int degreeVal = did.Value;
            
            sum = sum + (degreeVal * element.Index);

            if (degreeVal < deg) {
                deg = degreeVal;
                iMinDeg = i;
            }

            if (element.ENegativity > eNeg) {
                eNeg = element.ENegativity;
                iMaxENeg = i;
            }
        }

        return (sum == this.Charge && iMinDeg == iMaxENeg);
    }

    private boolean checkDegreesChoice(int depth)
    {
        DegreeId vid = this.getElement(depth).DegreeSet.getFirstDegree();
        while (vid != null) {
            CompoundElement elem = this.fElements.get(depth);
            elem.DegreeID = vid;

            boolean sub = false;
            if (depth == this.fElements.size() - 1) {
                if (this.checkDegrees()) {
                    return true;
                }
            } else {
                sub = this.checkDegreesChoice(depth + 1);
            }

            if (sub) {
                return sub;
            }

            vid = elem.DegreeSet.getSuccDegree(vid);
        }

        return false;
    }

    public final int getCompoundCount()
    {
        return this.fCompounds.size();
    }

    public final CompoundSolver getCompound(int index)
    {
        CompoundSolver result = null;
        if (index >= 0 && index < this.fCompounds.size()) {
            result = this.fCompounds.get(index);
        }
        return result;
    }

    public final CompoundElement getElement(int index)
    {
        CompoundElement result = null;
        if (index >= 0 && index < this.fElements.size()) {
            result = this.fElements.get(index);
        }
        return result;
    }

    public final int getElementCount()
    {
        return this.fElements.size();
    }

    public final boolean isElement()
    {
        return (this.fElements.size() == 1);
    }

    private void analyseFormula(String formula, double index)
    {
        if (StringHelper.isNullOrEmpty(formula)) {
            return;
        }

        int i = 0;
        int obCnt = 0;
        int cbCnt = 0;
        int obPos = 0;
        while (i < formula.length()) {
            if (OpenBr.indexOf(formula.charAt(i)) >= 0) {
                obCnt++;
                if (obCnt == 1) {
                    obPos = i;
                }
                i++;
            } else {
                if (CloseBr.indexOf(formula.charAt(i)) >= 0) {
                    cbCnt++;
                    if (obCnt - cbCnt == 0) {
                        RefObject<String> refFormula = new RefObject<>(formula);
                        double ind = ChemUtils.extractNumber(refFormula, i + 1);
                        formula = refFormula.argValue;
                        String simp = formula.substring(obPos + 1, obPos + 1 + i - (obPos + 1));
                        formula = formula.substring(0, obPos) + formula.substring(obPos + simp.length() + 2);
                        this.analyseFormula(simp, (ind * index));
                    }
                }
                i++;
            }
        }

        this.analyseSimpleFormula(formula, index);
    }

    private void analyseSimpleFormula(String formula, double comIndex)
    {
        if (StringHelper.isNullOrEmpty(formula)) {
            return;
        }

        int i = 0;
        while (i < formula.length()) {
            String sym = "";
            if (CLData.LatUpper.indexOf(formula.charAt(i)) >= 0) {
                sym += (formula.charAt(i));
                i++;
                if (i < formula.length() && CLData.LatLower.indexOf(formula.charAt(i)) >= 0) {
                    sym += (formula.charAt(i));
                    i++;
                }
                if (i < formula.length() && CLData.LatLower.indexOf(formula.charAt(i)) >= 0) {
                    sym += (formula.charAt(i));
                    i++;
                }

                RefObject<String> refFormula = new RefObject<>(formula);
                double elIndex = (i < formula.length() && CLData.Numbers.indexOf(formula.charAt(i)) >= 0) ? ChemUtils.extractNumber(refFormula, i) : 1.0;
                formula = refFormula.argValue;

                CompoundElement element = new CompoundElement(sym, elIndex * comIndex);
                this.fElements.add(element);
            }
        }
    }

    public final void analyse()
    {
        String sFormula = this.Formula;

        if (StringHelper.isNullOrEmpty(sFormula)) {
            throw new ECompoundIncorrect("Формула соединения неверна.");
        }

        int br = 0;
        for (int i = 0; i < sFormula.length(); i++) {
            char c = sFormula.charAt(i);
            if (PossibleChar.indexOf(c) >= 0) {
                if (c == '(' || c == '[') {
                    br++;
                }
                if (c == ')' || c == ']') {
                    br--;
                }
            }
        }

        if (br != 0) {
            throw new ECompoundIncorrect("Проверьте парность скобок.");
        }

        this.clear();

        RefObject<String> refSFormula = new RefObject<>(sFormula);
        this.Factor = ChemUtils.extractNumber(refSFormula, 0);
        sFormula = refSFormula.argValue;

        if (sFormula.indexOf('*') < 0) {
            this.analyseFormula(sFormula, 1.0);
            this.normalization();
        } else {
            sFormula = this.Formula + "*";
            while (sFormula.indexOf('*') >= 0) {
                int pos = sFormula.indexOf('*');
                String shortFormula = sFormula.substring(0, pos);
                sFormula = sFormula.substring(0, 0) + sFormula.substring(0 + pos + 1);

                CompoundSolver comp = new CompoundSolver();
                comp.Formula = shortFormula;
                comp.Charge = 0;
                comp.analyse();
                this.fCompounds.add(comp);
            }
        }

        this.fTotalAtoms = 0.0;
        for (CompoundElement element : this.fElements) {
            this.fTotalAtoms = (this.fTotalAtoms + element.Index);
        }
    }

    public final void clear()
    {
        for (CompoundSolver subComp : this.fCompounds) {
            subComp.dispose();
        }
        this.fCompounds.clear();

        for (CompoundElement element : this.fElements) {
            element.dispose();
        }
        this.fElements.clear();
    }

    public final void consolidateData()
    {
        if (this.fCompounds.size() > 0) {
            for (CompoundElement elem : this.fElements) {
                elem.dispose();
            }
            this.fElements.clear();

            CompoundElement elem = null;

            for (CompoundSolver cmp : this.fCompounds) {
                for (int k = 0; k < cmp.getElementCount(); k++) {
                    boolean ex = false;

                    for (int m = 0; m < this.fElements.size(); m++) {
                        if (StringHelper.equals(this.getElement(m).Symbol, cmp.getElement(k).Symbol)) {
                            elem = this.fElements.get(m);
                            elem.Index = (elem.Index + cmp.getElement(k).Index);
                            ex = true;
                            break;
                        }
                    }

                    if (!ex) {
                        elem = new CompoundElement();
                        elem.Symbol = cmp.getElement(k).Symbol;
                        elem.Index = cmp.getElement(k).Index;
                        elem.AtomicMass = cmp.getElement(k).AtomicMass;
                        this.fElements.add(elem);
                    }

                    elem.Index = (elem.Index * cmp.Factor);
                }
            }
        }
    }

    public final void loadData()
    {
        this.loadData(false);
    }

    private void loadData(boolean sub)
    {
        if (this.fCompounds.size() > 0) {
            for (CompoundSolver subComp : this.fCompounds) {
                subComp.loadData(true);
            }
        } else {
            for (CompoundElement elem : this.fElements) {
                ElementRecord eRec = CLData.ElementsBook.findElement(elem.Symbol);

                elem.AtomicMass = eRec.FAtomic_Mass;
                elem.ENegativity = eRec.FElectronegativity;
                elem.DegreeSet = eRec.FOxidation_Degree;
                elem.ValencySet = eRec.FValency;
                elem.DegreeID = DegreeId.Zero;
                elem.ValencyID = ValencyId.V1;
            }
        }

        this.sortByENeg();
        
        // data
        if (!sub && this.Charge == 0) {
            CompoundRecord compRec = CLData.CompoundsBook.checkCompound(this.Formula);
        }
    }

    private void sortByENeg()
    {
        this.fElements.sort(new Comparator<CompoundElement>()
        {
            @Override
            public int compare(CompoundElement o1, CompoundElement o2)
            {
                return Double.compare(o1.ENegativity, o2.ENegativity);
            }
        });
    }

    public final boolean calculateDegrees()
    {
        boolean result = false;

        if (this.fCompounds.size() > 0) {
            for (CompoundSolver subComp : this.fCompounds) {
                subComp.calculateDegrees();
            }
        } else if (this.fElements.size() == 1) {
            this.fElements.get(0).DegreeID = DegreeId.Zero;
        } else {
            result = this.checkDegreesChoice(0);
            if (!result) {
                for (CompoundElement elem : this.fElements) {
                    elem.DegreeID = DegreeId.Zero;
                }
                //throw new Exception(CLCommon.rs_EODCalcImpossible);
            }
        }
        return result;
    }

    public final int findElement(String symbol)
    {
        for (int i = 0; i < this.fElements.size(); i++) {
            CompoundElement elem = this.fElements.get(i);
            
            if (StringHelper.equals(elem.Symbol, symbol)) {
                return i;
            }
        }

        return -1;
    }

    public final void calculateMolecularMass()
    {
        try {
            this.fMolecularMass = 0.0;

            if (this.fCompounds.size() > 0) {
                for (CompoundSolver subComp : this.fCompounds) {
                    subComp.calculateMolecularMass();
                    this.fMolecularMass = (this.fMolecularMass + subComp.getMolecularMass());
                }
            } else {
                for (CompoundElement elem : this.fElements) {
                    this.fMolecularMass = (this.fMolecularMass + elem.AtomicMass * elem.Index);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ChemUtils.rs_EMMCalcImpossible);
        }
    }

    public final void normalization()
    {
        if (this.fCompounds.size() > 0) {
            for (CompoundSolver subComp : this.fCompounds) {
                subComp.normalization();
            }
        } else {
            int i = 0;
            int count = this.fElements.size();
            while (i < count) {
                CompoundElement iElem = this.fElements.get(i);

                int j = i + 1;
                while (j < count) {
                    CompoundElement jElem = this.fElements.get(j);

                    if (StringHelper.equals(jElem.Symbol, iElem.Symbol)) {
                        iElem.Index = iElem.Index + jElem.Index;
                        this.fElements.remove(j);
                        count--;
                    } else {
                        j++;
                    }
                }

                i++;
            }
        }
    }

    public final void calculateShare()
    {
        if (this.fCompounds.size() > 0) {
            for (CompoundSolver subComp : this.fCompounds) {
                subComp.calculateShare();
            }
        } else {
            this.calculateMolecularMass();
            double mMass = this.getMolecularMass();
            for (CompoundElement elem : this.fElements) {
                elem.MassShare = ((elem.AtomicMass * elem.Index * 100.0 / mMass));
            }
        }
    }

    public final void calculateValencies()
    {
        boolean result = true;

        for (CompoundElement elem : this.fElements) {
            if (elem.ValencySet.isEmpty()) {
                result = false;
                break;
            }

            if (elem.ValencySet.getValencyCount() == 1) {
                elem.ValencyID = elem.ValencySet.getFirstValency();
            } else {
                result = false;
                break;
            }
        }

        if (result) {
            return;
        }

        for (CompoundElement elem : this.fElements) {
            elem.ValencyID = ValencyId.V1;
        }
        
        throw new RuntimeException(ChemUtils.rs_EVCalcImpossible);
    }

    public final void analyseFull(String formula, int charge)
    {
        this.Formula = formula;
        this.Charge = charge;

        this.analyse();
        this.loadData();

        this.calculateMolecularMass();
        this.calculateShare();
        this.calculateDegrees();
        //this.calculateValencies();
    }
}
