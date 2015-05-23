package chemlab.core.controls;

import bslib.common.INotifyHandler;
import javax.swing.JPanel;

public abstract class EditorControl extends JPanel
{
    private INotifyHandler fOnChange;

    public boolean Modified;

    protected EditorControl()
    {
        super();
    }

    public final INotifyHandler getOnChange()
    {
        return this.fOnChange;
    }

    public final void setOnChange(INotifyHandler value)
    {
        this.fOnChange = value;
    }

    public void DoChange()
    {
        this.Modified = true;

        if (this.fOnChange != null) {
            this.fOnChange.invoke(this);
        }
    }

    public abstract void loadFromFile(String fileName);

    public abstract void saveToFile(String fileName);
}
