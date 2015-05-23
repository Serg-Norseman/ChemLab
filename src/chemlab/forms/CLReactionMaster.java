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
package chemlab.forms;

import bslib.common.AuxUtils;
import bslib.common.FramesHelper;
import bslib.common.StringHelper;
import chemlab.core.chemical.ChemUtils;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.ReactionSolver;
import chemlab.core.chemical.StoichiometricSolver;
import chemlab.core.chemical.Substance;
import chemlab.core.chemical.InputParams;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public final class CLReactionMaster extends JFrame implements ActionListener
{
    private JLabel Label1;
    private JTextField eEquation;
    private JLabel Label2;
    private JComboBox cbTemperature;
    private JButton btnAnalysis;
    private JTabbedPane PageControl;
    private JPanel panSubstances;
    private VirtualTable tblCompounds;
    private JPanel panProperties;
    private VirtualTable tblProperties;
    private JPanel panStoichiometry;
    private VirtualTable tblStoichiometry;

    private JPopupMenu menuStoichiometry;
    
    private final ReactionSolver fReactionMaster;
    private final StoichiometricSolver fStoichiometricSolver;

    public CLReactionMaster()
    {
        super();
        this.initializeComponents();

        this.fReactionMaster = new ReactionSolver();
        this.fStoichiometricSolver = new StoichiometricSolver(this.fReactionMaster);
    }

    private void initializeComponents()
    {
        this.Label1 = new JLabel();
        this.eEquation = new JTextField();
        this.Label2 = new JLabel();
        this.cbTemperature = new JComboBox();
        this.btnAnalysis = new JButton();
        this.PageControl = new JTabbedPane();
        this.panSubstances = new JPanel();
        this.tblCompounds = new VirtualTable();
        this.panProperties = new JPanel();
        this.tblProperties = new VirtualTable();
        this.panStoichiometry = new JPanel();
        this.tblStoichiometry = new VirtualTable();
        this.menuStoichiometry = new JPopupMenu();

        this.setLayout(null);

        FramesHelper.setClientSize(this, 800, 400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Мастер реакций");

        Label1.setText("Уравнение реакции");
        Label1.setLocation(8, 8);
        Label1.setSize(115, 15);

        // K2O + HNO3 = KNO3 + H2O
        // NH4I + H2SO4 + KMnO4 = I2 + MnSO4 + H2O + (NH4)2SO4 + K2SO4
        eEquation.setText("K2O + HNO3 = KNO3 + H2O");
        eEquation.setLocation(8, 23);
        eEquation.setSize(582, 20);
        //eEquation.TextChanged += this.EqChanged;

        Label2.setText("Температура");
        Label2.setLocation(597, 8);
        Label2.setSize(94, 15);

        cbTemperature.addItem("298.15 °K");
        cbTemperature.setSelectedItem("298.15 °K");
        cbTemperature.setLocation(597, 23);
        cbTemperature.setSize(90, 20);
        cbTemperature.setEditable(true);

        this.btnAnalysis.setText("Анализ");
        this.btnAnalysis.setLocation(698, 21);
        this.btnAnalysis.setSize(94, 25);
        this.btnAnalysis.addActionListener(this);
        this.btnAnalysis.setActionCommand("ACTION_ANALYSIS");

        this.PageControl.setLocation(8, 59);
        this.PageControl.setSize(784, 312);

        this.panSubstances.setLayout(new BorderLayout());
        this.panSubstances.add(this.tblCompounds, BorderLayout.CENTER);
        this.PageControl.addTab("Реагенты", this.panSubstances);

        tblCompounds.setLocation(0, 0);
        tblCompounds.setSize(583, 285);
        tblCompounds.addActionListener(this);
        tblCompounds.addColumn("Коэффициент", 88);
        tblCompounds.addColumn("Соединение", 88);
        tblCompounds.addColumn("Молек. масса", 88);
        tblCompounds.addColumn("Тип", 57);
        tblCompounds.addColumn("Cp°", 59);
        tblCompounds.addColumn("S°", 59);
        tblCompounds.addColumn("dH°", 59);
        tblCompounds.addColumn("dG°", 59);

        this.panProperties.setLayout(new BorderLayout());
        this.panProperties.add(this.tblProperties, BorderLayout.CENTER);
        this.PageControl.addTab("Свойства", this.panProperties);

        tblProperties.setLocation(0, 0);
        tblProperties.setSize(583, 285);

        tblProperties.addColumn("Величина", 231);
        tblProperties.addColumn("Значение", 139);
        tblProperties.addColumn("Размерность", 92);

        StoichiometryTableModel model = new StoichiometryTableModel(tblStoichiometry);
        tblStoichiometry.setTableModel(model);
        tblStoichiometry.setEditable(true);
        tblStoichiometry.addColumn("[К] Соединение", 88);
        tblStoichiometry.addColumn("Молек. масса", 88);
        tblStoichiometry.addColumn("Тип", 57);
        tblStoichiometry.addColumn("Состояние", 57);
        tblStoichiometry.addColumn("Вх/Вых", 57);
        tblStoichiometry.addColumn("Данные", 57);
        tblStoichiometry.addColumn("Результат", 57);
        
        tblStoichiometry.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menuStoichiometry.show(tblStoichiometry, e.getX(), e.getY());
                }
            }
        });
        
        /*TableColumn col = tblStoichiometry.getTable().getColumnModel().getColumn(3);
        col.setCellEditor(new MyComboBoxEditor(CLData.SubstanceStates));
        col.setCellRenderer(new MyComboBoxRenderer(CLData.SubstanceStates));*/

        this.panStoichiometry.setLayout(new BorderLayout());
        this.panStoichiometry.add(this.tblStoichiometry, BorderLayout.CENTER);
        this.PageControl.addTab("Стехиометрия", this.panStoichiometry);
        
        JMenuItem miSetInput = new JMenuItem();
        miSetInput.setText("Задать исходные данные");
        miSetInput.setActionCommand("SET_INPUT");
        miSetInput.addActionListener(this);

        JMenuItem miSetOutput = new JMenuItem();
        miSetOutput.setText("Задать выходные данные");
        miSetOutput.setActionCommand("SET_OUTPUT");
        miSetOutput.addActionListener(this);

        JMenuItem miSolve = new JMenuItem();
        miSolve.setText("Расчет");
        miSolve.setActionCommand("ST_SOLVE");
        miSolve.addActionListener(this);
        
        this.menuStoichiometry.add(miSetInput);
        this.menuStoichiometry.add(miSetOutput);
        this.menuStoichiometry.add(miSolve);
        
        this.add(this.Label1);
        this.add(this.eEquation);
        this.add(this.Label2);
        this.add(this.cbTemperature);
        this.add(this.btnAnalysis);
        this.add(this.PageControl);
        
        CommonUtils.changeFont(this);
    }

    public void EqChanged(Object sender)
    {
        /*try {
            Graphics gr = Graphics.FromHwnd(this.Handle);
            CLCommon.ChemTextOut(gr, this.Font, ExtRect.Create(8, 377, 607, 400), eEquation.Text);
        } catch (Exception ex) {
        }*/
    }

    public final void actAnalysis()
    {
        try {
            try {
                super.setEnabled(false);

                this.fReactionMaster.setEquation(this.eEquation.getText());
                
                try {
                    String temp = (String) this.cbTemperature.getSelectedItem();
                    if (temp.equals("298.15 °K")) {
                        this.fReactionMaster.setTemperature(298.15f);
                    } else {
                        this.fReactionMaster.setTemperature((float) AuxUtils.ParseFloat(temp, 0));
                    }
                } catch (ParseException ex) {
                    throw new Exception("Ошибка формата температуры");
                }

                this.fReactionMaster.analyse();
                this.fReactionMaster.calculate();

                this.eEquation.setText(this.fReactionMaster.getEquation());

                this.updateTables();
            } finally {
                super.setEnabled(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "ChemLab", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTables()
    {
        try {
            this.tblCompounds.clear();
            
            for (int i = 0; i < this.fReactionMaster.getSubstanceCount(); i++) {
                Substance substance = this.fReactionMaster.getSubstance(i);

                String substType = "";
                switch (substance.Type) {
                    case Reagent:
                        substType = ChemUtils.rs_SubstReagent;
                        break;
                    case Product:
                        substType = ChemUtils.rs_SubstProduct;
                        break;
                }

                Object[] rowData = new Object[]{
                    AuxUtils.FloatToStr(substance.Factor),
                    substance.Formula,
                    CommonUtils.formatFloat(substance.getMolecularMass(), 5),
                    substType,
                    CommonUtils.formatFloat(substance.getSM_HeatCapacity(), 3),
                    CommonUtils.formatFloat(substance.getSM_Entropy(), 3),
                    CommonUtils.formatFloat(substance.getSMF_Enthalpy(), 3),
                    CommonUtils.formatFloat(substance.getSMF_Gibbs_Energy(), 3)
                };

                this.tblCompounds.addRow(rowData);

                /*ListViewItem item = this.lvCompounds.Items.Add(AuxUtils.FloatToStr(substance.Factor));
                 item.SubItems.Add(substance.Formula);
                 item.SubItems.Add(CLData.formatFloat(substance.getMolecularMass(), 5));
                 item.SubItems.Add(substType);
                 item.SubItems.Add(CLData.formatFloat(substance.getSM_HeatCapacity(), 3));
                 item.SubItems.Add(CLData.formatFloat(substance.getSM_Entropy(), 3));
                 item.SubItems.Add(CLData.formatFloat(substance.getSMF_Enthalpy(), 3));
                 item.SubItems.Add(CLData.formatFloat(substance.getSMF_Gibbs_Energy(), 3));*/
            }
            this.tblCompounds.packColumns(10);

            this.tblProperties.clear();
            this.addProperty(ChemUtils.rs_ReagentsMass, CommonUtils.formatFloat(this.fReactionMaster.getSourceMass(), 5), "");
            this.addProperty(ChemUtils.rs_ProductsMass, CommonUtils.formatFloat(this.fReactionMaster.getProductMass(), 5), "");
            this.addProperty("dH° (298 K)", CommonUtils.formatFloat(this.fReactionMaster.getSM_Enthalpy(), 3), "кДж");
            this.addProperty("dS° (298 K)", CommonUtils.formatFloat(this.fReactionMaster.getSM_Entropy(), 3), "Дж/К");
            this.addProperty("dG° (298 K)", CommonUtils.formatFloat(this.fReactionMaster.getSM_Gibbs_Energy(), 3), "кДж");
            this.addProperty("lg K°", CommonUtils.formatFloat(this.fReactionMaster.getlg_K(), 3), "");
            this.addProperty("K°", CommonUtils.formatFloat(this.fReactionMaster.getStdBalanceConstant(), 3), "");
            this.addProperty(ChemUtils.rs_MolsInc, CommonUtils.formatFloat(this.fReactionMaster.getdN(), 3), "");
            this.addProperty(ChemUtils.rs_BCFirstApproximation, CommonUtils.formatFloat(this.fReactionMaster.getFBalanceConstant(), 3), "");
            this.addProperty(ChemUtils.rs_BCSecondApproximation, CommonUtils.formatFloat(this.fReactionMaster.getSBalanceConstant(), 3), "");
            this.tblProperties.packColumns(10);
            
            this.updateStoic();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "ChemLab", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStoic()
    {
        try {
            this.tblStoichiometry.clear();
            for (int i = 0; i < this.fReactionMaster.getSubstanceCount(); i++) {
                Substance substance = this.fReactionMaster.getSubstance(i);

                String substType = "";
                switch (substance.Type) {
                    case Reagent:
                        substType = ChemUtils.rs_SubstReagent;
                        break;
                    case Product:
                        substType = ChemUtils.rs_SubstProduct;
                        break;
                }

                String comp = "[" + AuxUtils.FloatToStr(substance.Factor) + "] " + substance.Formula;
                String state = substance.State.toString();
                InputParams params = (InputParams) substance.ExtData;
                String strParams = (params == null) ? "" : params.toString();
                String typ = (params == null) ? "" : params.Type.toString();
                String result = (params == null) ? "" : ((params.Result == null) ? "" : params.Result.toString());
                
                Object[] rowData = new Object[]{
                    comp,
                    CommonUtils.formatFloat(substance.getMolecularMass(true), 5),
                    substType,
                    state,
                    typ,
                    strParams,
                    result
                };

                this.tblStoichiometry.addRow(rowData);
            }
            this.tblStoichiometry.packColumns(10);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "ChemLab", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addProperty(String valueName, String value, String mUnit)
    {
        Object[] rowData = new Object[] {
            valueName, value, mUnit
        };
        
        this.tblProperties.addRow(rowData);
        
        /*ListViewItem item = this.lvProperties.Items.Add(valueName);
        item.SubItems.Add(value);
        item.SubItems.Add(mUnit);*/
    }

    private void solve()
    {
        String source = null;
        String target = null;
        for (int i = 0; i < this.fReactionMaster.getSubstanceCount(); i++) {
            Substance substance = this.fReactionMaster.getSubstance(i);
            InputParams params = (InputParams) substance.ExtData;
            
            if (params != null) {
                if (params.Type == InputParams.ParamType.Input) {
                    source = substance.Formula;
                }
                if (params.Type == InputParams.ParamType.Output) {
                    target = substance.Formula;
                }
            }
        }
        
        if (!StringHelper.isNullOrEmpty(source) && !StringHelper.isNullOrEmpty(target)) {
            double result = this.fStoichiometricSolver.calculate();
            if (result < 0) {
                JOptionPane.showMessageDialog(null, "Error");
                //Answer.setText("Error");
            } else {
                this.updateStoic();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Не заданы исходное и расчитываемое вещества");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();

        switch (actionPerformed) {
            case "ACTION_ANALYSIS":
                this.actAnalysis();
                break;

            case VirtualTable.ACTION_DOUBLE_CLICK:
                if (source == this.tblCompounds) {
                    int row = this.tblCompounds.getSelectedRow();
                    String formula = (String) this.tblCompounds.getValueAt(row, 1);

                    CLCompoundMaster compoundMaster = new CLCompoundMaster();
                    compoundMaster.setLocationRelativeTo(this);
                    compoundMaster.setFormula(formula);
                    compoundMaster.actAnalysis();
                    compoundMaster.setVisible(true);
                }
                break;

            case "SET_INPUT": {
                int row = this.tblStoichiometry.getSelectedRow();
                Substance substance = this.fReactionMaster.getSubstance(row);

                CLSubstanceInput input = new CLSubstanceInput(this, substance, true);
                input.setVisible(true);

                this.updateStoic();
                break;
            }

            case "SET_OUTPUT": {
                int row = this.tblStoichiometry.getSelectedRow();
                Substance substance = this.fReactionMaster.getSubstance(row);

                CLSubstanceInput input = new CLSubstanceInput(this, substance, false);
                input.setVisible(true);

                this.updateStoic();
                break;
            }

            case "ST_SOLVE": {
                this.solve();
                break;
            }
        }
    }
    
    private class StoichiometryTableModel extends DefaultTableModel
    {
        private final VirtualTable fTable;

        public StoichiometryTableModel(VirtualTable table)
        {
            this.fTable = table;
        }

        @Override
        public boolean isCellEditable(int row, int column)
        {
            if (!this.fTable.getEditable()) return false;
            
            return (column >= 3);
        }
    }
    
    class MyComboBoxRenderer extends JComboBox implements TableCellRenderer
    {
        public MyComboBoxRenderer(String[] items)
        {
            super(items);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column)
        {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelectedItem(value);
            return this;
        }
    }

    class MyComboBoxEditor extends DefaultCellEditor
    {
        public MyComboBoxEditor(String[] items)
        {
            super(new JComboBox(items));
        }
    }
}
