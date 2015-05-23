package chemlab.core.controls.molecule;

public enum ViewOption
{
    mvoAtomSign,
    mvoAtomName,
    mvoAtomNumber,
    mvoAtomCharge,
    mvoAtomType,
    mvoAtomMass,
    mvoAtomChirality,
    mvoBalls,
    mvoSticks;

    public int getValue()
    {
        return this.ordinal();
    }

    public static ViewOption forValue(int value)
    {
        return values()[value];
    }
}
