package nu.esox.gui.aspect;


import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;


public class ComboBoxIndexAdapter extends AbstractAdapter implements ActionListener
{
    private final JComboBox m_comboBox;
    private transient boolean m_isUpdating = false;

    
    public ComboBoxIndexAdapter( JComboBox cb,
                                 ModelOwnerIF modelOwner,
                                 Class modelClass,
                                 String getAspectMethodName,
                                 String setAspectMethodName,
                                 String aspectName )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, int.class, aspectName, null, null );

        m_comboBox = cb;
        m_comboBox.addActionListener( this );
        update();
    }


    public void actionPerformed( ActionEvent ev )
    {
        if ( m_isUpdating ) return;
        setAspectValue( (Integer) m_comboBox.getSelectedIndex() );
    }

    protected void update( Object index )
    {
        m_isUpdating = true;
        if ( index != null ) m_comboBox.setSelectedIndex( (Integer) index );
        m_isUpdating = false;
    }
}





    
