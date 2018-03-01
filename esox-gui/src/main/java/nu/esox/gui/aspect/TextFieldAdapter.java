package nu.esox.gui.aspect;

import java.awt.event.*;
import nu.esox.util.*;
import javax.swing.*;


public class TextFieldAdapter extends AbstractAdapter implements ActionListener
{
    private final JTextField m_textField;

    
    public TextFieldAdapter( JTextField textField, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
        this( textField, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, String.class, aspectName, "", "" );
    }
    
    public TextFieldAdapter( JTextField textField, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, Class aspectClass, String aspectName )
    {
        this( textField, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, "", "" );
    }
    
    public TextFieldAdapter( JTextField textField,
                             ModelOwnerIF modelOwner,
                             Class modelClass,
                             String getAspectMethodName,
                             String setAspectMethodName,
                             Class aspectClass,
                             String aspectName,
                             String nullValue,
                             String undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, nullValue, undefinedValue );

        assert nullValue != null;
        assert undefinedValue != null;

        m_textField = textField;
        m_textField.addActionListener( this );
        m_textField.setEditable( setAspectMethodName != null );
        update();
    }
    
            
    public final void actionPerformed( ActionEvent ev )
    {
        setAspectValue( m_textField.getText() );
    }

    protected void update( Object projectedValue )
    {
        m_textField.setText( "" + projectedValue );
    }
}
