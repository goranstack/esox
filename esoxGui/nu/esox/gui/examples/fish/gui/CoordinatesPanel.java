package nu.esox.gui.examples.fish.gui;

import java.util.*;
import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.layout.*;
import nu.esox.util.*;
import nu.esox.gui.examples.fish.domain.*;


public class CoordinatesPanel extends ModelPanel
{
    public CoordinatesPanel()
    {
        super( new FormLayout() );

        {
            add( new JLabel( "Venue" ) );
            JTextField tf = new JTextField( 20 );
            add( tf );
            new TextFieldAdapter( tf, this, Coordinates.class, "getVenue", "setVenue", "venue" );
            TextFieldFocusHandler.add( tf );
        }
        
        {
            add( new JLabel( "Swim" ) );
            JTextField tf = new JTextField( 20 );
            add( tf );
            new TextFieldAdapter( tf, this, Coordinates.class, "getSwim", "setSwim", "swim" );
            TextFieldFocusHandler.add( tf );
        }
        
        {
            add( new JLabel( "When" ) );
            JFormattedTextField tf = new JFormattedTextField( new SimpleDateFormat( "yyyy-MM-dd" ) );
            add( tf );
            new FormattedTextFieldAdapter( tf, this, Coordinates.class, "getWhen", "setWhen", Date.class, "when", null, null );
            TextFieldFocusHandler.add( tf );
        }
    }


    public Coordinates getCoordinates() { return (Coordinates) getModel(); }

    public void setCoordinates( Coordinates coordinates )
    {
        setModel( coordinates );
    }
}
