package chemlab.core.controls.experiment;

public enum DeviceCling
{
    dcAbove,
    dcLeft,
    dcRight,
    dcBelow;

    public int getValue()
    {
        return this.ordinal();
    }

    public static DeviceCling forValue(int value)
    {
        return values()[value];
    }
}
