package chemlab.core.controls.molecule;

public enum MCMLineKind
{
    lkND,
    lkAtom,
    lkBond;

    public int getValue()
    {
        return this.ordinal();
    }

    public static MCMLineKind forValue(int value)
    {
        return values()[value];
    }
}
