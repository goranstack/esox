package nu.esox.gui.aspect;

import java.lang.reflect.*;


public class IndexedAspect extends Aspect
{
    private final int m_index;

    
    public IndexedAspect( int index, Class modelClass, String getAspectMethodName, String setAspectMethodName, Class aspectClass )
    {
        this( index,
              resolveGetAspectMethod( modelClass, getAspectMethodName ),
              resolveSetAspectMethod( modelClass, setAspectMethodName, aspectClass ) );
    }

    public IndexedAspect( int index, Method getAspectMethod, Method setAspectMethod )
    {
        super( getAspectMethod, setAspectMethod );
        m_index = index;
    }

    
    protected Object invokeGetAspectMethod( Method getAspectMethod, Object model ) throws IllegalAccessException, InvocationTargetException
    {
        return getAspectMethod.invoke( model, m_index );
    }
 
    protected void invokeSetAspectMethod( Method setAspectMethod, Object model, Object aspectValue ) throws IllegalAccessException, InvocationTargetException
    {
        setAspectMethod.invoke( model, m_index, aspectValue );
    }
    

    protected static Method resolveGetAspectMethod( Class<?> modelClass, String getAspectMethodName )
    {
        if ( getAspectMethodName == null ) return null;
        
        try
        {
            return modelClass.getMethod( getAspectMethodName, int.class );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + getAspectMethodName + "()" );
        }
    }
    
    protected static Method resolveSetAspectMethod( Class<?> modelClass, String setAspectMethodName, Class aspectClass )
    {
        if ( setAspectMethodName == null ) return null;

        try
        {
            return modelClass.getMethod( setAspectMethodName, int.class, aspectClass );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + modelClass + "." + setAspectMethodName + "( " + aspectClass + " )" );
        }
    }
}

