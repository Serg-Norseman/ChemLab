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
import bslib.common.Logger;
import chemlab.core.chemical.AtomSolver;
import chemlab.core.chemical.CLData;
import chemlab.core.CLUtils;
import chemlab.core.chemical.CrystalKind;
import chemlab.core.chemical.DegreeId;
import chemlab.core.chemical.DegreeSet;
import chemlab.core.chemical.SpectrumKind;
import chemlab.core.chemical.ValencyId;
import chemlab.core.chemical.ValencySet;
import chemlab.core.controls.AtomStructureGrid;
import chemlab.core.controls.AtomViewer;
import chemlab.core.controls.CrystalViewer;
import chemlab.core.controls.OxidationDegreeControl;
import chemlab.core.controls.PeriodicTable;
import chemlab.core.controls.SpectrumViewer;
import chemlab.core.controls.ValencyControl;
import chemlab.refbooks.DecayRecord;
import chemlab.refbooks.ElementRecord;
import chemlab.refbooks.NuclideRecord;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public final class CLElementsBook extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private JTabbedPane PageControl;
    private JPanel tsTables;
    private JPanel tsInfo;
    private JSplitPane Splitter2;
    private JToolBar DBNavigator1;
    private JTabbedPane PageControl1;
    private JPanel tsElement;
    private JLabel Label13;
    private JLabel Label14;
    private JLabel Label15;
    private JLabel Label16;
    private JLabel Label17;
    private JLabel Label18;
    private JLabel Label19;
    private JLabel Label20;
    private JLabel Label21;
    private JLabel Label1;
    private JLabel Label2;
    private JLabel Label6;
    private JLabel Label24;
    private JLabel Label25;
    private JLabel Label8;
    private JLabel Label9;
    private JLabel Label22;
    private JLabel Label23;
    private JLabel Label3;
    private JLabel Label27;
    private JLabel Label28;
    private JLabel Label26;
    private JTextField DBEdit3;
    private JTextField DBEdit4;
    private JTextField DBEdit5;
    private JTextField DBEdit6;
    private JTextField DBEdit7;
    private JTextField DBEdit8;
    private JTextField DBEdit9;
    private JTextField cbClass;
    private JTextField DBEdit10;
    private JTextField DBEdit13;
    private JTextField DBEdit17;
    private JTextField DBEdit18;
    private JTextField DBEdit11;
    private JTextField DBEdit12;
    private JTextField DBEdit15;
    private JTextField DBEdit16;
    private JTextArea DBMemo2;
    private JTextField DBEdit19;
    private JTextField DBEdit20;
    private JTextField DBEdit21;
    private JPanel tsHistory;
    private JLabel Label10;
    private JLabel Label11;
    private JLabel Label12;
    private JLabel Label4;
    private JLabel Label5;
    private JLabel Label7;
    private JTextField DBEdit1;
    private JTextField DBEdit2;
    private JTextArea DBMemo1;
    private JTextArea DBMemo3;
    private JTextArea DBMemo4;
    private JTextField DBEdit14;
    private JPanel tsAtomStructure;
    private JPanel tsIsotopes;
    private JButton tbDecayView;
    private JLabel Label36;
    private JTextField DBEdit26;
    private JLabel Label37;
    private JTextField DBEdit27;
    private JLabel Label38;
    private JTextField DBEdit28;
    private JLabel Label39;
    private JTextField DBEdit29;
    private JLabel Label40;
    private JTextField DBEdit30;
    private JLabel Label41;
    private JTextField DBEdit31;
    private JLabel Label42;
    private JTextField DBEdit32;
    private JLabel Label43;
    private JTextField DBEdit33;
    private JLabel Label44;
    private JTextField DBEdit34;
    private JLabel Label45;
    private JTextField DBEdit35;
    private JTextField cbABProperty;
    private JPanel gbAtomView;
    private JPanel gbAtomStructure;
    private JTextArea mStructure;
    private JPanel gbCrystalView;
    private JTextField cbCrystalStructure;
    private JPanel tsSpectrums;
    private JPanel GroupBox4;
    private JPanel GroupBox5;
    private VirtualTable ListDecay;
    private VirtualTable ListNuclides;
    private VirtualTable ListElements;
    private JButton tbFirst;
    private JButton tbPrior;
    private JButton tbNext;
    private JButton tbLast;
    private JLabel Label29;
    private JTextField Edit1;
    private JLabel Label30;
    private JTextField Edit2;
    private JLabel Label31;
    private JTextField Edit3;
    private JLabel Label32;
    private JTextField Edit4;
    private JLabel Label33;
    private JTextField Edit5;
    private JSplitPane Splitter4;
    private JPanel PanelPT;

    //private ImageList ImageList;
    //private ImageList ImageList1;

    private final PeriodicTable fPeriodicTable;
    private final OxidationDegreeControl fOxidationDegreeControl;
    private final ValencyControl fValencyControl;
    private final AtomSolver fAtomMaster;
    private final AtomStructureGrid fAtomStructureGrid;
    private final CrystalViewer fCrystalViewer;
    private final AtomViewer fAtomViewer;
    private final SpectrumViewer fSpectrumView1;
    private final SpectrumViewer fSpectrumView2;

    public CLElementsBook()
    {
        super();
        this.initializeComponents();

        fOxidationDegreeControl = new OxidationDegreeControl();
        fOxidationDegreeControl.setFont(this.getFont());
        fOxidationDegreeControl.setLocation(362, 15);
        fOxidationDegreeControl.setSize(360, 22);
        fOxidationDegreeControl.setDegreeSet(new DegreeSet(DegreeId.N1, DegreeId.P1));
        fOxidationDegreeControl.setEnabled(false);

        fValencyControl = new ValencyControl();
        fValencyControl.setFont(this.getFont());
        fValencyControl.setLocation(362, 52);
        fValencyControl.setSize(360, 22);
        fValencyControl.setValencySet(new ValencySet(ValencyId.V1));
        fValencyControl.setEnabled(false);

        this.tsElement.add(fOxidationDegreeControl);
        this.tsElement.add(fValencyControl);

        this.fAtomMaster = new AtomSolver();

        fAtomStructureGrid = new AtomStructureGrid();
        fAtomStructureGrid.setLocation(8, 15);
        fAtomStructureGrid.setSize(267, 149);
        fAtomStructureGrid.setEnabled(false);
        fAtomStructureGrid.setAtomMaster(this.fAtomMaster);

        fCrystalViewer = new CrystalViewer();
        fCrystalViewer.setLocation(8, 15);
        fCrystalViewer.setSize(36, 37);
        fCrystalViewer.setCrystal(CrystalKind.Cubic);

        fAtomViewer = new AtomViewer();
        fAtomViewer.setLocation(2, 14);
        fAtomViewer.setSize(464, 355);

        this.gbAtomStructure.add(this.fAtomStructureGrid);
        this.gbCrystalView.add(this.fCrystalViewer);
        this.gbAtomView.add(this.fAtomViewer);
        
        fSpectrumView1 = new SpectrumViewer();
        fSpectrumView1.setFont(this.getFont());
        fSpectrumView1.setLocation(2, 14);
        fSpectrumView1.setSize(676, 95);
        fSpectrumView1.setKind(SpectrumKind.skAbsorption);

        fSpectrumView2 = new SpectrumViewer();
        fSpectrumView2.setFont(this.getFont());
        fSpectrumView2.setLocation(2, 14);
        fSpectrumView2.setSize(676, 96);
        fSpectrumView2.setKind(SpectrumKind.skEmission);

        this.GroupBox4.add(fSpectrumView1, BorderLayout.CENTER);
        this.GroupBox5.add(fSpectrumView2, BorderLayout.CENTER);
        
        fPeriodicTable = new PeriodicTable();
        fPeriodicTable.setLocation(0, 349);
        fPeriodicTable.setSize(760, 200);
        fPeriodicTable.setColouration(PeriodicTable.Colouration.ByClass);
        fPeriodicTable.setOnChange(this::PeriodicTableChange);
        PanelPT.add(fPeriodicTable, BorderLayout.CENTER);

        CommonUtils.changeFont(this);
        
        this.updateElementsList();

        this.fPeriodicTable.setElement("H");
    }

    public void PeriodicTableChange(Object sender)
    {
        String sym = this.fPeriodicTable.getElement();
        int eId = CLData.ElementsBook.findElementNumber(sym) + 1;
        this.ListElements.setSelectedRow(eId);
    }

    public void ListElementsSelectItem(Object sender)
    {
        int row = this.ListElements.getSelectedRow();
        String sym = (String) this.ListElements.getValueAt(row, 1);
        if (row > 0) {
            //this.tsElement.setText("Элемент [" + sym + "]");
            this.fPeriodicTable.setElement(sym);

            ElementRecord elRec = CLData.ElementsBook.findElement(sym);

            this.updateNuclidesList(elRec.FNumber);
            this.updateElementControls(elRec.FNumber);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        Object source = e.getSource();
        
        int row = this.ListElements.getSelectedRow();
        int newIndex;

        switch (actionPerformed) {
            case "btn_First":
                newIndex = 0;
                this.ListElements.setSelectedRow(newIndex);
                break;

            case "btn_Prior":
                newIndex = row - 1;
                if (newIndex >= 0) {
                    this.ListElements.setSelectedRow(newIndex);
                }
                break;
            
            case "btn_Next":
                newIndex = row + 1;
                if (newIndex < CLData.ElementsBook.size()) {
                    this.ListElements.setSelectedRow(newIndex);
                }
                break;
            
            case "btn_Last":
                newIndex = CLData.ElementsBook.size() - 1;
                this.ListElements.setSelectedRow(newIndex);
                break;
            
            case "btn_DecayView":
                row = this.ListNuclides.getSelectedRow();
                Object val = this.ListNuclides.getValueAt(row, 0);
                if (val != null) {
                    String nuclide = (String) val;
                    CLDecayViewer fmViewDecay = new CLDecayViewer(nuclide);
                    fmViewDecay.setLocationRelativeTo(this);
                    fmViewDecay.setVisible(true);
                }
                break;
        }
    }

    private void updateNuclidesList(int elementId)
    {
        try {
            //this.ListNuclides.BeginUpdate();
            this.ListNuclides.clear();
            for (NuclideRecord nucRec : CLData.NuclidesBook.getList()) {
                if (nucRec.ElementId == elementId) {
                    Object[] rowData = new Object[] {
                        nucRec.Sign,
                        AuxUtils.FloatToStr(nucRec.Abundance),
                        AuxUtils.FloatToStr(nucRec.Weight),
                        AuxUtils.FloatToStr(nucRec.Spin),
                        nucRec.HalfLife
                    };
                    
                    this.ListNuclides.addRow(rowData);
                    
                    /*ListViewItem item = this.ListNuclides.Items.Add(nucRec.Sign);
                    item.SubItems.Add(AuxUtils.FloatToStr(nucRec.Abundance));
                    item.SubItems.Add(AuxUtils.FloatToStr(nucRec.Weight));
                    item.SubItems.Add(AuxUtils.FloatToStr(nucRec.Spin));
                    item.SubItems.Add(nucRec.HalfLife);
                    item.Tag = nucRec;*/
                }
            }
            //this.ListNuclides.EndUpdate();
        } catch (Exception ex) {
            Logger.write("TfmBookElement.UpdateNuclidesList(): " + ex.getMessage());
        }
    }

    private void updateDecayList(int nuclideId)
    {
        try {
            //this.ListDecay.BeginUpdate();
            this.ListDecay.clear();
            
            ArrayList<DecayRecord> decays = CLData.DecayBook.getDecayByNuclide(nuclideId);
            for (DecayRecord decRec : decays) {
                Object[] rowData = new Object[]{
                    CLData.DecayMode[decRec.Mode.getValue()].ShortName,
                    CLData.NuclidesBook.get((int) decRec.DescendantId - 1).Sign
                };

                this.ListDecay.addRow(rowData);

                /*ListViewItem item = this.ListDecay.Items.Add(CLData.DecayMode[decRec.DecayMode.getValue()].ShortName);
                 item.SubItems.Add(CLData.NuclidesTable.get((int) decRec.DescendantId - 1).Sign);*/
            }
            //this.ListDecay.EndUpdate();
        } catch (Exception ex) {
            Logger.write("TfmBookElement.UpdateDecayList(): " + ex.getMessage());
        }
    }

    public void ListNuclidesSelectItem(Object sender)
    {
        int row = ListNuclides.getSelectedRow();
        if (row >= 0) {
            Object val = this.ListNuclides.getValueAt(row, 0);
            NuclideRecord nucRec = CLData.NuclidesBook.getNuclideBySign((String)val);
            this.updateDecayList(nucRec.NuclideId);
        }
    }

    private void updateElementControls(int elementId)
    {
        ElementRecord elem = CLData.ElementsBook.get(elementId - -1);

        this.DBEdit4.setText((new Integer(elem.FNumber)).toString());
        this.DBEdit3.setText(elem.FSymbol);
        this.DBEdit5.setText(elem.FRus_Name);
        this.DBEdit6.setText(elem.FLat_Name);

        this.DBEdit17.setText(AuxUtils.FloatToStr(elem.FElectron_Affinity));
        this.DBEdit18.setText(AuxUtils.FloatToStr(elem.FHardness));
        this.DBEdit11.setText(AuxUtils.FloatToStr(elem.FCovalent_Radius));

        this.DBEdit26.setText(AuxUtils.FloatToStr(elem.FVDW_Radius));
        this.DBEdit31.setText(AuxUtils.FloatToStr(elem.FFusion_Heat));
        this.DBEdit7.setText(AuxUtils.FloatToStr(elem.FAtomic_Mass));
        this.DBEdit9.setText(AuxUtils.FloatToStr(elem.FAtomic_Radius));
        this.DBEdit13.setText(AuxUtils.FloatToStr(elem.FAtomic_Volume));
        this.DBEdit8.setText(AuxUtils.FloatToStr(elem.FElectronegativity));
        this.DBEdit12.setText(AuxUtils.FloatToStr(elem.FIP_1ST));
        this.DBEdit15.setText(AuxUtils.FloatToStr(elem.FIP_2ND));
        this.DBEdit16.setText(AuxUtils.FloatToStr(elem.FIP_3RD));
        this.DBEdit27.setText(AuxUtils.FloatToStr(elem.FBoiling_Point));
        this.DBEdit32.setText(AuxUtils.FloatToStr(elem.FElectric_Conductivity));
        this.DBEdit10.setText(AuxUtils.FloatToStr(elem.FTerrestrial_Cortex));
        this.DBEdit19.setText(AuxUtils.FloatToStr(elem.FCritical_Temperature));
        this.DBEdit20.setText(AuxUtils.FloatToStr(elem.FCritical_Pressure));
        this.DBEdit21.setText(AuxUtils.FloatToStr(elem.FCritical_Density));
        this.DBEdit28.setText(AuxUtils.FloatToStr(elem.FMelting_Point));
        this.DBEdit33.setText(AuxUtils.FloatToStr(elem.FThermal_Conductivity));
        this.DBEdit29.setText(AuxUtils.FloatToStr(elem.FDensity));
        this.DBEdit30.setText(AuxUtils.FloatToStr(elem.FVaporization_Heat));
        this.DBEdit34.setText(AuxUtils.FloatToStr(elem.FSpecific_Heat_Capacity));

        this.DBMemo2.setText(elem.FDescription);
        this.fOxidationDegreeControl.setDegreeSet(elem.FOxidation_Degree);
        this.fValencyControl.setValencySet(elem.FValency);
        this.cbClass.setText(CLData.ElementClasses[elem.FClass.getValue()]);
        this.cbABProperty.setText(CLData.ElementABProperty[elem.FABProperty.getValue()]);
        this.DBEdit14.setText(elem.FOriginal_Name);
        this.DBEdit1.setText(elem.FDiscovery_Date);
        this.DBEdit2.setText(elem.FDiscoverers);
        this.DBMemo1.setText(elem.FDiscovery_Particularities);
        this.DBMemo3.setText(elem.FSources);
        this.DBMemo4.setText(elem.FUses);

        this.fAtomMaster.setStructureStr(elem.FStructure);
        this.fAtomStructureGrid.refresh();
        this.fAtomViewer.setStructure(this.fAtomMaster.getStructure());

        this.mStructure.setText(elem.FStructure);
        this.fCrystalViewer.setCrystal(elem.FCrystal_Structure);
        this.cbCrystalStructure.setText(CLData.Crystal[elem.FCrystal_Structure.getValue()]);

        this.fSpectrumView1.setLinesStr(elem.FAbsorption_Spectrum);
        this.fSpectrumView2.setLinesStr(elem.FEmission_Spectrum);

        this.Edit1.setText(elem.FEng_Name);
        this.Edit2.setText(elem.FGer_Name);
        this.Edit3.setText(elem.FFre_Name);
        this.Edit4.setText(elem.FSwd_Name);
        this.Edit5.setText(elem.FNor_Name);

        int eIdx = elementId - -1;
        this.tbFirst.setEnabled(eIdx > 0);
        this.tbPrior.setEnabled(eIdx > 0);
        this.tbNext.setEnabled(eIdx < CLData.ElementsBook.size() - 1);
        this.tbLast.setEnabled(eIdx < CLData.ElementsBook.size() - 1);
    }

    private void updateElementsList()
    {
        this.ListElements.clear();
        this.addColumn(res_i18n.getString("CL_Number"));
        this.addColumn(res_i18n.getString("CL_Symbol"));
        this.addColumn(res_i18n.getString("CL_Name"));
        this.addColumn(res_i18n.getString("CL_LatName"));
        this.addColumn(res_i18n.getString("CL_OriginalName"));
        this.addColumn(res_i18n.getString("CL_Class"));
        this.addColumn("Характер");
        this.addColumn(res_i18n.getString("CL_AtomicMass"));
        this.addColumn(res_i18n.getString("CL_AtomicRadius"));
        this.addColumn(res_i18n.getString("CL_AtomicVolume"));
        this.addColumn(res_i18n.getString("CL_CovalentRadius"));
        this.addColumn(res_i18n.getString("CL_ENEGATIVITY"));
        this.addColumn(res_i18n.getString("CL_OXIDATION_DEGREES"));
        this.addColumn(res_i18n.getString("CL_VALENCIES"));
        this.addColumn("Дата открытия");
        this.addColumn("Исследователи");
        this.addColumn("Содер-ние в зем. коре");
        this.addColumn("Кристал. структура");
        this.addColumn("Первый потенциал ионизации");
        this.addColumn("Второй потенциал ионизации");
        this.addColumn("Третий потенциал ионизации");
        this.addColumn(res_i18n.getString("CL_ELECTRON_AFFINITY"));
        this.addColumn(res_i18n.getString("CL_HARDNESS"));
        this.addColumn("Крит. температура");
        this.addColumn("Крит. плотность");
        this.addColumn("Крит. давление");
        this.addColumn(res_i18n.getString("CL_VDW_RADIUS"));
        this.addColumn(res_i18n.getString("CL_BOILING_POINT"));
        this.addColumn(res_i18n.getString("CL_MELTING_POINT"));
        this.addColumn(res_i18n.getString("CL_Density"));
        this.addColumn(res_i18n.getString("CL_VAPORIZATION_HEAT"));
        this.addColumn(res_i18n.getString("CL_FUSION_HEAT"));
        this.addColumn(res_i18n.getString("CL_ELECTRIC_CONDUCTIVITY"));
        this.addColumn("Теплопроводность");
        this.addColumn(res_i18n.getString("CL_SPECIFIC_HEAT_CAPACITY"));

        //this.ListElements.BeginUpdate();
        this.ListElements.clear();

        for (ElementRecord elem : CLData.ElementsBook.getList()) {
            Object[] rowData = new Object[] {
                String.valueOf(elem.FNumber),
                elem.FSymbol,
                elem.FRus_Name,
                elem.FLat_Name,
                elem.FOriginal_Name,
                CLData.ElementClasses[elem.FClass.getValue()],
                CLData.ElementABProperty[elem.FABProperty.getValue()],
                AuxUtils.FloatToStr(elem.FAtomic_Mass),
                AuxUtils.FloatToStr(elem.FAtomic_Radius),
                AuxUtils.FloatToStr(elem.FAtomic_Volume),
                AuxUtils.FloatToStr(elem.FCovalent_Radius),
                AuxUtils.FloatToStr(elem.FElectronegativity),
                elem.FOxidation_Degree.getDegreeStr(),
                elem.FValency.getValencyStr(),
                elem.FDiscovery_Date,
                elem.FDiscoverers,
                AuxUtils.FloatToStr(elem.FTerrestrial_Cortex),
                CLData.Crystal[elem.FCrystal_Structure.getValue()],
                AuxUtils.FloatToStr(elem.FIP_1ST),
                AuxUtils.FloatToStr(elem.FIP_2ND),
                AuxUtils.FloatToStr(elem.FIP_3RD),
                AuxUtils.FloatToStr(elem.FElectron_Affinity),
                AuxUtils.FloatToStr(elem.FHardness),
                AuxUtils.FloatToStr(elem.FCritical_Temperature),
                AuxUtils.FloatToStr(elem.FCritical_Density),
                AuxUtils.FloatToStr(elem.FCritical_Pressure),
                AuxUtils.FloatToStr(elem.FVDW_Radius),
                AuxUtils.FloatToStr(elem.FBoiling_Point),
                AuxUtils.FloatToStr(elem.FMelting_Point),
                AuxUtils.FloatToStr(elem.FDensity),
                AuxUtils.FloatToStr(elem.FVaporization_Heat),
                AuxUtils.FloatToStr(elem.FFusion_Heat),
                AuxUtils.FloatToStr(elem.FElectric_Conductivity),
                AuxUtils.FloatToStr(elem.FThermal_Conductivity),
                AuxUtils.FloatToStr(elem.FSpecific_Heat_Capacity)
            };
            
            this.ListElements.addRow(rowData);
            
            /*ListViewItem item = this.ListElements.Items.Add((new Integer(elem.FNumber)).toString());
            item.SubItems.Add(elem.FSymbol);
            item.SubItems.Add(elem.FRus_Name);
            item.SubItems.Add(elem.FLat_Name);
            item.SubItems.Add(elem.FOriginal_Name);
            item.SubItems.Add(CLData.ElementClasses[elem.FClass.getValue()]);
            item.SubItems.Add(CLData.ElementABProperty[elem.FABProperty.getValue()]);

            item.SubItems.Add(AuxUtils.FloatToStr(elem.FAtomic_Mass));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FAtomic_Radius));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FAtomic_Volume));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FCovalent_Radius));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FElectronegativity));

            item.SubItems.Add(CLCommon.GetDegreeStr(elem.FOxidation_Degree));
            item.SubItems.Add(CLCommon.GetValencyStr(elem.FValency));
            item.SubItems.Add(elem.FDiscovery_Date);
            item.SubItems.Add(elem.FDiscoverers);

            item.SubItems.Add(AuxUtils.FloatToStr(elem.FTerrestrial_Cortex));

            item.SubItems.Add(CLData.Crystal[elem.FCrystal_Structure.getValue()]);

            item.SubItems.Add(AuxUtils.FloatToStr(elem.FIP_1ST));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FIP_2ND));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FIP_3RD));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FElectron_Affinity));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FHardness));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FCritical_Temperature));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FCritical_Density));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FCritical_Pressure));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FVDW_Radius));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FBoiling_Point));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FMelting_Point));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FDensity));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FVaporization_Heat));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FFusion_Heat));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FElectric_Conductivity));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FThermal_Conductivity));
            item.SubItems.Add(AuxUtils.FloatToStr(elem.FSpecific_Heat_Capacity));*/
        }

        //this.ListElements.EndUpdate();
        
        this.ListElements.packColumns(10);
    }

    private void addColumn(String cap)
    {
        this.ListElements.addColumn(cap, 100);
    }

    private void initializeComponents()
    {
        this.PageControl = new JTabbedPane();
        this.tsTables = new JPanel();
        this.tsInfo = new JPanel();
        this.Splitter2 = new JSplitPane();
        this.DBNavigator1 = new JToolBar();
        this.PageControl1 = new JTabbedPane();
        this.tsElement = new JPanel();
        this.Label13 = new JLabel();
        this.Label14 = new JLabel();
        this.Label15 = new JLabel();
        this.Label16 = new JLabel();
        this.Label17 = new JLabel();
        this.Label18 = new JLabel();
        this.Label19 = new JLabel();
        this.Label20 = new JLabel();
        this.Label21 = new JLabel();
        this.Label1 = new JLabel();
        this.Label2 = new JLabel();
        this.Label6 = new JLabel();
        this.Label24 = new JLabel();
        this.Label25 = new JLabel();
        this.Label8 = new JLabel();
        this.Label9 = new JLabel();
        this.Label22 = new JLabel();
        this.Label23 = new JLabel();
        this.Label3 = new JLabel();
        this.Label27 = new JLabel();
        this.Label28 = new JLabel();
        this.Label26 = new JLabel();
        this.DBEdit3 = new JTextField();
        this.DBEdit4 = new JTextField();
        this.DBEdit5 = new JTextField();
        this.DBEdit6 = new JTextField();
        this.DBEdit7 = new JTextField();
        this.DBEdit8 = new JTextField();
        this.DBEdit9 = new JTextField();
        this.cbClass = new JTextField();
        this.DBEdit10 = new JTextField();
        this.DBEdit13 = new JTextField();
        this.DBEdit17 = new JTextField();
        this.DBEdit18 = new JTextField();
        this.DBEdit11 = new JTextField();
        this.DBEdit12 = new JTextField();
        this.DBEdit15 = new JTextField();
        this.DBEdit16 = new JTextField();
        this.DBMemo2 = new JTextArea();
        this.DBEdit19 = new JTextField();
        this.DBEdit20 = new JTextField();
        this.DBEdit21 = new JTextField();
        this.tsHistory = new JPanel();
        this.Label10 = new JLabel();
        this.Label11 = new JLabel();
        this.Label12 = new JLabel();
        this.Label4 = new JLabel();
        this.Label5 = new JLabel();
        this.Label7 = new JLabel();
        this.DBEdit1 = new JTextField();
        this.DBEdit2 = new JTextField();
        this.DBMemo1 = new JTextArea();
        this.DBMemo3 = new JTextArea();
        this.DBMemo4 = new JTextArea();
        this.DBEdit14 = new JTextField();
        this.tsAtomStructure = new JPanel();
        this.tsIsotopes = new JPanel();
        this.tbDecayView = new JButton();
        this.Label36 = new JLabel();
        this.DBEdit26 = new JTextField();
        this.Label37 = new JLabel();
        this.DBEdit27 = new JTextField();
        this.Label38 = new JLabel();
        this.DBEdit28 = new JTextField();
        this.Label39 = new JLabel();
        this.DBEdit29 = new JTextField();
        this.Label40 = new JLabel();
        this.DBEdit30 = new JTextField();
        this.Label41 = new JLabel();
        this.DBEdit31 = new JTextField();
        this.Label42 = new JLabel();
        this.DBEdit32 = new JTextField();
        this.Label43 = new JLabel();
        this.DBEdit33 = new JTextField();
        this.Label44 = new JLabel();
        this.DBEdit34 = new JTextField();
        this.Label45 = new JLabel();
        this.DBEdit35 = new JTextField();
        this.cbABProperty = new JTextField();
        this.gbAtomView = new JPanel();
        this.gbAtomStructure = new JPanel();
        this.mStructure = new JTextArea();
        this.gbCrystalView = new JPanel();
        this.cbCrystalStructure = new JTextField();
        this.tsSpectrums = new JPanel();
        this.GroupBox4 = new JPanel();
        this.GroupBox5 = new JPanel();
        this.ListDecay = new VirtualTable();
        this.ListNuclides = new VirtualTable();
        this.ListElements = new VirtualTable();
        this.tbFirst = new JButton();
        this.tbPrior = new JButton();
        this.tbNext = new JButton();
        this.tbLast = new JButton();
        this.Label29 = new JLabel();
        this.Edit1 = new JTextField();
        this.Label30 = new JLabel();
        this.Edit2 = new JTextField();
        this.Label31 = new JLabel();
        this.Edit3 = new JTextField();
        this.Label32 = new JLabel();
        this.Edit4 = new JTextField();
        this.Label33 = new JLabel();
        this.Edit5 = new JTextField();
        this.PanelPT = new JPanel();

        //this.ImageList = new ImageList();
        //this.ImageList1 = new ImageList();

        this.setLayout(new BorderLayout());
        FramesHelper.setClientSize(this, 800, 600);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setTitle(res_i18n.getString("CL_ELEMENTS_BOOK"));
        //this.WindowState = FormWindowState.Maximized;

        this.add(this.DBNavigator1, BorderLayout.NORTH);
        this.add(this.PageControl, BorderLayout.CENTER);

        this.DBNavigator1.setLocation(0, 0);
        this.DBNavigator1.setSize(757, 25);
        //DBNavigator1.ButtonClick += this.tbNavigatorClick;
        this.DBNavigator1.add(this.tbFirst);
        this.DBNavigator1.add(this.tbPrior);
        this.DBNavigator1.add(this.tbNext);
        this.DBNavigator1.add(this.tbLast);
        this.DBNavigator1.add(this.tbDecayView);

        Color transColor = new Color(128, 128, 0);
        
        this.tbFirst.setText("Первый");
        this.tbFirst.addActionListener(this);
        this.tbFirst.setActionCommand("btn_First");
        this.tbFirst.setIcon(CLUtils.loadIcon("ui/first.png", transColor));

        this.tbPrior.setText("Предыдущий");
        this.tbPrior.addActionListener(this);
        this.tbPrior.setActionCommand("btn_Prior");
        this.tbPrior.setIcon(CLUtils.loadIcon("ui/prior.png", transColor));

        this.tbNext.setText("Следующий");
        this.tbNext.addActionListener(this);
        this.tbNext.setActionCommand("btn_Next");
        this.tbNext.setIcon(CLUtils.loadIcon("ui/next.png", transColor));

        this.tbLast.setText("Последний");
        this.tbLast.addActionListener(this);
        this.tbLast.setActionCommand("btn_Last");
        this.tbLast.setIcon(CLUtils.loadIcon("ui/last.png", transColor));

        this.tbDecayView.setText("Диаграмма распада");
        this.tbDecayView.addActionListener(this);
        this.tbDecayView.setActionCommand("btn_DecayView");
        this.tbDecayView.setToolTipText("Диаграмма радиоактивного распада");
        this.tbDecayView.setIcon(CLUtils.loadIcon("ui/decay.png"));

        this.PageControl.setLocation(0, 0);
        this.PageControl.setSize(765, 450);
        this.PageControl.addTab("Таблицы", this.tsTables);
        this.PageControl.addTab("Информация", this.tsInfo);

        this.tsTables.setLayout(new BorderLayout());

        this.Splitter2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ListElements, PanelPT);
        this.Splitter2.setOneTouchExpandable(true);
        this.Splitter2.setDividerLocation(150);
        this.Splitter2.setResizeWeight(1.0);
        this.tsTables.add(this.Splitter2, BorderLayout.CENTER);

        //ListElements.Dock = DockStyle.Top;
        //ListElements.LabelEdit = false;
        //ListElements.FullRowSelect = true;
        //ListElements.TabIndex = 0;
        //ListElements.View = View.Details;
        ListElements.setLocation(0, 0);
        ListElements.setSize(757, 345);
        //ListElements.HideSelection = false;
        //ListElements.SelectedIndexChanged += this.ListElementsSelectItem;
        ListElements.setOnSelectionChange(this::ListElementsSelectItem);

        this.PanelPT.setLayout(new BorderLayout());
        
        this.tsInfo.setLayout(new BorderLayout());
        this.tsInfo.add(this.PageControl1, BorderLayout.CENTER);

        this.PageControl1.addTab("Элемент", this.tsElement);
        this.PageControl1.addTab("Изотопы", this.tsIsotopes);
        this.PageControl1.addTab("История", this.tsHistory);
        this.PageControl1.addTab("Структура атома", this.tsAtomStructure);
        this.PageControl1.addTab("Спектры", this.tsSpectrums);


        this.tsElement.setLayout(null);

        this.tsElement.add(this.Label13);
        this.tsElement.add(this.Label14);
        this.tsElement.add(this.Label15);
        this.tsElement.add(this.Label16);
        this.tsElement.add(this.Label17);
        this.tsElement.add(this.Label18);
        this.tsElement.add(this.Label19);
        this.tsElement.add(this.Label20);
        this.tsElement.add(this.Label21);
        this.tsElement.add(this.Label1);
        this.tsElement.add(this.Label2);
        this.tsElement.add(this.Label6);
        this.tsElement.add(this.Label24);
        this.tsElement.add(this.Label25);
        this.tsElement.add(this.Label8);
        this.tsElement.add(this.Label9);
        this.tsElement.add(this.Label22);
        this.tsElement.add(this.Label23);
        this.tsElement.add(this.Label3);
        this.tsElement.add(this.Label27);
        this.tsElement.add(this.Label28);
        this.tsElement.add(this.Label26);
        this.tsElement.add(this.Label36);
        this.tsElement.add(this.Label37);
        this.tsElement.add(this.Label38);
        this.tsElement.add(this.Label39);
        this.tsElement.add(this.Label40);
        this.tsElement.add(this.Label41);
        this.tsElement.add(this.Label42);
        this.tsElement.add(this.Label43);
        this.tsElement.add(this.Label44);
        this.tsElement.add(this.Label45);

        this.tsElement.add(this.DBEdit3);
        this.tsElement.add(this.DBEdit4);
        this.tsElement.add(this.DBEdit5);
        this.tsElement.add(this.DBEdit6);
        this.tsElement.add(this.DBEdit7);
        this.tsElement.add(this.DBEdit8);
        this.tsElement.add(this.DBEdit9);
        this.tsElement.add(this.cbClass);
        this.tsElement.add(this.DBEdit10);
        this.tsElement.add(this.DBEdit13);
        this.tsElement.add(this.DBEdit17);
        this.tsElement.add(this.DBEdit18);
        this.tsElement.add(this.DBEdit11);
        this.tsElement.add(this.DBEdit12);
        this.tsElement.add(this.DBEdit15);
        this.tsElement.add(this.DBEdit16);
        this.tsElement.add(this.DBMemo2);
        this.tsElement.add(this.DBEdit19);
        this.tsElement.add(this.DBEdit20);
        this.tsElement.add(this.DBEdit21);
        this.tsElement.add(this.DBEdit26);
        this.tsElement.add(this.DBEdit27);
        this.tsElement.add(this.DBEdit28);
        this.tsElement.add(this.DBEdit29);
        this.tsElement.add(this.DBEdit30);
        this.tsElement.add(this.DBEdit31);
        this.tsElement.add(this.DBEdit32);
        this.tsElement.add(this.DBEdit33);
        this.tsElement.add(this.DBEdit34);
        this.tsElement.add(this.DBEdit35);
        this.tsElement.add(this.cbABProperty);

        this.Label13.setText("Символ");
        this.Label13.setLocation(8, 37);
        this.Label13.setSize(41, 12);

        this.Label14.setText("Номер");
        this.Label14.setLocation(8, 0);
        this.Label14.setSize(36, 12);

        this.Label15.setText("Латинское название");
        this.Label15.setLocation(8, 111);
        this.Label15.setSize(116, 12);

        this.Label16.setText("Название");
        this.Label16.setLocation(8, 74);
        this.Label16.setSize(54, 12);

        this.Label17.setText("Атомная масса");
        this.Label17.setLocation(185, 0);
        this.Label17.setSize(81, 12);

        this.Label18.setText("Электроотрицательность");
        this.Label18.setLocation(185, 111);
        this.Label18.setSize(141, 12);

        this.Label19.setText("Атомный радиус[A]");
        this.Label19.setLocation(185, 37);
        this.Label19.setSize(107, 12);

        this.Label20.setText("Степени окисления");
        this.Label20.setLocation(362, 0);
        this.Label20.setSize(111, 12);

        this.Label21.setText("Валентности");
        this.Label21.setLocation(362, 37);
        this.Label21.setSize(70, 12);

        this.Label1.setText("Класс");
        this.Label1.setLocation(362, 111);
        this.Label1.setSize(30, 12);

        this.Label2.setText("Содержание в земной коре");
        this.Label2.setLocation(362, 74);
        this.Label2.setSize(156, 12);

        this.Label6.setText("Атомный объем[см^3/моль]");
        this.Label6.setLocation(185, 74);
        this.Label6.setSize(155, 12);

        this.Label24.setText("Сродство к электрону");
        this.Label24.setLocation(8, 148);
        this.Label24.setSize(124, 12);

        this.Label25.setText("Твердость[mohs]");
        this.Label25.setLocation(8, 185);
        this.Label25.setSize(97, 12);

        this.Label8.setText("Ковалентный радиус[A]");
        this.Label8.setLocation(8, 221);
        this.Label8.setSize(133, 12);

        this.Label9.setText("Первый потенциал ионизации");
        this.Label9.setLocation(185, 148);
        this.Label9.setSize(172, 12);

        this.Label22.setText("Второй потенциал ионизации");
        this.Label22.setLocation(185, 185);
        this.Label22.setSize(169, 12);

        this.Label23.setText("Третий потенциал ионизации");
        this.Label23.setLocation(185, 221);
        this.Label23.setSize(169, 12);

        this.Label3.setText("Описание элемента");
        this.Label3.setLocation(532, 74);
        this.Label3.setSize(112, 12);

        this.Label27.setText("Критическое давление");
        this.Label27.setLocation(362, 185);
        this.Label27.setSize(129, 12);

        this.Label28.setText("Критическая плотность");
        this.Label28.setLocation(362, 221);
        this.Label28.setSize(130, 12);

        this.Label26.setText("Критическая температура");
        this.Label26.setLocation(362, 148);
        this.Label26.setSize(147, 12);

        this.Label36.setText("Радиус Ван-дер-Ваальса");
        this.Label36.setLocation(8, 259);
        this.Label36.setSize(138, 12);

        this.Label37.setText("Точка кипения");
        this.Label37.setLocation(185, 259);
        this.Label37.setSize(86, 12);

        this.Label38.setText("Точка плавления");
        this.Label38.setLocation(362, 259);
        this.Label38.setSize(97, 12);

        this.Label39.setText("Плотность[г/см^3]");
        this.Label39.setLocation(532, 185);
        this.Label39.setSize(103, 12);

        this.Label40.setText("Теплота парообразования");
        this.Label40.setLocation(532, 221);
        this.Label40.setSize(151, 12);
        //label27.Hint = "[кДж/моль]";

        this.Label41.setText("Теплота слияния");
        this.Label41.setLocation(8, 296);
        this.Label41.setSize(94, 12);
        //label28.Hint = "[кДж/моль]";

        this.Label42.setText("Электропроводность");
        this.Label42.setLocation(185, 296);
        this.Label42.setSize(117, 12);

        this.Label43.setText("Теплопроводность");
        this.Label43.setLocation(362, 296);
        this.Label43.setSize(104, 12);

        this.Label44.setText("Уд. теплоемкость");
        this.Label44.setLocation(532, 259);
        this.Label44.setSize(97, 12);
        //label31.Hint = "[Дж*г^-1*K^-1]";

        this.Label45.setText("Характер");
        this.Label45.setLocation(532, 296);
        this.Label45.setSize(54, 12);

        JTextField dBEdit = this.DBEdit3;
        dBEdit.setEditable(false);
        dBEdit.setLocation(8, 52);
        dBEdit.setSize(162, 20);

        JTextField dBEdit2 = this.DBEdit4;
        dBEdit2.setEditable(false);
        dBEdit2.setLocation(8, 15);
        dBEdit2.setSize(162, 20);

        JTextField dBEdit3 = this.DBEdit5;
        dBEdit3.setEditable(false);
        dBEdit3.setLocation(8, 89);
        dBEdit3.setSize(162, 20);

        JTextField dBEdit4 = this.DBEdit6;
        dBEdit4.setEditable(false);
        dBEdit4.setLocation(8, 125);
        dBEdit4.setSize(162, 20);

        JTextField dBEdit5 = this.DBEdit7;
        dBEdit5.setEditable(false);
        dBEdit5.setLocation(185, 15);
        dBEdit5.setSize(162, 20);

        JTextField dBEdit6 = this.DBEdit8;
        dBEdit6.setEditable(false);
        dBEdit6.setLocation(185, 125);
        dBEdit6.setSize(162, 20);

        JTextField dBEdit7 = this.DBEdit9;
        dBEdit7.setEditable(false);
        dBEdit7.setLocation(185, 52);
        dBEdit7.setSize(162, 20);

        JTextField tEdit = this.cbClass;
        tEdit.setEditable(false);
        tEdit.setLocation(362, 125);
        tEdit.setSize(162, 20);

        JTextField dBEdit8 = this.DBEdit10;
        dBEdit8.setEditable(false);
        dBEdit8.setLocation(362, 89);
        dBEdit8.setSize(162, 20);

        JTextField dBEdit9 = this.DBEdit13;
        dBEdit9.setEditable(false);
        dBEdit9.setLocation(185, 89);
        dBEdit9.setSize(162, 20);

        JTextField dBEdit10 = this.DBEdit17;
        dBEdit10.setEditable(false);
        dBEdit10.setLocation(8, 163);
        dBEdit10.setSize(162, 20);
        dBEdit10.setToolTipText("[кДж/моль]");

        JTextField dBEdit11 = this.DBEdit18;
        dBEdit11.setEditable(false);
        dBEdit11.setLocation(8, 200);
        dBEdit11.setSize(162, 20);

        this.DBEdit11.setEditable(false);
        this.DBEdit11.setLocation(8, 236);
        this.DBEdit11.setSize(162, 20);

        JTextField dBEdit13 = this.DBEdit12;
        dBEdit13.setEditable(false);
        dBEdit13.setLocation(185, 163);
        dBEdit13.setSize(162, 20);
        dBEdit13.setToolTipText("[В]");

        JTextField dBEdit14 = this.DBEdit15;
        dBEdit14.setEditable(false);
        dBEdit14.setLocation(185, 200);
        dBEdit14.setSize(162, 20);
        dBEdit14.setToolTipText("[В]");

        JTextField dBEdit15 = this.DBEdit16;
        dBEdit15.setEditable(false);
        dBEdit15.setLocation(185, 236);
        dBEdit15.setSize(162, 20);
        dBEdit15.setToolTipText("[В]");

        JTextArea dBMemo = this.DBMemo2;
        dBMemo.setEditable(false);
        dBMemo.setLocation(532, 89);
        dBMemo.setSize(163, 96);
        dBMemo.setEditable(false);
        dBMemo.setLineWrap(true);

        JTextField dBEdit16 = this.DBEdit19;
        dBEdit16.setEditable(false);
        dBEdit16.setLocation(362, 163);
        dBEdit16.setSize(162, 20);

        JTextField dBEdit17 = this.DBEdit20;
        dBEdit17.setEditable(false);
        dBEdit17.setLocation(362, 200);
        dBEdit17.setSize(162, 20);

        JTextField dBEdit18 = this.DBEdit21;
        dBEdit18.setEditable(false);
        dBEdit18.setLocation(362, 236);
        dBEdit18.setSize(162, 20);

        JTextField dBEdit19 = this.DBEdit26;
        dBEdit19.setEditable(false);
        dBEdit19.setLocation(8, 273);
        dBEdit19.setSize(162, 20);

        this.DBEdit27.setEditable(false);
        this.DBEdit27.setLocation(185, 273);
        this.DBEdit27.setSize(162, 20);

        this.DBEdit28.setEditable(false);
        this.DBEdit28.setLocation(362, 273);
        this.DBEdit28.setSize(162, 20);

        this.DBEdit29.setEditable(false);
        this.DBEdit29.setLocation(532, 200);
        this.DBEdit29.setSize(163, 20);

        this.DBEdit30.setEditable(false);
        this.DBEdit30.setLocation(532, 236);
        this.DBEdit30.setSize(163, 20);

        this.DBEdit31.setEditable(false);
        this.DBEdit31.setLocation(8, 311);
        this.DBEdit31.setSize(162, 20);

        this.DBEdit32.setEditable(false);
        this.DBEdit32.setLocation(185, 311);
        this.DBEdit32.setSize(162, 20);

        this.DBEdit33.setEditable(false);
        this.DBEdit33.setLocation(362, 311);
        this.DBEdit33.setSize(162, 20);

        this.DBEdit34.setEditable(false);
        this.DBEdit34.setLocation(532, 273);
        this.DBEdit34.setSize(163, 20);

        this.DBEdit35.setLocation(185, 657);
        this.DBEdit35.setSize(132, 20);

        this.cbABProperty.setEditable(false);
        this.cbABProperty.setLocation(532, 311);
        this.cbABProperty.setSize(163, 20);


        tsHistory.setLayout(null);

        tsHistory.add(this.Label10);
        tsHistory.add(this.Label11);
        tsHistory.add(this.Label12);
        tsHistory.add(this.Label4);
        tsHistory.add(this.Label5);
        tsHistory.add(this.Label7);
        tsHistory.add(this.Label29);
        tsHistory.add(this.Label30);
        tsHistory.add(this.Label31);
        tsHistory.add(this.Label32);
        tsHistory.add(this.Label33);

        tsHistory.add(this.DBEdit1);
        tsHistory.add(this.DBEdit2);
        tsHistory.add(this.DBMemo1);
        tsHistory.add(this.DBMemo3);
        tsHistory.add(this.DBMemo4);
        tsHistory.add(this.DBEdit14);
        tsHistory.add(this.Edit1);
        tsHistory.add(this.Edit2);
        tsHistory.add(this.Edit3);
        tsHistory.add(this.Edit4);
        tsHistory.add(this.Edit5);

        this.Label10.setText("Общепринятая дата открытия");
        this.Label10.setLocation(8, 37);
        this.Label10.setSize(170, 12);

        this.Label11.setText("Исследователи");
        this.Label11.setLocation(8, 59);
        this.Label11.setSize(82, 12);

        this.Label12.setText("Особенности открытия");
        this.Label12.setLocation(8, 81);
        this.Label12.setSize(129, 12);

        this.Label4.setText("Источники");
        this.Label4.setLocation(8, 125);
        this.Label4.setSize(59, 12);

        this.Label5.setText("Применение");
        this.Label5.setLocation(8, 170);
        this.Label5.setSize(71, 12);

        this.Label7.setText("Оригинальное название");
        this.Label7.setLocation(8, 15);
        this.Label7.setSize(139, 12);

        this.Label29.setText("Английский");
        this.Label29.setLocation(8, 221);
        this.Label29.setSize(65, 12);

        this.Label30.setText("Немецкий");
        this.Label30.setLocation(8, 244);
        this.Label30.setSize(57, 12);

        this.Label31.setText("Французский");
        this.Label31.setLocation(8, 266);
        this.Label31.setSize(74, 12);

        this.Label32.setText("Шведский");
        this.Label32.setLocation(8, 288);
        this.Label32.setSize(56, 12);

        this.Label33.setText("Норвежский");
        this.Label33.setLocation(8, 311);
        this.Label33.setSize(68, 12);

        this.DBEdit1.setEditable(false);
        this.DBEdit1.setLocation(192, 29);
        this.DBEdit1.setSize(503, 20);

        this.DBEdit2.setEditable(false);
        this.DBEdit2.setLocation(192, 52);
        this.DBEdit2.setSize(503, 20);

        this.DBMemo1.setBorder(BorderFactory.createEtchedBorder());
        this.DBMemo1.setEditable(false);
        this.DBMemo1.setLocation(192, 74);
        this.DBMemo1.setSize(503, 45);
        this.DBMemo1.setLineWrap(true);

        this.DBMemo3.setBorder(BorderFactory.createEtchedBorder());
        this.DBMemo3.setEditable(false);
        this.DBMemo3.setLocation(192, 119);
        this.DBMemo3.setSize(503, 45);
        this.DBMemo3.setLineWrap(true);

        this.DBMemo4.setBorder(BorderFactory.createEtchedBorder());
        this.DBMemo4.setEditable(false);
        this.DBMemo4.setLocation(192, 163);
        this.DBMemo4.setSize(503, 45);
        this.DBMemo4.setLineWrap(true);

        this.DBEdit14.setEditable(false);
        this.DBEdit14.setLocation(192, 8);
        this.DBEdit14.setSize(503, 20);

        this.Edit1.setEditable(false);
        this.Edit1.setLocation(192, 215);
        this.Edit1.setSize(503, 20);

        this.Edit2.setEditable(false);
        this.Edit2.setLocation(192, 236);
        this.Edit2.setSize(503, 20);

        this.Edit3.setEditable(false);
        this.Edit3.setLocation(192, 259);
        this.Edit3.setSize(503, 20);

        this.Edit4.setEditable(false);
        this.Edit4.setLocation(192, 281);
        this.Edit4.setSize(503, 20);

        this.Edit5.setEditable(false);
        this.Edit5.setLocation(192, 303);
        this.Edit5.setSize(503, 20);

        ///
        
        gbAtomStructure.setLayout(null);
        gbAtomStructure.setBorder(BorderFactory.createTitledBorder("Структура атома"));
        //gbAtomStructure.setLocation(0, 0);
        gbAtomStructure.setMinimumSize(new Dimension(281, 371));
        gbAtomStructure.setPreferredSize(new Dimension(281, 371));
        gbAtomStructure.add(this.mStructure);
        gbAtomStructure.add(this.gbCrystalView);

        gbAtomView.setLayout(new BorderLayout());
        gbAtomView.setBorder(BorderFactory.createTitledBorder("Вид атома"));
        //gbAtomView.setLocation(281, 0);
        gbAtomView.setSize(468, 371);

        tsAtomStructure.setLayout(new BorderLayout());
        tsAtomStructure.add(this.gbAtomStructure, BorderLayout.WEST);
        tsAtomStructure.add(this.gbAtomView, BorderLayout.CENTER);

        mStructure.setEditable(false);
        mStructure.setLocation(8, 170);
        mStructure.setSize(267, 53);
        mStructure.setLineWrap(true);

        gbCrystalView.setLayout(null);
        gbCrystalView.setBorder(BorderFactory.createTitledBorder("Кристаллическая структура"));
        gbCrystalView.setLocation(8, 229);
        gbCrystalView.setSize(267, 60);
        gbCrystalView.add(this.cbCrystalStructure);

        cbCrystalStructure.setEditable(false);
        cbCrystalStructure.setLocation(52, 23);
        cbCrystalStructure.setSize(208, 20);

        tsIsotopes.setLayout(new BorderLayout());
        this.Splitter4 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ListNuclides, ListDecay);
        this.Splitter4.setOneTouchExpandable(true);
        this.Splitter4.setDividerLocation(300);
        this.Splitter4.setResizeWeight(1.0);
        tsIsotopes.add(this.Splitter4, BorderLayout.CENTER);
        
        //ListNuclides.LabelEdit = false;
        //ListNuclides.FullRowSelect = true;
        //ListNuclides.View = View.Details;
        ListNuclides.setLocation(0, 0);
        ListNuclides.setSize(467, 350);
        //ListNuclides.Dock = DockStyle.Left;
        ListNuclides.addColumn("Изотоп", 69);
        ListNuclides.addColumn("Избыток", 92);
        ListNuclides.addColumn("Вес", 92);
        ListNuclides.addColumn("Спин", 92);
        ListNuclides.addColumn("Период полураспада", 92);
        ListNuclides.setOnSelectionChange(this::ListNuclidesSelectItem);

        //ListDecay.LabelEdit = false;
        //ListDecay.FullRowSelect = true;
        //ListDecay.TabIndex = 1;
        //ListDecay.View = View.Details;
        ListDecay.setLocation(470, 0);
        ListDecay.setSize(279, 350);
        //ListDecay.Dock = DockStyle.Fill;
        ListDecay.addColumn("Тип распада", 92);
        ListDecay.addColumn("Нуклид", 92);

        this.tsSpectrums.setLayout(null);
        this.tsSpectrums.add(this.GroupBox4);
        this.tsSpectrums.add(this.GroupBox5);

        GroupBox4.setLayout(new BorderLayout());
        GroupBox4.setBorder(BorderFactory.createTitledBorder("Спектр поглощения"));
        GroupBox4.setLocation(8, 8);
        GroupBox4.setSize(680, 111);

        GroupBox5.setLayout(new BorderLayout());
        GroupBox5.setBorder(BorderFactory.createTitledBorder("Спектр эмиссии"));
        GroupBox5.setLocation(8, 133);
        GroupBox5.setSize(680, 112);
    }
}
