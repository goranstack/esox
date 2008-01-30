package nu.esox.gui.examples.fish.gui;

import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;
import nu.esox.util.*;
import nu.esox.gui.examples.fish.domain.*;


public class CatchPanel extends ModelPanel
{
    public CatchPanel()
    {
        super( new RowLayout( 5 ) );

        {
            FishPanel p = new FishPanel();
            add( p );
            new SubModelAdapter( p, "setFish", Fish.class, this, Catch.class, "getFish", "fish" );
        }

        {
            CoordinatesPanel p = new CoordinatesPanel();
            add( p );
            new SubModelAdapter( p, "setCoordinates", Coordinates.class, this, Catch.class, "getCoordinates", "coordinates" );
        }

        {
            JPanel p = new JPanel( new FormLayout() );
            add( p );
            
            {
                p.add( new JLabel( "Method" ) );
                JComboBox cb = new JComboBox();
                p.add( cb );
                cb.addItem( "ledgering" );
                cb.addItem( "float" );
                cb.addItem( "freelining" );
                cb.addItem( "spinning" );
                cb.addItem( "ice fishing" );
                new ComboBoxAdapter( cb, this, Catch.class, "getMethod", "setMethod", String.class, "method", null, null );
            }
            

            {
                p.add( new JLabel( "Bait" ) );
                JTextField tf = new JTextField( 20 );
                p.add( tf );
                new TextFieldAdapter( tf, this, Catch.class, "getBait", "setBait", "bait" );
                TextFieldFocusHandler.add( tf );
            }
        }
    }


    public Catch getCatch() { return (Catch) getModel(); }

    public void setCatch( Catch c )
    {
        setModel( c );
    }

    public void setEnabled( boolean e )
    {
        super.setEnabled( e );
        setEnabled( this, e );
    }

    private void setEnabled( java.awt.Container C, boolean e )
    {
        for
            ( java.awt.Component c : C.getComponents() )
        {
            c.setEnabled( e );
            if ( c instanceof java.awt.Container ) setEnabled( (java.awt.Container) c, e );
        }
    }
}
