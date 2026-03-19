package nu.esox.gui.aspect;


import java.util.function.*;
import nu.esox.util.*;
import javax.swing.*;


public class VisibleAdapter extends AbstractBooleanAdapter
{
    private final JComponent m_component;


    public <M, V> VisibleAdapter( JComponent c,
                                ModelOwnerIF modelOwner,
                                Function<M, V> getter,
                                String aspectName,
                                Object trueValue,
                                Object falseValue,
                                Object nullValue,
                                Object undefinedValue )
    {
        super( modelOwner, getter, null, aspectName, trueValue, falseValue, nullValue, undefinedValue );
        m_component = c;
        update();
    }

    public VisibleAdapter( JComponent c,
                           ModelOwnerIF modelOwner,
                           Class modelClass,
                           String getAspectMethodName,
                           String aspectName,
                           Object trueValue,
                           Object falseValue,
                           Object nullValue,
                           Object undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, trueValue, falseValue, nullValue, undefinedValue );
        m_component = c;
        update();
    }


    protected void update( Object projectedValue )
    {
        m_component.setVisible( (Boolean) projectedValue );
    }
}
