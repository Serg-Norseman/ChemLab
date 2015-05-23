package chemlab.core.chemical;

public enum DecayMode
{
    dmStable,
    dmAD,
    dmDBD,
    dmBD,
    dmPD,
    dmEC,
    dmIT,
    dmSF;

    public int getValue()
    {
        return this.ordinal();
    }

    public static DecayMode forValue(int value)
    {
        return values()[value];
    }
}
