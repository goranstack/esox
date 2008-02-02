package nu.esox.gui.aspect;

import java.lang.reflect.*;
import nu.esox.util.*;


public class ModelPredicateProxy extends PredicateProxy implements ModelOwnerIF.Listener
{
    private final ModelOwnerIF m_modelOwner;
    private final Method m_getPredicateMethod;
    
    
    public ModelPredicateProxy( ModelOwnerIF modelOwner, Class<?> modelClass, String getPredicateMethodName )
    {
        super( null );
        
        m_modelOwner = modelOwner;
        
        try
        {
            m_getPredicateMethod = modelClass.getMethod( getPredicateMethodName );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + getPredicateMethodName + "()" );
        }

        m_modelOwner.addListener( this );
        modelAssigned( null, m_modelOwner.getModel() );
    }


    public final void modelAssigned( ObservableIF oldModel, ObservableIF newModel ) // implements ModelOwnerIF.Listener
    {
        boolean tmp = m_getPredicateMethod.isAccessible();

        try
        {
            m_getPredicateMethod.setAccessible( true );
            setDelegate( newModel == null ? null : (PredicateIF) m_getPredicateMethod.invoke( newModel ) );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_getPredicateMethod );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_getPredicateMethod, ex );
        }
        finally
        {
            m_getPredicateMethod.setAccessible( tmp );
        }
    }

    static final long serialVersionUID = 42;
}

