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

import bslib.components.ComboItem;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.SubstanceState;
import chemlab.core.controls.experiment.LabDevice;
import chemlab.core.measure.ChemUnits;
import chemlab.core.measure.MeasureBox;
import chemlab.database.CLDB;
import chemlab.database.CompoundRecord;
import chemlab.database.PhysicalState;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.measure.Measure;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class CLDevSubstAdd extends JDialog implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private final LabDevice fDevice;
    private CompoundRecord fCompound;
    
    private final JPanel panSubstance;
    private final JPanel panCompound;
    private final JPanel panProps;
    private final JPanel panControl;
    
    private final JComboBox cmbCompounds;
    private final JComboBox cmbStates;
    private final MeasureBox meaAmount;
    
    public CLDevSubstAdd(Frame owner, LabDevice device)
    {
        super(owner, true);
        
        this.fDevice = device;

        this.setLayout(new BorderLayout());
        this.setTitle(this.fDevice.getID().name());
        this.setResizable(false);

        panSubstance = new JPanel();
        panSubstance.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(res_i18n.getString("CL_COMPOUND")),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
                
        panCompound = new JPanel();
        
        cmbCompounds = new JComboBox();
        CLDB cmbBook = CLData.Database;
        for (CompoundRecord cmp : cmbBook.getAllCompounds()) {
            String name = cmp.getFormula();
            cmbCompounds.addItem(new ComboItem(name, cmp));
        }
        cmbCompounds.setSelectedIndex(0);
        cmbCompounds.addActionListener(this);
        cmbCompounds.setEditable(true);
        
        cmbStates = new JComboBox();
        cmbStates.addItem("gas");
        cmbStates.addItem("liquid");
        cmbStates.addItem("solid");
        cmbStates.setSelectedItem(null);
        cmbStates.addActionListener(this);
        
        panCompound.add(cmbCompounds);
        panCompound.add(cmbStates);
        
        panSubstance.add(panCompound);
        
        JButton btnAddCompound = new JButton("Add");
        btnAddCompound.setActionCommand("ADD_COMPOUND");
        btnAddCompound.addActionListener(this);
        panSubstance.add(btnAddCompound);
        
        JButton btnEditCompound = new JButton("Edit");
        btnEditCompound.setActionCommand("EDIT_COMPOUND");
        btnEditCompound.addActionListener(this);
        panSubstance.add(btnEditCompound);
        
        panProps = new JPanel();
        panProps.setBorder(null);
        //panControl.setLayout(new BoxLayout(panControl, BoxLayout.X_AXIS));

        JLabel lblAmount = new JLabel();
        lblAmount.setText("Amount");
        panProps.add(lblAmount);
        
        meaAmount = new MeasureBox("", ChemUnits.GRAM);
        panProps.add(meaAmount);
        
        panControl = new JPanel();
        panControl.setBorder(null);
        //panControl.setLayout(new BoxLayout(panControl, BoxLayout.X_AXIS));

        JButton btnAddSubst = new JButton();
        btnAddSubst.setText("Add");
        btnAddSubst.setActionCommand("CMD_ADD");
        btnAddSubst.addActionListener(this);
        panControl.add(btnAddSubst);

        JButton btnClose = new JButton();
        btnClose.setText("Close");
        btnClose.setActionCommand("CMD_CLOSE");
        btnClose.addActionListener(this);
        panControl.add(btnClose);

        this.add(this.panSubstance, BorderLayout.NORTH);
        this.add(this.panProps, BorderLayout.CENTER);
        this.add(this.panControl, BorderLayout.SOUTH);
        
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void updateProps()
    {
        if (this.fCompound == null) {
            // TODO: unknown compound, activate edit button
            return;
        }
        
        Object item = cmbStates.getSelectedItem();
        SubstanceState state = (item == null) ? null : (SubstanceState) ((ComboItem) item).Data;
        if (state == null) {
            // TODO: activate edit button;
        } else {
            switch (state) {
                case Solid:
                    meaAmount.setBaseUnit(ChemUnits.GRAM);
                    break;

                case Liquid:
                case Gas:
                    meaAmount.setBaseUnit(ChemUnits.LITER);
                    break;
            }
        }
    }

    private void addCompound()
    {
        if (this.fCompound == null) {
            return;
        }

        Object item = cmbStates.getSelectedItem();
        SubstanceState state = (item == null) ? null : (SubstanceState) ((ComboItem) item).Data;
        if (state == null) {
            return;
        }

        Measure<Double, ?> amount = meaAmount.getMeasure();
        this.fDevice.addSubstance(this.fCompound.getFormula(), state, amount);
        this.fDevice.changeContents();

        this.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();

        if ("comboBoxChanged".equals(e.getActionCommand())) {
            if (e.getSource() == cmbCompounds) {
                ComboItem item = (ComboItem) cmbCompounds.getSelectedItem();
                this.fCompound = (item == null) ? null : (CompoundRecord) item.Data;

                cmbStates.removeAllItems();
                for (PhysicalState ps : this.fCompound.getStates()) {
                    SubstanceState state = ps.getState();
                    if (state != null) {
                        cmbStates.addItem(new ComboItem(state.name(), state));
                    }
                }
                cmbStates.setSelectedIndex(0);

                this.updateProps();
            }

            if (e.getSource() == cmbStates) {
                this.updateProps();
            }
            
            return;
        }
        
        switch (actionPerformed) {
            case "CMD_ADD":
                this.addCompound();
                break;

            case "CMD_CLOSE":
                this.setVisible(false);
                break;

            case "ADD_COMPOUND":
                break;

            case "EDIT_COMPOUND":
                CLCompoundEditor editor = new CLCompoundEditor(this, this.fCompound);
                editor.setVisible(true);
                break;
        }
    }
}
