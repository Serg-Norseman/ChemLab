package chemlab.sandbox;

import chemlab.core.chemical.ChemUnits;
import chemlab.core.controls.MeasureBox;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.measure.Measure;
import javax.measure.unit.Unit;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Converter implements ActionListener
{
    MeasureBox srcPanel, targetPanel;
    JPanel mainPane;

    public Converter()
    {
        srcPanel = new MeasureBox("Metric System", ChemUnits.KILOPASCAL);
        targetPanel = new MeasureBox("U.S. System", ChemUnits.KILOPASCAL);

        srcPanel.addActionListener(this);
        targetPanel.addActionListener(this);
        
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));
        mainPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(srcPanel);
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(targetPanel);
        mainPane.add(Box.createGlue());
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (MeasureBox.actionCommand.equals(e.getActionCommand())) {
            Unit<?> targetUnit = targetPanel.getSelectedUnit();
            Measure<Double, ?> result = srcPanel.getRequiredMeasure(targetUnit);
            targetPanel.setValue(result.getValue());
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Create and set up the window.
                JFrame frame = new JFrame("Converter");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Create and set up the content pane.
                Converter converter = new Converter();
                converter.mainPane.setOpaque(true); //content panes must be opaque
                frame.setContentPane(converter.mainPane);

                // Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
