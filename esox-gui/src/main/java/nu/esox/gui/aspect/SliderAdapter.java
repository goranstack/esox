package nu.esox.gui.aspect;


import javax.swing.*;
import javax.swing.event.*;


public class SliderAdapter extends AbstractAdapter implements ChangeListener
{
    private final JSlider m_slider;
    
    public SliderAdapter( JSlider slider, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName, int nullValue, int undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, Integer.class, aspectName, new Integer( nullValue ), new Integer( undefinedValue ) );

        m_slider = slider;
        m_slider.addChangeListener( this );
        update();
    }

    
    public void stateChanged( ChangeEvent ev )
    {
        Integer aspectValue = m_slider.getValue();
        setAspectValue( aspectValue );
    }

    protected void update( Object projectedValue )
    {
        m_slider.setValue( ( (Number) projectedValue ).intValue() );
    }
}





    
