package nu.esox.gui.aspect;


import java.awt.event.*;
import nu.esox.util.*;
import javax.swing.*;


public class ToggleButtonAdapter extends AbstractBooleanAdapter implements ActionListener
{
    private final JToggleButton m_button;
    
    public ToggleButtonAdapter( JToggleButton b, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
        this( b, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, Boolean.class, aspectName, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE );
    }
    
    public ToggleButtonAdapter( JToggleButton b,
                                ModelOwnerIF modelOwner,
                                Class modelClass,
                                String getAspectMethodName,
                                String setAspectMethodName,
                                Class aspectClass,
                                String aspectName,
                                Object trueValue,
                                Object falseValue,
                                Object nullValue,
                                Object undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, trueValue, falseValue, nullValue, undefinedValue );

        m_button = b;
        m_button.addActionListener( this );
        update();
    }


    public void actionPerformed( ActionEvent ev )
    {
        setAspectValue( m_button.isSelected() ? Boolean.TRUE : Boolean.FALSE );
    }

    
    protected void update( Object projectedValue )
    {
        m_button.setSelected( ( (Boolean) projectedValue ).booleanValue() );
    }
}
