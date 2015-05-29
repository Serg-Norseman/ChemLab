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
import chemlab.core.chemical.ChemUnits;
import chemlab.core.chemical.SubstanceState;
import chemlab.core.controls.MeasureBox;
import chemlab.core.controls.experiment.LabDevice;
import chemlab.refbooks.CompoundRecord;
import chemlab.refbooks.CompoundsBook;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
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
    private final JPanel panProps;
    private final JPanel panControl;
    
    private final JComboBox cmbCompounds;
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
                
        cmbCompounds = new JComboBox();
        CompoundsBook cmbBook = CLData.CompoundsBook;
        for (CompoundRecord cmp : cmbBook.getList()) {
            if (cmp.State == SubstanceState.Ion) continue;

            String name = cmp.Formula;
            if (cmp.State != null) {
                name += CLData.SubstanceStateSigns[cmp.State.getValue()];
            }

            cmbCompounds.addItem(new ComboItem(name, cmp));
        }
        cmbCompounds.setSelectedIndex(0);
        cmbCompounds.addActionListener(this);
        cmbCompounds.setEditable(true);
        panSubstance.add(cmbCompounds);
        
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
        
        SubstanceState state = this.fCompound.State;
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();

        if ("comboBoxChanged".equals(e.getActionCommand()) && e.getSource() == cmbCompounds) {
            ComboItem item = (ComboItem) cmbCompounds.getSelectedItem();
            this.fCompound = (item == null) ? null : (CompoundRecord) item.Data;
            this.updateProps();
            return;
        }
        
        switch (actionPerformed) {
            case "CMD_ADD":
                //this.addSubst();
                break;
            case "CMD_CLOSE":
                this.setVisible(false);
                break;
        }
    }
}
