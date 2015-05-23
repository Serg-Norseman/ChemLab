package chemlab.core.controls.molecule;

public enum MasterMode
{
    mm_Edit,
    mm_Scale,
    mm_SpaceMove,
    mm_PlaneMove,
    mm_SpaceRotate,
    mm_PlaneRotate;

    public int getValue()
    {
        return this.ordinal();
    }

    public static MasterMode forValue(int value)
    {
        return values()[value];
    }
}
