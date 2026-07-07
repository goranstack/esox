package nu.esox.gui.aspect;


import java.util.function.BiConsumer;
import java.util.function.Function;


public abstract class AbstractBooleanAdapter extends AbstractAdapter
{
    private final Object m_trueValue;
    private final Object m_falseValue;

    
    public AbstractBooleanAdapter( ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
        this( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, Boolean.class, aspectName, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE );
    }
    
    public AbstractBooleanAdapter( ModelOwnerIF modelOwner,
                                   Class modelClass,
                                   String getAspectMethodName,
                                   String setAspectMethodName,
                                   Class aspectClass,
                                   String aspectName,
                                   Object trueValue,
                                   Object falseValue,
                                   Object nullValue,
                                   Object undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, nullValue, undefinedValue );

        m_trueValue = trueValue;
        m_falseValue = falseValue;
    }

    public AbstractBooleanAdapter( ModelOwnerIF modelOwner,
                                   Function getter,
                                   String aspectName )
    {
        this( modelOwner, getter, null, aspectName, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE );
    }

    @SuppressWarnings( { "rawtypes" } )
    public AbstractBooleanAdapter( ModelOwnerIF modelOwner,
                                   Function getter,
                                   BiConsumer setter,
                                   String aspectName,
                                   Object trueValue,
                                   Object falseValue,
                                   Object nullValue,
                                   Object undefinedValue )
    {
        super( modelOwner, getter, setter, aspectName, nullValue, undefinedValue );

        m_trueValue = trueValue;
        m_falseValue = falseValue;
    }

    @SuppressWarnings("rawtypes")
	public AbstractBooleanAdapter( ModelOwnerIF modelOwner,
            Function getter,
            String aspectName,
            Object trueValue,
            Object falseValue,
            Object nullValue,
            Object undefinedValue )
    {
    	this( modelOwner, getter, null, aspectName, trueValue, falseValue, nullValue, undefinedValue );
    }
    
    public AbstractBooleanAdapter( ModelOwnerIF modelOwner,
                                   AspectIF aspect,
                                   String aspectName,
                                   Object trueValue,
                                   Object falseValue,
                                   Object nullValue,
                                   Object undefinedValue )
    {
        super( modelOwner, aspect, aspectName, nullValue, undefinedValue );

        m_trueValue = trueValue;
        m_falseValue = falseValue;
    }
    
    protected Object deriveProjectedValue( Object aspectValue )
    {
        Object projectedValue = super.deriveProjectedValue( aspectValue );

          // aspectValue -> Boolean
        if ( m_trueValue == projectedValue )       return Boolean.TRUE;
        if ( m_trueValue == null )                 return Boolean.FALSE;
        if ( m_trueValue.equals( projectedValue ) ) return Boolean.TRUE;
        return Boolean.FALSE;
    }


    protected Object deriveAspectValue( Object projectedValue )
    {
          // Boolean -> aspectValue
        Object aspectValue = ( (Boolean) projectedValue ).booleanValue() ? m_trueValue : m_falseValue;
        return super.deriveAspectValue( aspectValue );
    }
}
