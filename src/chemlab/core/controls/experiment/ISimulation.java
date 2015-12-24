/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-1998, 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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

import java.util.Date;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
public interface ISimulation
{
    /**
     * Pause the simulation execution cycle. It will be possible to edit the
     * environment manually and to restart the execution by calling play().
     */
    void pause();

    /**
     * Tries to resume the simulation from stopped and paused Status.
     */
    void play();

    /**
     * When called, the simulation stops.
     */
    void stop();

    /**
     * Allows to access the current environment.
     *
     * @return a reference to the current Environment. The environment is not a
     * copy but back-ends the real environment used in the simulation.
     */
    Environment getEnvironment();

    Date getBegTime();
    Date getEndTime();

    /**
     * Allows to know which is the current simulation time.
     *
     * @return the current time
     */
    Date getCurTime();

    /**
     * Allows to access the current status.
     *
     * @return the current Status of the simulation
     */
    SimulationStatus getStatus();

    /**
     * If the simulation is paused or stopped, a step is executed.
     */
    //void doStep();
}
