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
package chemlab.forms;

import bslib.common.FramesHelper;
import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class CommonUtils
{
    public static final Font DEFAULT_UI_FONT = new Font("Verdana", Font.PLAIN, 12);

    public static void changeFont(Component component)
    {
        FramesHelper.changeFont(component, DEFAULT_UI_FONT);
    }

    // for java -> "%5.5f"
    public static String formatFloat(double value, int decimals)
    {
        String fmt = "%5." + String.valueOf(decimals) + "f";
        return String.format(fmt, value);
    }
    
    private static String formatFloat3dp(double number) {
        DecimalFormat form = new DecimalFormat("#.###");
        return form.format(number);
    }

    public static final void showError(Component parent, String message)
    {
        JOptionPane.showMessageDialog(parent, message, "ChemLab", JOptionPane.ERROR_MESSAGE);
    }
}
