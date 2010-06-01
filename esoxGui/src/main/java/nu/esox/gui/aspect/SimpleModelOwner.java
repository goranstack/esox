package nu.esox.gui.aspect;

import nu.esox.util.*;


public class SimpleModelOwner implements ModelOwnerIF
{
    private static final Predicate m_hasModel = new Predicate();
    
    private final ObservableIF m_model;

    public SimpleModelOwner( ObservableIF model )
    {
        m_model = model;
    }
    
    public void addListener( Listener l ) {}
    public void removeListener( Listener l ) {}

    public ObservableIF getModel() { return m_model; }
    public void setModel( ObservableIF model ) { assert false; }

    public PredicateIF getHasModel() { return m_hasModel; }

    static
    {
        m_hasModel.set( true );
    }
}
