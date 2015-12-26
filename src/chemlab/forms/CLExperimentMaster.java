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
import bslib.common.StringHelper;
import chemlab.core.chemical.CompoundSolver;
import chemlab.core.controls.experiment.BenchListener;
import chemlab.core.controls.experiment.ExperimentBench;
import chemlab.core.controls.experiment.LabDevice;
import chemlab.core.controls.experiment.misc.BenchDropTarget;
import chemlab.core.controls.experiment.misc.DevicesList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public final class CLExperimentMaster extends JFrame implements ActionListener, BenchListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JMenuBar MainMenu;
    private JMenu miFile;
    private JMenuItem miCreate;
    private JMenuItem miLoad;
    private JMenuItem miSave;
    private JMenuItem miSaveAs;
    private JMenuItem miExit;
    private JMenu miEdit;
    private JMenuItem miDelete;
    private JMenuItem miSelectAll;
    private JMenu miView;
    private JToolBar ToolBarCommon;
    private JButton tbCreate;
    private JButton tbLoad;
    private JButton tbSave;
    private JButton tbPrint;
    private JButton tbClear;
    private JMenuItem miClose;
    private JMenuItem miServices;
    private JMenuItem miFileProperties;
    private JFileChooser OpenDialog;
    private JFileChooser SaveDialog;
    private DevicesList ListDevices;
    private JSplitPane Splitter1;
    private BenchDropTarget fPanelDropTarget;

    private String fFileName;
    private final CompoundSolver fCompoundMaster;
    private ExperimentBench fBench;

    public CLExperimentMaster()
    {
        super();
        this.initializeComponents();

        this.fCompoundMaster = new CompoundSolver();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(res_i18n.getString("CL_ExperimentFiles"), "edf");

        this.OpenDialog.setFileFilter(filter);
        this.OpenDialog.setCurrentDirectory(new File(AuxUtils.getAppPath()));

        this.SaveDialog.setFileFilter(filter);
        this.SaveDialog.setCurrentDirectory(new File(AuxUtils.getAppPath()));

        this.fFileName = res_i18n.getString("CL_UntitledFile");
    }

    private void initializeComponents()
    {
        this.MainMenu = new JMenuBar();
        this.miFile = new JMenu();
        this.miCreate = new JMenuItem();
        this.miLoad = new JMenuItem();
        this.miSave = new JMenuItem();
        this.miSaveAs = new JMenuItem();
        this.miExit = new JMenuItem();
        this.miEdit = new JMenu();
        this.miDelete = new JMenuItem();
        this.miSelectAll = new JMenuItem();
        this.miView = new JMenu();
        this.ToolBarCommon = new JToolBar();
        this.tbCreate = new JButton();
        this.tbLoad = new JButton();
        this.tbSave = new JButton();
        this.tbPrint = new JButton();
        this.tbClear = new JButton();
        this.miClose = new JMenuItem();
        this.miServices = new JMenu();
        this.miFileProperties = new JMenuItem();
        this.OpenDialog = new JFileChooser();
        this.SaveDialog = new JFileChooser();
        this.ListDevices = new DevicesList();

        this.setLayout(new BorderLayout());

        FramesHelper.setClientSize(this, 800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setJMenuBar(this.MainMenu);
        this.setTitle(res_i18n.getString("CL_EXPERIMENT_MASTER"));
        //this.Closing += this.Form_Closing;

        this.ToolBarCommon.setLocation(11, 2);
        this.ToolBarCommon.setSize(142, 22);
        //this.ToolBarCommon.Images = this.ImageList;
        this.add(this.ToolBarCommon, BorderLayout.NORTH);

        this.ToolBarCommon.add(this.tbCreate);
        this.ToolBarCommon.add(this.tbLoad);
        this.ToolBarCommon.add(this.tbSave);
        this.ToolBarCommon.addSeparator();
        this.ToolBarCommon.add(this.tbPrint);
        this.ToolBarCommon.addSeparator();
        this.ToolBarCommon.add(this.tbClear);

        this.tbCreate.setText(res_i18n.getString("CL_CreateNew"));
        //tToolButton.ImageIndex = 0;
        this.tbCreate.addActionListener(this);
        this.tbCreate.setActionCommand("ACTION_CREATE");

        this.tbLoad.setText(res_i18n.getString("CL_Load"));
        //tToolButton2.ImageIndex = 1;
        //tToolButton2.Click += this.miLoadClick;

        this.tbSave.setText(res_i18n.getString("CL_Save"));
        //tToolButton3.ImageIndex = 2;
        //tToolButton3.Click += this.miSaveClick;

        this.tbPrint.setText(res_i18n.getString("CL_Print"));
        //tToolButton4.ImageIndex = 5;
        this.tbPrint.setEnabled(false);

        this.tbClear.setText(res_i18n.getString("CL_Delete"));
        //tbClear.ImageIndex = 13;
        this.tbClear.setEnabled(false);
        //tbClear.Click += this.miDeleteClick;

        this.MainMenu.add(miFile);
        this.MainMenu.add(miEdit);
        this.MainMenu.add(miView);
        this.MainMenu.add(miServices);

        this.miFile.setText(res_i18n.getString("CL_File"));

        this.miFile.add(miCreate);
        this.miFile.add(miLoad);
        this.miFile.add(miSave);
        this.miFile.add(miSaveAs);
        this.miFile.add(miClose);
        this.miFile.addSeparator();
        this.miFile.add(miFileProperties);
        this.miFile.addSeparator();
        this.miFile.add(miExit);

        this.miCreate.setText(res_i18n.getString("CL_CreateNew"));
        this.miCreate.addActionListener(this);
        this.miCreate.setActionCommand("ACTION_CREATE");

        this.miLoad.setText(res_i18n.getString("CL_Load"));
        this.miLoad.addActionListener(this);
        this.miLoad.setActionCommand("ACTION_LOAD");

        this.miSave.setText(res_i18n.getString("CL_Save"));
        this.miSave.addActionListener(this);
        this.miSave.setActionCommand("ACTION_SAVE");

        this.miSaveAs.setText(res_i18n.getString("CL_SaveAs"));
        this.miSaveAs.addActionListener(this);
        this.miSaveAs.setActionCommand("ACTION_SAVEAS");

        this.miClose.setText(res_i18n.getString("CL_Close"));
        this.miClose.addActionListener(this);
        this.miClose.setActionCommand("ACTION_CLOSE");

        this.miExit.setText(res_i18n.getString("CL_Exit"));
        this.miExit.addActionListener(this);
        this.miExit.setActionCommand("ACTION_EXIT");

        this.miEdit.setText(res_i18n.getString("CL_Edit"));
        this.miEdit.add(this.miDelete);
        this.miEdit.add(this.miSelectAll);

        this.miDelete.setText(res_i18n.getString("CL_Delete"));
        this.miDelete.setEnabled(false);
        this.miDelete.addActionListener(this);
        this.miDelete.setActionCommand("ACTION_DELETE");

        this.miSelectAll.setText(res_i18n.getString("CL_SelectAll"));
        this.miSelectAll.setEnabled(false);
        this.miSelectAll.addActionListener(this);
        this.miSelectAll.setActionCommand("ACTION_SELECT_ALL");

        this.miView.setText(res_i18n.getString("CL_View"));

        this.miServices.setText(res_i18n.getString("CL_Services"));

        this.miFileProperties.setText(res_i18n.getString("CL_FileProperties"));
        this.miFileProperties.addActionListener(this);
        this.miFileProperties.setActionCommand("mi_FileProperties");

        this.ListDevices.setMinimumSize(new Dimension(120, 100));
        this.ListDevices.setPreferredSize(new Dimension(120, 100));

        this.fBench = new ExperimentBench();
        this.fBench.setMinimumSize(new Dimension(120, 100));
        this.fBench.setBenchListener(this);
        
        this.fPanelDropTarget = new BenchDropTarget(this.fBench);

        this.Splitter1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ListDevices, fBench);
        this.Splitter1.setOneTouchExpandable(true);
        this.Splitter1.setDividerLocation(120);
        this.Splitter1.setResizeWeight(0.2);
        this.add(this.Splitter1, BorderLayout.CENTER);
    }

    /*private void onFormClosing(Object sender, CancelEventArgs e)
     {
     if (this.fExperimentMaster.Modified) {
     int res = JOptionPane.showConfirmDialog(null, "Файл \"" + this.FFileName + "\" был изменен. Сохранить?", "", JOptionPane.YES_NO_CANCEL_OPTION);
     if (res == JOptionPane.CANCEL_OPTION) {
     e.Cancel = true;
     return;
     }
     if (res == JOptionPane.YES_OPTION) {
     this.SaveContents();
     }
     }
     }*/
    @Override
    public void deviceProperties(LabDevice device)
    {
        //JOptionPane.showConfirmDialog(null, "G");
        CLDeviceProps devProps = new CLDeviceProps(this, device);
        devProps.setVisible(true);
    }

    public void miLoadClick(Object sender)
    {
        if (this.OpenDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.fFileName = this.OpenDialog.getSelectedFile().getName();
            this.fBench.loadFromFile(this.fFileName);
            this.InterfaceUpdate();
        }
    }

    public void miSaveClick(Object sender)
    {
        if (StringHelper.equals(this.fFileName, res_i18n.getString("CL_UntitledFile"))) {
            this.miSaveAsClick(this);
        } else {
            this.fBench.saveToFile(this.fFileName);
        }
    }

    public void miSaveAsClick(Object sender)
    {
        //this.SaveDialog.FileName = "";
        if (this.SaveDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.fFileName = this.SaveDialog.getSelectedFile().getName();
            this.miSaveClick(this);
        }
    }

    public void miCloseClick(Object sender)
    {
        if (this.fBench.Modified) {
            int res = JOptionPane.showConfirmDialog(null, String.format(res_i18n.getString("CL_ChangedFileSaveQuery"), this.fFileName), "ChemLab", JOptionPane.YES_NO_CANCEL_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                this.SaveContents();
            }
        }
    }

    private void SaveContents()
    {
        if (StringHelper.equals(this.fFileName, res_i18n.getString("CL_UntitledFile"))) {
            if ((!StringHelper.equals(this.fFileName, "")) && (!StringHelper.equals(this.fFileName, res_i18n.getString("CL_UntitledFile")))) {
                String fileName = (new java.io.File(this.fFileName)).getName();
                //this.SaveDialog.FileName = fileName;
                //this.SaveDialog.InitialDirectory = (new java.io.File(this.FFileName)).getParent();
            } else {
                this.fFileName = "";
                this.SaveDialog.setCurrentDirectory(new File(AuxUtils.getAppPath()));
            }

            if (this.SaveDialog.showSaveDialog(this) == JOptionPane.OK_OPTION) {
                this.fFileName = this.SaveDialog.getSelectedFile().getName();
            }
        }

        this.fBench.saveToFile(this.fFileName);
        this.fBench.Modified = false;
    }

    private void InterfaceUpdate()
    {
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        //Object source = e.getSource();

        switch (actionPerformed) {
            case "ACTION_CREATE":
                this.fFileName = res_i18n.getString("CL_UntitledFile");
                this.fBench.clear();
                break;

            case "ACTION_EXIT":
                this.setVisible(false);
                break;
        }
    }
}
