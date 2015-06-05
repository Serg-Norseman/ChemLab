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
package chemlab.vtable;

import bslib.common.INotifyHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Класс виртуальной таблицы.
 *
 * @author Serg Zhdanovskih
 * @since 0.6.0
 */
public class VirtualTable extends JPanel
{
    public static final String ACTION_DOUBLE_CLICK = "ACTION_DOUBLE_CLICK";
    public static final String ACTION_ROW_CHANGED = "ACTION_ROW_CHANGED";
    
    private boolean fEditable;
    private AbstractTableModel fModel;
    private final JTable fTable;
    private boolean fVirtualMode;

    private INotifyHandler fOnSelectionChange;
    
    public VirtualTable()
    {
        this(null);
    }

    /**
     * Конструктор экземпляра класса PDTable.
     */
    public VirtualTable(AbstractTableModel model)
    {
        this.setLayout(new BorderLayout());

        if (model == null) {
            model = new VTDefTableModel(this);
        }
        
        this.fVirtualMode = false;
        this.fModel = model;
        this.fTable = new JTable(fModel);

        this.add(fTable.getTableHeader(), BorderLayout.PAGE_START);
        this.add(fTable, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(fTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(fTable);
        this.add(scrollPane);

        fTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fTable.setAutoCreateRowSorter(true);
        fTable.setRowHeight(fTable.getRowHeight() + 6);

        //fTable.setDefaultEditor(null, null);

        fTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent event)
            {
                fireRowChanged();
                if (fOnSelectionChange != null) {
                    fOnSelectionChange.invoke(this);
                }
            }
        });

        fTable.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2) {
                    fireDoubleClick();
                }
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }
        });
    }

    @Override
    public synchronized void addMouseListener(MouseListener l)
    {
        fTable.addMouseListener(l);
    }

    public final JTable getTable()
    {
        return this.fTable;
    }
    
    @Override
    public void setToolTipText(String text)
    {
        fTable.setToolTipText(text);
    }
    
    public void setReorderingAllowed(boolean reorderingAllowed)
    {
        this.fTable.getTableHeader().setReorderingAllowed(reorderingAllowed);
    }
    
    public INotifyHandler getOnSelectionChange()
    {
        return this.fOnSelectionChange;
    }

    public void setOnSelectionChange(INotifyHandler value)
    {
        this.fOnSelectionChange = value;
    }

    public boolean getVirtualMode()
    {
        return this.fVirtualMode;
    }

    public void setVirtualMode(boolean value)
    {
        this.fVirtualMode = value;

        if (value) {
            this.setTableModel(new PIBListModel(this));
        } else {
            this.setTableModel(new VTDefTableModel(this));
        }
    }

    public final boolean getEditable()
    {
        return this.fEditable;
    }

    public final void setEditable(boolean value)
    {
        this.fEditable = value;

        if (this.fVirtualMode) {
            return;
        }
        
        DefaultTableModel defModel = (DefaultTableModel) this.fModel;
    }

    public final int getSelectedRow()
    {
        int row = this.fTable.getSelectedRow();
        return (row < 0) ? row : this.fTable.convertRowIndexToModel(row);
    }

    public final void setSelectedRow(int row)
    {
        this.fTable.setRowSelectionInterval(row, row);
        this.fTable.scrollRectToVisible(new Rectangle(this.fTable.getCellRect(row, 0, true)));
        //tblModel.fireTableDataChanged();
    }

    public final int getSelectedColumn()
    {
        int column = this.fTable.getSelectedColumn();
        return (column < 0) ? column : this.fTable.convertColumnIndexToModel(column);
    }

    /*public final void setSelectedColumn(int column)
    {
        //this.fTable.setColumnSelectionInterval(column, column);
        //this.fTable.scrollRectToVisible(new Rectangle(this.fTable.getCellRect(row, 0, true)));
        //tblModel.fireTableDataChanged();
    }*/

    /**
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
    public final void packColumns(int margin)
    {
        TableColumnModel columnModel = this.fTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            this.packColumn(i, margin);
        }
    }

    /**
     * Sets the preferred width of the visible column specified by colIndex.
     * The column will be just wide enough to show the column head and the
     * widest cell in the column. margin pixels are added to the left and right
     * (resulting in an additional width of 2*margin pixels).
     */
    public final void packColumn(int colIndex, int margin)
    {
        JTable table = this.fTable;
        TableColumnModel colModel = table.getColumnModel();
        TableColumn col = colModel.getColumn(colIndex);
        int width = 0;

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
                table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        // Get maximum width of column data
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, colIndex);
            comp = renderer.getTableCellRendererComponent(
                    table, table.getValueAt(r, colIndex), false, false, r, colIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        // Add margin
        width += 2 * margin;

        // Set the width
        col.setPreferredWidth(width);
    }
    
    public final AbstractTableModel getTableModel()
    {
        return this.fModel;
    }
    
    public void setTableModel(AbstractTableModel model)
    {
        this.fModel = model;
        this.fTable.setModel(model);
        this.fTable.invalidate();
    }

    public final void clear()
    {
        if (this.fVirtualMode) {
            return;
        }

        DefaultTableModel defModel = (DefaultTableModel) this.fModel;
        defModel.setRowCount(0);
    }

    public final void addColumn(String columnName, int width)
    {
        if (this.fVirtualMode) {
            return;
        }

        DefaultTableModel defModel = (DefaultTableModel) this.fModel;
        defModel.addColumn(columnName);
    }

    public final void addRow(Object[] rowData)
    {
        if (this.fVirtualMode) {
            return;
        }

        DefaultTableModel defModel = (DefaultTableModel) this.fModel;
        defModel.addRow(rowData);
    }

    protected String[] getColumns()
    {
        // virtual stub
        return null;
    }

    /**
     * Количество записей в списке
     *
     * @return
     */
    protected int getRowCount()
    {
        if (this.fVirtualMode) {
            // virtual stub
            return 0;
        }

        return fModel.getRowCount();
    }
    
    public Object getValueAt(int row, int col)
    {
        if (this.fVirtualMode) {
            // virtual stub
            return null;
        } else {
            return this.fModel.getValueAt(row, col);
        }
    }

    /**
     * Adds an <code>ActionListener</code> to the VirtualTable.
     * @param l the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an <code>ActionListener</code> from the VirtualTable.
     * If the listener is the currently set <code>Action</code>
     * for the button, then the <code>Action</code>
     * is set to <code>null</code>.
     *
     * @param l the listener to be removed
     */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.
     *
     * @param e the <code>ActionEvent</code> to deliver to listeners
     * @see EventListenerList
     */
    protected void fireActionPerformed(ActionEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                // if (changeEvent == null)
                // changeEvent = new ChangeEvent(this);
                ((ActionListener)listeners[i+1]).actionPerformed(e);
            }
        }
    }

    private void fireRowChanged()
    {
        int modifiers = 0;
        fireActionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                        ACTION_ROW_CHANGED,
                        EventQueue.getMostRecentEventTime(), modifiers));
    }
    
    private void fireDoubleClick()
    {
        int modifiers = 0;
        fireActionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                        ACTION_DOUBLE_CLICK,
                        EventQueue.getMostRecentEventTime(), modifiers));
    }
    
    /**
     * Класс "модели" таблицы - ее внутреннего содержания, данных.
     */
    public class PIBListModel extends AbstractTableModel
    {
        //private final List<PIBListEntry> data;
        private final String[] fColumns;

        private final VirtualTable fTable;

        public PIBListModel(VirtualTable table)
        {
            this.fTable = table;

            this.fColumns = getColumns();

            //this.data = new ArrayList<PIBListEntry>();
            this.addEntry("Empty", "Empty");
        }

        /**
         * Добавление элемента в список
         *
         * @param name
         * @param desc
         */
        public void addEntry(String name, String desc)
        {
            /*PIBListEntry entry = new PIBListEntry();
             entry.selected = false;
             entry.tag = name;
             entry.desc = desc;
             entry.trend = null;
             // проверяем нет ли в табличке строчек с такимже тегом
             int add = 0;
             for (PIBListEntry e : data) {
             if (entry.tag.equals(e.tag)) {
             add++;
             }
             }
             if (add == 0) {
             data.add(entry);
             }*/
        }

        /**
         * Очистка списка тегов, с возможностью сохранения выделенных тегов.
         *
         * @param full - полная очистка
         */
        public void clear(Boolean full)
        {
            /*if (full) {
             data.clear();
             } else {
             int cnt = data.size();
             while (cnt != 0) {
             PIBListEntry entry = (PIBListEntry) data.get(cnt - 1);
             if (!entry.selected) {
             data.remove(entry);
             }
             cnt--;
             }
             }*/
        }

        @Override
        public int getColumnCount()
        {
            return this.fColumns.length;
        }

        @Override
        public String getColumnName(int col)
        {
            return this.fColumns[col];
        }

        @Override
        public Class getColumnClass(int c)
        {
            Object colValue = getValueAt(0, c);
            if (colValue == null) {
                return null;
            }
            return colValue.getClass();
        }

        @Override
        public int getRowCount()
        {
            return this.fTable.getRowCount();
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            return this.fTable.getValueAt(row, col);
        }

        @Override
        public boolean isCellEditable(int row, int col)
        {
            return false; //col == 0;
        }

        @Override
        public void setValueAt(Object value, int row, int col)
        {
            /*if (col == 0) {
             PIBListEntry entry = data.get(row);
             entry.selected = (Boolean) value;

             PDChart pibChart = browser.getChart();

             if (entry.selected) {
             entry.trend = pibChart.addTaggedTrend(entry.tag, entry.desc, true);
             } else {
             pibChart.removeTrend(entry.trend);
             }

             fireTableCellUpdated(row, col);
             }*/
        }
    }

    private static class VTDefTableModel extends DefaultTableModel
    {
        private final VirtualTable fTable;

        public VTDefTableModel(VirtualTable table)
        {
            this.fTable = table;
        }

        @Override
        public boolean isCellEditable(int row, int column)
        {
            if (!this.fTable.fEditable) return false;
            
            return super.isCellEditable(row, column);
        }
    }
}
