package nu.esox.fish.domain;

import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class Fish extends Observable
{
    private String m_species;
    private double m_weight = Double.NaN;
    private double m_length = Double.NaN;
    private double m_girth = Double.NaN;
    
    public Fish()
    {
    }

    public String getSpecies() { return m_species; }
    public double getWeight() { return m_weight; }
    public double getLength() { return m_length; }
    public double getGirth() { return m_girth; }

    public void setSpecies( String species )
    {
        if
            ( ! equals( species, m_species ) )
        {
            m_species = species;
            fireValueChanged( "species", m_species );
        }
    }

    public void setWeight( double weight )
    {
        if
            ( weight != m_weight )
        {
            m_weight = weight;
            fireValueChanged( "weight", null );
        }
    }

    public void setLength( double length )
    {
        if
            ( length != m_length )
        {
            m_length = length;
            fireValueChanged( "length", null );
        }
    }

    public void setGirth( double girth )
    {
        if
            ( girth != m_girth )
        {
            m_girth = girth;
            fireValueChanged( "girth", null );
        }
    }


    private static boolean equals( Object o1, Object o2 )
    {
        if ( o1 == o2 ) return true;
        if ( o1 == null ) return false;
        return o1.equals( o2 );
    }
}
