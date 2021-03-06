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
import bslib.common.EnumSet;
import bslib.common.FramesHelper;
import chemlab.core.chemical.CompoundSolver;
import chemlab.core.controls.molecule.MasterMode;
import chemlab.core.controls.molecule.MoleculeMaster;
import chemlab.core.controls.molecule.ViewOption;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.4.0
 */
public final class CLMoleculeViewer extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JMenuBar MainMenu;
    private JMenuItem miFile;
    private JMenuItem miLoad;
    private JMenuItem miExit;
    private JMenuItem miEdit;
    private JMenuItem miDelete;
    private JMenuItem miSelectAll;
    private JMenuItem miView;
    private JToolBar ToolBarCommon;
    private JButton tbLoad;
    private JButton tbClear;
    private JMenuItem miClose;
    private JButton tbEditMode;
    private JButton tbScaleMode;
    private JButton tbPlaneMoveMode;
    private JButton tbSpaceRotateMode;
    private JMenuItem miFunctions;
    private JButton tbPlaneRotateMode;
    private JButton tbFitToWnd;
    private JButton tbAutoRotate;
    private JMenuItem miMoleculeProperties;
    private JMenuItem miSign;
    private JMenuItem miName;
    private JMenuItem miNumber;
    private JMenuItem miAtomType;
    private JMenuItem miCharge;
    private JMenuItem miMass;
    private JMenu miAtomView;
    private JButton tbSpaceMoveMode;
    private JComboBox cbMolecules;
    private JButton tbRenameMolecule;
    private JMenu miMoleculeView;
    private JMenuItem miBalls;
    private JMenuItem miSticks;
    private JFileChooser OpenDialog;
    //private ImageList ImageList;
    //private ImageList ImageListMolecule;

    private String fFileName;
    private final CompoundSolver fCompoundMaster;
    private final MoleculeMaster fMoleculeMaster;

    public static final String UntitledFile = "<Безымянный>";

    public CLMoleculeViewer()
    {
        super();
        this.initializeComponents();

        this.fCompoundMaster = new CompoundSolver();

        this.fMoleculeMaster = new MoleculeMaster();
        this.fMoleculeMaster.setLocation(0, 30);
        this.fMoleculeMaster.setSize(812, 358);
        //tMoleculeMaster.Cursor = (TCursor)11;
        this.fMoleculeMaster.setAxisVisible(false);
        this.fMoleculeMaster.setBackground(Color.black);
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_Edit);
        this.add(this.fMoleculeMaster, BorderLayout.CENTER);

        /*this.miSign.Tag = 0;
        this.miName.Tag = 1;
        this.miNumber.Tag = 2;
        this.miAtomType.Tag = 4;
        this.miCharge.Tag = 3;
        this.miMass.Tag = 5;
        this.miChirality.Tag = 6;
        this.miBalls.Tag = 7;
        this.miSticks.Tag = 8;*/

        //this.OpenDialog.Filter = "Все файлы молекул|*.mdf;*.xyz;*.mm1gp;*.mcd;*.hin;*.sdf;*.sd;*.mdl;*.mol;*.mcm|Файлы молекул ChemLab|*.mdf|Файлы молекул ZYZ|*.xyz|Файлы молекул Ghemical|*.mm1gp|Файлы молекул MoluCAD|*.mcd|Файлы молекул HyperChem|*.hin|Файлы молекул MDL|*.sdf;*.sd;*.mdl;*.mol|Файлы молекул PCMolecule|*.mcm";
        //this.OpenDialog.Filter = "Все файлы молекул|*.mdf;*.xyz;*.mm1gp;*.mcd;*.hin|Файлы молекул ChemLab|*.mdf|Файлы молекул ZYZ|*.xyz|Файлы молекул Ghemical|*.mm1gp|Файлы молекул MoluCAD|*.mcd|Файлы молекул HyperChem|*.hin";
        
        String path = AuxUtils.getAppPath()+ "Molecules";
        boolean flag = (new java.io.File(path)).isDirectory();
        if (flag) {
            this.OpenDialog.setCurrentDirectory(new File(path));
        } else {
            this.OpenDialog.setCurrentDirectory(new File(AuxUtils.getAppPath()));
        }
        this.fFileName = CLMoleculeViewer.UntitledFile;
    }

    public void miAtomViewsClick(Object sender)
    {
        /*EnumSet<ViewOption> opts = this.fMoleculeMaster.getViewOptions();
        ViewOption opt = (ViewOption) ((MenuItem) sender).Tag;
        if (opts.contains(opt)) {
            opts.exclude(opt);
        } else {
            opts.include(opt);
        }
        this.InterfaceUpdate();*/
    }

    public void cbMoleculesChange(Object sender)
    {
        //this.fMoleculeMaster.ActiveMoleculeIndex = this.cbMolecules.ItemIndex;
    }

    public void miMoleculeViewsClick(Object sender)
    {
        /*EnumSet<ViewOption> opts = this.fMoleculeMaster.getViewOptions();
        ViewOption opt = (ViewOption) ((MenuItem) sender).Tag;
        if (opts.contains(opt)) {
            opts.exclude(opt);
        } else {
            opts.include(opt);
        }
        this.InterfaceUpdate();*/
    }

    public void miCloseClick(Object sender)
    {
    }

    public void miSelectAllClick(Object sender)
    {
        this.fMoleculeMaster.getActiveMolecule().selectAll();
    }

    public void miDeleteClick(Object sender)
    {
        this.fMoleculeMaster.getActiveMolecule().deleteSelected();
    }

    public void miExitClick(Object sender)
    {
        this.setVisible(false);
    }

    public void miSaveAsClick(Object sender)
    {
    }

    public void miSaveClick(Object sender)
    {
    }

    public void actLoadClick()
    {
        if (this.OpenDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.fFileName = this.OpenDialog.getSelectedFile().getPath();
            this.fMoleculeMaster.loadFromFile(this.fFileName);
            //this.fMoleculeMaster.setMoleculesList(this.cbMolecules);
            this.fMoleculeMaster.changeContent();
            this.InterfaceUpdate();
        }
    }

    public void miCreateClick(Object sender)
    {
        this.fFileName = CLMoleculeViewer.UntitledFile;
        this.fMoleculeMaster.clear();
    }

    public void tbAutoRotateClick(Object sender)
    {
        this.tbEditMode.setSelected(false);
        this.tbScaleMode.setSelected(false);
        this.tbSpaceMoveMode.setSelected(false);
        this.tbPlaneMoveMode.setSelected(false);
        this.tbSpaceRotateMode.setSelected(false);
        this.tbPlaneRotateMode.setSelected(false);
        this.tbAutoRotate.setSelected(!this.tbAutoRotate.isSelected());

        this.fMoleculeMaster.setAutoRotate(this.tbAutoRotate.isSelected());
    }

    public void tbFitToWndClick(Object sender)
    {
        this.fMoleculeMaster.fitToWnd();
    }

    public void tbPlaneRotateModeClick(Object sender)
    {
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_PlaneRotate);

        this.tbEditMode.setSelected(false);
        this.tbScaleMode.setSelected(false);
        this.tbSpaceMoveMode.setSelected(false);
        this.tbPlaneMoveMode.setSelected(false);
        this.tbSpaceRotateMode.setSelected(false);
        this.tbPlaneRotateMode.setSelected(true);

        /*if (this.tbAutoRotate.Pushed) {
            this.tbAutoRotateClick(null, e);
        }*/
    }

    public void tbSpaceRotateModeClick(Object sender)
    {
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_SpaceRotate);
        this.tbEditMode.setSelected(false);
        this.tbScaleMode.setSelected(false);
        this.tbSpaceMoveMode.setSelected(false);
        this.tbPlaneMoveMode.setSelected(false);
        this.tbSpaceRotateMode.setSelected(true);
        this.tbPlaneRotateMode.setSelected(false);
        
        /*if (this.tbAutoRotate.Pushed) {
            this.tbAutoRotateClick(null, e);
        }*/
    }

    public void tbPlaneMoveModeClick(Object sender)
    {
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_PlaneMove);
        this.tbEditMode.setSelected(false);
        this.tbScaleMode.setSelected(false);
        this.tbSpaceMoveMode.setSelected(false);
        this.tbPlaneMoveMode.setSelected(true);
        this.tbSpaceRotateMode.setSelected(false);
        this.tbPlaneRotateMode.setSelected(false);
        
        /*if (this.tbAutoRotate.Pushed) {
            this.tbAutoRotateClick(null, e);
        }*/
    }

    public void tbEditModeClick(Object sender)
    {
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_Edit);
        this.tbEditMode.setSelected(true);
        this.tbScaleMode.setSelected(false);
        this.tbSpaceMoveMode.setSelected(false);
        this.tbPlaneMoveMode.setSelected(false);
        this.tbSpaceRotateMode.setSelected(false);
        this.tbPlaneRotateMode.setSelected(false);
        
        /*if (this.tbAutoRotate.Pushed) {
            this.tbAutoRotateClick(null, e);
        }*/
    }

    public void tbScaleModeClick(Object sender)
    {
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_Scale);
        this.tbEditMode.setSelected(false);
        this.tbScaleMode.setSelected(true);
        this.tbSpaceMoveMode.setSelected(false);
        this.tbPlaneMoveMode.setSelected(false);
        this.tbSpaceRotateMode.setSelected(false);
        this.tbPlaneRotateMode.setSelected(false);
        
        /*if (this.tbAutoRotate.Pushed) {
            this.tbAutoRotateClick(null, e);
        }*/
    }

    public void tbSpaceMoveModeClick(Object sender)
    {
        this.fMoleculeMaster.setMasterMode(MasterMode.mm_SpaceMove);
        this.tbEditMode.setSelected(false);
        this.tbScaleMode.setSelected(false);
        this.tbSpaceMoveMode.setSelected(true);
        this.tbPlaneMoveMode.setSelected(false);
        this.tbSpaceRotateMode.setSelected(false);
        this.tbPlaneRotateMode.setSelected(false);
        
        /*if (this.tbAutoRotate.Pushed) {
            this.tbAutoRotateClick(null, e);
        }*/
    }

    private void InterfaceUpdate()
    {
        this.tbEditMode.setSelected(this.fMoleculeMaster.getMasterMode() == MasterMode.mm_Edit);
        this.tbScaleMode.setSelected(this.fMoleculeMaster.getMasterMode() == MasterMode.mm_Scale);
        this.tbPlaneMoveMode.setSelected(this.fMoleculeMaster.getMasterMode() == MasterMode.mm_PlaneMove);
        this.tbSpaceMoveMode.setSelected(this.fMoleculeMaster.getMasterMode() == MasterMode.mm_SpaceMove);
        this.tbSpaceRotateMode.setSelected(this.fMoleculeMaster.getMasterMode() == MasterMode.mm_SpaceRotate);
        this.tbPlaneRotateMode.setSelected(this.fMoleculeMaster.getMasterMode() == MasterMode.mm_PlaneRotate);

        EnumSet<ViewOption> opts = this.fMoleculeMaster.getViewOptions();
        this.miSign.setSelected(opts.contains(ViewOption.mvoAtomSign));
        this.miName.setSelected(opts.contains(ViewOption.mvoAtomName));
        this.miNumber.setSelected(opts.contains(ViewOption.mvoAtomNumber));
        this.miAtomType.setSelected(opts.contains(ViewOption.mvoAtomType));
        this.miCharge.setSelected(opts.contains(ViewOption.mvoAtomCharge));
        this.miMass.setSelected(opts.contains(ViewOption.mvoAtomMass));
        this.miBalls.setSelected(opts.contains(ViewOption.mvoBalls));
        this.miSticks.setSelected(opts.contains(ViewOption.mvoSticks));
    }

    private void initializeComponents()
    {
        this.MainMenu = new JMenuBar();
        this.miFile = new JMenu();
        this.miLoad = new JMenuItem();
        this.miExit = new JMenuItem();
        this.miEdit = new JMenu();
        this.miDelete = new JMenuItem();
        this.miSelectAll = new JMenuItem();
        this.miView = new JMenu();
        this.ToolBarCommon = new JToolBar();
        this.tbLoad = new JButton();
        this.tbClear = new JButton();
        this.miClose = new JMenuItem();
        this.tbEditMode = new JButton();
        this.tbScaleMode = new JButton();
        this.tbPlaneMoveMode = new JButton();
        this.tbSpaceRotateMode = new JButton();
        this.miFunctions = new JMenu();
        this.tbPlaneRotateMode = new JButton();
        this.tbFitToWnd = new JButton();
        this.tbAutoRotate = new JButton();
        this.miMoleculeProperties = new JMenuItem();
        this.miSign = new JMenuItem();
        this.miName = new JMenuItem();
        this.miNumber = new JMenuItem();
        this.miAtomType = new JMenuItem();
        this.miCharge = new JMenuItem();
        this.miMass = new JMenuItem();
        this.miAtomView = new JMenu();
        this.tbSpaceMoveMode = new JButton();
        this.cbMolecules = new JComboBox();
        this.tbRenameMolecule = new JButton();
        this.miMoleculeView = new JMenu();
        this.miBalls = new JMenuItem();
        this.miSticks = new JMenuItem();
        this.OpenDialog = new JFileChooser();
        //this.ImageList = new ImageList();
        //this.ImageListMolecule = new ImageList();

        this.setLayout(new BorderLayout());

        FramesHelper.setClientSize(this, 800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setJMenuBar(this.MainMenu);
        this.setTitle(res_i18n.getString("CL_MOLECULE_VIEWER"));

        this.ToolBarCommon.setLocation(11, 2);
        this.ToolBarCommon.setSize(142, 22);
        //this.ToolBarCommon.Images = this.ImageList;
        this.add(this.ToolBarCommon, BorderLayout.NORTH);

        this.tbLoad.setText(res_i18n.getString("CL_Load"));
        this.tbLoad.addActionListener(this);
        this.tbLoad.setActionCommand("act_Load");
        //tToolButton2.ImageIndex = 1;

        //tbClear.ImageIndex = 13;
        tbClear.setText(res_i18n.getString("CL_Clear"));
        tbClear.setEnabled(false);

        this.tbEditMode.setText(res_i18n.getString("CL_Edit"));
        //tToolButton6.ImageIndex = 0;
        this.tbEditMode.setSelected(true);
        //tToolButton6.Click += this.tbEditModeClick;
        this.tbEditMode.addActionListener(this);
        this.tbEditMode.setActionCommand("act_EditMode");

        this.tbScaleMode.setText(res_i18n.getString("CL_ModeScale"));
        //tToolButton7.ImageIndex = 1;
        //tToolButton7.Click += this.tbScaleModeClick;
        this.tbScaleMode.addActionListener(this);
        this.tbScaleMode.setActionCommand("act_ScaleMode");

        this.tbSpaceMoveMode.setText(res_i18n.getString("CL_ModeSpaceMove"));
        //tToolButton8.ImageIndex = 5;
        //tToolButton8.Click += this.tbSpaceMoveModeClick;
        this.tbSpaceMoveMode.addActionListener(this);
        this.tbSpaceMoveMode.setActionCommand("act_SpaceMoveMode");

        this.tbPlaneMoveMode.setText(res_i18n.getString("CL_ModePlaneMove"));
        //tToolButton9.ImageIndex = 2;
        //tToolButton9.Click += this.tbPlaneMoveModeClick;
        this.tbPlaneMoveMode.addActionListener(this);
        this.tbPlaneMoveMode.setActionCommand("act_PlaneMoveMode");

        this.tbSpaceRotateMode.setText(res_i18n.getString("CL_ModeSpaceRotate"));
        //tToolButton10.ImageIndex = 3;
        //tToolButton10.Click += this.tbSpaceRotateModeClick;
        this.tbSpaceRotateMode.addActionListener(this);
        this.tbSpaceRotateMode.setActionCommand("act_SpaceRotateMode");

        this.tbPlaneRotateMode.setText(res_i18n.getString("CL_ModePlaneRotate"));
        //tToolButton11.ImageIndex = 4;
        //tToolButton11.Click += this.tbPlaneRotateModeClick;
        this.tbPlaneRotateMode.addActionListener(this);
        this.tbPlaneRotateMode.setActionCommand("act_PlaneRotateMode");

        this.tbFitToWnd.setText(res_i18n.getString("CL_FitToWin"));
        //this.tbFitToWnd.ImageIndex = 6;
        //tToolButton12.Hint = "Выровнять по окну";
        //tToolButton12.Click += this.tbFitToWndClick;

        this.tbAutoRotate.setText(res_i18n.getString("CL_AutoRotate"));
        this.tbAutoRotate.addActionListener(this);
        this.tbAutoRotate.setActionCommand("act_AutoRotate");

        /*JComboBox tJComboBox = this.cbMolecules;
        tJComboBox.DropDownStyle = JComboBoxStyle.DropDownList;
        tJComboBox.ItemHeight = 12;
        tJComboBox.TabIndex = 0;
        tJComboBox.Items.Add("");
        tJComboBox.Items.Add("default molecule");
        tJComboBox.setLocation(0;
        tJComboBox.Top = 0;
        tJComboBox.setSize(185;
        tJComboBox.Height = 20;*/
        //tJComboBox.set_OnChange(new EventHandler(this.cbMoleculesChange));

        this.tbRenameMolecule.setText(res_i18n.getString("CL_FileProperties"));
        //this.tbRenameMolecule.ImageIndex = 10;
        //tToolButton14.Click += this.tbRenameMoleculeClick;

        this.ToolBarCommon.add(this.tbLoad);
        this.ToolBarCommon.addSeparator();
        this.ToolBarCommon.add(this.tbClear);
        this.ToolBarCommon.add(this.tbEditMode);
        this.ToolBarCommon.add(this.tbScaleMode);
        this.ToolBarCommon.add(this.tbSpaceMoveMode);
        this.ToolBarCommon.add(this.tbPlaneMoveMode);
        this.ToolBarCommon.add(this.tbSpaceRotateMode);
        this.ToolBarCommon.add(this.tbPlaneRotateMode);
        this.ToolBarCommon.addSeparator();
        this.ToolBarCommon.add(this.tbAutoRotate);
        this.ToolBarCommon.addSeparator();
        this.ToolBarCommon.add(this.tbRenameMolecule);

        this.MainMenu.add(this.miFile);
        this.MainMenu.add(this.miEdit);
        this.MainMenu.add(this.miView);
        this.MainMenu.add(this.miFunctions);

        this.miFile.setText(res_i18n.getString("CL_File"));

        this.miFile.add(this.miLoad);
        this.miFile.add(this.miClose);
        this.miFile.add(this.miExit);

        this.miLoad.setText(res_i18n.getString("CL_Load"));
        this.miLoad.addActionListener(this);
        this.miLoad.setActionCommand("act_Load");

        this.miClose.setText(res_i18n.getString("CL_Close"));
        //this.miClose.Click += this.miCloseClick;

        this.miExit.setText(res_i18n.getString("CL_Exit"));
        //this.miExit.Click += this.miExitClick;

        this.miEdit.setText(res_i18n.getString("CL_Edit"));
        this.miEdit.add(this.miDelete);
        this.miEdit.add(this.miSelectAll);

        this.miDelete.setText(res_i18n.getString("CL_Delete"));
        this.miDelete.setEnabled(false);
        //tMenuItem9.Click += this.miDeleteClick;

        this.miSelectAll.setText(res_i18n.getString("CL_SelectAll"));
        this.miSelectAll.setEnabled(false);
        //tMenuItem10.Click += this.miSelectAllClick;

        this.miView.setText(res_i18n.getString("CL_View"));
        this.miView.add(this.miAtomView);
        this.miView.add(this.miMoleculeView);

        this.miAtomView.setText(res_i18n.getString("CL_ViewAtom"));
        this.miAtomView.add(this.miSign); 
        this.miAtomView.add(this.miName);
        this.miAtomView.add(this.miNumber);
        this.miAtomView.add(this.miCharge);
        this.miAtomView.add(this.miAtomType);
        this.miAtomView.add(this.miMass);

        this.miSign.setText(res_i18n.getString("CL_Symbol"));
        this.miSign.setSelected(true);
        //this.miSign.Click += this.miAtomViewsClick;

        this.miName.setText(res_i18n.getString("CL_Name"));
        //this.miName.Click += this.miAtomViewsClick;

        this.miNumber.setText(res_i18n.getString("CL_Number"));
        //this.miNumber.Click += this.miAtomViewsClick;

        this.miCharge.setText(res_i18n.getString("CL_Charge"));
        //this.miCharge.Click += this.miAtomViewsClick;

        this.miAtomType.setText(res_i18n.getString("CL_Type"));
        this.miAtomType.setEnabled(false);
        //this.miAtomType.Click += this.miAtomViewsClick;

        this.miMass.setText(res_i18n.getString("CL_Mass"));
        //this.miMass.Click += this.miAtomViewsClick;

        this.miMoleculeView.setText(res_i18n.getString("CL_ViewMolecule"));
        this.miMoleculeView.add(this.miBalls);
        this.miMoleculeView.add(this.miSticks);

        this.miBalls.setText(res_i18n.getString("CL_Balls"));
        //this.miBalls.Click += this.miMoleculeViewsClick;

        this.miSticks.setText(res_i18n.getString("CL_Sticks"));
        //this.miSticks.Click += this.miMoleculeViewsClick;

        this.miFunctions.setText(res_i18n.getString("CL_Services"));
        this.miFunctions.add(this.miMoleculeProperties);

        this.miMoleculeProperties.setText(res_i18n.getString("CL_FileProperties"));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        //Object source = e.getSource();
        
        switch (actionPerformed) {
            case "act_Load":
                this.actLoadClick();
                break;
                
            case "act_EditMode":
                this.tbEditModeClick(null);
                break;
            case "act_ScaleMode":
                this.tbScaleModeClick(null);
                break;
            case "act_SpaceMoveMode":
                this.tbSpaceMoveModeClick(null);
                break;
            case "act_PlaneMoveMode":
                this.tbPlaneMoveModeClick(null);
                break;
            case "act_PlaneRotateMode":
                this.tbPlaneRotateModeClick(null);
                break;
                
            case "act_AutoRotate":
                this.tbAutoRotateClick(null);
                break;
        }
    }
}
