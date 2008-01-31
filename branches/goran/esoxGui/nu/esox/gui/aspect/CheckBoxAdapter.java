package nu.esox.gui.aspect;


import java.awt.event.*;
import nu.esox.util.*;
import javax.swing.*;


public class CheckBoxAdapter extends AbstractBooleanAdapter implements ActionListener
{
    private final AbstractButton m_button;
    
    public CheckBoxAdapter( AbstractButton cb, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
//        this( cb, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, Boolean.class, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE );
        this( cb, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, boolean.class, aspectName, true, false, null, false );
    }
    
    public CheckBoxAdapter( AbstractButton cb,
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

        m_button = cb;
        m_button.addActionListener( this );
        update();
    }


    public void actionPerformed( ActionEvent ev )
    {
        setAspectValue( m_button.isSelected() ? Boolean.TRUE : Boolean.FALSE );
    }

    
    protected void update( Object projectedValue )
    {
        m_button.setSelected( (Boolean) projectedValue );
    }
}
