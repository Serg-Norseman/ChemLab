/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Sergey V. Zhdanovskih.
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
package bslib.components.vtable;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public abstract class VTableModel extends AbstractTableModel
{
    private static class VTColumn
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

    public final void clearColumns()
    {
        fColumns.clear();
    }

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
