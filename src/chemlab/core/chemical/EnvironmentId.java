package chemlab.core.chemical;

public enum EnvironmentId
{
    eAcidic,
    eNeutral,
    eBasic;

    public int getValue()
    {
        return this.ordinal();
    }

    public static EnvironmentId forValue(int value)
    {
        return values()[value];
    }
}
