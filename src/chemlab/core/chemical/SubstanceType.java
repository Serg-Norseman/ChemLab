package chemlab.core.chemical;

public enum SubstanceType
{
    Reactant,
    Product;

    public int getValue()
    {
        return this.ordinal();
    }

    public static SubstanceType forValue(int value)
    {
        return values()[value];
    }
}
