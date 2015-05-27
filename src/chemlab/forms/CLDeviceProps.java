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
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Dimension;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class CLDeviceProps extends JDialog
{
    private final LabDevice fDevice;
    
    private JTabbedPane PageControl;
    private JPanel panSubstances;
    private VirtualTable tblCompounds;
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
        this.tblCompounds = new VirtualTable();
        this.panProperties = new JPanel();
        this.tblProperties = new VirtualTable();
        
        this.add(PageControl, BorderLayout.CENTER);

        this.panSubstances.setLayout(new BorderLayout());
        this.panSubstances.add(this.tblCompounds, BorderLayout.CENTER);
        this.PageControl.addTab("Реагенты", this.panSubstances);

        this.panProperties.setLayout(new BorderLayout());
        this.panProperties.add(this.tblProperties, BorderLayout.CENTER);
        this.PageControl.addTab("Свойства", this.panProperties);

        Dimension tblDim = new Dimension(580, 280);
        
        tblCompounds.setPreferredSize(tblDim);
        tblCompounds.addColumn("Коэффициент", 88);
        tblCompounds.addColumn("Соединение", 88);
        tblCompounds.addColumn("Молек. масса", 88);
        tblCompounds.addColumn("Тип", 57);
        tblCompounds.addColumn("Cp°", 59);
        tblCompounds.addColumn("S°", 59);
        tblCompounds.addColumn("dH°", 59);
        tblCompounds.addColumn("dG°", 59);

        tblProperties.setPreferredSize(tblDim);
        tblProperties.addColumn("Величина", 231);
        tblProperties.addColumn("Значение", 139);
        tblProperties.addColumn("Размерность", 92);

        this.pack();
        this.setLocationRelativeTo(owner);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
