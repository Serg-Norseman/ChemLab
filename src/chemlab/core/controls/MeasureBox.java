package chemlab.core.controls;

import chemlab.core.chemical.ChemUnits;
import chemlab.forms.ComboItem;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.Unit;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

public class MeasureBox extends JPanel implements ActionListener, PropertyChangeListener
{
    public static final String actionCommand = "measureChanged";

    // Flag to ensure that infinite loops do not occur with ActionEvents.
    private boolean firingActionEvent = false;

    private JFormattedTextField fTextField;
    private JComboBox fUnitChooser;
    private NumberFormat fNumberFormat;
    private Number fValue;

    public MeasureBox(String title, Unit<?> baseUnit)
    {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        // Create the text field format, and then the text field
        fNumberFormat = NumberFormat.getNumberInstance();
        fNumberFormat.setMinimumFractionDigits(2);
        NumberFormatter formatter = new NumberFormatter(fNumberFormat);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);//seems to be a no-op --
        // aha -- it changes the value property but doesn't cause the result to
        // be parsed (that happens on focus loss/return, I think).

        fTextField = new JFormattedTextField(formatter);
        fTextField.setColumns(10);
        fTextField.setValue(0);
        fTextField.addPropertyChangeListener(this);

        // Add the combo box
        fUnitChooser = new JComboBox();

        List<Unit<?>> units = ChemUnits.getInstance().getUnits();
        for (Unit<?> un : units) {
            if (un.isCompatible(baseUnit)) {
                fUnitChooser.addItem(new ComboItem(un.toString(), un));
            }
        }

        fUnitChooser.setSelectedIndex(0);
        fUnitChooser.addActionListener(this);

        java.awt.Dimension dim1 = fUnitChooser.getPreferredSize();
        fTextField.setPreferredSize(dim1);

        // Make the text field/slider group a fixed size
        // to make stacked ConversionPanels nicely aligned
        JPanel unitGroup = new JPanel()
        {
            @Override
            public Dimension getMinimumSize()
            {
                return getPreferredSize();
            }

            @Override
            public Dimension getPreferredSize()
            {
                return new Dimension(150, super.getPreferredSize().height);
            }

            @Override
            public Dimension getMaximumSize()
            {
                return getPreferredSize();
            }
        };
        unitGroup.setLayout(new BoxLayout(unitGroup, BoxLayout.PAGE_AXIS));
        unitGroup.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        unitGroup.add(fTextField);

        // Create a subpanel so the combo box isn't too tall
        // and is sufficiently wide
        JPanel chooserPanel = new JPanel();
        chooserPanel.setLayout(new BoxLayout(chooserPanel, BoxLayout.PAGE_AXIS));
        chooserPanel.add(fUnitChooser);
        chooserPanel.add(Box.createHorizontalStrut(100));

        // Put everything together
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(unitGroup);
        add(chooserPanel);
        unitGroup.setAlignmentY(TOP_ALIGNMENT);
        chooserPanel.setAlignmentY(TOP_ALIGNMENT);
    }

    @Override
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        this.fTextField.setEnabled(b);
        this.fUnitChooser.setEnabled(b);
    }
    
    public final void setBaseUnit(Unit<?> baseUnit)
    {
        fUnitChooser.removeAllItems();
        List<Unit<?>> units = ChemUnits.getInstance().getUnits();
        for (Unit<?> un : units) {
            if (un.isCompatible(baseUnit)) {
                fUnitChooser.addItem(new ComboItem(un.toString(), un));
            }
        }
        fUnitChooser.setSelectedIndex(0);
    }
    
    // Don't allow this panel to get taller than its preferred size.
    // BoxLayout pays attention to maximum size, though most layout
    // managers don't.
    @Override
    public Dimension getMaximumSize()
    {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    public final double getValue()
    {
        this.fValue = (Number) this.fTextField.getValue();

        return this.fValue.doubleValue();
    }

    public final void setValue(double value)
    {
        fTextField.setValue(value);
    }

    public final Unit<?> getSelectedUnit()
    {
        ComboItem item = (ComboItem) fUnitChooser.getSelectedItem();
        if (item != null) {
            Unit<?> unit = (Unit<?>) item.Data;
            return unit;
        } else {
            return null;
        }
    }

    public final Measure<Double, ?> getMeasure()
    {
        Unit<?> unit = this.getSelectedUnit();
        if (unit == null) {
            return null;
        }

        double value = this.getValue();
        Measure<Double, ?> result = Measure.valueOf(value, unit);
        return result;
    }

    public final Measure<Double, ?> getRequiredMeasure(Unit<?> targetUnit)
    {
        Measure<Double, ?> measure = this.getMeasure();
        if (measure == null) {
            return null;
        }

        Measure<Double, ?> result = ChemUnits.convert(measure, targetUnit);
        return result;
    }

    /**
     * Adds an <code>ActionListener</code>.
     * <p>
     * The <code>ActionListener</code> will receive an <code>ActionEvent</code>
     * when a selection has been made. If the combo box is editable, then an
     * <code>ActionEvent</code> will be fired when editing has stopped.
     *
     * @param l the <code>ActionListener</code> that is to be notified
     * @see #setSelectedItem
     */
    public void addActionListener(ActionListener l)
    {
        listenerList.add(ActionListener.class, l);
    }

    /**
     * Removes an <code>ActionListener</code>.
     *
     * @param l the <code>ActionListener</code> to remove
     */
    public void removeActionListener(ActionListener l)
    {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type.
     *
     * @see EventListenerList
     */
    protected void fireActionEvent()
    {
        if (!firingActionEvent) {
            // Set flag to ensure that an infinite loop is not created
            firingActionEvent = true;
            ActionEvent e = null;
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            long mostRecentEventTime = EventQueue.getMostRecentEventTime();
            int modifiers = 0;
            AWTEvent currentEvent = EventQueue.getCurrentEvent();
            if (currentEvent instanceof InputEvent) {
                modifiers = ((InputEvent) currentEvent).getModifiers();
            } else if (currentEvent instanceof ActionEvent) {
                modifiers = ((ActionEvent) currentEvent).getModifiers();
            }

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ActionListener.class) {
                    // Lazily create the event:
                    if (e == null) {
                        e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                                actionCommand, mostRecentEventTime, modifiers);
                    }
                    ((ActionListener) listeners[i + 1]).actionPerformed(e);
                }
            }

            firingActionEvent = false;
        }
    }

    /**
     * Responds to the user choosing a new unit from the combo box.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if ("comboBoxChanged".equals(e.getActionCommand())) {
            this.fireActionEvent();
        }
    }

    /**
     * Detects when the value of the text field (not necessarily the same number
     * as you'd get from getText) changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        if ("value".equals(e.getPropertyName())) {
            this.fireActionEvent();
        }
    }
}
