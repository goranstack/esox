package nu.esox.util;


import javax.swing.event.EventListenerList;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.beans.*;


public class BeanObservable extends Observable implements PropertyChangeListener
{
    private static Object m_bean;
    
    public BeanObservable( Object bean )
    {
        m_bean = bean;

        try
        {
            m_bean.getClass().getMethod( "addPropertyChangeListener", PropertyChangeListener.class ).invoke( this );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + m_bean.getClass() + ".addPropertyChangeListener( PropertyChangeListener )" );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_bean.getClass() + ".addPropertyChangeListener( PropertyChangeListener )" );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_bean.getClass() + ".addPropertyChangeListener( PropertyChangeListener )" );
        }
    }

    public BeanObservable( Object bean, String property )
    {
        m_bean = bean;

        try
        {
            m_bean.getClass().getMethod( "addPropertyChangeListener", String.class, PropertyChangeListener.class ).invoke( property, this );
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
            throw new Error( "Invovation failure: " + m_bean.getClass() + ".addPropertyChangeListener( String, PropertyChangeListener )" );
        }
    }

    public void propertyChange( PropertyChangeEvent ev )
    {
        fireValueChanged( ev.getPropertyName(), ev.getNewValue() );
    }

    static final long serialVersionUID = -4590833817237017531L;
}
