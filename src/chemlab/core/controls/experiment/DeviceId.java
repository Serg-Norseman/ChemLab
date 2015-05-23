package chemlab.core.controls.experiment;

public enum DeviceId
{
    dev_Beaker_100,
    dev_Beaker_250,
    dev_Beaker_600,
    dev_Conical_Flask_100,
    dev_Conical_Flask_250,
    dev_Roundbottom_Flask_100,
    dev_TestTube_50,
    dev_Bunsen_Burner,
    dev_Buret_10,
    dev_Buret_50,
    dev_Electronic_Balance_250,
    dev_Graduated_Cylinder_10,
    dev_Graduated_Cylinder_100,
    dev_Heater;

    public int getValue()
    {
        return this.ordinal();
    }

    public static DeviceId forValue(int value)
    {
        return values()[value];
    }
}
