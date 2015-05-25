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

import bslib.common.AuxUtils;
import bslib.common.ColorUtil;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class DeviceBoiling implements IDeviceProcess
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

    private static final int MAX_SIZE = 6;
    private static final int BUBBLES_COUNT = 25;

    private LabDevice fDevice;
    private Bubble[] fBubbles;
    private int h;

    public DeviceBoiling()
    {
        
    }

    @Override
    public void doStep()
    {
        if (this.fBubbles == null) {
            return;
        }
        
        int liqLevel = this.fDevice.getLiquidLevel();
        
        for (Bubble bubble : fBubbles) {
            if (bubble.Stroke < 3) {
                bubble.Stroke += 0.025f;
            }

            if (bubble.Size > MAX_SIZE) {
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

                if (bubble.Y + MAX_SIZE / 2 <= liqLevel) {
                    initBubble(bubble);
                }
            } else {
                bubble.Size++;
                bubble.Y = (int) (h - bubble.Size / 2);
            }
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        if (this.fBubbles == null) {
            return;
        }
        
        int devX = this.fDevice.getLeft();
        int devY = this.fDevice.getTop();
        
        for (Bubble bubble : fBubbles) {
            g.setColor(bubble.Color);
            g.setStroke(new BasicStroke(bubble.Stroke));
            g.drawOval(devX + bubble.X, devY + bubble.Y, bubble.Size, bubble.Size);
        }
    }

    @Override
    public void init(LabDevice device)
    {
        this.fDevice = device;

        this.h = device.getHeight();

        this.fBubbles = new Bubble[BUBBLES_COUNT];
        for (int i = 0; i < fBubbles.length; i++) {
            fBubbles[i] = new Bubble();
            initBubble(fBubbles[i]);
        }
    }

    private void initBubble(Bubble bubble)
    {
        DevBottom db = this.fDevice.getDevBottom();
        
        bubble.Color = ColorUtil.darker(this.fDevice.getLiquidColor(), (float) (Math.random() * 0.75f));
        bubble.Size = 1;
        bubble.Stroke = 1.0f;
        bubble.X = (int) (AuxUtils.getBoundedRnd(db.X1, db.X2));
        bubble.Y = (int) ((db.Y - (1 / 2)));
        bubble.Vel = AuxUtils.getBoundedRnd(1, 3);
    }
}
