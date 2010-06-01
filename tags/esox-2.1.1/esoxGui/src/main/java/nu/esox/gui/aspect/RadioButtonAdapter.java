package nu.esox.gui.aspect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import nu.esox.gui.aspect.AbstractAdapter;
import nu.esox.gui.aspect.ModelOwnerIF;

/**
 * Since JRadioButton has no capacity to store an object representing the choice
 * we use the adaptor it self for that matter. You have to accomplish the toggling 
 * behavour among the radio button by adding them to a ButtonGroup.
 * 
 * @author GStack
 *
 */
public class RadioButtonAdapter extends AbstractAdapter implements ActionListener
{
    private final JRadioButton m_radioButton;
    private final Object m_choice;
    private transient boolean m_isUpdating = false;

    
    public RadioButtonAdapter( JRadioButton radioButton,
                               ModelOwnerIF modelOwner,
                               Class modelClass,
                               String getAspectMethodName,
                               String setAspectMethodName,
                               Class aspectClass,
                               String aspectName,
                               Object choice )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, null, null );

        m_radioButton = radioButton;
        m_radioButton.addActionListener(this);
        m_choice = choice;
        update();
    }

    public void actionPerformed(ActionEvent ev)
    {
        if ( m_isUpdating ) return;	
        if ( m_radioButton.isSelected() ) setAspectValue( m_choice );
    }

    protected void update( Object projectedValue )
    {
        m_isUpdating = true;
        m_radioButton.setSelected( m_choice.equals( projectedValue ) );
        m_isUpdating = false;
    }

}
