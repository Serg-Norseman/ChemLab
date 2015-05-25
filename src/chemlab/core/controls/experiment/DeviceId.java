package chemlab.core.controls.experiment;

public enum DeviceId
{
    dev_Beaker_100(DeviceType.Container),
    dev_Beaker_250(DeviceType.Container),
    dev_Beaker_600(DeviceType.Container),
    dev_Conical_Flask_100(DeviceType.Container),
    dev_Conical_Flask_250(DeviceType.Container),
    dev_Roundbottom_Flask_100(DeviceType.Container),
    dev_TestTube_50(DeviceType.Container),
    dev_Bunsen_Burner(DeviceType.Heater),
    dev_Buret_10(DeviceType.Container),
    dev_Buret_50(DeviceType.Container),
    dev_Electronic_Balance_250(DeviceType.Meter),
    dev_Graduated_Cylinder_10(DeviceType.Container),
    dev_Graduated_Cylinder_100(DeviceType.Container),
    dev_Heater(DeviceType.Heater);

    public final DeviceType Type;
    
    private DeviceId(DeviceType type)
    {
        this.Type = type;
    }
    
    public int getValue()
    {
        return this.ordinal();
    }

    public static DeviceId forValue(int value)
    {
        return values()[value];
    }
}
