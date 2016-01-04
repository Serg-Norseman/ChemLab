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

import bslib.common.Logger;
import chemlab.database.CompoundName;
import chemlab.database.CompoundRecord;
import chemlab.vtable.VirtualTable;
import com.j256.ormlite.dao.ForeignCollection;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
public class CLCompoundEditor extends JDialog implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private final CompoundRecord fCompound;
    
    private JTabbedPane PageControl;
    private JPanel panGeneral;
    private JPanel panNames;
    private VirtualTable tblNames;
    private JPanel panRadicals;
    private VirtualTable tblRadicals;
    
    public CLCompoundEditor(JDialog owner, CompoundRecord compound)
    {
        super(owner, true);
        
        this.fCompound = compound;

        this.setLayout(new BorderLayout());
        //this.setTitle();
        this.setResizable(false);

        this.PageControl = new JTabbedPane();
        this.panGeneral = new JPanel();
        this.panNames = new JPanel();
        this.tblNames = new VirtualTable();
        this.panRadicals = new JPanel();
        this.tblRadicals = new VirtualTable();
        
        this.add(PageControl, BorderLayout.CENTER);

        this.panGeneral.setLayout(new BorderLayout());
        this.PageControl.addTab(res_i18n.getString("CL_COMMON"), this.panGeneral);

        this.panNames.setLayout(new BorderLayout());
        this.PageControl.addTab(res_i18n.getString("CL_Names"), this.panNames);

        this.panRadicals.setLayout(new BorderLayout());
        this.PageControl.addTab(res_i18n.getString("CL_RADICALS"), this.panRadicals);

        JPanel panNamesToolbar = new JPanel();
        panNamesToolbar.setLayout(new BoxLayout(panNamesToolbar, BoxLayout.LINE_AXIS));

        JButton btnAddName = new JButton("Add name");
        btnAddName.setActionCommand("ADD_NAME");
        btnAddName.addActionListener(this);
        panNamesToolbar.add(btnAddName);


        JPanel panRadicalsToolbar = new JPanel();
        panRadicalsToolbar.setLayout(new BoxLayout(panRadicalsToolbar, BoxLayout.LINE_AXIS));

        JButton btnAddSubst = new JButton("Add radical");
        btnAddSubst.setActionCommand("ADD_RADICAL");
        btnAddSubst.addActionListener(this);
        panRadicalsToolbar.add(btnAddSubst);

        this.panNames.add(panNamesToolbar, BorderLayout.NORTH);
        this.panNames.add(this.tblNames, BorderLayout.CENTER);

        this.panRadicals.add(panRadicalsToolbar, BorderLayout.NORTH);
        this.panRadicals.add(this.tblRadicals, BorderLayout.CENTER);

        Dimension tblDim = new Dimension(580, 280);
        
        tblNames.setPreferredSize(tblDim);
        tblNames.addColumn("Lang", 88);
        tblNames.addColumn("Name", 88);

        tblRadicals.setPreferredSize(tblDim);
        tblRadicals.addColumn("Formula", 231);
        tblRadicals.addColumn("Charge", 139);

        this.pack();
        this.setLocationRelativeTo(owner);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        this.updateControls();
    }
    
    private void updateControls()
    {
        try {
            this.tblNames.clear();
            ForeignCollection<CompoundName> states = this.fCompound.getNames();
            for (CompoundName name : states) {
                Object[] rowData = new Object[]{
                    name.getLang(),
                    name.getName()
                };
                this.tblNames.addRow(rowData);
            }
            this.tblNames.packColumns(10);

            /*this.tblRadicals.clear();
            for (RadicalRecord rad : this.fCompound.Radicals) {
                Object[] rowData = new Object[]{
                    rad.Formula,
                    String.valueOf(rad.Charge)
                };
                this.tblRadicals.addRow(rowData);
            }
            this.tblRadicals.packColumns(10);*/
        } catch (Exception ex) {
            Logger.write(ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();

        switch (actionPerformed) {
            case "ADD_NAME":
                break;

            case "EDIT_NAME":
                break;
            
            case "DEL_NAME":
                break;


            case "ADD_RADICAL":
                break;
            
            case "EDIT_RADICAL":
                break;
            
            case "DEL_RADICAL":
                break;
        }
    }
}
