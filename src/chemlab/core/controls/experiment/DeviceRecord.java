package chemlab.core.controls.experiment;

public final class DeviceRecord
{
    public String Name;
    public short RealVolume;
    public short RealMass;
    public int Frames;
    public DeviceClingSet Cling;
    
    public DeviceRecord()
    {
        this.Cling = new DeviceClingSet();
    }
}
