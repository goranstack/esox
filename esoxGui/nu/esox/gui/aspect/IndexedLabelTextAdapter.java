package nu.esox.gui.aspect;


import java.lang.reflect.*;
import nu.esox.util.*;
import javax.swing.*;


public class IndexedLabelTextAdapter extends AbstractAdapter
{
    private final JLabel m_label;

    public IndexedLabelTextAdapter( int index, JLabel l, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName, String nullValue, String undefinedValue )
    {
        super( modelOwner,
               new IndexedAspect( index, modelClass, getAspectMethodName, null, null ),
               aspectName,
               nullValue,
               undefinedValue );

        m_label = l;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_label.setText( getTextFor( projectedValue ) );
    }

    protected String getTextFor( Object value )
    {
        return "" + value;
    }
}
