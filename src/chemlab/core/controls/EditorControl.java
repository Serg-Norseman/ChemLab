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

import bslib.common.INotifyHandler;
import javax.swing.JPanel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.4.0
 */
public abstract class EditorControl extends JPanel
{
    private INotifyHandler fOnChange;

    public boolean Modified;

    protected EditorControl()
    {
        super();
    }

    public final INotifyHandler getOnChange()
    {
        return this.fOnChange;
    }

    public final void setOnChange(INotifyHandler value)
    {
        this.fOnChange = value;
    }

    public void DoChange()
    {
        this.Modified = true;

        if (this.fOnChange != null) {
            this.fOnChange.invoke(this);
        }
    }

    public abstract void loadFromFile(String fileName);

    public abstract void saveToFile(String fileName);
}
