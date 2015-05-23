package chemlab.core.controls.experiment;

@FunctionalInterface
public interface DeviceEventHandler
{
    void invoke(Object sender, LabDevice device);
}
