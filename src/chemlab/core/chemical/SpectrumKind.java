package chemlab.core.chemical;

public enum SpectrumKind
{
    skVisible,
    skEmission,
    skAbsorption;

    public int getValue()
    {
        return this.ordinal();
    }

    public static SpectrumKind forValue(int value)
    {
        return values()[value];
    }
}
