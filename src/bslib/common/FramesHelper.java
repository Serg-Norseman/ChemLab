/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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
package bslib.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public final class FramesHelper
{
    public static final void setClientSize(Window window, int width, int height)
    {
        window.setSize(width, height);
        window.pack();

        Insets insets = window.getInsets();
        Dimension prefSize = new Dimension(width + (insets.left + insets.right), height + (insets.top + insets.bottom));
        window.setPreferredSize(prefSize);
        window.setMinimumSize(prefSize);
        window.pack();
    }

    public static void changeFont(Component component, Font font)
    {
        component.setFont(font);
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                changeFont(child, font);
            }
        }
    }
}
