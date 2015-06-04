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

import bslib.common.FramesHelper;
import bslib.components.ComboItem;
import chemlab.core.chemical.StoicParams;
import chemlab.core.chemical.Substance;
import chemlab.core.measure.MeasureBox;
import chemlab.core.measure.ChemUnits;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;
import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.Unit;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class CLChemCalc extends JDialog
{
    public enum GasOutputCalc
    {
        Pressure, Volume, Temperature, Density
    }
    
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private Substance fSubstance;
    private StoicParams fParams;
    private boolean fIsInputSubst;
    private GasOutputCalc GasOutput;

    private final JPanel panHeader;
    private final JPanel panMain;
    private final JPanel panFooter;

    private final JComboBox cmbPhase;
    private final JComboBox cmbUnits;
    
    private final JPanel panSolid;
    private final JPanel panLiquid;
    private final JPanel panGas;

    private final JRadioButton rbSolidByMass;
    private final JRadioButton rbSolidByVD;
    private final MeasureBox txtMass;
    private final MeasureBox txtVolume;
    private final MeasureBox txtDensity;
    
    private final JRadioButton rbLiquidByM;
    private final MeasureBox txtLiqVolume;
    private final JTextField txtConcM;
    private final JRadioButton rbLiquidByMP;
    private final JTextField txtMP;
    private final MeasureBox txtLiqDensity;

    private final MeasureBox txtGasPressure;
    private final MeasureBox txtGasVolume;
    private final MeasureBox txtGasTemperature;
    private final MeasureBox txtGasDensity;
    private final JCheckBox chkSTP;
    
    private final JRadioButton rbSelPressure;
    private final JRadioButton rbSelVolume;
    private final JRadioButton rbSelTemp;
    private final JRadioButton rbSelDensity;
    
    public CLChemCalc(Frame owner)
    {
        super(owner, true);

        //this.fSubstance = substance;
        //this.fIsInputSubst = start;
        
        //this.fParams = substance.getStoicParams();
        //this.fParams.Type = (start) ? StoicParams.ParamType.Input : StoicParams.ParamType.Output;
        
        this.setLayout(new BorderLayout());

        FramesHelper.setClientSize(this, 540, 380);
        
        this.setLocationRelativeTo(owner);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setTitle("Input");

        KeyAdapter numKeys = new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent ke)
            {
                char z = ke.getKeyChar();
                boolean burn = !Character.isDigit(z) && !(z == 8)
                        && !(z == 46) && !(z < 32) && !(z == 127);//burn if its not a diget and its not a backspace
                /*if (ke.getComponent().getName().contains(".") && z == 46) {
                    burn = true;
                }*/
                if (burn) {
                    ke.consume();
                }
            }
        };

        panHeader = new JPanel();
        panHeader.setLayout(new GridLayout(1, 4, 5, 5));
        panHeader.setBorder(BorderFactory.createEtchedBorder());
        panHeader.setLocation(10, 40);
        panHeader.setPreferredSize(new Dimension(200, 30));
        this.add(panHeader, BorderLayout.NORTH);
        
        JLabel lblPhase = new JLabel();
        lblPhase.setText("Фаза");
        lblPhase.setLocation(10, 10);
        lblPhase.setSize(100, 25);
        panHeader.add(lblPhase);
        
        cmbPhase = new JComboBox();
        cmbPhase.addItem("gas");
        cmbPhase.addItem("liquid");
        cmbPhase.addItem("solid");
        cmbPhase.setSelectedItem(null);
        cmbPhase.setLocation(120, 10);
        cmbPhase.setSize(100, 25);
        panHeader.add(cmbPhase);
        
        JLabel lblUnits = new JLabel();
        lblUnits.setText("Ед. изм.");
        lblUnits.setLocation(10, 35);
        lblUnits.setSize(100, 25);
        panHeader.add(lblUnits);
        
        cmbUnits = new JComboBox();
        List<Unit<?>> units = ChemUnits.getInstance().getUnits();
        for (Unit<?> un : units) {
            if (un.isCompatible(ChemUnits.GRAM) || un.isCompatible(ChemUnits.LITER)) {
                cmbUnits.addItem(new ComboItem(un.toString(), un));
            }
        }        
        cmbUnits.setSelectedItem(null);
        cmbUnits.setLocation(120, 35);
        cmbUnits.setSize(100, 25);
        panHeader.add(cmbUnits);

        CardLayout cardLayout = new CardLayout();
        Dimension dim = new Dimension(100, 25);
        
        panMain = new JPanel();
        panMain.setLayout(cardLayout);
        panMain.setBorder(BorderFactory.createEtchedBorder());
        this.add(panMain, BorderLayout.CENTER);

        // solid page
        
        panSolid = new JPanel(new BorderLayout());
        panSolid.setBorder(BorderFactory.createEtchedBorder());
        panSolid.setLocation(10, 40);
        panSolid.setSize(280, 200);
        panMain.add("solid", panSolid);
        
        JPanel panSolidContent = new JPanel(new GridLayout(5, 2, 4, 4));
        panSolid.add(panSolidContent, BorderLayout.NORTH);
        
        rbSolidByMass = new JRadioButton("по массе");
        panSolidContent.add(rbSolidByMass);
        
        panSolidContent.add(new JLabel(" "));
        
        JLabel lblMass = new JLabel();
        lblMass.setText("Масса, г");
        panSolidContent.add(lblMass);
        
        txtMass = new MeasureBox("", ChemUnits.KILOGRAM);
        txtMass.addKeyListener(numKeys);
        panSolidContent.add(txtMass);
        
        rbSolidByVD = new JRadioButton("по объему и плотности");
        panSolidContent.add(rbSolidByVD);
        
        panSolidContent.add(new JLabel(" "));
        
        JLabel lblVolume = new JLabel();
        lblVolume.setText("Объем, л");
        panSolidContent.add(lblVolume);
        
        txtVolume = new MeasureBox("", ChemUnits.CUBIC_METRE);
        txtVolume.addKeyListener(numKeys);
        panSolidContent.add(txtVolume);
        
        JLabel lblDensity = new JLabel();
        lblDensity.setText("Плотность, г/л");
        panSolidContent.add(lblDensity);
        
        txtDensity = new MeasureBox("", ChemUnits.KG_M3);
        txtDensity.addKeyListener(numKeys);
        panSolidContent.add(txtDensity);
        
        ButtonGroup group = new ButtonGroup();
        group.add(rbSolidByMass);
        group.add(rbSolidByVD);

        /// liquid page

        panLiquid = new JPanel(new BorderLayout());
        panLiquid.setBorder(BorderFactory.createEtchedBorder());
        panLiquid.setLocation(10, 40);
        panLiquid.setSize(280, 200);
        panMain.add("liquid", panLiquid);
        
        JPanel panLiquidContent = new JPanel(new GridLayout(4, 2, 4, 4));
        panLiquid.add(panLiquidContent, BorderLayout.NORTH);
        
        JLabel lblLiqVolume = new JLabel();
        lblLiqVolume.setText("Объем, л");
        lblLiqVolume.setMinimumSize(dim);
        lblLiqVolume.setPreferredSize(dim);
        panLiquidContent.add(lblLiqVolume);
        
        txtLiqVolume = new MeasureBox("", ChemUnits.CUBIC_METRE);
        txtLiqVolume.addKeyListener(numKeys);
        panLiquidContent.add(txtLiqVolume);
        
        rbLiquidByM = new JRadioButton("Концентрация, М");
        rbLiquidByM.setLocation(10, 10);
        rbLiquidByM.setSize(100, 25);
        panLiquidContent.add(rbLiquidByM);
        
        txtConcM = new JTextField();
        txtConcM.addKeyListener(numKeys);
        panLiquidContent.add(txtConcM);
        
        rbLiquidByMP = new JRadioButton("% по массе");
        rbLiquidByMP.setLocation(10, 10);
        rbLiquidByMP.setSize(100, 25);
        panLiquidContent.add(rbLiquidByMP);
        
        txtMP = new JTextField();
        txtMP.addKeyListener(numKeys);
        panLiquidContent.add(txtMP);
        
        JLabel lblLiqDensity = new JLabel();
        lblLiqDensity.setText("Плотность, г/л");
        panLiquidContent.add(lblLiqDensity);
        
        txtLiqDensity = new MeasureBox("", ChemUnits.KG_M3);
        txtLiqDensity.addKeyListener(numKeys);
        panLiquidContent.add(txtLiqDensity);
        
        group = new ButtonGroup();
        group.add(rbLiquidByM);
        group.add(rbLiquidByMP);

        /// gas page

        panGas = new JPanel();
        panGas.setLayout(new BorderLayout());
        panGas.setBorder(BorderFactory.createEtchedBorder());
        panGas.setLocation(10, 40);
        panGas.setSize(280, 200);
        panMain.add("gas", panGas);
        
        JPanel panGasContent = new JPanel(new GridLayout(9, 2, 4, 4));
        panGas.add(panGasContent, BorderLayout.NORTH);
        
        JLabel lblGasPressure = new JLabel();
        lblGasPressure.setText("Давление");
        lblGasPressure.setLocation(10, 10);
        lblGasPressure.setSize(100, 25);
        panGasContent.add(lblGasPressure);
        
        txtGasPressure = new MeasureBox("", ChemUnits.PASCAL);
        txtGasPressure.addKeyListener(numKeys);
        panGasContent.add(txtGasPressure);
        
        JLabel lblGasVolume = new JLabel();
        lblGasVolume.setText("Объем");
        lblGasVolume.setLocation(10, 35);
        lblGasVolume.setSize(100, 25);
        panGasContent.add(lblGasVolume);
        
        txtGasVolume = new MeasureBox("", ChemUnits.CUBIC_METRE);
        txtGasVolume.addKeyListener(numKeys);
        panGasContent.add(txtGasVolume);
        
        JLabel lblGasTemperature = new JLabel();
        lblGasTemperature.setText("Температура");
        lblGasTemperature.setLocation(10, 60);
        lblGasTemperature.setSize(100, 25);
        panGasContent.add(lblGasTemperature);
        
        txtGasTemperature = new MeasureBox("", ChemUnits.CELSIUS);
        txtGasTemperature.addKeyListener(numKeys);
        panGasContent.add(txtGasTemperature);
        
        JLabel lblGasDensity = new JLabel();
        lblGasDensity.setText("Плотность, г/л");
        lblGasDensity.setLocation(10, 85);
        lblGasDensity.setSize(100, 25);
        panGasContent.add(lblGasDensity);
        
        txtGasDensity = new MeasureBox("", ChemUnits.KG_M3);
        txtGasDensity.addKeyListener(numKeys);
        panGasContent.add(txtGasDensity);
        
        chkSTP = new JCheckBox();
        chkSTP.setText("STP");
        chkSTP.setLocation(10, 110);
        chkSTP.setSize(100, 25);
        panGasContent.add(chkSTP);

        panGasContent.add(new JLabel(" "));
        
        ///
        
        rbSelPressure = new JRadioButton();
        rbSelPressure.setText("Расчет давления");
        rbSelPressure.setLocation(10, 135);
        rbSelPressure.setSize(100, 25);
        panGasContent.add(rbSelPressure);

        panGasContent.add(new JLabel(" "));
        
        rbSelVolume = new JRadioButton();
        rbSelVolume.setText("Расчет объема");
        rbSelVolume.setLocation(10, 160);
        rbSelVolume.setSize(100, 25);
        panGasContent.add(rbSelVolume);

        panGasContent.add(new JLabel(" "));
        
        rbSelTemp = new JRadioButton();
        rbSelTemp.setText("Расчет температуры");
        rbSelTemp.setLocation(10, 185);
        rbSelTemp.setSize(100, 25);
        panGasContent.add(rbSelTemp);

        panGasContent.add(new JLabel(" "));
        
        rbSelDensity = new JRadioButton();
        rbSelDensity.setText("Расчет плотности");
        rbSelDensity.setLocation(10, 210);
        rbSelDensity.setSize(100, 25);
        panGasContent.add(rbSelDensity);

        panGasContent.add(new JLabel(" "));
        
        group = new ButtonGroup();
        group.add(rbSelPressure);
        group.add(rbSelVolume);
        group.add(rbSelTemp);
        group.add(rbSelDensity);
        
        /// footer

        panFooter = new JPanel();
        panFooter.setBorder(null);
        panFooter.setPreferredSize(new Dimension(200, 40));
        this.add(panFooter, BorderLayout.SOUTH);

        JButton btnDone = new JButton();
        btnDone.setText("Расчет");
        btnDone.setPreferredSize(new Dimension(100, 30));
        panFooter.add(btnDone);

        ///
        
        cmbUnits.addActionListener((ActionEvent e) -> {
            Object item = cmbUnits.getSelectedItem();
            Unit<?> unit = (item == null) ? null : (Unit<?>) ((ComboItem) item).Data;
            this.fParams.ResultUnit = unit;
        });

        ///
        
        cmbPhase.addActionListener((ActionEvent e) -> {
            //this.fSubstance.State = StoichiometricSolver.getState((String) cmbPhase.getSelectedItem());

            cardLayout.show(panMain, (String) cmbPhase.getSelectedItem());
            updateControls();
            
            /*switch (this.fSubstance.State) {
                case Solid:
                    //units.setEnabled(true);
                    break;

                case Liquid:
                    //units.setEnabled(true);
                    break;

                case Gas:
                    //units.setEnabled(false);
                    break;
            }*/
        });

        btnDone.addActionListener((ActionEvent e) -> {
            double result;

            try {
                StoicParams.InputMode mode = this.fParams.Mode;
                
                switch (mode) {
                    case imSolid_M: // only in
                        this.fParams.Mass = (Measure<Double, Mass>) txtMass.getMeasure();
                        break;
                    
                    case imSolid_V_D:
                        this.fParams.Density = (Measure<Double, VolumetricDensity>) txtDensity.getMeasure();
                        /*if (this.fIsInputSubst) {
                            this.fParams.Volume = (Measure<Double, Volume>) txtVolume.getMeasure();
                        } else {
                        }*/
                        break;

                    case imLiquid_M: // in/out
                        this.fParams.Volume = (Measure<Double, Volume>) txtLiqVolume.getMeasure();
                        this.fParams.ConcM = Double.parseDouble(txtConcM.getText());
                        break;
                        
                    case imLiquid_MP_D: // only in
                        this.fParams.Volume = (Measure<Double, Volume>) txtLiqVolume.getMeasure();
                        this.fParams.MassPercent = Double.parseDouble(txtMP.getText());
                        this.fParams.Density = (Measure<Double, VolumetricDensity>) txtLiqDensity.getMeasure();
                        break;

                    case imGas_In: // only in
                        this.fParams.isSTP = chkSTP.isSelected();
                        
                        if (!chkSTP.isSelected()) {
                            this.fParams.Pressure = (Measure<Double, Pressure>) txtGasPressure.getMeasure();
                            this.fParams.Volume = (Measure<Double, Volume>) txtGasVolume.getMeasure();
                            this.fParams.Temperature = (Measure<Double, Temperature>) txtGasTemperature.getMeasure();
                        } else {
                            this.fParams.Volume = (Measure<Double, Volume>) txtGasVolume.getMeasure();
                        }
                        break;

                    case imGas_Out: // only out
                        double v;
                        switch (this.GasOutput) {
                            case Pressure:
                                this.fParams.Volume = (Measure<Double, Volume>) txtGasVolume.getMeasure();
                                this.fParams.Temperature = (Measure<Double, Temperature>) txtGasTemperature.getMeasure();
                                break;

                            case Volume:
                                this.fParams.Pressure = (Measure<Double, Pressure>) txtGasPressure.getMeasure();
                                this.fParams.Temperature = (Measure<Double, Temperature>) txtGasTemperature.getMeasure();
                                break;

                            case Temperature:
                                this.fParams.Pressure = (Measure<Double, Pressure>) txtGasPressure.getMeasure();
                                this.fParams.Volume = (Measure<Double, Volume>) txtGasVolume.getMeasure();
                                break;

                            case Density:
                                this.fParams.Pressure = (Measure<Double, Pressure>) txtGasPressure.getMeasure();
                                this.fParams.Temperature = (Measure<Double, Temperature>) txtGasTemperature.getMeasure();
                                break;
                        }
                        break;
                }

                setVisible(false);
            } catch (NumberFormatException ex) {
                CommonUtils.showError(this, "Необходимо правильно заполнить поля");
            }
        });
    }
    
    private void updateControls()
    {
        if (this.fIsInputSubst) {
            switch (this.fSubstance.getState()) {
                case Solid:
                    rbSolidByMass.setEnabled(true);
                    rbSolidByVD.setEnabled(true);
                    txtMass.setEnabled(true);
                    txtVolume.setEnabled(true);
                    txtDensity.setEnabled(true);

                    rbSolidByMass.addActionListener((ActionEvent ae) -> {
                        boolean isSel = rbSolidByMass.isSelected();
                        txtMass.setEnabled(isSel);
                        txtVolume.setEnabled(!isSel);
                        txtDensity.setEnabled(!isSel);
                        
                        this.fParams.Mode = StoicParams.InputMode.imSolid_M;
                    });

                    rbSolidByVD.addActionListener((ActionEvent ae) -> {
                        boolean isSel = rbSolidByVD.isSelected();
                        txtMass.setEnabled(!isSel);
                        txtVolume.setEnabled(isSel);
                        txtDensity.setEnabled(isSel);
                        
                        this.fParams.Mode = StoicParams.InputMode.imSolid_V_D;
                    });
                    break;

                case Liquid:
                    rbLiquidByM.setEnabled(true);
                    txtConcM.setEnabled(true);
                    rbLiquidByMP.setEnabled(true);
                    txtMP.setEnabled(true);
                    txtLiqDensity.setEnabled(true);

                    rbLiquidByM.addActionListener((ActionEvent ae) -> {
                        boolean isSel = rbLiquidByM.isSelected();
                        txtConcM.setEnabled(isSel);
                        txtMP.setEnabled(!isSel);
                        txtLiqDensity.setEnabled(!isSel);
                        
                        this.fParams.Mode = StoicParams.InputMode.imLiquid_M;
                    });

                    rbLiquidByMP.addActionListener((ActionEvent ae) -> {
                        boolean isSel = rbLiquidByMP.isSelected();
                        txtConcM.setEnabled(!isSel);
                        txtMP.setEnabled(isSel);
                        txtLiqDensity.setEnabled(isSel);
                        
                        this.fParams.Mode = StoicParams.InputMode.imLiquid_MP_D;
                    });
                    break;

                case Gas:
                    txtGasPressure.setEnabled(true);
                    txtGasVolume.setEnabled(true);
                    txtGasTemperature.setEnabled(true);
                    txtGasDensity.setEnabled(false);
                    chkSTP.setEnabled(true);
                    
                    rbSelPressure.setEnabled(false);
                    rbSelVolume.setEnabled(false);
                    rbSelTemp.setEnabled(false);
                    rbSelDensity.setEnabled(false);
                    
                    chkSTP.addActionListener((ActionEvent e) -> {
                        boolean isSel = chkSTP.isSelected();
                        
                        this.fParams.isSTP = isSel;

                        txtGasPressure.setEnabled(!isSel);
                        txtGasTemperature.setEnabled(!isSel);
                    });
                    
                    this.fParams.Mode = StoicParams.InputMode.imGas_In;
                    break;
            }
        } else {
            switch (this.fSubstance.getState()) {
                case Solid:
                    rbSolidByMass.setEnabled(false);
                    rbSolidByVD.setEnabled(false);
                    txtMass.setEnabled(false);
                    txtVolume.setEnabled(false);
                    txtDensity.setEnabled(true); // input
                    
                    this.fParams.Mode = StoicParams.InputMode.imSolid_V_D;
                    break;

                case Liquid:
                    rbLiquidByM.setEnabled(false);
                    txtConcM.setEnabled(true); // input
                    rbLiquidByMP.setEnabled(false);
                    txtMP.setEnabled(false);
                    txtLiqDensity.setEnabled(false);
                    
                    this.fParams.Mode = StoicParams.InputMode.imLiquid_M;
                    break;

                case Gas:
                    txtGasDensity.setEnabled(true);
                    
                    chkSTP.setEnabled(false);
                    
                    rbSelPressure.setEnabled(true);
                    rbSelVolume.setEnabled(true);
                    rbSelTemp.setEnabled(true);
                    rbSelDensity.setEnabled(true);
                    
                    this.fParams.Mode = StoicParams.InputMode.imGas_Out;
                    
                    rbSelPressure.addActionListener((ActionEvent ae) -> {
                        if (rbSelPressure.isSelected()) {
                            txtGasPressure.setEnabled(false);
                            txtGasVolume.setEnabled(true);
                            txtGasTemperature.setEnabled(true);
                            
                            this.GasOutput = GasOutputCalc.Pressure;
                        } else if (rbSelVolume.isSelected()) {
                            txtGasPressure.setEnabled(true);
                            txtGasVolume.setEnabled(false);
                            txtGasTemperature.setEnabled(true);
                            
                            this.GasOutput = GasOutputCalc.Volume;
                        } else if (rbSelTemp.isSelected()) {
                            txtGasPressure.setEnabled(true);
                            txtGasVolume.setEnabled(true);
                            txtGasTemperature.setEnabled(false);
                            
                            this.GasOutput = GasOutputCalc.Temperature;
                        } else if (rbSelDensity.isSelected()) {
                            txtGasPressure.setEnabled(true);
                            txtGasVolume.setEnabled(false);
                            txtGasTemperature.setEnabled(true);
                            
                            this.GasOutput = GasOutputCalc.Density;
                        }
                    });
                    rbSelVolume.addActionListener(rbSelPressure.getActionListeners()[0]);
                    rbSelTemp.addActionListener(rbSelPressure.getActionListeners()[0]);
                    rbSelDensity.addActionListener(rbSelPressure.getActionListeners()[0]);
                    break;
            }
        }
    }
}
