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

import bslib.common.AuxUtils;
import bslib.common.FramesHelper;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.ValueId;
import chemlab.refbooks.UnitRecord;
import chemlab.refbooks.ValueRecord;
import chemlab.vtable.VTableModel;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
        this.setTitle("Справочник величин");

        this.ListValues.setTableModel(new ValuesModel());
        this.ListValues.addActionListener(this);
        this.ListValues.packColumns(10);
        //this.ListValues.setEditable(false);

        this.ListUnits.setTableModel(new UnitsModel());
        //this.ListUnits.setEditable(false);

        this.gbValues.setBorder(BorderFactory.createTitledBorder("Величины"));
        this.gbValues.setMinimumSize(new Dimension(600, 100));
        this.gbValues.setLayout(new BorderLayout());
        this.gbValues.add(this.ListValues, BorderLayout.CENTER);

        this.gbUnits.setBorder(BorderFactory.createTitledBorder("Единицы измерения"));
        this.gbUnits.setMinimumSize(new Dimension(600, 100));
        this.gbUnits.setLayout(new BorderLayout());
        this.gbUnits.add(this.ListUnits, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gbValues, gbUnits);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
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

                    ValueRecord valRec = CLData.ValuesTable.get(rowIndex);
                    UnitsModel unModel = (UnitsModel) this.ListUnits.getTableModel();
                    unModel.setValueId(valRec.Id);
                    this.ListUnits.packColumns(10);
                }
                break;
        }
    }

    private class ValuesModel extends VTableModel
    {
        public ValuesModel()
        {
            super();
        }

        @Override
        protected void initColumns()
        {
            this.addColumn("Обозначение", String.class);
            this.addColumn("Название", String.class);
            this.addColumn("Значение", String.class);
            this.addColumn("Единица измерения", String.class);
        }

        @Override
        public int getRowCount()
        {
            return CLData.ValuesTable.size();
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            ValueRecord valRec = CLData.ValuesTable.get(row);

            Object val = null;
            switch (col) {
                case 0:
                    val = valRec.Sign;
                    break;
                case 1:
                    val = valRec.Name;
                    break;
                case 2:
                    val = AuxUtils.FloatToStr(valRec.Value);
                    break;
                case 3:
                    val = CLData.UnitsTable.get(valRec.UnitId).Name;
                    break;
            }
            return val;
        }
    }

    private class UnitsModel extends VTableModel
    {
        private ValueId fValueId;
        private final ArrayList<UnitRecord> fList;
        
        public UnitsModel()
        {
            super();
            this.fList = new ArrayList<>();
        }

        public final void setValueId(ValueId valueId)
        {
            this.fValueId = valueId;

            this.fList.clear();
            for (UnitRecord unRec : CLData.UnitsTable) {
                if (unRec.ValId == this.fValueId) {
                    this.fList.add(unRec);
                }
            }
            
            this.fireTableDataChanged();
        }
        
        @Override
        protected void initColumns()
        {
            this.addColumn("Обозначение", String.class);
            this.addColumn("Название", String.class);
            this.addColumn("Коэффициент", String.class);
            this.addColumn("Система", String.class);
        }

        @Override
        public int getRowCount()
        {
            return this.fList.size();
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            UnitRecord unRec = this.fList.get(row);

            Object val = null;
            switch (col) {
                case 0:
                    val = unRec.Sign;
                    break;
                case 1:
                    val = unRec.Name;
                    break;
                case 2:
                    val = AuxUtils.FloatToStr(unRec.Factor);
                    break;
                case 3:
                    val = "";
                    break;
            }
            return val;
        }
    }
}
