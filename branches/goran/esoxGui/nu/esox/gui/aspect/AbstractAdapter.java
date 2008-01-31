package nu.esox.gui.aspect;


import java.util.*;
import java.lang.reflect.*;
import nu.esox.util.*;
import javax.swing.*;

public abstract class AbstractAdapter implements ObservableListener, ModelOwnerIF.Listener
{
    private final ModelOwnerIF m_modelOwner;
    private final Method m_getAspectMethod;
    private final Method m_setAspectMethod;
    private final String m_aspectName;
    private final Object m_nullValue;
    private final Object m_undefinedValue;


    public AbstractAdapter( ModelOwnerIF modelOwner,
                            Class modelClass,
                            String getAspectMethodName,
                            String setAspectMethodName,
                            Class aspectClass,
                            String aspectName,
                            Object nullValue,
                            Object undefinedValue )
    {
        this( modelOwner,
              resolveGetAspectMethod( modelClass, getAspectMethodName ),
              resolveSetAspectMethod( modelClass, setAspectMethodName, aspectClass ),
              aspectName,
              nullValue,
              undefinedValue );
    }

    public AbstractAdapter( ModelOwnerIF modelOwner,
                            Method getAspectMethod,
                            Method setAspectMethod,
                            String aspectName,
                            Object nullValue,
                            Object undefinedValue )
    {
        m_modelOwner = modelOwner;
        m_aspectName = aspectName;
        m_nullValue = ( nullValue == null ) ? EQUALS_NOTHING : nullValue;
        m_undefinedValue = undefinedValue;

        m_getAspectMethod = getAspectMethod;
        m_setAspectMethod = setAspectMethod;

        if ( m_modelOwner.getModel() != null ) m_modelOwner.getModel().addObservableListener( this );

        m_modelOwner.addListener( this );
    }

    protected static Method resolveGetAspectMethod( Class modelClass, String getAspectMethodName )
    {
        if ( getAspectMethodName == null ) return null;
        
        try
        {
            return modelClass.getMethod( getAspectMethodName );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + getAspectMethodName + "()" );
        }
    }
    
    protected static Method resolveSetAspectMethod( Class modelClass, String setAspectMethodName, Class aspectClass )
    {
        if ( setAspectMethodName == null ) return null;

        try
        {
            return modelClass.getMethod( setAspectMethodName, aspectClass );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + setAspectMethodName + "( " + aspectClass + " )" );
        }
    }


    public final void modelAssigned( ObservableIF oldModel, ObservableIF newModel ) // implements ModelOwnerIF.Listener
    {
        if ( oldModel != null ) oldModel.removeObservableListener( this );
        if ( newModel != null ) newModel.addObservableListener( this );
        valueChanged( null );
    }

    
    public final void valueChanged( ObservableEvent ev ) // implements ObservableListener
    {
        if
            ( ( m_aspectName == null ) || ( ev == null ) || ( m_aspectName.equals( ev.getInfo() ) ) )
        {
            update();
        }
    }

    protected Object invokeGetAspectMethod( Method getAspectMethod, Object model ) throws IllegalAccessException, InvocationTargetException
    {
        return getAspectMethod.invoke( model );
    }
    
    protected Object invokeSetAspectMethod( Method setAspectMethod, Object model, Object aspectValue ) throws IllegalAccessException, InvocationTargetException
    {
        return setAspectMethod.invoke( model, aspectValue );
    }
    
    protected final void update()
    {
        Object aspect = null;
        
        if
            ( m_getAspectMethod == null )
        {
            aspect = m_modelOwner.getModel();
        } else {
            if
                ( m_modelOwner.getModel() == null )
            {
                update( m_undefinedValue );
                return;
            }
        
            boolean tmp = m_getAspectMethod.isAccessible();

            try
            {
                m_getAspectMethod.setAccessible( true );
                aspect = invokeGetAspectMethod( m_getAspectMethod, m_modelOwner.getModel() );
            }
            catch ( IllegalAccessException ex )
            {
                throw new Error( "Method not accessible: " + m_getAspectMethod );
            }
            catch ( InvocationTargetException ex )
            {
                throw new Error( "Invovation failure: " + m_getAspectMethod, ex );
            }
            finally
            {
                m_getAspectMethod.setAccessible( tmp );
            }
        }

        update( deriveProjectedValue( aspect ) );
    }
    
    protected abstract void update( Object projectedValue );

    
    protected final void setAspectValue( Object projectedValue )
    {
        if ( m_modelOwner.getModel() == null ) return;
        if ( m_setAspectMethod == null ) return;

        Object aspectValue = deriveAspectValue( projectedValue );
        
        boolean tmp = m_setAspectMethod.isAccessible();
        try
        {
            m_setAspectMethod.setAccessible( true );
            invokeSetAspectMethod( m_setAspectMethod, m_modelOwner.getModel(), aspectValue );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_setAspectMethod );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invocation failure: " + m_setAspectMethod + ", " + aspectValue, ex );
        }
        catch ( Throwable ex )
        {
            throw new Error( "Failure: " + m_setAspectMethod + ", " + aspectValue, ex );
        }
        finally
        {
        	m_setAspectMethod.setAccessible( tmp );
        }
    }
 


    protected Object deriveProjectedValue( Object aspectValue )
    {
        Object projectedValue = aspectValue;

          // If null can't be projected, use m_nullValue instead
        if
            ( aspectValue == null )
        {
            if ( m_nullValue != EQUALS_NOTHING ) projectedValue = m_nullValue;
        }
        
        return projectedValue;
    }

    
    protected Object deriveAspectValue( Object projectedValue )
    {
          // If m_nullValue is projected, aspect value is null
        if ( projectedValue == m_nullValue ) return null;
        if ( m_nullValue.equals( projectedValue ) ) return null;
        
        return projectedValue;
    }


    private static final Object EQUALS_NOTHING =
        new Object()
        {
            public boolean equals( Object o ) { return false; }
        };
}
