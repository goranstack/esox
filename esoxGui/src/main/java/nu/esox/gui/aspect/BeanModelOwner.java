package nu.esox.gui.aspect;

import java.beans.*;
import java.lang.*;
import java.lang.reflect.*;
import nu.esox.util.*;


public class BeanModelOwner extends AbstractModelOwner implements PropertyChangeListener
{
    private final Object m_bean;
    private final Method m_get;
    private final Method m_set;
    private Predicate m_hasModel = null;


    public BeanModelOwner( Object bean, String property, String getMethodName, String setMethodName, Class setMethodArgClass )
    {
        m_bean = bean;

        try
        {
            m_get = m_bean.getClass().getMethod( getMethodName );
            m_set = m_bean.getClass().getMethod( setMethodName, setMethodArgClass );
            m_bean.getClass().getMethod( "addPropertyChangeListener", String.class, PropertyChangeListener.class ).invoke( m_bean, property, this );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )" );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )" );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )", ex );
        }
    }

    public void propertyChange( PropertyChangeEvent ev )
    {
        if ( m_hasModel != null ) m_hasModel.set( ev.getNewValue() != null );
        fireModelAssigned( (ObservableIF) ev.getOldValue(), (ObservableIF) ev.getNewValue() );
    }

    public final ObservableIF getModel()
    {
        try
        {
            return (ObservableIF) m_get.invoke( m_bean );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )" );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )", ex );
        }
    }

    public final void setModel( ObservableIF model )
    {
        try
        {
            m_set.invoke( m_bean, model );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )" );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )", ex );
        }
    }

    public final PredicateIF getHasModel()
    {
        if
            ( m_hasModel == null )
        {
            m_hasModel = new Predicate();
            m_hasModel.set( getModel() != null );
        }
        
        return m_hasModel;
    }
}
