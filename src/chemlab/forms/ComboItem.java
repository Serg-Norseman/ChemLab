package chemlab.forms;

public class ComboItem
{
    public String Caption;
    public Object Data;

    public ComboItem(String caption, Object data)
    {
        this.Caption = caption;
        this.Data = data;
    }

    @Override
    public String toString()
    {
        return this.Caption;
    }
}
