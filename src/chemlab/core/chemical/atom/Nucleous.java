package chemlab.core.chemical.atom;

public class Nucleous
{
    private final int fProtons;
    private final int fNeutrons;
    private final double fAMU;
    private final int fChargeSign;

    public Nucleous(int protons, int neutrons)
    {
        this.fProtons = protons;
        this.fNeutrons = neutrons;
        this.fAMU = INeutron.AMU * neutrons + IProton.AMU * protons;
        this.fChargeSign = IProton.CHARGE_SIGN * protons;
    }
}
