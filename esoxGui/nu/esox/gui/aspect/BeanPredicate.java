package nu.esox.gui.aspect;

import java.beans.*;
import java.lang.*;
import java.lang.reflect.*;
import nu.esox.util.*;


public class BeanPredicate extends Predicate implements PropertyChangeListener
{
    private final Object m_bean;


    public BeanPredicate( Object bean, String property, String getMethodName )
    {
        m_bean = bean;

        
        try
        {
            m_bean.getClass().getMethod( "addPropertyChangeListener", String.class, PropertyChangeListener.class ).invoke( m_bean, property, this );
            set( test( m_bean.getClass().getMethod( getMethodName ).invoke( m_bean ) ) );
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
        set( test( ev.getNewValue() ) );
    }

    protected boolean test( Object value )
    {
        return value != null;
    }
}
