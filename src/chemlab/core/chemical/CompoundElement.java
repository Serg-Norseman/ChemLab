package chemlab.core.chemical;

import bslib.common.BaseObject;

public class CompoundElement extends BaseObject
{
    public String Symbol;
    public double Index;
    public double AtomicMass;
    public double ENegativity;
    
    public DegreeId DegreeID = DegreeId.Zero;
    public ValencyId ValencyID = ValencyId.V1;

    public DegreeSet DegreeSet;
    public ValencySet ValencySet;
    
    public double MassShare;

    public CompoundElement()
    {
        super();
    }

    public CompoundElement(String symbol, double index)
    {
        super();
        this.Symbol = symbol;
        this.Index = index;
    }
}
