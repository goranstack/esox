package nu.esox.fish.gui;

import java.util.*;
import nu.esox.gui.*;
import nu.esox.gui.model.*;
//import nu.esox.lang.*;
import nu.esox.util.*;
import nu.esox.fish.domain.*;


@SuppressWarnings( "serial" )
public class CatchTableModel extends ObservableListTableModel<Catch>
{
    public CatchTableModel( ObservableList<Catch> catches )
    {
        super( catches, false );
    }

    
    protected Column<Catch> [] getColumns() { return m_columns; }

    
    private final Column<Catch> [] m_columns =
        new CatchColumn []
        {
            new CatchColumn( "Species", String.class ) { public Object getValue( Catch c ) { return c.getFish().getSpecies(); } },
            new CatchColumn( "Weight",  Number.class ) { public Object getValue( Catch c ) { return c.getFish().getWeight(); } },
            new CatchColumn( "Length",  Number.class ) { public Object getValue( Catch c ) { return c.getFish().getLength(); } },
            new CatchColumn( "Girth",   Number.class ) { public Object getValue( Catch c ) { return c.getFish().getGirth(); } },
            new CatchColumn( "Venue",   String.class ) { public Object getValue( Catch c ) { return c.getCoordinates().getVenue(); } },
            new CatchColumn( "Swim",    String.class ) { public Object getValue( Catch c ) { return c.getCoordinates().getSwim(); } },
            new CatchColumn( "When",    Date.class )   { public Object getValue( Catch c ) { return c.getCoordinates().getWhen(); } },
            new CatchColumn( "Method",  String.class ) { public Object getValue( Catch c ) { return c.getMethod(); } },
            new CatchColumn( "Bait",    String.class ) { public Object getValue( Catch c ) { return c.getBait(); } },
        };

    
    private static abstract class CatchColumn extends Column<Catch>
    {
        CatchColumn( String name, Class type ) { super( name, type, false ); }
    }
}
