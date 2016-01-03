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
package chemlab.sandbox;

import bslib.common.RefObject;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.ChemUtils;
import chemlab.core.chemical.Substance;
import chemlab.database.CompoundRecord;
import chemlab.refbooks.RadicalRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Bad attempt, wasted time :(
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class ReactionsEnv
{
    private final List<Substance> fSubstances;
    private final List<CompoundRecord> fProducts;
    
    public ReactionsEnv()
    {
        this.fSubstances = new ArrayList<>();
        this.fProducts = new ArrayList<>();
    }
    
    public final void addSubstance(Substance subst)
    {
        this.fSubstances.add(subst);
    }
    
    /*private RadicalRecord clearRadical(RadicalRecord rad)
    {
        RefObject<String> refFormula = new RefObject<>(rad.Formula);
        ChemUtils.extractNumber(refFormula, 0);
        
        return new RadicalRecord(refFormula.argValue, rad.Charge);
    }
    
    private boolean checkRadical(List<RadicalRecord> sourceRadicals, RadicalRecord rad)
    {
        for (RadicalRecord srcRad : sourceRadicals) {
            if (srcRad.Formula.equals(rad.Formula)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkProduct(List<RadicalRecord> sourceRadicals, CompoundRecord compRec)
    {
        if (compRec.Radicals.size() < 1) {
            return false;
        }
        
        for (RadicalRecord rad : compRec.Radicals) {
            RadicalRecord rd = clearRadical(rad);
            
            if (!checkRadical(sourceRadicals, rd)) {
                return false;
            }
        }
        
        return true;
    }*/
    
    public final void search()
    {
        /*List<CompoundRecord> sourceCompounds = new ArrayList<>();
        List<RadicalRecord> sourceRadicals = new ArrayList<>();
        
        for (Substance subst : this.fSubstances) {
            CompoundRecord compRec = CLData.CompoundsBook.checkCompound(subst.Formula);
            
            sourceCompounds.add(compRec);
            
            if (compRec != null) {
                for (RadicalRecord rad : compRec.Radicals) {
                    RadicalRecord rd = clearRadical(rad);
                    sourceRadicals.add(rd);
                }
            }
        }
        
        // optimize radicals, remove duplicates
        
        // search products
        for (CompoundRecord compRec : CLData.CompoundsBook.getList()) {
            if (!sourceCompounds.contains(compRec)) {
                if (checkProduct(sourceRadicals, compRec)) {
                    this.fProducts.add(compRec);
                }
            }
        }*/
    }
    
    @Override
    public String toString()
    {
        StringBuilder res = new StringBuilder();
        
        for (int i = 0; i < this.fSubstances.size(); i++) {
            Substance subst = this.fSubstances.get(i);
            
            if (i > 0 && i < this.fSubstances.size()) {
                res.append(" + ");
            }
            
            res.append(subst.Formula);
        }
        
        res.append(" = ");
        
        for (int i = 0; i < this.fProducts.size(); i++) {
            CompoundRecord subst = this.fProducts.get(i);
            
            if (i > 0 && i < this.fProducts.size()) {
                res.append(" + ");
            }
            
            res.append(subst.getFormula());
        }
        
        return res.toString();
    }
}
