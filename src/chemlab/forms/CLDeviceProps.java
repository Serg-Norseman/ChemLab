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
import chemlab.core.chemical.ChemUtils;
import chemlab.core.chemical.Substance;
import chemlab.core.controls.experiment.LabDevice;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class CLDeviceProps extends JDialog implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private final LabDevice fDevice;
    
    private JTabbedPane PageControl;
    private JPanel panSubstances;
    private VirtualTable tblSubstances;
    private JPanel panProperties;
    private VirtualTable tblProperties;
    
    public CLDeviceProps(Frame owner, LabDevice device)
    {
        super(owner, true);
        
        this.fDevice = device;

        this.setLayout(new BorderLayout());
        this.setTitle(this.fDevice.getID().name());
        this.setResizable(false);

        this.PageControl = new JTabbedPane();
        this.panSubstances = new JPanel();
        this.tblSubstances = new VirtualTable();
        this.panProperties = new JPanel();
        this.tblProperties = new VirtualTable();
        
        this.add(PageControl, BorderLayout.CENTER);

        this.panSubstances.setLayout(new BorderLayout());
        this.PageControl.addTab(res_i18n.getString("CL_SUBSTANCES"), this.panSubstances);

        this.panProperties.setLayout(new BorderLayout());
        this.panProperties.add(this.tblProperties, BorderLayout.CENTER);
        this.PageControl.addTab(res_i18n.getString("CL_PROPERTIES"), this.panProperties);

        JPanel panSubstToolbar = new JPanel();
        panSubstToolbar.setLayout(new BoxLayout(panSubstToolbar, BoxLayout.LINE_AXIS));

        JButton btnAddSubst = new JButton();
        btnAddSubst.setText("Add subst");
        btnAddSubst.setActionCommand("ADD_SUBST");
        btnAddSubst.addActionListener(this);
        panSubstToolbar.add(btnAddSubst);

        this.panSubstances.add(panSubstToolbar, BorderLayout.NORTH);
        this.panSubstances.add(this.tblSubstances, BorderLayout.CENTER);

        Dimension tblDim = new Dimension(580, 280);
        
        tblSubstances.setPreferredSize(tblDim);
        tblSubstances.addColumn(res_i18n.getString("CL_FACTOR"), 88);
        tblSubstances.addColumn(res_i18n.getString("CL_COMPOUND"), 88);
        tblSubstances.addColumn(res_i18n.getString("CL_MOLAR_MASS"), 88);
        tblSubstances.addColumn(res_i18n.getString("CL_TYPE"), 57);
        tblSubstances.addColumn("Cp°", 59);
        tblSubstances.addColumn("S°", 59);
        tblSubstances.addColumn("dH°", 59);
        tblSubstances.addColumn("dG°", 59);

        tblProperties.setPreferredSize(tblDim);
        tblProperties.addColumn(res_i18n.getString("CL_DIMENSION"), 231);
        tblProperties.addColumn(res_i18n.getString("CL_VALUE"), 139);
        tblProperties.addColumn(res_i18n.getString("CL_UNIT"), 92);

        this.pack();
        this.setLocationRelativeTo(owner);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.updateControls();
    }
    
    private void updateControls()
    {
        try {
            this.tblSubstances.clear();
            
            for (int i = 0; i < this.fDevice.getSubstancesCount(); i++) {
                Substance substance = this.fDevice.getSubstance(i);

                String substType = "";
                switch (substance.Type) {
                    case Reactant:
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

                this.tblSubstances.addRow(rowData);

                /*ListViewItem item = this.lvCompounds.Items.Add(AuxUtils.FloatToStr(substance.Factor));
                 item.SubItems.Add(substance.Formula);
                 item.SubItems.Add(CLData.formatFloat(substance.getMolecularMass(), 5));
                 item.SubItems.Add(substType);
                 item.SubItems.Add(CLData.formatFloat(substance.getSM_HeatCapacity(), 3));
                 item.SubItems.Add(CLData.formatFloat(substance.getSM_Entropy(), 3));
                 item.SubItems.Add(CLData.formatFloat(substance.getSMF_Enthalpy(), 3));
                 item.SubItems.Add(CLData.formatFloat(substance.getSMF_Gibbs_Energy(), 3));*/
            }
            this.tblSubstances.packColumns(10);

            this.tblProperties.clear();
            /*this.addProperty(res_i18n.getString("CL_REACTION_TYPE"), this.fReactionMaster.getReactionType().name(), "");
            this.addProperty(ChemUtils.rs_ReagentsMass, CommonUtils.formatFloat(this.fReactionMaster.getSourceMass(), 5), "");
            this.addProperty(ChemUtils.rs_ProductsMass, CommonUtils.formatFloat(this.fReactionMaster.getProductMass(), 5), "");
            this.addProperty("dH° (298 K)", CommonUtils.formatFloat(this.fThermodynamicSolver.getSM_Enthalpy(), 3), "кДж");
            this.addProperty("dS° (298 K)", CommonUtils.formatFloat(this.fThermodynamicSolver.getSM_Entropy(), 3), "Дж/К");
            this.addProperty("dG° (298 K)", CommonUtils.formatFloat(this.fThermodynamicSolver.getSM_Gibbs_Energy(), 3), "кДж");
            this.addProperty("lg K°", CommonUtils.formatFloat(this.fThermodynamicSolver.getlg_K(), 3), "");
            this.addProperty("K°", CommonUtils.formatFloat(this.fThermodynamicSolver.getStdBalanceConstant(), 3), "");
            this.addProperty(ChemUtils.rs_MolsInc, CommonUtils.formatFloat(this.fThermodynamicSolver.getdN(), 3), "");
            this.addProperty(ChemUtils.rs_BCFirstApproximation, CommonUtils.formatFloat(this.fThermodynamicSolver.getFBalanceConstant(), 3), "");
            this.addProperty(ChemUtils.rs_BCSecondApproximation, CommonUtils.formatFloat(this.fThermodynamicSolver.getSBalanceConstant(), 3), "");
            this.tblProperties.packColumns(10);
            
            this.updateStoic();*/
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "ChemLab", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSubst()
    {
        CLDevSubstAdd addDlg = new CLDevSubstAdd(null, this.fDevice);
        addDlg.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();

        switch (actionPerformed) {
            case "ADD_SUBST":
                this.addSubst();
                break;
        }
    }
}
