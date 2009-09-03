package nu.esox.util;


import java.io.*;
import java.util.*;


public class ObservableSet<T> extends ObservableCollection<T> implements Set<T>, Serializable
{
    public ObservableSet()
    {
        super();
    }

    public ObservableSet( Set<T> s )
    {
        super( s );
    }

    void setSet( Set<T> s )
    {
        setCollection( s );
    }

    @SuppressWarnings("unchecked")
    protected void itemChanged( ObservableEvent ev )
    {
          // reinsert changed item to maintain set integrity
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


    static final long serialVersionUID = -7206059221649125657L;
}
