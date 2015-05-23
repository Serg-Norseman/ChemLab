/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-2001 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package chemlab.core.controls.experiment;

import java.io.Serializable;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public abstract class Experiment implements Serializable
{
    protected final ExperimentParams fParams;
    protected double fPressure;
    protected double fTemperature;

    public Experiment()
    {
        this.fParams = new ExperimentParams();
    }

    public Experiment(double pressure, double temperature)
    {
        this.fParams = new ExperimentParams();
        this.fPressure = pressure;
        this.fTemperature = temperature;
    }

    /**
     * @return the pressure
     */
    public double getPressure()
    {
        return fPressure;
    }

    /**
     * @param pressure the pressure to set
     */
    public void setPressure(double pressure)
    {
        this.fPressure = pressure;
    }

    /**
     * @return the temperature
     */
    public double getTemperature()
    {
        return fTemperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(double temperature)
    {
        this.fTemperature = temperature;
    }
}
