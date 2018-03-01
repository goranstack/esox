package nu.esox.util;

import java.io.*;
import java.util.*;


public class ObservableCollection<T> extends Observable implements ObservableCollectionIF<T>, Serializable
{
    protected Collection<T> m_collection;


    protected transient ObservableListener m_listener;

    
    public ObservableCollection()
    {
        init();
    }

    public ObservableCollection( Collection<T> c )
    {
        setCollection( c );
    }
    


    protected void itemChanged( ObservableEvent ev )
    {
    }

    @SuppressWarnings("unchecked")
    protected ObservableEvent createItemChangedEvent( ObservableEvent ev )
    {
        return new ObservableCollectionEvent<T>( this, "Item changed: " + ev.getInfo(), (T) ev.getObservable(), ObservableCollectionEvent.ITEM_CHANGED );
    }
     
    private void init()
    {
        m_listener =
            new ObservableListener()
            {
                public void valueChanged( ObservableEvent ev )
                {
                    itemChanged( ev );
                    fireValueChanged( createItemChangedEvent( ev ) );
                }
            };
        
        if
            ( m_collection != null )
        {
            for ( T o : m_collection ) listenTo( o );
        }
    }
    

    void setCollection( Collection<T> c )
    {
        m_collection = c;
        init();
    }

    

    protected void listenTo( Object o )
    {
        if ( o instanceof ObservableIF ) ( (ObservableIF) o ).addObservableListener( m_listener );
    }


    protected void unlistenTo( Object o )
    {
        if ( o instanceof ObservableIF ) ( (ObservableIF) o ).removeObservableListener( m_listener );
    }

    
    public boolean contains( Object o ) { return m_collection.contains( o ); } 
    public boolean containsAll( Collection<?> c ) { return m_collection.containsAll( c ); } 
    public boolean equals( Object o ) { return m_collection.equals( o ); } 
    public int hashCode() { return m_collection.hashCode(); } 
    public boolean isEmpty() { return m_collection.isEmpty(); } 
    public Iterator<T> iterator() { return m_collection.iterator(); } 
    public int size() { return m_collection.size(); } 
    public Object[] toArray() { return m_collection.toArray(); } 
    public <T> T[] toArray( T[] a ) { return m_collection.toArray( a ); }

 
    public boolean add( T o )
    {
        boolean b = m_collection.add( o );
        if
            ( b )
        {
            listenTo( o );
            fireValueChanged( new ObservableCollectionEvent<T>( this,  "item added: " + o, o, ObservableCollectionEvent.ADDED ) );
        }
        return b;
    }

    
    public boolean addAll( Collection<? extends T> c )
    {
        boolean b = m_collection.addAll( c );
        if
            ( b )
        {
            for ( T o : c ) listenTo( o );
            fireValueChanged( new ObservableCollectionEvent<T>( this,  "items added: " + c, null, ObservableCollectionEvent.ADDED ) ); // PENDING: send c
        }
        return b;
    }
    
    public void clear()
    {
        for ( T o : m_collection ) unlistenTo( o );

        boolean b = ! isEmpty();
        m_collection.clear();
        if
            ( b )
        {
            fireValueChanged( new ObservableCollectionEvent<T>( this,  "all items removed", null, ObservableCollectionEvent.REMOVED ) );
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public boolean remove( Object o )
    {
        boolean b = m_collection.remove( o );
        if
            ( b )
        {
            unlistenTo( o );
            fireValueChanged( new ObservableCollectionEvent<T>( this,  "item removed: " + o, (T) o, ObservableCollectionEvent.REMOVED ) );
        }
        return b;
    }
    
    public boolean removeAll( Collection c )
    {
        boolean b = m_collection.removeAll( c );
        if
            ( b )
        {
            for ( Object o : c ) unlistenTo( o );
            fireValueChanged( new ObservableCollectionEvent<T>( this,  "items removed: " + c, null, ObservableCollectionEvent.REMOVED ) ); // PENDING: send c
        }
        return b;
    }
    
    public boolean retainAll( Collection c )
    {
          //PENDING: removeObservableListener( m_listener );
        boolean b = m_collection.retainAll( c );
        if
            ( b )
        {
            fireValueChanged( new ObservableCollectionEvent<T>( this,  "items retained: " + c, null, ObservableCollectionEvent.REMOVED ) ); // PENDING: send c'
        }
        return b;
    }





    
    private void readObject( ObjectInputStream stream ) throws IOException
    {
        try
        {
            stream.defaultReadObject();
            init();
        }
        catch ( ClassNotFoundException e )
        {
            System.err.println( "KNAS " + e );
        }

    }



    public String toString()
    {
        return m_collection.toString();
    }


    static final long serialVersionUID = -3808370187775586340L;

}
