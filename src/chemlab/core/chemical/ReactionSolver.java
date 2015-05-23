package chemlab.core.chemical;

import bslib.common.BaseObject;
import bslib.common.RefObject;
import bslib.common.StringHelper;
import bslib.math.DoubleHelper;
import java.util.ArrayList;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public class ReactionSolver extends BaseObject
{
    private static final String Syms = "+=";

    private String fEquation;
    private ArrayList<Substance> fSubstances;

    private double fSourceMass;
    private double fProductMass;

    private EnvironmentId fEnvironment = EnvironmentId.eNeutral;
    private double fSM_Entropy;
    private double fSM_Enthalpy;
    private double fSM_Gibbs_Energy;
    private double flg_K;
    private double fStdBalanceConstant;
    private double fdN;
    private double fFBalanceConstant;
    private double fSBalanceConstant;
    private double fTemperature;

    public ReactionSolver()
    {
        super();
        this.fSubstances = new ArrayList<>();
        this.fTemperature = ChemConsts.T0;
    }

    @Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.clearSubstances();
            this.fSubstances = null;
        }
        super.dispose(disposing);
    }

    public final double getSourceMass()
    {
        return this.fSourceMass;
    }

    public final double getProductMass()
    {
        return this.fProductMass;
    }

    public final double getSM_Entropy()
    {
        return this.fSM_Entropy;
    }

    public final double getSM_Enthalpy()
    {
        return this.fSM_Enthalpy;
    }

    public final double getSM_Gibbs_Energy()
    {
        return this.fSM_Gibbs_Energy;
    }

    public final double getlg_K()
    {
        return this.flg_K;
    }

    public final double getStdBalanceConstant()
    {
        return this.fStdBalanceConstant;
    }

    public final double getdN()
    {
        return this.fdN;
    }

    public final double getFBalanceConstant()
    {
        return this.fFBalanceConstant;
    }

    public final double getSBalanceConstant()
    {
        return this.fSBalanceConstant;
    }

    public final EnvironmentId getEnvironment()
    {
        return this.fEnvironment;
    }

    public final void setEnvironment(EnvironmentId value)
    {
        this.fEnvironment = value;
    }

    public final String getEquation()
    {
        return this.fEquation;
    }

    public final void setEquation(String value)
    {
        this.fEquation = value;
    }

    public final double getTemperature()
    {
        return this.fTemperature;
    }

    public final void setTemperature(float value)
    {
        this.fTemperature = value;
    }

    public final Substance getSubstance(int index)
    {
        Substance result = null;
        if (index >= 0 && index <= this.fSubstances.size() - 1) {
            result = this.fSubstances.get(index);
        }
        return result;
    }

    public final int getSubstanceCount()
    {
        return this.fSubstances.size();
    }

    private void clearSubstances()
    {
        for (Substance substance : this.fSubstances) {
            substance.dispose();
        }
        this.fSubstances.clear();
    }

    public final Substance find(String formula)
    {
        for (Substance substance : this.fSubstances) {
            if (StringHelper.equals(substance.Formula, formula)) {
                return substance;
            }
        }
        return null;
    }
    
    public final void analyse()
    {
        if (StringHelper.isNullOrEmpty(this.fEquation)) return;
        
        this.clearSubstances();

        String equation = this.fEquation + "+"; // + final separator
        SubstanceType rid = SubstanceType.Reagent;
        String cmp = "";

        int i = 0;
        while (i < equation.length()) {
            char c = equation.charAt(i);
            
            if (Syms.indexOf(c) >= 0) {
                cmp = cmp.trim();
                RefObject<String> refCmp = new RefObject<>(cmp);
                double factor = ChemUtils.extractNumber(refCmp, 0);
                cmp = refCmp.argValue;
                
                Substance substance = new Substance();
                substance.Formula = cmp;
                substance.Factor = factor;
                substance.Type = rid;
                this.fSubstances.add(substance);

                if (c == '=') {
                    rid = SubstanceType.Product;
                }
                cmp = "";
            } else {
                cmp += c;
            }

            i++;
        }
    }

    private void calcMasses()
    {
        this.fSourceMass = 0.0;
        this.fProductMass = 0.0;

        for (Substance subst : this.fSubstances) {
            subst.calculateMolecularMass();

            double substMass = subst.Factor * subst.getMolecularMass();
            
            switch (subst.Type) {
                case Reagent:
                    this.fSourceMass = (this.fSourceMass + substMass);
                    break;
                case Product:
                    this.fProductMass = (this.fProductMass + substMass);
                    break;
            }
        }
    }
    
    public final boolean calculate()
    {
        boolean result = false;

        // data prepare
        for (Substance subst : this.fSubstances) {
            subst.analyse();
            subst.normalization();
            subst.consolidateData();
            subst.loadData();
        }

        this.calcMasses();
        
        if (!DoubleHelper.equals(this.fSourceMass, this.fProductMass)) {
            if (!this.calculateFactors()) {
                throw new RuntimeException("Массы реагентов и продуктов не равны");
            }
        } else {
            result = true;
        }

        this.calcMasses();
        
        this.calcThermodynamics();

        return result;
    }

    public final boolean calculateFactors()
    {
        boolean result = false;

        BalanceSolver bal = new BalanceSolver();
        try {
            bal.setReagentsCount(this.fSubstances.size());

            for (int i = 0; i < this.fSubstances.size(); i++) {
                Substance subst = this.fSubstances.get(i);
                subst.Factor = 1.0;

                for (int k = 0; k < subst.getElementCount(); k++) {
                    CompoundElement element = subst.getElement(k);
                    int eNum = CLData.ElementsBook.findElementNumber(element.Symbol);
                    int m = bal.addElement(eNum);
                    bal.setData(m, i + 1, subst.Type == SubstanceType.Product, (int) (Math.round(element.Index)));
                }
            }

            int rest = bal.balanceByLeastSquares();

            if (rest == BalanceSolver.RES_NoUniqueSolution) {
                throw new RuntimeException("No unique solution!");
            }
            if (rest == BalanceSolver.RES_EquationCanNotBeBalanced) {
                throw new RuntimeException("Equation can not be balanced!");
            }

            for (int i = 0; i < this.fSubstances.size(); i++) {
                Substance subst = this.getSubstance(i);
                subst.Factor = bal.getFactor(i + 1);
            }

            result = true;
        } finally {
            bal.dispose();
        }

        if (result) {
            String eq = "";
            for (int i = 0; i < this.fSubstances.size(); i++) {
                Substance subst = this.getSubstance(i);

                int f = (int) subst.Factor;
                if (f > 1) {
                    eq += String.valueOf(f);
                }

                eq += subst.Formula;
                if (i < this.fSubstances.size() - 1) {
                    if (this.getSubstance(i + 1).Type == subst.Type) {
                        eq += " + ";
                    } else {
                        eq += " = ";
                    }
                }
            }
            this.fEquation = eq;
        }
        
        return result;
    }

    public void calcThermodynamics()
    {
        try {
            this.fSM_Entropy = 0.0;
            this.fSM_Enthalpy = 0.0;
            this.fdN = 0.0;
            double FIntegral = 0.0;
            double SIntegral = 0.0;
            double T = this.fTemperature;
            double dT = (T - ChemConsts.T0);

            for (int i = 0; i < this.fSubstances.size(); i++) {
                Substance subst = this.getSubstance(i);

                switch (subst.Type) {
                    case Reagent:
                        this.fSM_Entropy = (this.fSM_Entropy - subst.Factor * subst.getSM_Entropy());
                        this.fSM_Enthalpy = (this.fSM_Enthalpy - subst.Factor * subst.getSMF_Enthalpy());
                        this.fdN = (this.fdN - subst.Factor);
                        FIntegral = (FIntegral - (((subst.getSMHC_A() * dT) + subst.getSMHC_B() * (dT * dT) / 2.0) + (subst.getSMHC_C() * dT) * (dT * dT) / 3.0));
                        SIntegral = (SIntegral - ((subst.getSMHC_A() * Math.log10((Math.abs(dT))) + subst.getSMHC_B() * dT) + subst.getSMHC_C() * (dT * dT) / 2.0));
                        break;

                    case Product:
                        this.fSM_Entropy = (this.fSM_Entropy + subst.Factor * subst.getSM_Entropy());
                        this.fSM_Enthalpy = (this.fSM_Enthalpy + subst.Factor * subst.getSMF_Enthalpy());
                        this.fdN = (this.fdN + subst.Factor);
                        FIntegral = (FIntegral + (((subst.getSMHC_A() * dT) + subst.getSMHC_B() * (dT * dT) / 2.0) + (subst.getSMHC_C() * dT) * (dT * dT) / 3.0));
                        SIntegral = (SIntegral + ((subst.getSMHC_A() * Math.log10((Math.abs(dT))) + subst.getSMHC_B() * dT) + subst.getSMHC_C() * (dT * dT) / 2.0));
                        break;
                }
            }

            this.fSM_Gibbs_Energy = (this.fSM_Enthalpy - ChemConsts.T0 * this.fSM_Entropy * 0.001);
            this.flg_K = (-(this.fSM_Gibbs_Energy * 1000.0 / (2.3 * ChemConsts.R * ChemConsts.T0)));
            this.fStdBalanceConstant = Math.pow(10.0, this.flg_K);
            this.fFBalanceConstant = (this.fStdBalanceConstant * Math.pow(0.1013, this.fdN));
            this.flg_K = (-(this.fSM_Enthalpy / (ChemConsts.R * T)) + this.fSM_Entropy / ChemConsts.R - 1.0 / (ChemConsts.R * T) * FIntegral + 1.0 / ChemConsts.R * SIntegral);
            this.fStdBalanceConstant = Math.pow(10.0, this.flg_K);
            this.fSBalanceConstant = (this.fStdBalanceConstant * Math.pow(0.1013, this.fdN));
        } catch (Exception ex) {

        }
    }
}
