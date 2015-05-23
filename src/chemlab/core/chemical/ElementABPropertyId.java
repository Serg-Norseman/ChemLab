package chemlab.core.chemical;

public enum ElementABPropertyId
{
    eabAcidic,
    eabAmphoteric,
    eabBasic;

    public int getValue()
    {
        return this.ordinal();
    }

    public static ElementABPropertyId forValue(int value)
    {
        return values()[value];
    }
}
