package nu.esox.gui.aspect;


import nu.esox.util.*;
import javax.swing.*;


public class LabelIconAdapter extends AbstractAdapter
{
    private final JLabel m_label;

    public LabelIconAdapter( JLabel l, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName, String nullValue, String undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, nullValue, undefinedValue );

        m_label = l;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_label.setIcon( getIconFor( projectedValue ) );
    }
    
    protected Icon getIconFor( Object projectedValue )
    {
        return null;
    }
}
