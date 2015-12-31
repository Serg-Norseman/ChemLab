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
import bslib.common.ImageHelper;
import bslib.common.Logger;
import chemlab.core.chemical.CLData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 * Main frame of application.
 * 
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public final class CLMainWindow extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JPanel StatusBar;
    private JLabel StatusLabel;
    private JMenuBar MainMenu;
    private JMenu miRefBooks;
    private JMenuItem miBookElement;
    private JMenuItem miBookValue;
    private JMenuItem miBookCompounds;
    private JMenuItem miBookReactions;
    private JMenu miService;
    private JMenuItem miCompoundMaster;
    private JMenuItem miReactionMaster;
    private JMenuItem miMoleculeMaster;
    private JMenuItem miUnitMaster;
    private JMenuItem miExperimentMaster;
    private JMenuItem miCalcMaster;
    private JMenu miHelp;
    private JMenuItem miContents;
    private JMenuItem miAbout;
    //private JMenuItem miExit;

    public CLMainWindow()
    {
        super();

        Logger.init(AuxUtils.getAppPath() + "ChemLab.log");

        /*
        -Duser.country=EN -Duser.language=en
        */
        
        this.initializeComponents();
        
        
        //CLData.transferData();
    }

    private void initializeComponents()
    {
        this.StatusBar = new JPanel();
        this.MainMenu = new JMenuBar();
        this.miRefBooks = new JMenu();
        this.miBookElement = new JMenuItem();
        this.miBookValue = new JMenuItem();
        this.miBookCompounds = new JMenuItem();
        this.miBookReactions = new JMenuItem();
        this.miService = new JMenu();
        this.miCompoundMaster = new JMenuItem();
        this.miReactionMaster = new JMenuItem();
        this.miMoleculeMaster = new JMenuItem();
        this.miUnitMaster = new JMenuItem();
        this.miExperimentMaster = new JMenuItem();
        this.miCalcMaster = new JMenuItem();
        this.miHelp = new JMenu();
        this.miContents = new JMenuItem();
        this.miAbout = new JMenuItem();
        //this.miExit = new JMenuItem();

        this.setLayout(new BorderLayout());

        FramesHelper.setClientSize(this, 800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setLocationRelativeTo(null);
        //this.IsMdiContainer = true;
        this.setTitle(res_i18n.getString("CL_APPNAME"));
        //this.WindowState = FormWindowState.Maximized;
        this.setIconImage(ImageHelper.loadBitmap("ui/chemlab.png", true));

        this.StatusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.StatusBar.setFont(this.getFont());
        this.StatusBar.setPreferredSize(new Dimension(this.getWidth(), 16));
        this.StatusBar.setLayout(new BoxLayout(this.StatusBar, BoxLayout.X_AXIS));
        this.StatusLabel = new JLabel("");
        this.StatusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        this.StatusLabel.setFont(this.getFont());
        this.StatusBar.add(StatusLabel);

        this.MainMenu.setFont(this.getFont());
        
        this.miRefBooks.setText(res_i18n.getString("CL_REFBOOKS"));
        this.miRefBooks.setFont(this.getFont());

        this.miBookElement.setText(res_i18n.getString("CL_ELEMENTS_BOOK"));
        this.miBookElement.addActionListener(this);
        this.miBookElement.setActionCommand("cmd_PTable");
        this.miBookElement.setFont(this.getFont());
        this.miBookElement.setIcon(ImageHelper.loadIcon("ui/rb_ptable.png"));

        this.miBookValue.setText(res_i18n.getString("CL_VALUES_BOOK"));
        this.miBookValue.addActionListener(this);
        this.miBookValue.setActionCommand("cmd_UnitsTable");
        this.miBookValue.setFont(this.getFont());
        this.miBookValue.setIcon(ImageHelper.loadIcon("ui/rb_values.png"));

        this.miBookCompounds.setText(res_i18n.getString("CL_COMPOUNDS_BOOK"));
        this.miBookCompounds.addActionListener(this);
        this.miBookCompounds.setActionCommand("");
        this.miBookCompounds.setFont(this.getFont());
        this.miBookCompounds.setIcon(ImageHelper.loadIcon("ui/rb_compounds.png"));
        this.miBookCompounds.setEnabled(false);

        this.miBookReactions.setText(res_i18n.getString("CL_REACTIONS_BOOK"));
        this.miBookReactions.addActionListener(this);
        this.miBookReactions.setActionCommand("");
        this.miBookReactions.setFont(this.getFont());
        this.miBookReactions.setIcon(ImageHelper.loadIcon("ui/rb_reactions.png"));
        this.miBookReactions.setEnabled(false);
        
        this.miService.setText(res_i18n.getString("CL_SERVICE"));
        this.miService.setFont(this.getFont());

        this.miUnitMaster.setText(res_i18n.getString("CL_UNITS_CONVERTER"));
        this.miUnitMaster.addActionListener(this);
        this.miUnitMaster.setActionCommand("cmd_UnitsConverter");
        this.miUnitMaster.setFont(this.getFont());
        this.miUnitMaster.setIcon(ImageHelper.loadIcon("ui/measure.png"));

        this.miCompoundMaster.setText(res_i18n.getString("CL_COMPOUND_MASTER"));
        this.miCompoundMaster.addActionListener(this);
        this.miCompoundMaster.setActionCommand("cmd_CompoundMaster");
        this.miCompoundMaster.setFont(this.getFont());
        this.miCompoundMaster.setIcon(ImageHelper.loadIcon("ui/compound.png"));

        this.miReactionMaster.setText(res_i18n.getString("CL_REACTION_MASTER"));
        this.miReactionMaster.addActionListener(this);
        this.miReactionMaster.setActionCommand("cmd_ReactionMaster");
        this.miReactionMaster.setFont(this.getFont());
        this.miReactionMaster.setIcon(ImageHelper.loadIcon("ui/reaction.png"));

        this.miMoleculeMaster.setText(res_i18n.getString("CL_MOLECULE_VIEWER"));
        this.miMoleculeMaster.addActionListener(this);
        this.miMoleculeMaster.setActionCommand("cmd_MoleculeMaster");
        this.miMoleculeMaster.setFont(this.getFont());
        this.miMoleculeMaster.setIcon(ImageHelper.loadIcon("ui/molecule.png"));

        this.miExperimentMaster.setText(res_i18n.getString("CL_EXPERIMENT_MASTER"));
        this.miExperimentMaster.addActionListener(this);
        this.miExperimentMaster.setActionCommand("cmd_ExperimentMaster");
        this.miExperimentMaster.setFont(this.getFont());
        this.miExperimentMaster.setIcon(ImageHelper.loadIcon("ui/experiment.png"));

        this.miCalcMaster.setText(res_i18n.getString("CL_CALCS"));
        this.miCalcMaster.addActionListener(this);
        this.miCalcMaster.setActionCommand("cmd_CalcMaster");
        this.miCalcMaster.setFont(this.getFont());

        this.miHelp.setText(res_i18n.getString("CL_HELP"));
        this.miHelp.setFont(this.getFont());

        this.miContents.setText(res_i18n.getString("CL_HELP_CONTENTS"));
        this.miContents.setEnabled(false);
        this.miContents.setFont(this.getFont());
        this.miContents.setIcon(ImageHelper.loadIcon("ui/help.png"));

        this.miAbout.setText(res_i18n.getString("CL_ABOUT"));
        this.miAbout.addActionListener(this);
        this.miAbout.setActionCommand("cmd_About");
        this.miAbout.setFont(this.getFont());
        this.miAbout.setIcon(ImageHelper.loadIcon("ui/about.png"));

        /*this.miExit.setText("Выход");
        this.miExit.addActionListener(this);
        this.miExit.setActionCommand("cmd_Exit");
        this.miExit.setFont(this.getFont());*/

        this.MainMenu.add(this.miRefBooks);
        this.MainMenu.add(this.miService);
        this.MainMenu.add(this.miHelp);

        this.miRefBooks.add(this.miBookElement);
        this.miRefBooks.add(this.miBookValue);
        this.miRefBooks.add(this.miBookCompounds);
        this.miRefBooks.add(this.miBookReactions);

        this.miService.add(this.miUnitMaster);
        this.miService.add(this.miCompoundMaster);
        this.miService.add(this.miReactionMaster);
        this.miService.add(this.miMoleculeMaster);
        this.miService.add(this.miExperimentMaster);
        this.miService.addSeparator();
        this.miService.add(this.miCalcMaster);

        this.miHelp.add(this.miContents);
        this.miHelp.add(this.miAbout);

        this.add(this.StatusBar, BorderLayout.SOUTH);
        this.setJMenuBar(MainMenu);
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                CLData.CompoundsBook.saveXML();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();

        switch (actionPerformed) {
            case "cmd_About":
                JDialog fmAbout = new CLAboutDialog(this);
                fmAbout.setLocationRelativeTo(this);
                fmAbout.setVisible(true);
                break;

            case "cmd_Exit":
                this.setVisible(false);
                break;

            case "cmd_PTable":
                JFrame fmBE = new CLElementsBook();
                fmBE.setLocationRelativeTo(this);
                //dlg.MdiParent = this;
                fmBE.setVisible(true);
                break;
            
            case "cmd_UnitsTable":
                JFrame fmBV = new CLValuesBook();
                fmBV.setLocationRelativeTo(this);
                //dlg.MdiParent = this;
                fmBV.setVisible(true);
                break;
            
            case "cmd_UnitsConverter":
                JFrame fmMU = new CLUnitsConverter();
                fmMU.setLocationRelativeTo(this);
                fmMU.setVisible(true);
                break;
            
            case "cmd_CompoundMaster":
                JFrame fmMC = new CLCompoundMaster();
                fmMC.setLocationRelativeTo(this);
                fmMC.setVisible(true);
                break;
            
            case "cmd_ReactionMaster":
                JFrame fmMR = new CLReactionMaster();
                fmMR.setLocationRelativeTo(this);
                fmMR.setVisible(true);
                break;
            
            case "cmd_MoleculeMaster":
                JFrame fmMM = new CLMoleculeViewer();
                fmMM.setLocationRelativeTo(this);
                //dlg.MdiParent = this;
                fmMM.setVisible(true);
                break;
            
            case "cmd_ExperimentMaster":
                JFrame fmME = new CLExperimentMaster();
                fmME.setLocationRelativeTo(this);
                //dlg.MdiParent = this;
                fmME.setVisible(true);
                break;
                
            case "cmd_CalcMaster":
                JDialog fmCalc = new CLChemCalc(this);
                fmCalc.setLocationRelativeTo(this);
                fmCalc.setVisible(true);
                break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        /*try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
        }*/
        
        CLMainWindow app = new CLMainWindow();
        app.setVisible(true);
    }
}
