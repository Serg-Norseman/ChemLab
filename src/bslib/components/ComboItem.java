package bslib.components;

/**
 * @author Serg V. Zhdanovskih
 */
public class ComboItem
{
    public final String Caption;
    public final Object Data;

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
