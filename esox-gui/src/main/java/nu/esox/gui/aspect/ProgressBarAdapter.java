package nu.esox.gui.aspect;


import java.util.function.*;
import nu.esox.util.*;
import javax.swing.*;


public class ProgressBarAdapter extends AbstractAdapter
{
    private final JProgressBar m_progressBar;

    public <M, V> ProgressBarAdapter( JProgressBar progressBar, ModelOwnerIF modelOwner, Function<M, V> getter, String aspectName )
    {
        super( modelOwner, getter, null, aspectName, (Integer) 0, (Integer) 0 );

        m_progressBar = progressBar;
        update();
    }

    public ProgressBarAdapter( JProgressBar progressBar, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String aspectName )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, (Integer) 0, (Integer) 0 );

        m_progressBar = progressBar;
        update();
    }

    protected void update( Object projectedValue )
    {
        m_progressBar.getModel().setValue( ( (Number) projectedValue ).intValue() );
    }
}
