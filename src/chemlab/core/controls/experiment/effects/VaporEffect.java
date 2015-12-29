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
package chemlab.core.controls.experiment.effects;

import bslib.common.AuxUtils;
import bslib.common.ColorUtil;
import chemlab.core.controls.experiment.DevBottom;
import chemlab.core.controls.experiment.LabDevice;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class VaporEffect implements IDeviceEffect
{
    private static class Particle
    {
        public int X;
        public int Y;
        public int Size;
        public int Vel;
        public Color Color;
    }

    private static final int PARTICLES_COUNT = 50;

    private LabDevice fDevice;
    private final Particle[] fParticles;
    private final BasicStroke fStroke;

    public VaporEffect()
    {
        this.fStroke = new BasicStroke(1.0f);
        this.fParticles = new Particle[PARTICLES_COUNT];
        for (int i = 0; i < fParticles.length; i++) {
            fParticles[i] = new Particle();
        }
    }

    @Override
    public void init(LabDevice device)
    {
        this.fDevice = device;

        for (int i = 0; i < fParticles.length; i++) {
            initParticle(fParticles[i]);
        }
    }

    private void initParticle(Particle pcl)
    {
        DevBottom db = this.fDevice.getDevBottom();

        pcl.Color = ColorUtil.darker(this.fDevice.getLiquidColor(), (float) (Math.random() * 0.85f));
        pcl.Size = 2;
        pcl.X = (int) (AuxUtils.getBoundedRnd(db.X1, db.X2));
        pcl.Y = this.fDevice.getLiquidLevel();
        pcl.Vel = AuxUtils.getBoundedRnd(1, 3);
    }

    @Override
    public void doStep()
    {
        int topLevel = -15;

        for (Particle pcl : fParticles) {
            int dx = 1 - AuxUtils.getRandom(3);
            int dy = -pcl.Vel;
            int num = AuxUtils.getRandom(2);
            switch (num) {
                case 0:
                    dx = 0;
                    break;
                case 1:
                    dy = 0;
                    break;
            }

            pcl.X += dx;
            pcl.Y += dy;

            if (pcl.Y <= topLevel) {
                initParticle(pcl);
            } else if (pcl.Y > 0 && !this.fDevice.isValidPoint(pcl.X, pcl.Y)) {
                initParticle(pcl);
            }
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        for (Particle pcl : fParticles) {
            g.setColor(pcl.Color);
            g.setStroke(this.fStroke);
            g.drawOval(pcl.X, pcl.Y, pcl.Size, pcl.Size);
        }
    }
}
