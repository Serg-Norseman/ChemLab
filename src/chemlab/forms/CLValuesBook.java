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
import chemlab.core.measure.ChemUnits;
import chemlab.core.measure.DimensionsList;
import chemlab.vtable.VTableModel;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.measure.unit.Unit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public final class CLValuesBook extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JSplitPane splitPane;
    private JPanel gbValues;
    private JPanel gbUnits;
    private VirtualTable ListValues;
    private VirtualTable ListUnits;

    public CLValuesBook()
    {
        super();
        this.initializeComponents();
    }

    private void initializeComponents()
    {
        this.gbValues = new JPanel();
        this.gbUnits = new JPanel();
        this.ListValues = new VirtualTable();
        this.ListUnits = new VirtualTable(null);

        this.setLayout(new BorderLayout());
        FramesHelper.setClientSize(this, 800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setTitle(res_i18n.getString("CL_VALUES_BOOK"));

        this.ListValues.setTableModel(new ValuesModel());
        this.ListValues.addActionListener(this);
        this.ListValues.packColumns(10);
        //this.ListValues.setEditable(false);

        this.ListUnits.setTableModel(new UnitsModel());
        //this.ListUnits.setEditable(false);

        this.gbValues.setBorder(BorderFactory.createTitledBorder(res_i18n.getString("CL_DIMENSIONS")));
        this.gbValues.setMinimumSize(new Dimension(600, 100));
        this.gbValues.setLayout(new BorderLayout());
        this.gbValues.add(this.ListValues, BorderLayout.CENTER);

        this.gbUnits.setBorder(BorderFactory.createTitledBorder(res_i18n.getString("CL_UNITS")));
        this.gbUnits.setMinimumSize(new Dimension(600, 100));
        this.gbUnits.setLayout(new BorderLayout());
        this.gbUnits.add(this.ListUnits, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gbValues, gbUnits);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(1.0);

        this.add(this.splitPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();
        
        switch (actionPerformed) {
            case VirtualTable.ACTION_ROW_CHANGED:
                if (source == this.ListValues) {
                    int rowIndex = this.ListValues.getSelectedRow();

                    DimensionsList.DimRecord valRec = DimensionsList.getInstance().getList().get(rowIndex);
                    UnitsModel unModel = (UnitsModel) this.ListUnits.getTableModel();
                    unModel.setValueId(valRec.BaseUnit);
                    this.ListUnits.packColumns(10);
                }
                break;
        }
    }

    private static class ValuesModel extends VTableModel
    {
        public ValuesModel()
        {
            super();
        }

        @Override
        protected void initColumns()
        {
            this.addColumn(res_i18n.getString("CL_Sign"), String.class);
            this.addColumn(res_i18n.getString("CL_Name"), String.class);
            this.addColumn(res_i18n.getString("CL_Value"), String.class);
            this.addColumn(res_i18n.getString("CL_Unit"), String.class);
        }

        @Override
        public int getRowCount()
        {
            return DimensionsList.getInstance().getList().size();
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            DimensionsList.DimRecord valRec = DimensionsList.getInstance().getList().get(row);

            Object val = null;
            switch (col) {
                case 0:
                    val = "";
                    break;
                case 1:
                    val = valRec.Name;
                    break;
                case 2:
                    val = "";
                    break;
                case 3:
                    val = valRec.BaseUnit.toString();
                    break;
            }
            return val;
        }
    }

    private static class UnitsModel extends VTableModel
    {
        private Unit<?> fValueId;
        private final ArrayList<Unit<?>> fList;
        
        public UnitsModel()
        {
            super();
            this.fList = new ArrayList<>();
        }

        public final void setValueId(Unit<?> valueId)
        {
            this.fValueId = valueId;

            this.fList.clear();
            List<Unit<?>> units = ChemUnits.getInstance().getUnits();
            for (Unit<?> un : units) {
                if (un.isCompatible(valueId)) {
                    this.fList.add(un);
                }
            }
            
            this.fireTableDataChanged();
        }
        
        @Override
        protected void initColumns()
        {
            this.addColumn(res_i18n.getString("CL_Sign"), String.class);
            this.addColumn(res_i18n.getString("CL_Name"), String.class);
            this.addColumn(res_i18n.getString("CL_Factor"), String.class);
            this.addColumn(res_i18n.getString("CL_System"), String.class);
        }

        @Override
        public int getRowCount()
        {
            return this.fList.size();
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            Unit<?> unRec = this.fList.get(row);

            Object val = null;
            switch (col) {
                case 0:
                    val = unRec.toString();
                    break;
                case 1:
                    val = "";
                    break;
                case 2:
                    val = "";
                    break;
                case 3:
                    val = "";
                    break;
            }
            return val;
        }
    }
}
