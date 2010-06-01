package nu.esox.gui.aspect;


import java.awt.event.*;
import java.beans.*;
import javax.swing.*;


public class FormattedTextFieldAdapter extends AbstractAdapter implements ActionListener, PropertyChangeListener
{
    private final JFormattedTextField m_textField;
    private final Converter m_converter;

    public FormattedTextFieldAdapter( JFormattedTextField tf,
                                      ModelOwnerIF modelOwner,
                                      Class modelClass,
                                      String getAspectMethodName,
                                      String setAspectMethodName,
                                      Class aspectClass,
                                      String aspectName, 
                                      Object nullValue,
                                      Object undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, nullValue, undefinedValue );

        if       ( Long.class.equals( aspectClass ) ) m_converter = LONG_CONVERTER;
        else if ( Integer.class.equals( aspectClass ) ) m_converter = INTEGER_CONVERTER;
        else if ( Short.class.equals( aspectClass ) ) m_converter = SHORT_CONVERTER;
        else if ( Byte.class.equals( aspectClass ) ) m_converter = BYTE_CONVERTER;
        else if ( Float.class.equals( aspectClass ) ) m_converter = FLOAT_CONVERTER;
        else if ( Double.class.equals( aspectClass ) ) m_converter = DOUBLE_CONVERTER;
        else if ( long.class.equals( aspectClass ) ) m_converter = LONG_CONVERTER;
        else if ( int.class.equals( aspectClass ) ) m_converter = INTEGER_CONVERTER;
        else if ( short.class.equals( aspectClass ) ) m_converter = SHORT_CONVERTER;
        else if ( byte.class.equals( aspectClass ) ) m_converter = BYTE_CONVERTER;
        else if ( float.class.equals( aspectClass ) ) m_converter = FLOAT_CONVERTER;
        else if ( double.class.equals( aspectClass ) ) m_converter = DOUBLE_CONVERTER;
        else m_converter = DEFAULT_CONVERTER;
        
        m_textField = tf;
        m_textField.addActionListener( this );
        m_textField.addPropertyChangeListener( "value", this );
        m_textField.setEditable( setAspectMethodName != null );
        update();
    }


    public final void actionPerformed( ActionEvent ev )
    {
        setAspectValue( m_textField.getValue() );
    }
    
    public void propertyChange( PropertyChangeEvent ev )
    {
        setAspectValue( m_textField.getValue() ); 
    }

    protected void update( Object projectedValue )
    {
        m_textField.setValue( projectedValue );
    }
    
    protected Object deriveAspectValue( Object projectedValue )
    {
        Object aspectValue = super.deriveAspectValue( projectedValue );
        if ( aspectValue != null ) aspectValue = m_converter.convert( aspectValue );
        return aspectValue;
    }



    private interface Converter
    {
        public Object convert( Object value );
    }

    private static Converter DEFAULT_CONVERTER = new Converter() { public Object convert( Object value ) { return value; } };
    private static Converter LONG_CONVERTER = new Converter() { public Object convert( Object value ) { return toNumber( value ).longValue(); } };
    private static Converter INTEGER_CONVERTER = new Converter() { public Object convert( Object value ) { return toNumber( value ).intValue(); } };
    private static Converter SHORT_CONVERTER = new Converter() { public Object convert( Object value ) { return toNumber( value ).shortValue(); } };
    private static Converter BYTE_CONVERTER = new Converter() { public Object convert( Object value ) { return toNumber( value ).byteValue(); } };
    private static Converter FLOAT_CONVERTER = new Converter() { public Object convert( Object value ) { return toNumber( value ).floatValue(); } };
    private static Converter DOUBLE_CONVERTER = new Converter() { public Object convert( Object value ) { return toNumber( value ).doubleValue(); } };






    private static Number toNumber( Object value )
    {
        if ( value instanceof Number ) return (Number) value;
        
        try
        {
            return Long.parseLong( value.toString() );
        }
        catch ( NumberFormatException tryDouble ) {}
        
        try
        {
            return Double.parseDouble( value.toString() );
        }
        catch ( NumberFormatException tryDouble ) {}

        assert false;
        return null;
    }
    
}
