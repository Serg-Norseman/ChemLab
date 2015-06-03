package chemlab.core.controls.experiment.matter;

import chemlab.core.chemical.ChemConsts;
import chemlab.core.chemical.SubstanceState;

/**
 *
 * @author neduard (sepr4bar)
 */
public final class Liquid extends Matter
{
    // water properties
    public static final double NORMAL_FREEZING_POINT = 273.15;
    public static final double NORMAL_BOILING_POINT = NORMAL_FREEZING_POINT + 100;
    public static final double NORMAL_DENSITY_WATER = 1000; // kg / m3
    public static final double LATENT_HEAT_WATER = 2260;    // kjoules

    protected Liquid(String formula, double temperature, double mass)
    {
        super(formula, temperature, mass);
        this.setState(SubstanceState.Liquid);
    }

    // approximate function
    public static double getBoilingTemperature(double pressure)
    {
        double atmospheres = pressure / ChemConsts.ATMOSPHERIC_PRESSURE;
        if (pressure < ChemConsts.ATMOSPHERIC_PRESSURE) {
            return Liquid.NORMAL_BOILING_POINT + 20 * Math.log(atmospheres);
        } else {
            return Liquid.NORMAL_BOILING_POINT + 100 * Math.log10(atmospheres);
        }

    }

    // inverse of previous function
    public static double getBoilingPressure(double temperature)
    {
        if (temperature < Liquid.NORMAL_BOILING_POINT) {
            return ChemConsts.ATMOSPHERIC_PRESSURE * Math.exp((temperature - Liquid.NORMAL_BOILING_POINT) / 20);
        } else {
            return ChemConsts.ATMOSPHERIC_PRESSURE * Math.pow(10, (temperature - Liquid.NORMAL_BOILING_POINT) / 100);
        }
    }

    // approximate function in kg / m^3
    // see http://www.engineeringtoolbox.com/water-thermal-properties-d_162.html
    // for details
    public static double getDensityAt(double temperature)
    {
        final double BOILING_DENSITY = Liquid.NORMAL_DENSITY_WATER - (Liquid.NORMAL_BOILING_POINT - Liquid.NORMAL_FREEZING_POINT) / 2;
        if (temperature < Liquid.NORMAL_BOILING_POINT) {
            if (temperature < Liquid.NORMAL_FREEZING_POINT) {
                return Liquid.NORMAL_DENSITY_WATER;
            } else {
                return Liquid.NORMAL_DENSITY_WATER - (temperature - Liquid.NORMAL_FREEZING_POINT) / 2;
            }
        } else {
            return BOILING_DENSITY - (temperature - Liquid.NORMAL_BOILING_POINT);
        }
    }

    public double getDensity()
    {
        return getDensityAt(getTemperature());
    }

    public double getVolume()
    {
        return getMass() / getDensity();
    }

    public final double energyNeededToBoil(double pressure)
    {
        return energyNeededToTemperature(getBoilingTemperature(pressure));
    }

    public final void add(Liquid w)
    {
        super.add(w);
    }

    // latent heat = ammount of energy required to transform 
    // L = E / m; m = E / L
    // return the ammount of steam created after energy has beeen added
    public Steam addEnergy(double energy, double pressure) // in KJ
    {
        //double energyNeeded = Math.max(0, energyNeededToBoil(pressure));
        double energyNeeded = energyNeededToBoil(pressure);
        if (energyNeeded > energy) {
            super.addEnergy(energy);

            return Matter.createSteam(this.Formula, 0, 0);
        } else {
            double deltaEnergy = energy - energyNeeded;
            super.addEnergy(energyNeeded);
            double steamMass = deltaEnergy / Liquid.LATENT_HEAT_WATER;

            Steam generated = Matter.createSteam(this.Formula, getTemperature(), (steamMass));

            //long deltaQty = Math.min(getParticleNr(), generated.getParticleNr());
            double deltaMass = Math.min(this.getMass(), generated.getMass());
            this.remove(deltaMass);

            return generated; // Matter.createSteam(this.Formula, getTemperature(), deltaQty);
        }
    }

    /*
     * pressure at bottom if this water was put in a cilinder with a certain area
     */
    public double getHydrostaticPressure(double area)
    {
        return getDensity() * ChemConsts.GRAVITATIONAL_ACCELERATION * (getVolume() / area);
    }

    @Override
    public String toString()
    {
        return "Wtr: " + super.toString();
    }
}
