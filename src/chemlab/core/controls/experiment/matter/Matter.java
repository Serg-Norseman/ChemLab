package chemlab.core.controls.experiment.matter;

import chemlab.core.chemical.ChemConsts;
import chemlab.core.chemical.Substance;
import chemlab.core.chemical.SubstanceState;

/**
 * Note about particleNr: pV = NRT. Considering Avogadro's number (number of
 * atoms per mol) as 600. R then becomes 1.38 * 10^(-2) so, quantities remain
 * expressed in moles, it's just that R is now 10^21 times bigger
 *
 * @author neduard (sepr4bar)
 */
public abstract class Matter extends Substance
{
    protected Matter(String formula, double temperature, double mass)
    {
        this.Formula = formula;
        this.setMass(mass);
        this.fTemperature = temperature;
    }

    public static final Steam createSteam(String formula, double temperature, double mass)
    {
        return new Steam(formula, temperature, mass);
    }

    public static final Liquid createLiquid(String formula, double temperature, double mass)
    {
        return new Liquid(formula, temperature, mass);
    }

    public static final Solid createSolid(String formula, double temperature, double mass)
    {
        return new Solid(formula, temperature, mass);
    }

    public static final Matter createMatter(String formula, SubstanceState state, double mass)
    {
        return createMatter(formula, state, ChemConsts.ROOM_TEMP, mass);
    }

    public static final Matter createMatter(String formula, SubstanceState state, double temperature, double mass)
    {
        Matter result = null;
        switch (state) {
            case Solid:
                result = new Solid(formula, temperature, mass);
                break;
            case Liquid:
                result = new Liquid(formula, temperature, mass);
                break;
            case Gas:
                result = new Steam(formula, temperature, mass);
                break;
        }
        return result;
    }

    /**
     * Adds more matter, also calculates new mixed temperature (weighted
     * average).
     */
    protected final void add(Matter x)
    {
        double mass1 = this.getMass();
        double mass2 = x.getMass();
        double newMass = mass1 + mass2;
        if (newMass != 0) {
            double newTemperature = (mass1 * this.fTemperature + mass2 * x.getTemperature()) / newMass;
            this.setMass(newMass);
            this.fTemperature = newTemperature;
        }
    }

    /**
     *
     * @param massRemoved in grams
     */
    public final void remove(double massRemoved)
    {
        if (this.getMass() < massRemoved) {
            throw new RuntimeException("Can not remove more than there is");
        }

        this.adjustMass(-massRemoved);
    }

    /*public double calculateTemperatureEqulibrium(Matter x)
    {
        return (particleNr * temperature + x.getParticleNr() * x.getTemperature()) / (particleNr + x.getParticleNr());
    }*/

    public void addEnergy(double energy) // in J
    {
        // specific heat = ammount of *energy* required to raise it by 1 degree / mole = energy / (mol * temp)
        // specificHeat = Energy / (kg * K)
        // K(temp) = Energy / (kg * specificH)
        double mass = getMass();
        if (mass < 0.0001) {
            throw new RuntimeException("Can not add energy to nothing");
        }
        double molHC = this.getMolarHeatCapacity(); // J / (mole * K)
        double moles = this.getMoles();
        this.fTemperature += energy / (moles * molHC);
    }

    public final double energyNeededToTemperature(double temperature)
    {
        // neededEnergy = (boilingPtAtPressure - temperature.inKelvin()) * waterMass.inKilograms() * specificHeatOfWater;
        // E = Temp * mass * specificHeat
        return (temperature - this.getTemperature()) * this.getMoles() * this.getMolarHeatCapacity();
    }

    /*public final double getParticlesPerKilo()
    {
        return ChemConsts.AVOGADRO / getMolarMass();
    }*/

    /*public final double getMoles()
    {
        return (double) getParticleNr() / ChemConsts.AVOGADRO;
    }*/

    @Override
    public String toString()
    {
        return String.format("M: %f\tT: %f\t", getMass(), getTemperature());
    }
}
