package chemlab.core.chemical;

public enum OrbitalId
{
    o_s('s', 2),
    o_p('p', 6),
    o_d('d', 10),
    o_f('f', 14),
    o_g('g', 18);

    public final char Sym;
    public final int Capacity;

    private OrbitalId(char sym, int capacity)
    {
        this.Sym = sym;
        this.Capacity = capacity;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static OrbitalId forValue(int value)
    {
        return values()[value];
    }
}
