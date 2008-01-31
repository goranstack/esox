package nu.esox.gui.aspect;


import java.lang.reflect.*;
import nu.esox.util.*;
import javax.swing.*;


public class IndexedLabelTextAdapter extends AbstractAdapter
{
    private final int m_index;
    private final JLabel m_label;

    public IndexedLabelTextAdapter( int index, JLabel l, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName, String nullValue, String undefinedValue )
    {
        super( modelOwner,
               resolveGetAspectMethod( modelClass, getAspectMethodName ),
               null,
               aspectName,
               nullValue,
               undefinedValue );

        m_label = l;
        m_index = index;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_label.setText( getTextFor( projectedValue ) );
    }

    protected String getTextFor( Object value )
    {
        return "" + value;
    }

    protected Object invokeGetAspectMethod( Method getAspectMethod, Object model ) throws IllegalAccessException, InvocationTargetException
    {
        return getAspectMethod.invoke( model, m_index );
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
}
