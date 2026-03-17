package nu.esox.gui.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class SubModelAdapter extends AbstractAdapter
{
    private final Object m_subModelTarget;
    private final Method m_setSubModelMethod;
    private final BiConsumer m_setSubModelConsumer;


    public <T, M> SubModelAdapter( T subModelTarget,
                                   BiConsumer<T, ?> setSubModel,
                                   ModelOwnerIF modelOwner,
                                   Function<M, ?> getter,
                                   String aspectName )
    {
        super( modelOwner, getter, null, aspectName, null, null );
        m_subModelTarget = subModelTarget;
        m_setSubModelMethod = null;
        m_setSubModelConsumer = setSubModel;
        update();
    }

    public SubModelAdapter( Object subModelTarget,
            String setSubModelMethodName,
            Class subModelClass,
            ModelOwnerIF modelOwner,
            AspectIF aspect,
            String aspectName)
    {
    	super(modelOwner, aspect, aspectName, null, null);
        m_subModelTarget = subModelTarget;
        m_setSubModelConsumer = null;
        try
        {
            m_setSubModelMethod = m_subModelTarget.getClass().getMethod( setSubModelMethodName, subModelClass );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + m_subModelTarget.getClass() + "." + setSubModelMethodName + "( " + subModelClass + " )" );
        }
        update();

    }

    public SubModelAdapter( Object subModelTarget,
                            String setSubModelMethodName,
                            Class subModelClass,
                            ModelOwnerIF modelOwner,
                            String aspectName )
    {
        this( subModelTarget, setSubModelMethodName, subModelClass, modelOwner, null, null, aspectName );
    }

    public SubModelAdapter( Object subModelTarget,
                            String setSubModelMethodName,
                            Class subModelClass,
                            ModelOwnerIF modelOwner,
                            Class modelClass,
                            String getAspectMethodName,
                            String aspectName )
    {
    	this(subModelTarget, setSubModelMethodName, subModelClass, modelOwner, new Aspect(modelClass, getAspectMethodName, null, subModelClass), aspectName);
    }

    @SuppressWarnings( "unchecked" )
    protected void update( Object projectedValue )
    {
        if ( m_setSubModelConsumer != null )
        {
            m_setSubModelConsumer.accept( m_subModelTarget, projectedValue );
            return;
        }

        boolean tmp = m_setSubModelMethod.isAccessible();
        try
        {
            m_setSubModelMethod.setAccessible( true );
            m_setSubModelMethod.invoke( m_subModelTarget, projectedValue );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_setSubModelMethod );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_setSubModelMethod, ex );
        }
        finally
        {
            m_setSubModelMethod.setAccessible( tmp );
        }
    }
}
