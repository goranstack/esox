package nu.esox.util;

public abstract class BinaryPredicate extends Predicate implements ObservableListener
{
    private final PredicateIF m_left;
    private final PredicateIF m_right;

    public BinaryPredicate( PredicateIF left, PredicateIF right )
    {
        m_left = left;
        m_right = right;
        set( isTrue( m_left, m_right ) );
        m_left.addObservableListener( this );
        m_right.addObservableListener( this );
    }
    
	public void valueChanged( ObservableEvent ev )
	{
      set( isTrue( m_left, m_right ) );
	}
	
	protected abstract boolean isTrue( PredicateIF left, PredicateIF right );
}
