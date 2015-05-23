package chemlab.core.controls;

import bslib.common.AuxUtils;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.ValencyId;
import chemlab.core.chemical.ValencySet;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public final class ValencyControl extends JPanel
{
    private static class ValencyButton extends JToggleButton
    {
        public ValencyId Index = ValencyId.V1;

        public ValencyButton()
        {
            super();
        }
    }

    private final ValencyButton[] fButtons = new ValencyButton[8];
    private ValencySet fValencySet;

    public ValencyControl()
    {
        super();

        this.setLayout(null);

        for (ValencyId val : ValencyId.values()) {
            ValencyButton btn = new ValencyButton();
            btn.Index = val;
            btn.setEnabled(true);
            btn.setVisible(true);
            btn.setLocation(val.getValue() * 24, 0);
            btn.setSize(super.getWidth() / 8, 24);
            btn.setText(val.Sign);
            btn.setFont(this.getFont());
            btn.setForeground(AuxUtils.BGRToRGB(8421376));
            btn.setMargin(new Insets(0, 0, 0, 0));
            //btn.Click += this.ButtonClick;

            this.fButtons[val.getValue()] = btn;
            this.add(btn);
        }

        this.fValencySet = new ValencySet();
    }

    public final ValencySet getValencySet()
    {
        return this.fValencySet;
    }

    public final void setValencySet(ValencySet value)
    {
        if (!this.fValencySet.equals(value)) {
            this.fValencySet = value;

            for (ValencyId vi : ValencyId.values()) {
                if (this.fValencySet.contains(vi)) {
                    this.fButtons[vi.getValue()].setSelected(true);
                } else {
                    this.fButtons[vi.getValue()].setSelected(false);
                }
            }
        }
    }

    /*private void JButtonClick(Object Sender, EventArgs e)
    {
        ValencyId Index = ((ValencyButton) ((Sender instanceof ValencyButton) ? Sender : null)).Index;
        if (this.fValencySet.contains(Index)) {
            this.fValencySet.exclude(Index);
        } else {
            this.fValencySet.include(Index);
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

        int btnW = width / 8;

        for (ValencyId i : ValencyId.values()) {
            int idx = i.getValue();
            this.fButtons[idx].setSize(btnW, height);
            this.fButtons[idx].setLocation(idx * btnW, 0);
        }
    }
}
