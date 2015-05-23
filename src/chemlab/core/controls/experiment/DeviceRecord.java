package chemlab.core.controls.experiment;

public final class DeviceRecord
{
    public String Name;
    public boolean Substances_Container;
    public boolean Devices_Container;
    public short RealVolume;
    public short RealMass;
    public int Frames;
    public DeviceClingSet Cling;
    
    public DeviceRecord()
    {
        this.Cling = new DeviceClingSet();
    }
}
