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

import chemlab.core.controls.experiment.LabDevice;
import chemlab.core.controls.experiment.matter.Matter;
import chemlab.core.measure.ChemUnits;
import chemlab.sandbox.ReactionsEnv;
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
    private JPanel panReactions;
    private VirtualTable tblReactions;
    
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
        this.panReactions = new JPanel();
        this.tblReactions = new VirtualTable();
        
        this.add(PageControl, BorderLayout.CENTER);

        this.panSubstances.setLayout(new BorderLayout());
        this.PageControl.addTab(res_i18n.getString("CL_SUBSTANCES"), this.panSubstances);

        this.panProperties.setLayout(new BorderLayout());
        this.panProperties.add(this.tblProperties, BorderLayout.CENTER);
        this.PageControl.addTab(res_i18n.getString("CL_Properties"), this.panProperties);

        this.panReactions.setLayout(new BorderLayout());
        this.PageControl.addTab(res_i18n.getString("CL_REACTIONS"), this.panReactions);

        JPanel panSubstToolbar = new JPanel();
        panSubstToolbar.setLayout(new BoxLayout(panSubstToolbar, BoxLayout.LINE_AXIS));

        JButton btnAddSubst = new JButton();
        btnAddSubst.setText("Add subst");
        btnAddSubst.setActionCommand("ADD_SUBSTANCE");
        btnAddSubst.addActionListener(this);
        panSubstToolbar.add(btnAddSubst);

        JButton btnDeleteSubst = new JButton();
        btnDeleteSubst.setText("Delete subst");
        btnDeleteSubst.setActionCommand("DELETE_SUBSTANCE");
        btnDeleteSubst.addActionListener(this);
        panSubstToolbar.add(btnDeleteSubst);

        JButton btnClearSubsts = new JButton();
        btnClearSubsts.setText("Clear substs");
        btnClearSubsts.setActionCommand("CLEAR_SUBSTANCES");
        btnClearSubsts.addActionListener(this);
        panSubstToolbar.add(btnClearSubsts);

        this.panSubstances.add(panSubstToolbar, BorderLayout.NORTH);
        this.panSubstances.add(this.tblSubstances, BorderLayout.CENTER);

        Dimension tblDim = new Dimension(580, 280);
        
        tblSubstances.setPreferredSize(tblDim);
        tblSubstances.addColumn(res_i18n.getString("CL_COMPOUND"), 88);
        tblSubstances.addColumn(res_i18n.getString("CL_MolarMass"), 88);
        tblSubstances.addColumn("Mass", 59);

        tblProperties.setPreferredSize(tblDim);
        tblProperties.addColumn(res_i18n.getString("CL_DIMENSION"), 231);
        tblProperties.addColumn(res_i18n.getString("CL_Value"), 139);
        tblProperties.addColumn(res_i18n.getString("CL_Unit"), 92);


        JPanel panReactsToolbar = new JPanel();
        panReactsToolbar.setLayout(new BoxLayout(panReactsToolbar, BoxLayout.LINE_AXIS));

        JButton btnSearch = new JButton();
        btnSearch.setText("Search reactions");
        btnSearch.setActionCommand("SEARCH");
        btnSearch.addActionListener(this);
        panReactsToolbar.add(btnSearch);

        JButton btnAddReact = new JButton();
        btnAddReact.setText("Add reaction");
        btnAddReact.setActionCommand("ADD_REACTION");
        btnAddReact.addActionListener(this);
        panReactsToolbar.add(btnAddReact);

        JButton btnDeleteReact = new JButton();
        btnDeleteReact.setText("Delete reaction");
        btnDeleteReact.setActionCommand("DELETE_REACTION");
        btnDeleteReact.addActionListener(this);
        panReactsToolbar.add(btnDeleteReact);

        JButton btnClearReactions = new JButton();
        btnClearReactions.setText("Clear reactions");
        btnClearReactions.setActionCommand("CLEAR_REACTIONS");
        btnClearReactions.addActionListener(this);
        panReactsToolbar.add(btnClearReactions);

        this.panReactions.add(panReactsToolbar, BorderLayout.NORTH);
        this.panReactions.add(this.tblReactions, BorderLayout.CENTER);

        tblReactions.setPreferredSize(tblDim);
        tblReactions.addColumn(res_i18n.getString("CL_Equation"), 400);

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
                Matter substance = this.fDevice.getSubstance(i);

                Object[] rowData = new Object[]{
                    substance.Formula,
                    CommonUtils.formatFloat(substance.getMolecularMass(), 5),
                    ChemUnits.toString(substance.getMeasureMass(), "%5.5f")
                };

                this.tblSubstances.addRow(rowData);
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
            CommonUtils.showError(this, ex.getMessage());
        }
    }

    private void addSubst()
    {
        CLDevSubstAdd addDlg = new CLDevSubstAdd(null, this.fDevice);
        addDlg.setVisible(true);

        this.updateControls();
    }

    private void deleteSubst()
    {
        int row = this.tblSubstances.getSelectedRow();
        if (row < 0) return;

        this.fDevice.deleteSubstance(row);
        this.updateControls();
    }

    private void clearSubsts()
    {
        this.fDevice.clear();
        this.updateControls();
    }
    
    private void searchReactions()
    {
        ReactionsEnv reactsEnv = new ReactionsEnv();
        
        for (int i = 0; i < this.fDevice.getSubstancesCount(); i++) {
            reactsEnv.addSubstance(this.fDevice.getSubstance(i));
        }
        
        reactsEnv.search();
        
        tblReactions.clear();
        Object[] rowData = new Object[]{
            reactsEnv.toString()
        };
        this.tblReactions.addRow(rowData);
    }
    
    private void addReaction()
    {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();

        switch (actionPerformed) {
            case "ADD_SUBSTANCE":
                this.addSubst();
                break;

            case "DELETE_SUBSTANCE":
                this.deleteSubst();
                break;

            case "CLEAR_SUBSTANCES":
                this.clearSubsts();
                break;

            case "SEARCH":
                this.searchReactions();
                break;

            case "ADD_REACTION":
                this.addReaction();
                break;

            case "DELETE_REACTION":
                break;

            case "CLEAR_REACTIONS":
                break;
        }
    }
}
