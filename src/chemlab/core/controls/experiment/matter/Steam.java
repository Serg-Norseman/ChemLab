package chemlab.core.controls.experiment.matter;

import chemlab.core.chemical.SubstanceState;

/**
 *
 * @author neduard (sepr4bar)
 */
public final class Steam extends Matter
{
    protected Steam(String formula, double temp, double mass)
    {
        super(formula, temp, mass);
        this.setState(SubstanceState.Gas);
    }

    @Override
    public String toString()
    {
        return "Stm " + super.toString();
    }

    // pV = NKT
    /*public double getPressure(double volume)
    {
        return (getParticleNr() * getTemperature() * ChemConsts.BOLTZMAN_CONSTANT) / volume;
    }*/

    /*public int getParticlesAtState(double pressure, double volume)
    {
        return (int) ((pressure * volume) / (ChemConsts.BOLTZMAN_CONSTANT * getTemperature()));
    }*/
}
