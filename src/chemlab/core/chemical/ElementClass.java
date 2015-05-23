package chemlab.core.chemical;

public enum ElementClass
{
    AlkalineMetals,
    AlkalineEarthMetals,
    TransitionMetals,
    RareEarthMetals,
    OtherMetals,
    NobleGases,
    Halogens,
    OtherNonmetals;

    public int getValue()
    {
        return this.ordinal();
    }

    public static ElementClass forValue(int value)
    {
        return values()[value];
    }
}
