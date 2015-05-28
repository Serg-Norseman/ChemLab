package chemlab.forms;

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

    MeasureBox srcPanel, targetPanel;
    JPanel mainPane;
    JPanel valPane;
    JComboBox cmbDimensions;
    private boolean fDimChanging;

    public CLUnitsConverter()
    {
        super();
        
        valPane = new JPanel();
        valPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(res_i18n.getString("CL_DIMENSION")),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        
        cmbDimensions = new JComboBox();
        List<DimensionsList.DimRecord> list = DimensionsList.getInstance().getList();
        for (DimensionsList.DimRecord dim : list) {
            cmbDimensions.addItem(new ComboItem(dim.Name, dim.BaseUnit));
        }
        cmbDimensions.setSelectedIndex(0);
        cmbDimensions.addActionListener(this);
        valPane.add(cmbDimensions);
        
        srcPanel = new MeasureBox(res_i18n.getString("CL_SOURCE_DATA"), ChemUnits.KILOPASCAL);
        targetPanel = new MeasureBox(res_i18n.getString("CL_CALC_DATA"), ChemUnits.KILOPASCAL);

        srcPanel.addActionListener(this);
        targetPanel.addActionListener(this);
        
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
        mainPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(valPane);

        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(srcPanel);
        
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(targetPanel);
        
        mainPane.add(Box.createGlue());

        mainPane.setOpaque(true); //content panes must be opaque

        this.setTitle(res_i18n.getString("CL_UNITS_CONVERTER"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPane);
        this.setResizable(false);
        this.pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (MeasureBox.actionCommand.equals(e.getActionCommand()) && !this.fDimChanging) {
            Unit<?> targetUnit = targetPanel.getSelectedUnit();
            if (targetUnit != null) {
                Measure<Double, ?> result = srcPanel.getRequiredMeasure(targetUnit);
                targetPanel.setValue(result.getValue());
            }
        } else if ("comboBoxChanged".equals(e.getActionCommand()) && e.getSource() == cmbDimensions) {
            this.fDimChanging = true;
            Unit<?> baseUnit = (Unit<?>) ((ComboItem) cmbDimensions.getSelectedItem()).Data;
            srcPanel.setBaseUnit(baseUnit);
            targetPanel.setBaseUnit(baseUnit);
            this.fDimChanging = false;
        }
    }
}
