package nu.esox.util;

public class PredicateProxy extends Observable implements PredicateIF, ObservableListener
{
    private PredicateIF m_delegate;
    
    public PredicateProxy( PredicateIF delegate )
    {
        setDelegate( delegate );
    }
    
    public final void setDelegate( PredicateIF delegate )
    {
        if ( delegate == m_delegate ) return;

        if ( m_delegate != null ) m_delegate.removeObservableListener( this );
        m_delegate = delegate;
        if ( m_delegate != null ) m_delegate.addObservableListener( this );
        fireValueChanged( null, null );
    }
	
    public final boolean isTrue()
    {
        return m_delegate != null && m_delegate.isTrue();
    }

    public void valueChanged( ObservableEvent ev )
    {
        fireValueChanged( null, null );
    }

    public void dispose( boolean deep )
    {
        setDelegate( null );
    }

    static final long serialVersionUID = -3808370187775586340L;
}
