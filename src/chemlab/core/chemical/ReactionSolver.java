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

import bslib.common.BaseObject;
import bslib.common.RefObject;
import bslib.common.StringHelper;
import bslib.math.DoubleHelper;
import java.util.ArrayList;
import java.util.List;

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

    public ReactionSolver()
    {
        super();
        this.fSubstances = new ArrayList<>();
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
        SubstanceType rid = SubstanceType.Reactant;
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
                case Reactant:
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
                    bal.setData(bal.addElement(eNum), i + 1, subst.Type == SubstanceType.Product, (int) (Math.round(element.Index)));
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
                subst.Factor = bal.getFactor(i);
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

    public final List<Substance> getReactants()
    {
        List<Substance> result = new ArrayList<>();
        for (Substance subst : this.fSubstances) {
            if (subst.Type == SubstanceType.Reactant) {
                result.add(subst);
            }
        }
        return result;
    }

    public final List<Substance> getProducts()
    {
        List<Substance> result = new ArrayList<>();
        for (Substance subst : this.fSubstances) {
            if (subst.Type == SubstanceType.Product) {
                result.add(subst);
            }
        }
        return result;
    }

    public final ReactionType getReactionType()
    {
        List<Substance> reactants = this.getReactants();

        if (reactants.size() == 2) {
            if (reactants.get(0).isElement() && reactants.get(1).isElement()) {
                // 2 elements
                return ReactionType.Synthesis;
            } else if (reactants.get(0).isElement() && !reactants.get(1).isElement()
                    || !reactants.get(0).isElement() && reactants.get(1).isElement()) {
                // element + compound
                return ReactionType.SingleReplacement;
            } else if (!reactants.get(0).isElement() && !reactants.get(1).isElement()) {
                // 2 compounds
                return ReactionType.DoubleReplacement;
            }
        } else if (reactants.size() == 1) {
            if (!reactants.get(0).isElement()) {
                // 1 compound
                return ReactionType.Decomposition;
            }
        }

        return ReactionType.Unknown;
    }
    
    public final ReactionDirection getReactionDirection()
    {
        return ReactionDirection.Unknown;
    }
}
