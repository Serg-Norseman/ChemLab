package chemlab.forms;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
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
