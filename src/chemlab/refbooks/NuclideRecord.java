package chemlab.refbooks;

import chemlab.core.chemical.DecayModeSet;

public final class NuclideRecord
{
    public int NuclideId;
    public int ElementId;
    public String Sign;
    public double Abundance;
    public double Weight;
    public double Spin;
    public String HalfLife;
    public DecayModeSet DecayModes;
    
    public NuclideRecord()
    {
        this.DecayModes = new DecayModeSet();
    }
}
