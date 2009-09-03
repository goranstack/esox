package nu.esox.gui.aspect;

import java.util.*;
import nu.esox.util.ObservableIF;


public abstract class AbstractModelOwner implements ModelOwnerIF
{
    private final LinkedList<ModelOwnerIF.Listener> m_listeners = new LinkedList<ModelOwnerIF.Listener>();

    
    public final void addListener( ModelOwnerIF.Listener l )
    {
        m_listeners.add( l );
    }
            
    public final void removeListener( ModelOwnerIF.Listener l )
    {
        m_listeners.remove( l );
    }

    protected final void fireModelAssigned( ObservableIF oldModel, ObservableIF newModel )
    {
        for ( ModelOwnerIF.Listener l : m_listeners ) l.modelAssigned( oldModel, newModel );
    }
}
