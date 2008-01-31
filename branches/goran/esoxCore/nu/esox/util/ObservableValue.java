package nu.esox.util;

import java.io.*;


public class ObservableValue<T> extends Observable implements ObservableValueIF<T>, Serializable
{
    private String m_name;
    private T m_value;

    public ObservableValue( String name, T value )
    {
        m_name = name;
        set( value );
    }
    
   

    public T get()
    {
        return m_value;
    }
    
    public void set( T value )
    {
        if ( equals( m_value, value ) ) return;
        m_value = value;
        fireValueChanged( m_name, m_value );
    }
    
    
    private boolean equals( Object v1, Object v2 )
    {
        if ( v1 == v2 ) return true;
        if ( v1 == null ) return false;
        if ( v2 == null ) return false;
        return v1.equals( v2 );
    }
    
    
    public String toString()
    {
        return "" + m_value;
    }
    
    public boolean equals( Object o )
    {
        if ( o instanceof ObservableValue ) return equals( ( (ObservableValue) o ).get() );
        return equals( m_value, (T) o );
    }

    static final long serialVersionUID = -3808370187775586340L;
}
