package nu.esox.gui.aspect;


import java.lang.reflect.*;
import java.awt.event.*;
import nu.esox.util.*;
import javax.swing.*;


public class IndexedCheckBoxAdapter extends AbstractBooleanAdapter implements ActionListener
{
    private final int m_index;
    private final AbstractButton m_button;
    
    public IndexedCheckBoxAdapter( int index, AbstractButton cb, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
        this( index, cb, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, boolean.class, aspectName, true, false, null, false );
    }
    
    public IndexedCheckBoxAdapter( int index,
                                   AbstractButton cb,
                                   ModelOwnerIF modelOwner,
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
        super( modelOwner,
               resolveGetAspectMethod( modelClass, getAspectMethodName ),
               resolveSetAspectMethod( modelClass, setAspectMethodName, aspectClass ),
               aspectName,
               trueValue,
               falseValue,
               nullValue,
               undefinedValue );

        m_index = index;
        m_button = cb;
        m_button.addActionListener( this );
        update();
    }


    public void actionPerformed( ActionEvent ev )
    {
        setAspectValue( m_button.isSelected() ? Boolean.TRUE : Boolean.FALSE );
    }

    
    protected void update( Object projectedValue )
    {
        m_button.setSelected( (Boolean) projectedValue );
    }

    protected Object invokeGetAspectMethod( Method getAspectMethod, Object model ) throws IllegalAccessException, InvocationTargetException
    {
        return getAspectMethod.invoke( model, m_index );
    }
    
    protected Object invokeSetAspectMethod( Method setAspectMethod, Object model, Object aspectValue ) throws IllegalAccessException, InvocationTargetException
    {
        return setAspectMethod.invoke( model, m_index, aspectValue );
    }

    
    protected static Method resolveGetAspectMethod( Class modelClass, String getAspectMethodName )
    {
        if ( getAspectMethodName == null ) return null;
        
        try
        {
            return modelClass.getMethod( getAspectMethodName, int.class );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + getAspectMethodName + "( int )" );
        }
    }
    
    protected static Method resolveSetAspectMethod( Class modelClass, String setAspectMethodName, Class aspectClass )
    {
        if ( setAspectMethodName == null ) return null;

        try
        {
            return modelClass.getMethod( setAspectMethodName, int.class, aspectClass );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + setAspectMethodName + "( int, " + aspectClass + " )" );
        }
    }
}
