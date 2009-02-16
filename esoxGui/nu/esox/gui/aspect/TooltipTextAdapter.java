package nu.esox.gui.aspect;


import nu.esox.util.*;
import javax.swing.*;


public class TooltipTextAdapter extends AbstractAdapter
{
    private final JComponent m_component;

    public TooltipTextAdapter( JComponent l, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName, String nullValue, String undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, nullValue, undefinedValue );

        m_component = l;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_component.setToolTipText( getTextFor( projectedValue ) );
    }

    protected String getTextFor( Object value )
    {
        return "" + value;
    }
}
