package nu.esox.accounting;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.layout.*;
import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class NamedAndNumberedEditor extends ModelPanel
{
    public final boolean m_isNumberEditable;

    
    public NamedAndNumberedEditor( boolean isNumberEditable )
    {
        super( new ColumnLayout( 5, ! true, true ) );

        m_isNumberEditable = isNumberEditable;

        {
            JFormattedTextField t = new NumberTextField();
            add( new LabelPanel( t, "Nummer" ) );
            new FormattedTextFieldAdapter( t, this, NamedAndNumbered.class, "getNumber", m_isNumberEditable ? "setNumber" : null, int.class, null, null, null );
        }

        {
            JTextField t = new JTextField( 20 );
            add( new LabelPanel( t, "Namn" ) );
            new TextFieldAdapter( t, this, NamedAndNumbered.class, "getName", "setName", String.class, null, "", "" );
            TextFieldFocusHandler.add( t );
        }
    }

    public NamedAndNumbered getNamedAndNumbered() { return (NamedAndNumbered) getModel(); }

    public void setNamedAndNumbered( NamedAndNumbered namedAndNumbered ) { setModel( namedAndNumbered ); }



    public void setEnabled( boolean b ) // todo: enable adapter conflicts with FormattedTextFieldAdapter/TextFieldAdapter
    {
        super.setEnabled( b );
        ( (Container) getComponent( 0 ) ).getComponent( 1 ).setEnabled( b & m_isNumberEditable );
        ( (Container) getComponent( 1 ) ).getComponent( 1 ).setEnabled( b );
    }
    

    public static void main( String [] args )
    {
        JFrame f = new JFrame();

        NamedAndNumberedEditor e1 = new NamedAndNumberedEditor( true );

        JPanel p = new JPanel( new ColumnLayout() );
        f.getContentPane().add( p );
        p.add( e1 );

        NamedAndNumbered a = new NamedAndNumbered();

        e1.setNamedAndNumbered( a );

        f.pack();
        f.setVisible( true );
    }
}
