package nu.esox.gui.aspect;


import nu.esox.util.*;
import javax.swing.*;
import javax.swing.event.*;


public class SpinnerAdapter extends AbstractAdapter implements ChangeListener
{
    private final JSpinner m_spinner;

    
    public SpinnerAdapter( JSpinner spinner,
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

        m_spinner = spinner;
        m_spinner.addChangeListener( this );
        update();
    }

    
    public void stateChanged( ChangeEvent ev )
    {
        setAspectValue( m_spinner.getValue() );
    }

    protected void update( Object projectedValue )
    {
        m_spinner.setValue( projectedValue );
    }
}





    
