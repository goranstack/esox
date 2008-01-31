package nu.esox.gui;

import java.awt.Dialog;
import java.awt.Frame;
import javax.swing.JDialog;
import nu.esox.util.*;
import nu.esox.gui.aspect.*;


public class ModelDialog extends JDialog implements ModelOwnerIF
{
    private final DefaultModelOwner m_modelOwner =
        new DefaultModelOwner()
        {
            protected void preSetModel( ObservableIF oldModel, ObservableIF newModel )
            {
                super.preSetModel( oldModel, newModel );
                ModelDialog.this.preSetModel( oldModel, newModel );
            }
            
            protected void postSetModel( ObservableIF oldModel, ObservableIF newModel )
            {
                super.postSetModel( oldModel, newModel );
                ModelDialog.this.postSetModel( oldModel, newModel );
            }
        };

    
    public ModelDialog( Dialog owner, String title, boolean modal )
    {
        super( owner, title, modal );
    }
    
    public ModelDialog( Frame owner, String title, boolean modal )
    {
        super( owner, title, modal );
    }

    public final void addListener( ModelOwnerIF.Listener l ) { m_modelOwner.addListener( l ); }
    public final void removeListener( ModelOwnerIF.Listener l ) { m_modelOwner.removeListener( l ); }
    public final ObservableIF getModel() { return m_modelOwner.getModel(); }
    public final PredicateIF getHasModel() { return m_modelOwner.getHasModel(); }
    public final void setModel( ObservableIF model ) { m_modelOwner.setModel( model ); }

    protected void preSetModel( ObservableIF oldModel, ObservableIF newModel ) {}
    protected void postSetModel( ObservableIF oldModel, ObservableIF newModel ) {}
}
