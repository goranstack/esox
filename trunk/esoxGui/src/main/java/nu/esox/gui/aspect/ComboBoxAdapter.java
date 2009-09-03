package nu.esox.gui.aspect;


import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;


public class ComboBoxAdapter extends AbstractAdapter implements ActionListener
{
    private final JComboBox m_comboBox;
    private transient boolean m_isUpdating = false;

    
    public ComboBoxAdapter( JComboBox cb,
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

        m_comboBox = cb;
        m_comboBox.addActionListener( this );
        update();
    }


    public void actionPerformed( ActionEvent ev )
    {
        if ( m_isUpdating ) return;

        Object aspectValue = m_comboBox.getSelectedItem();
        
        if
            ( m_comboBox.getSelectedIndex() == -1 )
        {
            try
            {
                aspectValue = parseProjectedValue( (String) aspectValue );
            }
            catch ( ParseException ex )
            {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        }

        setAspectValue( aspectValue );
    }

    protected void update( Object projectedValue )
    {
        m_isUpdating = true;
        m_comboBox.setSelectedItem( projectedValue );
        m_isUpdating = false;
    }

    protected Object parseProjectedValue( String str ) throws ParseException
    {
        return str;
    }
}





    
