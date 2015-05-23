package chemlab.core.chemical;

public enum ValueId
{
    vid_Length,
    vid_Mass,
    vid_Time,
    vid4,
    vid5,
    vid_SubstanceQuantity, // Moles
    vid_FlatAngle,
    vid_SolidAngle,
    vid_Area,
    vid_Volume,
    vid_Velocity,
    vid_Speedup,
    vid13,
    vid14,
    vid15,
    vid16,
    vid17,
    vid_Density,
    vid19,
    vid20,
    vid21,
    vid22,
    vid23,
    vid24,
    vid25,
    vid26,
    vid_Pressure,
    vid28,
    vid_Energy,
    vid_Power,
    vid31,
    vid32,
    vid33,
    vid34,
    vid_Entropy,
    vid36,
    vid37,
    vid38,
    vid39,
    vid40,
    vid_Molarity, // mole/L, mole/m3
    vid_Temperature,
    vid_None;

    public int getValue()
    {
        return this.ordinal();
    }

    public static ValueId forValue(int value)
    {
        return values()[value];
    }
}
