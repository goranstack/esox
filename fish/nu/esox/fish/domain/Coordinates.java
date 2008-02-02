package nu.esox.fish.domain;

import java.util.*;

import nu.esox.util.Observable;


@SuppressWarnings( "serial" )
public class Coordinates extends Observable
{
    private String m_venue;
    private String m_swim;
    private Date m_when = new Date();
    
    public Coordinates()
    {
    }

    public String getVenue() { return m_venue; }
    public String getSwim() { return m_swim; }
    public Date getWhen() { return m_when; }

    public void setVenue( String venue )
    {
        if
            ( ! equals( venue, m_venue ) )
        {
            m_venue = venue;
            fireValueChanged( "venue", m_venue );
        }
    }

    public void setSwim( String swim )
    {
        if
            ( ! equals( swim, m_swim ) )
        {
            m_swim = swim;
            fireValueChanged( "swim", m_swim );
        }
    }

    public void setWhen( Date when )
    {
        if
            ( ! equals( when, m_when ) )
        {
            m_when = when;
            fireValueChanged( "when", m_when );
        }
    }


    private static boolean equals( Object o1, Object o2 )
    {
        if ( o1 == o2 ) return true;
        if ( o1 == null ) return false;
        return o1.equals( o2 );
    }
}
