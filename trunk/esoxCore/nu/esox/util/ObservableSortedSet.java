package nu.esox.util;


import java.io.*;
import java.util.*;


public class ObservableSortedSet<T> extends ObservableCollection<T> implements SortedSet<T>, Serializable
{
    public ObservableSortedSet()
    {
        super();
    }

    public ObservableSortedSet( SortedSet<T> s )
    {
        super( s );
    }

    public void setSortedSet( SortedSet<T> s )
    {
        setCollection( s );
    }

    protected void itemChanged( ObservableEvent ev )
    {
          // reinsert changed item to maintain sorted set integrity
        T item = (T) ev.getObservable();
        Iterator i = m_collection.iterator();
        while
            ( i.hasNext() )
        {
            if
                ( i.next() == item )
            {
                i.remove();
                m_collection.add( item );
                break;
            }
        }
        super.itemChanged( ev );
    }

    public SortedSet<T> tailSet( T o ) { return ( (SortedSet<T>) m_collection ).tailSet( o ); }
    public T first() { return ( (SortedSet<T>) m_collection ).first(); }
    public SortedSet<T> subSet( T o, T p ) { return ( (SortedSet<T>) m_collection ).subSet( o, p ); }
    public T last() { return ( (SortedSet<T>) m_collection ).last(); }
    public Comparator<? super T> comparator() { return ( (SortedSet<T>) m_collection ).comparator(); }
    public SortedSet<T> headSet( T o ) { return ( (SortedSet<T>) m_collection ).headSet( o ); }

    static final long serialVersionUID = -5336524341001433505L;
}
