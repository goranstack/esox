package nu.esox.gui;

import java.awt.LayoutManager;
import javax.swing.JPanel;
import nu.esox.util.*;
import nu.esox.gui.aspect.*;


public class ModelPanel extends JPanel implements ModelOwnerIF
{
    private final DefaultModelOwner m_modelOwner =
        new DefaultModelOwner()
        {
            protected void preSetModel( ObservableIF oldModel, ObservableIF newModel )
            {
                super.preSetModel( oldModel, newModel );
                ModelPanel.this.preSetModel( oldModel, newModel );
            }
            
            protected void postSetModel( ObservableIF oldModel, ObservableIF newModel )
            {
                super.postSetModel( oldModel, newModel );
                ModelPanel.this.postSetModel( oldModel, newModel );
            }
        };

    
    public ModelPanel()
    {
    }
    
    public ModelPanel( LayoutManager lm )
    {
        super( lm );
    }

    public final void addListener( ModelOwnerIF.Listener l ) { m_modelOwner.addListener( l ); }
    public final void removeListener( ModelOwnerIF.Listener l ) { m_modelOwner.removeListener( l ); }
    public final ObservableIF getModel() { return m_modelOwner.getModel(); }
    public final PredicateIF getHasModel() { return m_modelOwner.getHasModel(); }
    public final void setModel( ObservableIF model ) { m_modelOwner.setModel( model ); }

    protected void preSetModel( ObservableIF oldModel, ObservableIF newModel ) {}
    protected void postSetModel( ObservableIF oldModel, ObservableIF newModel ) {}
}
