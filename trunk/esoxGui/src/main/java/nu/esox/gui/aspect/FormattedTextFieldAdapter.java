package nu.esox.gui.aspect;


import java.awt.event.*;
import java.beans.*;
import javax.swing.*;


public class FormattedTextFieldAdapter extends AbstractAdapter implements ActionListener, PropertyChangeListener
{
    private final JFormattedTextField m_textField;

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
}
