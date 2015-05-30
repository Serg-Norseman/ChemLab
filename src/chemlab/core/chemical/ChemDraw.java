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
package chemlab.core.chemical;

import bslib.common.Rect;
import bslib.common.StringHelper;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public class ChemDraw
{
    private static final String SNE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz)";
    private static final String SND = ". ,;(*";
    private static final String PM = "+-";
    private static final String SNA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-)";
    private static final char[] SUB_INDEXES = new char[]{'₀','₁','₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉'};
    
    private static int drawTextSlice(Graphics canvas, Font font, /*Brush brush, */String s, int x, int y)
    {
        canvas.setFont(font);
        canvas.drawString(s, x, y);
        FontMetrics metrics = canvas.getFontMetrics();
        return metrics.stringWidth(s);
    }

    public static void ChemTextOut(Graphics canvas, Font font, Rect aRect, String text)
    {
        if (canvas == null || font == null) {
            return;
        }
        if (StringHelper.isNullOrEmpty(text)) {
            return;
        }

        aRect.inflate(-4, 0);

        int X = aRect.Left;
        int Y = aRect.Top;

        float indSize = font.getSize() * 0.8f;
        Font indFont = new Font(font.getFontName(), font.getStyle(), (int)indSize);
        //Brush brush = new SolidBrush(Color.Black);

        int offset = (int) (font.getSize() * 0.45f);
        int xx = X;
        boolean subNos = false;
        int i = 0;
        while (i < text.length()) {
            String s = "" + text.charAt(i);

            if (ChemDraw.SNE.indexOf(s.charAt(0)) >= 0) {
                subNos = true;
            }
            if (ChemDraw.SND.indexOf(s.charAt(0)) >= 0) {
                subNos = false;
            }

            if (CLData.DecimNumbers.indexOf(s.charAt(0)) >= 0) {
                while (i + 1 < text.length() && CLData.DecimNumbers.indexOf(text.charAt(i + 1)) >= 0) {
                    s += text.charAt(i + 1);
                    i++;
                }

                if (subNos) {
                    xx += drawTextSlice(canvas, indFont, /*brush, */s, xx, Y + offset);
                } else {
                    xx += drawTextSlice(canvas, font, /*brush, */s, xx, Y);
                }
            } else if (ChemDraw.PM.indexOf(s.charAt(0)) >= 0 && i > 0 && ChemDraw.SNA.indexOf(text.charAt(i - 1)) >= 0) {
                while (i + 1 < text.length() && CLData.DecimNumbers.indexOf(text.charAt(i + 1)) >= 0) {
                    s += text.charAt(i + 1);
                    i++;
                }

                xx += drawTextSlice(canvas, indFont, /*brush, */s, xx, Y - offset);
            } else {
                if (CLData.LatSymbols.indexOf(text.charAt(i)) >= 0) {
                    while (i + 1 < text.length() && CLData.LatSymbols.indexOf(text.charAt(i + 1)) >= 0) {
                        s += text.charAt(i + 1);
                        i++;
                    }
                }

                xx += drawTextSlice(canvas, font, /*brush, */s, xx, Y);
            }

            i++;
        }
    }

    public static String textToChem(String text)
    {
        if (StringHelper.isNullOrEmpty(text)) {
            return "";
        }

        StringBuilder res = new StringBuilder(text);
        
        boolean subNos = false;
        int i = 0;
        while (i < res.length()) {
            char sym = res.charAt(i);

            if (ChemDraw.SNE.indexOf(sym) >= 0) {
                subNos = true;
            }
            if (ChemDraw.SND.indexOf(sym) >= 0) {
                subNos = false;
            }

            if (CLData.DecimNumbers.indexOf(sym) >= 0) {
                while (i < res.length() && CLData.DecimNumbers.indexOf(res.charAt(i)) >= 0) {
                    if (subNos) {
                        int n = ((int) res.charAt(i)) - 48;
                        res.setCharAt(i, SUB_INDEXES[n]);
                    }
                    i++;
                }
            }

            i++;
        }
        
        return res.toString();
    }
}
