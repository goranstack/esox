package nu.esox.gui.aspect;

import java.lang.reflect.*;
import nu.esox.util.*;


public class SubModelAdapter extends AbstractAdapter
{
    private final Object m_subModelTarget;
    private final Method m_setSubModelMethod;

    
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
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, null, null );

        m_subModelTarget = subModelTarget;
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
    
    protected void update( Object projectedValue )
    {
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
