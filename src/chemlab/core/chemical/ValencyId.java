package chemlab.core.chemical;

public enum ValencyId
{
    V1("1", 1),
    V2("2", 2),
    V3("3", 3),
    V4("4", 4),
    V5("5", 5),
    V6("6", 6),
    V7("7", 7),
    V8("8", 8);

    public final String Sign;
    public final byte Value;

    private ValencyId(String sign, int value)
    {
        this.Sign = sign;
        this.Value = (byte)value;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static ValencyId forValue(int value)
    {
        return values()[value];
    }
}
