package nu.esox.fish.domain;

import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class Catch extends Observable
{
    private final Fish m_fish = new Fish();
    private final Coordinates m_coordinates = new Coordinates();
    private String m_method;
    private String m_bait;

    {
        listenTo( m_fish );
        listenTo( m_coordinates );
    }
    
    public Catch()
    {
    }

    public Fish getFish() { return m_fish; }
    public Coordinates getCoordinates() { return m_coordinates; }
    public String getMethod() { return m_method; }
    public String getBait() { return m_bait; }

    public void setMethod( String method )
    {
        if
            ( ! equals( method, m_method ) )
        {
            m_method = method;
            fireValueChanged( "method", m_method );
        }
    }

    public void setBait( String bait )
    {
        if
            ( ! equals( bait, m_bait ) )
        {
            m_bait = bait;
            fireValueChanged( "bait", m_bait );
        }
    }


    private static boolean equals( Object o1, Object o2 )
    {
        if ( o1 == o2 ) return true;
        if ( o1 == null ) return false;
        return o1.equals( o2 );
    }
}
