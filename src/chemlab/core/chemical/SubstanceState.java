package chemlab.core.chemical;

public enum SubstanceState
{
    Solid,
    Liquid,
    Gas,
    Ion;

    public int getValue()
    {
        return this.ordinal();
    }

    public static SubstanceState forValue(int value)
    {
        return values()[value];
    }
}
