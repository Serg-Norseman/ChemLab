package chemlab.core.chemical;

public enum ShellId
{
    s_K('K', 2),
    s_L('L', 8),
    s_M('M', 18),
    s_N('N', 32),
    s_O('O', 32),
    s_P('P', 32),
    s_Q('Q', 32),
    s_R('R', 32);

    public final char Sym;
    public final int Capacity;

    private ShellId(char sym, int capacity)
    {
        this.Sym = sym;
        this.Capacity = capacity;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static ShellId forValue(int value)
    {
        return values()[value];
    }
}
