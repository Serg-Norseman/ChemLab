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
import chemlab.core.chemical.ChemDraw;
import chemlab.core.chemical.ReactionSolver;
import chemlab.core.chemical.StoicParams;
import chemlab.core.chemical.StoichiometricSolver;
import chemlab.core.chemical.Substance;
import chemlab.core.chemical.SubstanceState;
import chemlab.core.chemical.ThermodynamicSolver;
import chemlab.core.measure.ChemUnits;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public final class CLReactionMaster extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JLabel Label1;
    private JTextField eEquation;
    private JLabel Label2;
    private JComboBox cbTemperature;
    private JButton btnAnalysis;
    private JTabbedPane PageControl;
    private JPanel panSubstances;
    private VirtualTable tblSubstances;
    private JPanel panProperties;
    private VirtualTable tblProperties;
    private JTextArea txtProps;
    private JSplitPane Splitter1;

    private JPopupMenu menuStoichiometry;
    
    private final ReactionSolver fReactionMaster;
    private final StoichiometricSolver fStoichiometricSolver;
    private final ThermodynamicSolver fThermodynamicSolver;

    public CLReactionMaster()
    {
        super();
        this.initializeComponents();

        this.fReactionMaster = new ReactionSolver();
        this.fStoichiometricSolver = new StoichiometricSolver(this.fReactionMaster);
        this.fThermodynamicSolver = new ThermodynamicSolver(this.fReactionMaster);
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
        this.tblSubstances = new VirtualTable();
        this.panProperties = new JPanel();
        this.tblProperties = new VirtualTable();
        this.menuStoichiometry = new JPopupMenu();
        this.txtProps = new JTextArea();

        this.setLayout(null);

        FramesHelper.setClientSize(this, 1000, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setTitle(res_i18n.getString("CL_REACTION_MASTER"));

        Label1.setText(res_i18n.getString("CL_Equation"));
        Label1.setLocation(8, 8);
        Label1.setSize(150, 15);

        // K2O + HNO3 = KNO3 + H2O
        // NH4I + H2SO4 + KMnO4 = I2 + MnSO4 + H2O + (NH4)2SO4 + K2SO4
        // K2O + HNO3 = KNO3 + H2O
        // SO2Cl2 = SO2 + Cl2
        eEquation.setText("NH4I + H2SO4 + KMnO4 = I2 + MnSO4 + H2O + (NH4)2SO4 + K2SO4");
        eEquation.setLocation(8, 23);
        eEquation.setSize(782, 20);
        
        // TODO: debug, remove
        eEquation.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                setTitle(ChemDraw.textToChem(eEquation.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                setTitle(ChemDraw.textToChem(eEquation.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                setTitle(ChemDraw.textToChem(eEquation.getText()));
            }
        });

        Label2.setText(res_i18n.getString("CL_Temperature"));
        Label2.setLocation(797, 8);
        Label2.setSize(94, 15);

        cbTemperature.addItem("298.15 °K");
        cbTemperature.setSelectedItem("298.15 °K");
        cbTemperature.setLocation(797, 23);
        cbTemperature.setSize(90, 20);
        cbTemperature.setEditable(true);

        this.btnAnalysis.setText(res_i18n.getString("CL_Analysis"));
        this.btnAnalysis.setLocation(898, 21);
        this.btnAnalysis.setSize(94, 25);
        this.btnAnalysis.addActionListener(this);
        this.btnAnalysis.setActionCommand("ACTION_ANALYSIS");

        this.PageControl.setLocation(8, 59);
        this.PageControl.setSize(984, 546);

        JPanel panCalcToolbar = new JPanel();
        panCalcToolbar.setLayout(new BoxLayout(panCalcToolbar, BoxLayout.LINE_AXIS));

        JButton btnSetInput = new JButton(res_i18n.getString("CL_SET_SOURCE"));
        btnSetInput.setActionCommand("SET_INPUT");
        btnSetInput.addActionListener(this);

        JButton btnSetOutput = new JButton(res_i18n.getString("CL_SET_TARGET"));
        btnSetOutput.setActionCommand("SET_OUTPUT");
        btnSetOutput.addActionListener(this);

        JButton btnThermSolve = new JButton(/*res_i18n.getString("CL_Solve")*/"Therm Solve");
        btnThermSolve.setActionCommand("ST_THERM_SOLVE");
        btnThermSolve.addActionListener(this);

        JButton btnStoicSolve = new JButton("Stoic " + res_i18n.getString("CL_Solve"));
        btnStoicSolve.setActionCommand("ST_SOLVE");
        btnStoicSolve.addActionListener(this);
        
        panCalcToolbar.add(btnSetInput);
        panCalcToolbar.add(btnSetOutput);
        panCalcToolbar.add(btnThermSolve);
        panCalcToolbar.add(btnStoicSolve);

        txtProps.setEditable(false);
        txtProps.setLineWrap(true);
        txtProps.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(txtProps, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(txtProps);
        scrollPane.setPreferredSize(new Dimension(100, 150));

        this.Splitter1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tblSubstances, scrollPane);
        this.Splitter1.setOneTouchExpandable(true);
        this.Splitter1.setDividerLocation(250);
        this.Splitter1.setResizeWeight(0.2);

        this.panSubstances.setLayout(new BorderLayout());
        this.panSubstances.add(panCalcToolbar, BorderLayout.NORTH);
        this.panSubstances.add(this.Splitter1, BorderLayout.CENTER);
        //this.panSubstances.add(this.txtProps, BorderLayout.SOUTH);
        //this.panSubstances.add(this.tblSubstances, BorderLayout.CENTER);
        this.PageControl.addTab(res_i18n.getString("CL_SUBSTANCES"), this.panSubstances);

        tblSubstances.addActionListener(this);
        tblSubstances.addColumn(res_i18n.getString("CL_F_COMPOUND"), 88);
        tblSubstances.addColumn(res_i18n.getString("CL_MolarMass"), 88);
        tblSubstances.addColumn("[F *] " + res_i18n.getString("CL_MolarMass"), 88);
        tblSubstances.addColumn(res_i18n.getString("CL_Type"), 57);
        tblSubstances.addColumn(res_i18n.getString("CL_State"), 57);
        tblSubstances.addColumn("Cp°", 59);
        tblSubstances.addColumn("S°", 59);
        tblSubstances.addColumn("dH°", 59);
        tblSubstances.addColumn("dG°", 59);
        tblSubstances.addColumn(res_i18n.getString("CL_IO"), 57);
        tblSubstances.addColumn(res_i18n.getString("CL_DATA"), 57);
        tblSubstances.addColumn(res_i18n.getString("CL_RESULT"), 57);

        this.panProperties.setLayout(new BorderLayout());
        this.panProperties.add(this.tblProperties, BorderLayout.CENTER);
        this.PageControl.addTab(res_i18n.getString("CL_Properties"), this.panProperties);

        tblProperties.addColumn(res_i18n.getString("CL_DIMENSION"), 231);
        tblProperties.addColumn(res_i18n.getString("CL_Value"), 139);

        //StoichiometryTableModel model = new StoichiometryTableModel(tblStoichiometry);
        //tblStoichiometry.setTableModel(model);
        //tblStoichiometry.setEditable(true);
        
        tblSubstances.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    menuStoichiometry.show(tblSubstances, e.getX(), e.getY());
                }
            }
        });
        
        /*TableColumn col = tblStoichiometry.getTable().getColumnModel().getColumn(3);
        col.setCellEditor(new MyComboBoxEditor(CLData.SubstanceStates));
        col.setCellRenderer(new MyComboBoxRenderer(CLData.SubstanceStates));*/
        
        JMenuItem miSetInput = new JMenuItem(res_i18n.getString("CL_SET_SOURCE"));
        miSetInput.setActionCommand("SET_INPUT");
        miSetInput.addActionListener(this);

        JMenuItem miSetOutput = new JMenuItem(res_i18n.getString("CL_SET_TARGET"));
        miSetOutput.setActionCommand("SET_OUTPUT");
        miSetOutput.addActionListener(this);

        JMenuItem miSolve = new JMenuItem(res_i18n.getString("CL_Solve"));
        miSolve.setActionCommand("ST_SOLVE");
        miSolve.addActionListener(this);

        JMenuItem miSep = new JMenuItem("States:");
        miSep.setEnabled(false);

        JMenuItem miStSolid = new JMenuItem("CL_Solid");
        miStSolid.setActionCommand("SET_SOLID");
        miStSolid.addActionListener(this);

        JMenuItem miStLiquid = new JMenuItem("CL_Liquid");
        miStLiquid.setActionCommand("SET_LIQUID");
        miStLiquid.addActionListener(this);

        JMenuItem miStGas = new JMenuItem("CL_Gas");
        miStGas.setActionCommand("SET_GAS");
        miStGas.addActionListener(this);
        
        this.menuStoichiometry.add(miSetInput);
        this.menuStoichiometry.add(miSetOutput);
        this.menuStoichiometry.add(miSolve);

        this.menuStoichiometry.add(miSep);
        this.menuStoichiometry.add(miStSolid);
        this.menuStoichiometry.add(miStLiquid);
        this.menuStoichiometry.add(miStGas);
        
        this.add(this.Label1);
        this.add(this.eEquation);
        this.add(this.Label2);
        this.add(this.cbTemperature);
        this.add(this.btnAnalysis);
        this.add(this.PageControl);
        
        CommonUtils.changeFont(this);
    }

    public final void actAnalysis()
    {
        try {
            try {
                super.setEnabled(false);

                this.fReactionMaster.setEquation(this.eEquation.getText());
                this.fReactionMaster.analyse();
                this.fReactionMaster.calculate();

                try {
                    String temp = (String) this.cbTemperature.getSelectedItem();
                    if (temp.equals("298.15 °K")) {
                        this.fThermodynamicSolver.setTemperature(298.15f);
                    } else {
                        this.fThermodynamicSolver.setTemperature((float) AuxUtils.parseFloat(temp, 0));
                    }
                } catch (ParseException ex) {
                    throw new Exception(res_i18n.getString("CL_TemperatureFormat_Invalid"));
                }
                
                this.eEquation.setText(this.fReactionMaster.getEquation());

                this.updateTables();
            } finally {
                super.setEnabled(true);
            }
        } catch (Exception ex) {
            CommonUtils.showError(this, ex.getMessage());
        }
    }

    private void updateTables()
    {
        try {
            this.tblSubstances.clear();
            this.txtProps.setText("");
            
            for (int i = 0; i < this.fReactionMaster.getSubstanceCount(); i++) {
                Substance substance = this.fReactionMaster.getSubstance(i);

                String substType = "";
                switch (substance.Type) {
                    case Reactant:
                        substType = res_i18n.getString("CL_reactant");
                        break;
                    case Product:
                        substType = res_i18n.getString("CL_product");
                        break;
                }

                String comp = "[" + AuxUtils.FloatToStr(substance.Factor) + "] " + substance.Formula;
                String state = substance.getState().toString();

                StoicParams params = substance.getStoicParams();
                String strParams = (params == null) ? "" : params.toString();
                String typ = (params == null) ? "" : params.Type.toString();
                String result = (params == null) ? "" : ((params.Result == null) ? "" : params.Result.toString());
                
                Object[] rowData = new Object[]{
                    comp,
                    CommonUtils.formatFloat(substance.getMolecularMass(), 5),
                    CommonUtils.formatFloat(substance.getMolecularMass(true), 5),
                    substType,
                    state,
                    CommonUtils.formatFloat(substance.getMolarHeatCapacity(), 3),
                    CommonUtils.formatFloat(substance.getStandardEntropy(), 3),
                    CommonUtils.formatFloat(substance.getHeatOfFormation(), 3),
                    CommonUtils.formatFloat(substance.getGibbsFreeEnergy(), 3),
                    typ,
                    strParams,
                    result
                };

                this.tblSubstances.addRow(rowData);
            }
            this.tblSubstances.packColumns(10);

            this.tblProperties.clear();
            this.addProperty(res_i18n.getString("CL_REACTION_TYPE"), this.fReactionMaster.getReactionType().name());
            this.addProperty(res_i18n.getString("CL_DIRECTION"), this.fReactionMaster.getReactionDirection().name());
            this.addProperty(res_i18n.getString("CL_REACTANTS_MASS"), CommonUtils.formatFloat(this.fReactionMaster.getSourceMass(), 5));
            this.addProperty(res_i18n.getString("CL_PRODUCTS_MASS"), CommonUtils.formatFloat(this.fReactionMaster.getProductMass(), 5));
            this.addProperty("dH° (298 K)", ChemUnits.toString(this.fThermodynamicSolver.getTotalEnthalpy(), "%5.5f"));
            this.addProperty("dS° (298 K)", ChemUnits.toString(this.fThermodynamicSolver.getTotalEntropy(), "%5.5f"));
            this.addProperty("dG° (298 K)", ChemUnits.toString(this.fThermodynamicSolver.getGibbsFreeEnergy(), "%5.5f"));
            this.addProperty("lg K°", CommonUtils.formatFloat(this.fThermodynamicSolver.getlg_K(), 3));
            this.addProperty("K°", CommonUtils.formatFloat(this.fThermodynamicSolver.getStdBalanceConstant(), 3));
            this.addProperty("Изменение числа молей", CommonUtils.formatFloat(this.fThermodynamicSolver.getdN(), 3));
            this.addProperty("K(реакц., первое прибл.)", CommonUtils.formatFloat(this.fThermodynamicSolver.getFBalanceConstant(), 3));
            this.addProperty("K(реакц., второе прибл.)", CommonUtils.formatFloat(this.fThermodynamicSolver.getSBalanceConstant(), 3));
            this.tblProperties.packColumns(10);
        } catch (Exception ex) {
            CommonUtils.showError(this, ex.getMessage());
        }
    }

    private void addProperty(String valueName, String value)
    {
        Object[] rowData = new Object[] { valueName, value };
        this.tblProperties.addRow(rowData);

        this.txtProps.setText(this.txtProps.getText() + "\n" + 
                AuxUtils.adjustString(valueName + ": ", 5) + value);
    }

    private void thermSolve()
    {
        if (!this.fThermodynamicSolver.checkInput()) {
            return;
        }

        this.fThermodynamicSolver.calculate();
        this.updateTables();
    }

    private void stoicSolve()
    {
        boolean hasSourceData = false;
        boolean hasTargetData = false;
        for (int i = 0; i < this.fReactionMaster.getSubstanceCount(); i++) {
            Substance substance = this.fReactionMaster.getSubstance(i);
            StoicParams params = substance.getStoicParams();
            
            if (params != null) {
                switch (params.Type) {
                    case None:
                        params.Type = StoicParams.ParamType.Output;
                        params.Mode = StoicParams.InputMode.imSolid_M;
                        params.ResultUnit = ChemUnits.GRAM;
                        hasTargetData = true;
                        break;

                    case Input:
                        hasSourceData = true;
                        break;

                    case Output:
                        hasTargetData = true;
                        break;
                }
            }
        }
        
        if (hasSourceData && hasTargetData) {
            double result = this.fStoichiometricSolver.calculate();
            if (result < 0) {
                CommonUtils.showError(this, "Error");
            } else {
                this.updateTables();
            }
        } else {
            CommonUtils.showError(this, res_i18n.getString("CL_INPUT_OUTPUT_SUBSTANCES_NOT_DEFINED"));
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
                if (source == this.tblSubstances) {
                    int row = this.tblSubstances.getSelectedRow();
                    Substance substance = this.fReactionMaster.getSubstance(row);

                    CLCompoundMaster compoundMaster = new CLCompoundMaster();
                    compoundMaster.setLocationRelativeTo(this);
                    compoundMaster.setFormula(substance.Formula);
                    compoundMaster.actAnalysis();
                    compoundMaster.setVisible(true);
                }
                break;

            case "SET_INPUT": {
                int row = this.tblSubstances.getSelectedRow();
                if (row < 0) {
                    CommonUtils.showError(this, res_i18n.getString("CL_SUBSTANCE_NOT_SELECTED"));
                } else {
                    Substance substance = this.fReactionMaster.getSubstance(row);
                    substance.getStoicParams().Type = StoicParams.ParamType.Input;

                    CLSubstanceInput input = new CLSubstanceInput(this, substance);
                    input.setVisible(true);

                    this.updateTables();
                }
                break;
            }

            case "SET_OUTPUT": {
                int row = this.tblSubstances.getSelectedRow();
                if (row < 0) {
                    CommonUtils.showError(this, res_i18n.getString("CL_SUBSTANCE_NOT_SELECTED"));
                } else {
                    Substance substance = this.fReactionMaster.getSubstance(row);
                    substance.getStoicParams().Type = StoicParams.ParamType.Output;

                    CLSubstanceInput input = new CLSubstanceInput(this, substance);
                    input.setVisible(true);

                    this.updateTables();
                }
                break;
            }

            case "ST_THERM_SOLVE": {
                this.thermSolve();
                break;
            }

            case "ST_SOLVE": {
                this.stoicSolve();
                break;
            }
            
            case "SET_SOLID": {
                int row = this.tblSubstances.getSelectedRow();
                Substance substance = this.fReactionMaster.getSubstance(row);
                substance.setState(SubstanceState.Solid);

                this.updateTables();
                break;
            }
            
            case "SET_LIQUID": {
                int row = this.tblSubstances.getSelectedRow();
                Substance substance = this.fReactionMaster.getSubstance(row);
                substance.setState(SubstanceState.Liquid);

                this.updateTables();
                break;
            }
            
            case "SET_GAS": {
                int row = this.tblSubstances.getSelectedRow();
                Substance substance = this.fReactionMaster.getSubstance(row);
                substance.setState(SubstanceState.Gas);

                this.updateTables();
                break;
            }
        }
    }
    
    /*private class StoichiometryTableModel extends DefaultTableModel
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
    }*/
}
