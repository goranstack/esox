package nu.esox.gui.aspect;

import nu.esox.util.*;


public class DefaultModelOwner extends AbstractModelOwner
{
    private ObservableIF m_model;
    private Predicate m_hasModel = null;


    public DefaultModelOwner()
    {
    }
    
    public DefaultModelOwner( ObservableIF model )
    {
        setModel( model );
    }
    
    public final ObservableIF getModel() { return m_model; }

    public final void setModel( ObservableIF model )
    {
        if ( m_model == model ) return;

        ObservableIF old = m_model;
        
        preSetModel( old, model );
        m_model = model;
        if ( m_hasModel != null ) m_hasModel.set( m_model != null );
        postSetModel( old, m_model );
        fireModelAssigned( old, m_model );
    }

    protected void preSetModel( ObservableIF oldModel, ObservableIF newModel ) {}
    protected void postSetModel( ObservableIF oldModel, ObservableIF newModel ) {}

    public final PredicateIF getHasModel()
    {
        if
            ( m_hasModel == null )
        {
            m_hasModel = new Predicate();
            m_hasModel.set( m_model != null );
        }
        
        return m_hasModel;
    }
}
