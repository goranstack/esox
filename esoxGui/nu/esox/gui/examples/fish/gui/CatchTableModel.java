package nu.esox.gui.examples.fish.gui;

import java.util.*;
import nu.esox.gui.*;
import nu.esox.gui.model.*;
//import nu.esox.lang.*;
import nu.esox.util.*;
import nu.esox.gui.examples.fish.domain.*;


public class CatchTableModel extends ObservableListTableModel
{
    public CatchTableModel( ObservableList catches )
    {
        super( catches, false );
    }

    
    protected Column [] getColumns() { return m_columns; }

    private Catch getCatch( Object o ) { return (Catch) o; }


//    private final MutableFloat m_float = new MutableFloat( 0 );
    
    private final Column [] m_columns =
        new Column []
        {
            new Column( "Species", String.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getFish().getSpecies(); }
            },
            new Column( "Weight", Number.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getFish().getWeight(); }
            },
            new Column( "Length", Number.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getFish().getLength(); }
            },
            new Column( "Girth", Number.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getFish().getGirth(); }
            },
            new Column( "Venue", String.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getCoordinates().getVenue(); }
            },
            new Column( "Swim", String.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getCoordinates().getSwim(); }
            },
            new Column( "When", Date.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getCoordinates().getWhen(); }
            },
            new Column( "Method", String.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getMethod(); }
            },
            new Column( "Bait", String.class, false )
            {
                public Object getValue( Object target ) { return getCatch( target ).getBait(); }
            },
        };
}
