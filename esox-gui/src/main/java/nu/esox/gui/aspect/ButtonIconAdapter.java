package nu.esox.gui.aspect;

import java.util.function.*;
import nu.esox.util.*;
import javax.swing.*;


public class ButtonIconAdapter extends AbstractAdapter
{
    private final AbstractButton m_button;


    public <M> ButtonIconAdapter( AbstractButton b,
                                   ModelOwnerIF modelOwner,
                                   Function<M, ?> getter,
                                   String aspectName,
                                   String nullValue,
                                   String undefinedValue )
    {
        super( modelOwner, getter, null, aspectName, nullValue, undefinedValue );
        m_button = b;
        update();
    }

    public ButtonIconAdapter( AbstractButton b,
                              ModelOwnerIF modelOwner,
                              Class modelClass,
                              String getAspectMethodName,
                              String aspectName,
                              String nullValue,
                              String undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, nullValue, undefinedValue );
        m_button = b;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_button.setIcon( getIconFor( projectedValue ) );
    }
    
    protected Icon getIconFor( Object projectedValue )
    {
        return null;
    }
}
