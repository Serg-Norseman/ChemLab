package chemlab.core.chemical;

import java.awt.Color;

public enum DegreeId
{
    N7("-7", -7, Color.blue),
    N6("-6", -6, Color.blue),
    N5("-5", -5, Color.blue),
    N4("-4", -4, Color.blue),
    N3("-3", -3, Color.blue),
    N2("-2", -2, Color.blue),
    N1("-1", -1, Color.blue),
    Zero("0", 0, Color.black),
    P1("+1", +1, Color.red),
    P2("+2", +2, Color.red),
    P3("+3", +3, Color.red),
    P4("+4", +4, Color.red),
    P5("+5", +5, Color.red),
    P6("+6", +6, Color.red),
    P7("+7", +7, Color.red);

    public final String Sign;
    public final byte Value;
    public final Color SColor;

    private DegreeId(String sign, int value, Color color)
    {
        this.Sign = sign;
        this.Value = (byte)value;
        this.SColor = color;
    }

    public int getValue()
    {
        return this.ordinal();
    }

    public static DegreeId forValue(int value)
    {
        return values()[value];
    }
}
