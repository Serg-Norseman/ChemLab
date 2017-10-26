/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2017 by Sergey V. Zhdanovskih.
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
package bslib.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * This class is helper for GridBagLayout.
 *
 * @author Sergey V. Zhdanovskih
 */
public class GBLayoutHelper
{
    private final GridBagConstraints gbc;
    private final JPanel fPanel;

    public GBLayoutHelper(JPanel panel)
    {
        fPanel = panel;
        fPanel.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 5, 5, 5);
    }

    public void addComp(JComponent comp,
            int x, int y, int gWidth, int gHeight)
    {
        addComp(comp, x, y, gWidth, gHeight, GridBagConstraints.BOTH, 1, 1);
    }

    public void addComp(JComponent comp,
            int x, int y, int gWidth, int gHeight,
            int fill, double weightx, double weighty)
    {
        if (comp == null) {
            return;
        }

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = gWidth;
        gbc.gridheight = gHeight;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;

        fPanel.add(comp, gbc);
    }
}
