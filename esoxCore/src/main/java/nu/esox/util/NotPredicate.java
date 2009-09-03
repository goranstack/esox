package nu.esox.util;

public class NotPredicate extends Predicate implements ObservableListener
{
    private final PredicateIF m_operand;

    public NotPredicate( PredicateIF operand )
    {
        m_operand = operand;
        m_operand.addObservableListener( this );
        set( ! m_operand.isTrue() );
    }
    
    public void valueChanged( ObservableEvent ev )
    {
        set( ! m_operand.isTrue() );
    }

    static final long serialVersionUID = -3808370187775586340L;
}
