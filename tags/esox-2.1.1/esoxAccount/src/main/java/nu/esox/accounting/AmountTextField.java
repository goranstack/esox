package nu.esox.accounting;

import java.text.*;
import javax.swing.*;
//import nu.esox.swing.*;
import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class AmountTextField extends JFormattedTextField
{
    public static final DecimalFormat FORMAT = new DecimalFormat( "####0.00" );

    
    public AmountTextField()
    {
        super( new JFormattedTextField.AbstractFormatter()
            {
                public Object stringToValue( String text ) { return AmountTextField.parse( text ); }
                public String valueToString( Object value ) { return AmountTextField.format( value ); }
            } );
        
        setColumns( 8 );
        setHorizontalAlignment( JTextField.RIGHT );
        setFocusLostBehavior( COMMIT );
    }

    
    public static String format(Object value )
    {
        if ( value == null ) return "";
        if ( Transaction.isAmountUndefined( (Double) value ) ) return "";
        return FORMAT.format( value );
    }

    public static Object parse( String text )
    {
        if ( text == null ) return Transaction.AMOUNT_UNDEFINED;
        if ( text.length() == 0 ) return Transaction.AMOUNT_UNDEFINED;
        try
        {
            return new Double( FORMAT.parse( text ).doubleValue() );
        }
        catch ( ParseException ex )
        {
            return null;
        }
    }
}
