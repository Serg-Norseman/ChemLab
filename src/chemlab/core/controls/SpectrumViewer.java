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

import bslib.common.AuxUtils;
import bslib.common.Logger;
import chemlab.core.chemical.ChemConsts;
import chemlab.core.chemical.SpectrumKind;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.4.0
 */
public final class SpectrumViewer extends JPanel
{
    private static final double Gamma = 0.8;
    private static final int IntensityMax = 255;

    private BufferedImage fBuffer;
    private boolean fChanged;
    private SpectrumKind fKind;
    private String fLineValues;
    private final ArrayList<Double> fNanometersList;
    private int fScaleInterval;
    private boolean fScaleVisible;

    public SpectrumViewer()
    {
        super();

        this.fBuffer = null;
        this.fChanged = true;
        this.fKind = SpectrumKind.skVisible;
        this.fNanometersList = new ArrayList<>();
        this.fScaleInterval = 50;
        this.fScaleVisible = true;
    }

    private void changed()
    {
        this.fChanged = true;
        this.repaint();
    }
    
    public final void clear()
    {
        this.fNanometersList.clear();
        
        this.changed();
    }

    public final String getLinesStr()
    {
        return this.fLineValues;
    }

    public final void setLinesStr(String value)
    {
        this.fLineValues = value;

        try {
            this.clear();
            
            String[] parts = value.split("[;]", -1);
            for (String val : parts) {
                this.fNanometersList.add(AuxUtils.ParseFloat(val, 0));
            }
            
            this.changed();
        } catch (ParseException ex) {
            Logger.write("SpectrumView.setLinesStr(): " + ex.getMessage());
            // ALERT!
        }
    }

    public final boolean getScaleVisible()
    {
        return this.fScaleVisible;
    }

    public final void setScaleVisible(boolean value)
    {
        this.fScaleVisible = value;
        
        this.changed();
    }

    public final int getScaleInterval()
    {
        return this.fScaleInterval;
    }

    public final void setScaleInterval(int value)
    {
        this.fScaleInterval = value;
        
        this.changed();
    }

    public final void setKind(SpectrumKind value)
    {
        if (this.fKind != value) {
            this.fKind = value;
            
            this.changed();
        }
    }

    private static int adjust(double color, double factor)
    {
        int result;
        if (color == 0.0f) {
            result = 0;
        } else {
            result = (int) (Math.round(SpectrumViewer.IntensityMax * Math.pow((color * factor), SpectrumViewer.Gamma)));
        }
        
        if (result < 0) {
            result = 0;
        }
        if (result > 255) {
            result = 255;
        }
        
        return result;
    }

    private static Color WavelengthToRGB(double wavelength)
    {
        try {
            int wl = (int) (wavelength);
            double red, green, blue;

            if (wl >= 380 && wl <= 439) {
                red = -(wavelength - 440) / (440 - 380);
                green = 0.0;
                blue = 1.0;
            } else if (wl >= 440 && wl <= 489) {
                red = 0.0;
                green = (wavelength - 440) / (490 - 440);
                blue = 1.0;
            } else if (wl >= 490 && wl <= 509) {
                red = 0.0;
                green = 1.0;
                blue = -(wavelength - 510) / (510 - 490);
            } else if (wl >= 510 && wl <= 579) {
                red = (wavelength - 510) / (580 - 510);
                green = 1.0;
                blue = 0.0;
            } else if (wl >= 580 && wl <= 644) {
                red = 1.0;
                green = -(wavelength - 645) / (645 - 580);
                blue = 0.0;
            } else if (wl >= 645 && wl <= 780) {
                red = 1.0;
                green = 0.0;
                blue = 0.0;
            } else {
                red = 0.0;
                green = 0.0;
                blue = 0.0;
            }

            double factor;
            // интенсивность должна ослабевать около пределов зрения
            if (wl >= 380 && wl <= 419) {
                factor = 0.3 + 0.7 * (wavelength - 380) / (420 - 380);
            } else if (wl >= 420 && wl <= 700) {
                factor = 1.0;
            } else if (wl >= 701 && wl <= 780) {
                factor = 0.3 + 0.7 * (780 - wavelength) / (780 - 700);
            } else {
                factor = 0.0;
            }

            int R = SpectrumViewer.adjust(red, factor);
            int G = SpectrumViewer.adjust(green, factor);
            int B = SpectrumViewer.adjust(blue, factor);
            
            return new Color(R, G, B);
        } catch (Exception ex) {
            Logger.write("SpectrumView.WavelengthToRGB(): " + ex.getMessage());
            return Color.black;
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        try {
            if (this.fChanged || this.fBuffer == null) {
                int fBw = super.getWidth();
                int fBh = super.getHeight();

                this.fBuffer = new BufferedImage(fBw, fBh, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gr = (Graphics2D) this.fBuffer.createGraphics();
                gr.setStroke(new BasicStroke(1));

                gr.setColor(Color.black);
                gr.fillRect(0, 0, fBw, fBh);

                byte[] flags;
                if (this.fKind == SpectrumKind.skVisible) {
                    flags = null;
                } else {
                    flags = new byte[fBw];
                    for (int i = 0; i < fBw; i++) {
                        flags[i] = 0;
                    }

                    for (double t : this.fNanometersList) {
                        int index = (int) (Math.round(((fBw - 1) * (t - ChemConsts.BlueWavelength) / (ChemConsts.RedWavelength - ChemConsts.BlueWavelength))));
                        if (index >= 0 && index < fBw) {
                            flags[index] = 1;
                        }
                    }
                }

                for (int i = 0; i < fBw; i++) {
                    double wavelength = (ChemConsts.BlueWavelength + (ChemConsts.RedWavelength - ChemConsts.BlueWavelength) * (double) i / (double) fBw);

                    Color color;
                    if ((this.fKind == SpectrumKind.skEmission && flags[i] == 0) || (this.fKind == SpectrumKind.skAbsorption && flags[i] == 1)) {
                        color = Color.black;
                    } else {
                        color = SpectrumViewer.WavelengthToRGB(wavelength);
                    }

                    gr.setColor(color);
                    gr.drawLine(i, 0, i, fBh - 1);
                }

                if (this.fScaleVisible && this.fScaleInterval > 0) {
                    double wavelength = (ChemConsts.BlueWavelength / (double) this.fScaleInterval) * this.fScaleInterval;

                    while (wavelength <= ChemConsts.RedWavelength) {
                        Color scaleColor;
                        if ((wavelength >= 470f && wavelength <= 620f) && (this.fKind == SpectrumKind.skVisible || this.fKind == SpectrumKind.skAbsorption)) {
                            scaleColor = Color.black;
                        } else {
                            scaleColor = Color.white;
                        }

                        gr.setColor(scaleColor);
                        //Brush brush = new SolidBrush(scaleColor);

                        int x = (int) (Math.round((fBw * (wavelength - ChemConsts.BlueWavelength) / (ChemConsts.RedWavelength - ChemConsts.BlueWavelength))));
                        int y = 8 * fBh / 10;
                        gr.drawLine(x, fBh - 1, x, y);

                        String s = String.valueOf((long) (wavelength));
                        FontMetrics metrics = gr.getFontMetrics();
                        int tw = metrics.stringWidth(s);
                        gr.drawString(s, x - tw / 2, y - 2);

                        wavelength = (wavelength + this.fScaleInterval);
                    }
                }

                if (this.fKind != SpectrumKind.skVisible) {
                }
                
                this.fChanged = false;
            }
            
            g.drawImage(this.fBuffer, 0, 0, null);
        } catch (Exception ex) {
            Logger.write("SpectrumView.paintComponent(): " + ex.getMessage());
        }
    }
}
