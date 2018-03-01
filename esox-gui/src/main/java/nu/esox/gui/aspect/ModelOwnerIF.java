package nu.esox.gui.aspect;

import nu.esox.util.*;


public interface ModelOwnerIF
{
    interface Listener
    {
        void modelAssigned( ObservableIF oldModel, ObservableIF newModel );
    }
    
    void addListener( Listener l );
    void removeListener( Listener l );

    ObservableIF getModel();
    void setModel( ObservableIF model );
    PredicateIF getHasModel();
}
