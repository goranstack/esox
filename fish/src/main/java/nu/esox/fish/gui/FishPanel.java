package nu.esox.fish.gui;

import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.layout.*;
import nu.esox.util.*;
import nu.esox.fish.domain.*;


@SuppressWarnings( "serial" )
public class FishPanel extends ModelPanel
{
    public FishPanel()
    {
        super( new FormLayout() );

        {
            add( new JLabel( "Species" ) );
            JComboBox cb = new JComboBox();
            add( cb );
            cb.addItem( "eel" );
            cb.addItem( "perch" );
            cb.addItem( "pike" );
            cb.addItem( "zander" );
            new ComboBoxAdapter( cb, this, Fish.class, "getSpecies", "setSpecies", String.class, "species", null, null );
        }

        {
            add( new JLabel( "Weight" ) );
            JFormattedTextField tf = new JFormattedTextField( new DecimalFormat( "0.000" ) );
            add( tf );
            new FormattedTextFieldAdapter( tf, this, Fish.class, "getWeight", "setWeight", double.class, "weight", null, null );
            TextFieldFocusHandler.add( tf );
        }
        
        {
            add( new JLabel( "Length" ) );
            JFormattedTextField tf = new JFormattedTextField( new DecimalFormat( "0" ) );
            add( tf );
            new FormattedTextFieldAdapter( tf, this, Fish.class, "getLength", "setLength", double.class, "length", null, null );
            TextFieldFocusHandler.add( tf );
        }
        
        {
            add( new JLabel( "Girth" ) );
            JFormattedTextField tf = new JFormattedTextField( new DecimalFormat( "0" ) );
            add( tf );
            new FormattedTextFieldAdapter( tf, this, Fish.class, "getGirth", "setGirth", double.class, "girth", null, null );
            TextFieldFocusHandler.add( tf );
        }
    }


    public Fish getFish() { return (Fish) getModel(); }

    public void setFish( Fish fish )
    {
        setModel( fish );
    }
}
