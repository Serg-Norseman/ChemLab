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
public final class BoilingEffect implements IDeviceEffect
{
    private static class Bubble
    {
        public int X;
        public int Y;
        public int Size;
        public int Vel;
        public float Stroke;
        public Color Color;
    }

    private static final int MAX_SIZE = 5;
    private static final int BUBBLES_COUNT = 25;

    private LabDevice fDevice;
    private final Bubble[] fBubbles;
    private int h;

    public BoilingEffect()
    {
        this.fBubbles = new Bubble[BUBBLES_COUNT];
        for (int i = 0; i < fBubbles.length; i++) {
            fBubbles[i] = new Bubble();
        }
    }

    @Override
    public void init(LabDevice device)
    {
        this.fDevice = device;
        this.h = device.getHeight();

        for (int i = 0; i < fBubbles.length; i++) {
            initBubble(fBubbles[i]);
        }
    }

    private void initBubble(Bubble bubble)
    {
        DevBottom db = this.fDevice.getDevBottom();
        
        bubble.Color = ColorUtil.darker(this.fDevice.getLiquidColor(), (float) (Math.random() * 0.85f));
        bubble.Size = 1;
        bubble.Stroke = 1.0f;
        bubble.Vel = AuxUtils.getBoundedRnd(1, 3);
        bubble.X = (int) (AuxUtils.getBoundedRnd(db.X1, db.X2));
        bubble.Y = (int) (db.Y - bubble.Vel);
    }

    @Override
    public void doStep()
    {
        int liqLevel = this.fDevice.getLiquidLevel();

        for (Bubble bubble : fBubbles) {
            if (bubble.Size == 0) {
                continue;
            }

            if (bubble.Stroke < 2) {
                bubble.Stroke += 0.025f;
            }

            int dx = 1 - AuxUtils.getRandom(3);
            int dy = -bubble.Vel;
            int num = AuxUtils.getRandom(2);
            switch (num) {
                case 0:
                    dx = 0;
                    break;
                case 1:
                    dy = 0;
                    break;
            }

            bubble.X += dx;
            bubble.Y += dy;

            if (bubble.Size < MAX_SIZE) {
                bubble.Size++;
            }

            if (bubble.Y + MAX_SIZE / 2 <= liqLevel) {
                initBubble(bubble);
            } else if (bubble.Y > 0 && !this.fDevice.isValidPoint(bubble.X, bubble.Y)) {
                initBubble(bubble);
            }
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        for (Bubble bubble : fBubbles) {
            if (bubble.Size == 0) {
                continue;
            }
            
            g.setColor(bubble.Color);
            g.setStroke(new BasicStroke(bubble.Stroke));
            g.drawOval(bubble.X, bubble.Y, bubble.Size, bubble.Size);
        }
    }
}
