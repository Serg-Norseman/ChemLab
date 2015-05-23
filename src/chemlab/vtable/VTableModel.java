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
package chemlab.vtable;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public abstract class VTableModel extends AbstractTableModel
{
    private class VTColumn
    {
        public String Name;
        public Class DataClass;
    }

    private final ArrayList<VTColumn> fColumns;

    public VTableModel()
    {
        this.fColumns = new ArrayList<>();
        this.initColumns();
    }

    protected final void addColumn(String name, Class dataClass)
    {
        VTColumn column = new VTColumn();
        column.Name = name;
        column.DataClass = dataClass;
        this.fColumns.add(column);
    }

    protected abstract void initColumns();

    @Override
    public final int getColumnCount()
    {
        return this.fColumns.size();
    }

    @Override
    public final String getColumnName(int col)
    {
        return this.fColumns.get(col).Name;
    }

    @Override
    public final Class getColumnClass(int col)
    {
        return this.fColumns.get(col).DataClass;
    }
}
