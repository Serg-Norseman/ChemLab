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
import chemlab.core.chemical.ValencyId;
import chemlab.core.chemical.ValencySet;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public final class ValencyControl extends JPanel
{
    private static class ValencyButton extends JToggleButton
    {
        public ValencyId Index = ValencyId.V1;

        public ValencyButton()
        {
            super();
        }
    }

    private final ValencyButton[] fButtons = new ValencyButton[8];
    private ValencySet fValencySet;

    public ValencyControl()
    {
        super();

        this.setLayout(null);

        for (ValencyId val : ValencyId.values()) {
            ValencyButton btn = new ValencyButton();
            btn.Index = val;
            btn.setEnabled(true);
            btn.setVisible(true);
            btn.setLocation(val.getValue() * 24, 0);
            btn.setSize(super.getWidth() / 8, 24);
            btn.setText(val.Sign);
            btn.setFont(this.getFont());
            btn.setForeground(ColorUtil.BGRToRGB(8421376));
            btn.setMargin(new Insets(0, 0, 0, 0));
            //btn.Click += this.ButtonClick;

            this.fButtons[val.getValue()] = btn;
            this.add(btn);
        }

        this.fValencySet = new ValencySet();
    }

    public final ValencySet getValencySet()
    {
        return this.fValencySet;
    }

    public final void setValencySet(ValencySet value)
    {
        if (!this.fValencySet.equals(value)) {
            this.fValencySet = value;

            for (ValencyId vi : ValencyId.values()) {
                if (this.fValencySet.contains(vi)) {
                    this.fButtons[vi.getValue()].setSelected(true);
                } else {
                    this.fButtons[vi.getValue()].setSelected(false);
                }
            }
        }
    }

    /*private void JButtonClick(Object Sender, EventArgs e)
    {
        ValencyId Index = ((ValencyButton) ((Sender instanceof ValencyButton) ? Sender : null)).Index;
        if (this.fValencySet.contains(Index)) {
            this.fValencySet.exclude(Index);
        } else {
            this.fValencySet.include(Index);
        }

        this.Change();
    }*/

    protected void Change()
    {
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);

        int btnW = width / 8;

        for (ValencyId i : ValencyId.values()) {
            int idx = i.getValue();
            this.fButtons[idx].setSize(btnW, height);
            this.fButtons[idx].setLocation(idx * btnW, 0);
        }
    }
}
