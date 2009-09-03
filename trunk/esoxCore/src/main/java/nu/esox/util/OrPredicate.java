package nu.esox.util;


public class OrPredicate extends BinaryPredicate
{
    public OrPredicate( PredicateIF left, PredicateIF right )
    {
        super( left, right );
    }

    protected boolean isTrue( PredicateIF left, PredicateIF right )
    {
        return left.isTrue() || right.isTrue();
    }

    static final long serialVersionUID = 5806067845682464164L;
}
