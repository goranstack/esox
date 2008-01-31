package nu.esox.gui.aspect;

import nu.esox.util.*;
import javax.swing.*;


public class ButtonTextAdapter extends AbstractAdapter
{
    private final AbstractButton m_button;

    
    public ButtonTextAdapter( AbstractButton b, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName, String nullValue, String undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, nullValue, undefinedValue );
        m_button = b;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_button.setText( getTextFor( projectedValue ) );
    }

    protected String getTextFor( Object value )
    {
        return value.toString();
    }
}
