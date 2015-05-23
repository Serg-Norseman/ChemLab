package chemlab.core.chemical;

public enum SubstanceRedoxType
{
    Oxidant,
    Reductant;

    public int getValue()
    {
        return this.ordinal();
    }

    public static SubstanceRedoxType forValue(int value)
    {
        return values()[value];
    }
}
