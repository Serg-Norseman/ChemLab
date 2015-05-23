package chemlab.core.chemical;

public enum CrystalKind
{
    Cubic,
    CubicBodyCentered,
    CubicFaceCentered,
    Hexagonal,
    Monoclinic,
    Orthorhombic,
    Rhombohedral,
    Tetragonal;

    public int getValue()
    {
        return this.ordinal();
    }

    public static CrystalKind forValue(int value)
    {
        return values()[value];
    }
}
