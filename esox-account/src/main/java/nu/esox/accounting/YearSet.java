package nu.esox.accounting;

import java.util.*;
import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class YearSet extends ObservableList<Year>
{
    private final Map<Integer, Year> m_map = new HashMap<Integer, Year>();
   
    
    public boolean add( Year year )
    {
        assert ! m_map.containsKey( year.getNumber() );
        
        m_map.put( year.getNumber(), year );
        super.add( - ( Collections.binarySearch( this, year ) + 1 ), year );
        return true;
    }
    
    public void remove( Year year )
    {
        if
            ( contains( year ) )
        {
            m_map.remove( year.getNumber() );
            super.remove( year );
        }
    }

    public Year getYear( int number )
    {
        return m_map.get( number );
    }
}
