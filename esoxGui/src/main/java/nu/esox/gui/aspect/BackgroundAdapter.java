package nu.esox.gui.aspect;


import nu.esox.util.*;
import javax.swing.*;
import java.awt.Color;


public class BackgroundAdapter extends AbstractAdapter
{
    private final JComponent m_component;

    public BackgroundAdapter( JComponent c, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName, Object nullValue, Object undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, nullValue, undefinedValue );

        m_component = c;
        update();
    }

    protected void update( Object projectedValue )
    {
        Color c = getColorFor( projectedValue );
        if
            ( c == null )
        {
            m_component.setOpaque( false );
        } else {
            m_component.setBackground( c );
            m_component.setOpaque( true );
        }
    }

    protected Color getColorFor( Object value )
    {
        return null;
    }
}
