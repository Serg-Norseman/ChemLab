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
import chemlab.core.chemical.ChemUnits;
import chemlab.core.chemical.StoicParams;
import chemlab.core.chemical.StoichiometricSolver;
import chemlab.core.chemical.Substance;
import chemlab.core.controls.MeasureBox;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class CLSubstanceInput extends JDialog
{
    private final Substance fSubstance;
    private StoicParams fParams;
    private final boolean fIsInputSubst;

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
        
    public CLSubstanceInput(Frame owner, Substance substance)
    {
        super(owner, true);

        this.fSubstance = substance;
        this.fParams = substance.getStoicParams();
        this.fIsInputSubst = (this.fParams.Type == StoicParams.ParamType.Input);
        
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
                        && !(z == 46) && !(z < 32) && !(z == 127);
                if (burn) {
                    ke.consume();
                }
            }
        };

        panHeader = new JPanel();
        panHeader.setLayout(new GridLayout(2, 2, 5, 5));
        panHeader.setBorder(BorderFactory.createEtchedBorder());
        this.add(panHeader, BorderLayout.NORTH);
        
        JLabel lblPhase = new JLabel();
        lblPhase.setText("Состояние");
        panHeader.add(lblPhase);
        
        cmbPhase = new JComboBox();
        cmbPhase.addItem("solid");
        cmbPhase.addItem("liquid");
        cmbPhase.addItem("gas");
        panHeader.add(cmbPhase);
        
        JLabel lblUnits = new JLabel();
        lblUnits.setText("Ед. изм. результата");
        panHeader.add(lblUnits);
        
        cmbUnits = new JComboBox();
        List<Unit<?>> units = ChemUnits.getInstance().getUnits();
        for (Unit<?> un : units) {
            if (un.isCompatible(ChemUnits.GRAM) || un.isCompatible(ChemUnits.LITER)) {
                cmbUnits.addItem(new ComboItem(un.toString(), un));
            }
        }        
        cmbUnits.setSelectedItem(null);
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
        
        /// footer

        panFooter = new JPanel();
        panFooter.setBorder(null);
        panFooter.setPreferredSize(new Dimension(200, 40));
        this.add(panFooter, BorderLayout.SOUTH);

        JButton btnDone = new JButton();
        btnDone.setText("Принять");
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
            this.fSubstance.State = StoichiometricSolver.getState((String) cmbPhase.getSelectedItem());

            cardLayout.show(panMain, (String) cmbPhase.getSelectedItem());
            updateControls();
            
            switch (this.fSubstance.State) {
                case Solid:
                    cmbUnits.setEnabled(true);
                    break;

                case Liquid:
                    cmbUnits.setEnabled(true);
                    break;

                case Gas:
                    cmbUnits.setEnabled(false);
                    this.fParams.ResultUnit = ChemUnits.LITER;
                    break;
            }
        });

        btnDone.addActionListener((ActionEvent e) -> {
            try {
                StoicParams.InputMode mode = this.fParams.Mode;
                
                switch (mode) {
                    case imSolid_M: // only in
                        this.fParams.Mass = (Measure<Double, Mass>) txtMass.getMeasure();
                        break;
                    
                    case imSolid_V_D:
                        this.fParams.Density = (Measure<Double, VolumetricDensity>) txtDensity.getMeasure();
                        if (this.fIsInputSubst) {
                            this.fParams.Volume = (Measure<Double, Volume>) txtVolume.getMeasure();
                        } else {
                        }
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
                        this.fParams.isSTP = chkSTP.isSelected();
                        
                        this.fParams.Pressure = (Measure<Double, Pressure>) txtGasPressure.getMeasure();
                        this.fParams.Temperature = (Measure<Double, Temperature>) txtGasTemperature.getMeasure();
                        break;
                }

                setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Необходимо правильно заполнить поля");
            }
        });
        
        this.setParams();
    }
    
    private void setParams()
    {
        cmbPhase.setSelectedIndex(this.fSubstance.State.getValue());
        
        for (int i = 0; i < cmbUnits.getItemCount(); i++) {
            Object item = cmbUnits.getItemAt(i);
            Unit<?> unit = (item == null) ? null : (Unit<?>) ((ComboItem) item).Data;
            if (this.fParams.ResultUnit == unit) {
                cmbUnits.setSelectedIndex(i);
                break;
            }
        }
        
        switch (this.fParams.Mode) {
            case imSolid_M:
                this.rbSolidByMass.setSelected(false);
                this.rbSolidByMass.doClick();

                this.txtMass.setMeasure(this.fParams.Mass);
                break;
            
            case imSolid_V_D:
                this.rbSolidByVD.setSelected(false);
                this.rbSolidByVD.doClick();

                this.txtVolume.setMeasure(this.fParams.Volume);
                this.txtDensity.setMeasure(this.fParams.Density);
                break;
            
            case imLiquid_M:
                this.rbLiquidByM.setSelected(false);
                this.rbLiquidByM.doClick();
                
                txtLiqVolume.setMeasure(this.fParams.Volume);
                txtConcM.setText(String.valueOf(this.fParams.ConcM));
                break;
            
            case imLiquid_MP_D:
                this.rbLiquidByMP.setSelected(false);
                this.rbLiquidByMP.doClick();
                
                txtLiqVolume.setMeasure(this.fParams.Volume);
                txtMP.setText(String.valueOf(this.fParams.MassPercent));
                txtLiqDensity.setMeasure(this.fParams.Density);
                break;
            
            case imGas_In:
                chkSTP.setSelected(!this.fParams.isSTP);
                chkSTP.doClick();

                if (!chkSTP.isSelected()) {
                    txtGasPressure.setMeasure(this.fParams.Pressure);
                    txtGasVolume.setMeasure(this.fParams.Volume);
                    txtGasTemperature.setMeasure(this.fParams.Temperature);
                } else {
                    txtGasVolume.setMeasure(this.fParams.Volume);
                }
                break;

            case imGas_Out:
                chkSTP.setSelected(!this.fParams.isSTP);
                chkSTP.doClick();

                txtGasPressure.setMeasure(this.fParams.Pressure);
                txtGasTemperature.setMeasure(this.fParams.Temperature);
                break;
        }
    }
    
    private void updateControls()
    {
        if (this.fIsInputSubst) {
            switch (this.fSubstance.State) {
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
            switch (this.fSubstance.State) {
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
                    txtGasDensity.setEnabled(false);
                    txtGasPressure.setEnabled(true);
                    txtGasVolume.setEnabled(false);
                    txtGasTemperature.setEnabled(true);
                    
                    chkSTP.setEnabled(false);
                    
                    this.fParams.Mode = StoicParams.InputMode.imGas_Out;                    
                    break;
            }
        }
    }
}
