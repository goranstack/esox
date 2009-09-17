package nu.esox.accounting;

import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class NumberTextField extends JFormattedTextField
{
    public static final DecimalFormat FORMAT = new DecimalFormat( "0000" );
    

    
    public NumberTextField()
    {
        setFormatterFactory( new FormatterFactory() );
        setColumns( 4 );
        setHorizontalAlignment( JTextField.RIGHT );
        TextFieldFocusHandler.add( this );
        setInputVerifier( new NumberVerifier() );
    }

    
    protected void verify( int value ) throws ParseException {}


    private class NumberVerifier extends InputVerifier
    {
        public boolean verify( JComponent input )
        {
            try
            {
                ( (Number) getFormatter().stringToValue( getText() ) ).intValue();
                return true;
            }
            catch ( java.text.ParseException ex )
            {
                return false;
            }
        }
        
        public boolean shouldYieldFocus( JComponent input )
        {
            return verify( input );
        }
    }

    
    private class NumberFormatter extends JFormattedTextField.AbstractFormatter
    {
        public Object stringToValue( String text ) throws ParseException
        {
            if ( text == null ) throw new ParseException( "", 0 );
            if ( text.length() == 0 ) throw new ParseException( "", 0 );
            int i = FORMAT.parse( text ).intValue();
            verify( i );
            return (Integer) i;
        }
        
        public String valueToString( Object value )
        {
            return ( ( value == null || ( ( (Number) value ).intValue() == 0 ) ) ) ? "" : FORMAT.format( value );
        }
    }

    
    private class FormatterFactory extends JFormattedTextField.AbstractFormatterFactory
    {
        private final NumberFormatter m_formatter = new NumberFormatter();
        
        public JFormattedTextField.AbstractFormatter	getFormatter( JFormattedTextField tf )
        {
            return m_formatter;
        }

    }
}
