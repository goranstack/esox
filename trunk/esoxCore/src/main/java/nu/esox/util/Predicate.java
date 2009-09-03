package nu.esox.util;

public class Predicate extends Observable implements PredicateIF
{
    private boolean m_isTrue;
	
	
    public Predicate()
    {
        this( false );
    }
	
    public Predicate( boolean isTrue )
    {
        m_isTrue = isTrue;
    }
	
    public final boolean isTrue() { return m_isTrue; }
	
    public final void set( boolean isTrue )
    {
        if ( m_isTrue == isTrue ) return;
        m_isTrue = isTrue;
        fireValueChanged( null, null );
    }

    static final long serialVersionUID = 5806067845682464164L;
}
