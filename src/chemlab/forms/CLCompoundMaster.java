package chemlab.forms;

import bslib.common.AuxUtils;
import bslib.common.FramesHelper;
import chemlab.core.chemical.CompoundElement;
import chemlab.core.chemical.CompoundSolver;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public final class CLCompoundMaster extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JLabel Label1;
    private JLabel Label4;
    private JTabbedPane PageControl;
    private JPanel tsElements;
    private VirtualTable ListView1;
    private JPanel tsProperties;
    private JLabel Label3;
    private JTextField eMolecularMass;
    private JTextField seCharge;
    private JButton btnAnalysis;
    private JTextField eFormula;

    private final CompoundSolver fCompoundMaster;

    public CLCompoundMaster()
    {
        super();
        this.initializeComponents();
        this.fCompoundMaster = new CompoundSolver();
    }

    private void initializeComponents()
    {
        this.Label1 = new JLabel();
        this.Label4 = new JLabel();
        this.PageControl = new JTabbedPane();
        this.tsElements = new JPanel();
        this.ListView1 = new VirtualTable();
        this.tsProperties = new JPanel();
        this.Label3 = new JLabel();
        this.eMolecularMass = new JTextField();
        this.eFormula = new JTextField();
        this.seCharge = new JTextField();
        this.btnAnalysis = new JButton();

        this.setLayout(null);

        FramesHelper.setClientSize(this, 703, 356);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setTitle(res_i18n.getString("CL_COMPOUND_MASTER"));

        this.Label1.setText(res_i18n.getString("CL_FORMULA"));
        this.Label1.setLocation(8, 8);
        this.Label1.setSize(76, 15);

        this.eFormula.setText("H2O");
        this.eFormula.setLocation(8, 23);
        this.eFormula.setSize(543, 20);

        this.Label4.setText(res_i18n.getString("CL_CHARGE"));
        this.Label4.setLocation(558, 8);
        this.Label4.setSize(40, 15);
        this.Label4.setToolTipText("Предназначен для расчета ионов(для расчета соединений = 0)");

        this.seCharge.setText("0");
        this.seCharge.setLocation(558, 23);
        this.seCharge.setSize(45, 20);

        this.btnAnalysis.setText(res_i18n.getString("CL_ANALYSIS"));
        this.btnAnalysis.setLocation(609, 21);
        this.btnAnalysis.setSize(85, 25);
        this.btnAnalysis.addActionListener(this);
        this.btnAnalysis.setActionCommand("ACTION_ANALYSIS");

        this.PageControl.setLocation(8, 59);
        this.PageControl.setSize(687, 289);

        this.tsElements.setLayout(new BorderLayout());
        this.tsElements.add(this.ListView1, BorderLayout.CENTER);
        this.PageControl.addTab(res_i18n.getString("CL_ELEMENTS_CONTENTS"), this.tsElements);

        this.ListView1.setLocation(0, 0);
        this.ListView1.setSize(673, 256);
        this.ListView1.addActionListener(this);
        this.ListView1.addColumn(res_i18n.getString("CL_ELEMENT"), 60);
        this.ListView1.addColumn(res_i18n.getString("CL_INDEX"), 60);
        this.ListView1.addColumn(res_i18n.getString("CL_ATOMIC_MASS"), 79);
        this.ListView1.addColumn(res_i18n.getString("CL_ENEGATIVITY"), 125);
        this.ListView1.addColumn(res_i18n.getString("CL_OXIDATION_NUMBER"), 119);
        this.ListView1.addColumn(res_i18n.getString("CL_VALENCY"), 80);
        this.ListView1.addColumn(res_i18n.getString("CL_MASS_SHARE"), 74);
        this.ListView1.addColumn(res_i18n.getString("CL_COMPONENT"), 74);

        this.tsProperties.setLayout(null);
        this.tsProperties.add(this.Label3);
        this.tsProperties.add(this.eMolecularMass);
        this.PageControl.addTab(res_i18n.getString("CL_PROPERTIES"), this.tsProperties);

        this.Label3.setText(res_i18n.getString("CL_MOLAR_MASS"));
        this.Label3.setLocation(8, 15);
        this.Label3.setSize(160, 15);

        this.eMolecularMass.setEditable(false);
        this.eMolecularMass.setLocation(495, 8);
        this.eMolecularMass.setSize(112, 20);

        this.add(this.Label1);
        this.add(this.Label4);
        this.add(this.PageControl);
        this.add(this.eFormula);
        this.add(this.seCharge);
        this.add(this.btnAnalysis);
        
        CommonUtils.changeFont(this);
    }

    public final String getFormula()
    {
        return this.eFormula.getText();
    }
    
    public final void setFormula(String value)
    {
        this.eFormula.setText(value);
    }

    private void showCompound(CompoundSolver compound)
    {
        for (int i = 0; i < compound.getElementCount(); i++) {
            CompoundElement elem = compound.getElement(i);
            
            Object[] rowData = new Object[] {
                elem.Symbol,
                CommonUtils.formatFloat(elem.Index, 2),
                CommonUtils.formatFloat(elem.AtomicMass, 5),
                CommonUtils.formatFloat(elem.ENegativity, 3),
                elem.DegreeID.Sign,
                elem.ValencyID.Sign,
                CommonUtils.formatFloat(elem.MassShare, 2) + " %",
                "(" + AuxUtils.FloatToStr(compound.Factor) + ")" + compound.Formula
            };
            
            this.ListView1.addRow(rowData);
            
            /*ListViewItem item = this.ListView1.Items.Add(elem.Symbol);

            item.SubItems.Add(CLData.formatFloat(elem.Index, 5));
            item.SubItems.Add(CLData.formatFloat(elem.AtomicMass, 5));
            item.SubItems.Add(CLData.formatFloat(elem.ENegativity, 2));

            item.SubItems.Add(CLData.Degree[elem.DegreeID.getValue()].ID);
            item.SubItems.Add(CLData.Valency[elem.ValencyID.getValue()].ID);
            item.SubItems.Add(CLData.formatFloat(elem.MassShare, 2) + " %");
            item.SubItems.Add("(" + AuxUtils.FloatToStr(compound.Factor) + ")" + compound.Formula);*/
        }
    }

    public final void actAnalysis()
    {
        try {
            this.fCompoundMaster.analyseFull(this.eFormula.getText(), AuxUtils.ParseInt(this.seCharge.getText(), 0));

            this.ListView1.clear();

            if (this.fCompoundMaster.getCompoundCount() > 0) {
                for (int i = 0; i < this.fCompoundMaster.getCompoundCount(); i++) {
                    this.showCompound(this.fCompoundMaster.getCompound(i));
                }
            } else {
                this.showCompound(this.fCompoundMaster);
            }

            this.ListView1.packColumns(10);
            
            this.eMolecularMass.setText(CommonUtils.formatFloat(this.fCompoundMaster.getMolecularMass(), 5));
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, ex.getMessage());
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
                if (source == this.ListView1) {
                    int row = this.ListView1.getSelectedRow();
                    String eSymbol = (String) this.ListView1.getValueAt(row, 0);

                    CLElementViewer elemViewer = new CLElementViewer(eSymbol);
                    elemViewer.setLocationRelativeTo(this);
                    elemViewer.setVisible(true);
                }
                break;
        }
    }
}
