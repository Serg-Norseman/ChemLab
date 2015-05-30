package chemlab.forms;

import bslib.components.ComboItem;
import chemlab.core.chemical.ChemUnits;
import chemlab.core.chemical.DimensionsList;
import chemlab.core.controls.MeasureBox;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import javax.measure.Measure;
import javax.measure.unit.Unit;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public final class CLUnitsConverter extends JFrame implements ActionListener
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    MeasureBox fSourceMeasure, fTargetMeasure;
    JComboBox cmbDimensions;
    private boolean fDimChanging;

    public CLUnitsConverter()
    {
        super();
        
        JPanel valPanel = new JPanel();
        valPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(res_i18n.getString("CL_DIMENSION")),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        
        cmbDimensions = new JComboBox();
        List<DimensionsList.DimRecord> list = DimensionsList.getInstance().getList();
        for (DimensionsList.DimRecord dim : list) {
            cmbDimensions.addItem(new ComboItem(dim.Name, dim.BaseUnit));
        }
        cmbDimensions.setSelectedIndex(0);
        cmbDimensions.addActionListener(this);
        valPanel.add(cmbDimensions);
        
        fSourceMeasure = new MeasureBox(res_i18n.getString("CL_SOURCE_DATA"), ChemUnits.KILOPASCAL);
        fTargetMeasure = new MeasureBox(res_i18n.getString("CL_CALC_DATA"), ChemUnits.KILOPASCAL);

        fSourceMeasure.addActionListener(this);
        fTargetMeasure.addActionListener(this);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(valPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(fSourceMeasure);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(fTargetMeasure);
        
        mainPanel.add(Box.createGlue());

        mainPanel.setOpaque(true);

        this.setTitle(res_i18n.getString("CL_UNITS_CONVERTER"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (MeasureBox.actionCommand.equals(e.getActionCommand()) && !this.fDimChanging) {
            Unit<?> targetUnit = fTargetMeasure.getSelectedUnit();
            if (targetUnit != null) {
                Measure<Double, ?> result = fSourceMeasure.getRequiredMeasure(targetUnit);
                fTargetMeasure.setValue(result.getValue());
            }
        } else if ("comboBoxChanged".equals(e.getActionCommand()) && e.getSource() == cmbDimensions) {
            this.fDimChanging = true;
            Unit<?> baseUnit = (Unit<?>) ((ComboItem) cmbDimensions.getSelectedItem()).Data;
            fSourceMeasure.setBaseUnit(baseUnit);
            fTargetMeasure.setBaseUnit(baseUnit);
            this.fDimChanging = false;
        }
    }
}
