package chemlab.core.chemical;

public enum BondKind
{
    bkSingle,
    bkDouble,
    bkTriple,
    bkConjugated,
    bkQuadruple;

    public int getValue()
    {
        return this.ordinal();
    }

    public static BondKind forValue(int value)
    {
        return values()[value];
    }
}
