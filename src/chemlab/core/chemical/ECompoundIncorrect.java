package chemlab.core.chemical;

public class ECompoundIncorrect extends RuntimeException
{
    public ECompoundIncorrect()
    {
    }

    public ECompoundIncorrect(String message)
    {
        super(message);
    }

    public ECompoundIncorrect(String message, Exception innerException)
    {
        super(message, innerException);
    }
}
