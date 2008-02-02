package nu.esox.gui.aspect;


import java.lang.reflect.*;
import java.awt.event.*;
import nu.esox.util.*;
import javax.swing.*;


public class IndexedCheckBoxAdapter extends AbstractBooleanAdapter implements ActionListener
{
    private final AbstractButton m_button;
    
    public IndexedCheckBoxAdapter( int index, AbstractButton cb, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
        this( index, cb, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, boolean.class, aspectName, true, false, null, false );
    }
    
    public IndexedCheckBoxAdapter( int index,
                                   AbstractButton cb,
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
        super( modelOwner,
               new IndexedAspect( index, modelClass, getAspectMethodName, setAspectMethodName, aspectClass ),
               aspectName,
               trueValue,
               falseValue,
               nullValue,
               undefinedValue );

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
