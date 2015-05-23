package chemlab.core.controls;

import chemlab.core.chemical.DegreeId;
import chemlab.core.chemical.DegreeSet;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public final class OxidationDegreeControl extends JPanel
{
    private static class DegreeButton extends JToggleButton
    {
        public DegreeId Index = DegreeId.Zero;

        public DegreeButton()
        {
            super();
        }
    }

    private final DegreeButton[] fButtons = new DegreeButton[15];
    private DegreeSet fDegreeSet;

    public OxidationDegreeControl()
    {
        super();

        this.setLayout(null);
        
        for (DegreeId deg : DegreeId.values()) {
            DegreeButton btn = new DegreeButton();
            btn.Index = deg;
            btn.setEnabled(true);
            btn.setVisible(true);
            btn.setLocation(deg.getValue() * 24, 0);
            btn.setSize(24, 24);
            btn.setText(deg.Sign);
            btn.setFont(this.getFont());
            btn.setForeground(deg.SColor);
            btn.setMargin(new Insets(0, 0, 0, 0));
            //btn.Click += this.ButtonClick;

            this.fButtons[deg.getValue()] = btn;
            this.add(btn);
        }

        this.fDegreeSet = new DegreeSet();
    }

    public final DegreeSet getDegreeSet()
    {
        return this.fDegreeSet;
    }

    public final void setDegreeSet(DegreeSet value)
    {
        if (!this.fDegreeSet.equals(value)) {
            this.fDegreeSet = value;

            for (DegreeId di : DegreeId.values()) {
                if (this.fDegreeSet.contains(di)) {
                    this.fButtons[di.getValue()].setSelected(true);
                } else {
                    this.fButtons[di.getValue()].setSelected(false);
                }
            }
        }
    }

    /*private void JButtonClick(Object sender, EventArgs e)
    {
        DegreeId index = ((DegreeButton) ((sender instanceof DegreeButton) ? sender : null)).Index;
        EnumSet<DegreeId> degreeSet = this.fDegreeSet;
        if (degreeSet.contains(index)) {
            degreeSet.exclude(index);
        } else {
            degreeSet.include(index);
        }
        this.Change();
    }*/

    protected void Change()
    {
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);

        int btnW = width / 15;

        for (DegreeId i : DegreeId.values()) {
            int idx = i.getValue();
            DegreeButton btn = this.fButtons[idx];
            btn.setSize(btnW, height);
            btn.setLocation(idx * btnW, 0);
        }
    }
}
