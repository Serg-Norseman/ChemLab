package chemlab.refbooks;

import chemlab.core.chemical.DecayMode;

public final class DecayRecord
{
    public int DecayId;
    public int NuclideId;
    public DecayMode Mode = DecayMode.dmStable;
    public int DescendantId;
}
