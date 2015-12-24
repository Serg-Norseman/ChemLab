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
package chemlab.core.controls;

import bslib.common.ColorUtil;
import bslib.common.Rect;
import bslib.common.StringHelper;
import chemlab.core.chemical.OrbitalId;
import chemlab.core.chemical.ShellId;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.3.0
 */
public final class AtomViewer extends JPanel
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private static final Color[] OrbitalColors;

    private int fCtX, fCtY;
    private final int[][] fOrbitalRadiuses = new int[8][5];
    private byte[][] fStructure = new byte[8][5];

    static {
        OrbitalColors = new Color[]{ColorUtil.BGRToRGB(16711680), ColorUtil.BGRToRGB(8388608), ColorUtil.BGRToRGB(65280), ColorUtil.BGRToRGB(32768), ColorUtil.BGRToRGB(16711935)};
    }

    public AtomViewer()
    {
        super();

        this.setBorder(BorderFactory.createEtchedBorder());
        this.setSize(240, 240);
        this.setDoubleBuffered(true);
        
        this.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                OnMouseMove(e);
            }
        });
    }

    public byte[][] getStructure()
    {
        byte[][] result = new byte[8][5];
        System.arraycopy(this.fStructure, 0, result, 0, 40);
        return result;
    }

    public void setStructure(byte[][] value)
    {
        this.fStructure = value;
        this.repaint();
    }

    private static void drawEllipse(Graphics2D gr, int x1, int y1, int x2, int y2)
    {
        Rect rt = new Rect(x1, y1, x2, y2);
        gr.drawOval(x1, y1, rt.getWidth(), rt.getHeight());
    }

    private void drawElectrons(Graphics2D gr, int iRad, ShellId s, OrbitalId o)
    {
        gr.setColor(Color.black);
        gr.setStroke(new BasicStroke(2));

        int elCount = this.fStructure[s.getValue()][o.getValue()];
        float sAngle = (float) ((2 * Math.PI) / elCount);

        for (int i = 0; i < elCount; i++) {
            float angle = (i * sAngle);
            int iX = (int) Math.round(Math.cos(angle) * iRad);
            int iY = (int) Math.round(Math.sin(angle) * iRad);
            drawEllipse(gr, this.fCtX + (iX - 1), this.fCtY + (iY - 1), this.fCtX + (iX + 1), this.fCtY + (iY + 1));
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D gr = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHints(rh);

        this.fCtX = super.getWidth() / 2;
        this.fCtY = super.getHeight() / 2;

        gr.setColor(Color.black);
        gr.setStroke(new BasicStroke(2));
        drawEllipse(gr, this.fCtX - 1, this.fCtY - 1, this.fCtX + 1, this.fCtY + 1);

        int iRad = 0;
        for (ShellId s : ShellId.values()) {
            for (OrbitalId o : OrbitalId.values()) {
                this.fOrbitalRadiuses[s.getValue()][o.getValue()] = 0;

                if (this.fStructure[s.getValue()][o.getValue()] != 0) {
                    iRad += 8;
                    this.fOrbitalRadiuses[s.getValue()][o.getValue()] = iRad;

                    gr.setColor(AtomViewer.OrbitalColors[o.getValue()]);
                    gr.setStroke(new BasicStroke(1));
                    drawEllipse(gr, this.fCtX - iRad, this.fCtY - iRad, this.fCtX + iRad, this.fCtY + iRad);

                    this.drawElectrons(gr, iRad, s, o);
                }
            }

            iRad += 12;
        }
    }

    protected void OnMouseMove(MouseEvent e)
    {
        int dx = Math.abs(e.getX() - this.fCtX);
        int dy = Math.abs(e.getY() - this.fCtY);
        int iRad = (int) Math.round(Math.sqrt((dx * dx + dy * dy)));

        String hint = "";
        for (ShellId shl : ShellId.values()) {
            for (OrbitalId orb : OrbitalId.values()) {
                if (this.fOrbitalRadiuses[shl.getValue()][orb.getValue()] >= iRad - 2 && this.fOrbitalRadiuses[shl.getValue()][orb.getValue()] <= iRad + 2) {
                    int cnt = this.fStructure[shl.getValue()][orb.getValue()];
                    String text = String.valueOf(cnt);
                    hint = String.format(res_i18n.getString("CL_AV_Hint"), shl.Sym, orb.Sym, text);
                }
            }
        }

        if (!StringHelper.equals(hint, "")) {
            this.setToolTipText(hint);
        } else {
            this.setToolTipText(null);
        }
    }
}
