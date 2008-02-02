package nu.esox.util;


import java.io.*;
import java.util.*;


public class ObservableList<T> extends ObservableCollection<T> implements ObservableListIF<T>, Serializable
{
//     public static final ObservableList EMPTY = new ObservableList<Object>( Collections.EMPTY_LIST );

    
    public ObservableList()
    {
        this( new ArrayList<T>() );
    }

    public ObservableList( List<T> l )
    {
        super( l );
    }

    void setList( List<T> l )
    {
        setCollection( l );
    }

    
    @SuppressWarnings("unchecked")
    protected ObservableEvent createItemChangedEvent( ObservableEvent ev )
    {
        return new ObservableListEvent<T>( this, "Item changed: " + ev.getInfo(), (T) ev.getObservable(), ObservableCollectionEvent.ITEM_CHANGED, indexOf( ev.getObservable() ) );
    }
 
    
    public int lastIndexOf( Object o ) { return ( (List<T>) m_collection ).lastIndexOf( o ); }
    public int indexOf( Object o ) { return ( (List<T>) m_collection ).indexOf( o ); }
    public ListIterator<T> listIterator() { return ( (List<T>) m_collection ).listIterator(); }
    public ListIterator<T> listIterator( int i ) { return ( (List<T>) m_collection ).listIterator( i ); }
    public T get( int i ) { return ( (List<T>) m_collection ).get( i ); }
    public List<T> subList( int i, int j ) { return ( (List<T>) m_collection ).subList( i, j ); }



    
    public boolean add( T o )
    {
        boolean b = m_collection.add( o );
        if
            ( b )
        {
            listenTo( o );
            fireValueChanged( new ObservableListEvent<T>( this, "item added: " + o, o, ObservableListEvent.ADDED, indexOf( o ) ) );
        }
        return b;
    }

    public void add( int index, T o )
    {
        ( (List<T>) m_collection ).add( index, o );
        listenTo( o );
        fireValueChanged( new ObservableListEvent<T>( this, "item added at: " + index + ": " + o, o, ObservableListEvent.ADDED, index ) );
    }
    
    public boolean addAll( Collection<? extends T> c )
    {
        boolean b = m_collection.addAll( c );
        if
            ( b )
        {
            for ( T o : c ) listenTo( o );
            fireValueChanged( new ObservableListEvent<T>( this,  "items added: " + c, null, ObservableCollectionEvent.ADDED, -1 ) ); // PENDING: send c
        }
        return b;
    }
    
    public boolean addAll( int index, Collection<? extends T> c )
    {
        boolean b = ( (List<T>) m_collection ).addAll( index, c );
        if
            ( b )
        {
            for ( T o : c ) listenTo( o );
            fireValueChanged( new ObservableListEvent<T>( this, "items added at: " + index + ": " + c, null, ObservableListEvent.ADDED, -1 ) );
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
            fireValueChanged( new ObservableListEvent<T>( this,  "all items removed", null, ObservableCollectionEvent.REMOVED, -1 ) );
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public boolean remove( Object o )
    {
        int i = indexOf( o );
        boolean b = m_collection.remove( o );
        if
            ( b )
        {
            unlistenTo( o );
            fireValueChanged( new ObservableListEvent<T>( this, "item removed: " + o, (T) o, ObservableListEvent.REMOVED, i ) );
        }
        return b;
    }
    
    public T remove( int index )
    {
        T o = ( (List<T>) m_collection ).remove( index );
        unlistenTo( o );
        fireValueChanged( new ObservableListEvent<T>( this, "item removed: " + o, o, ObservableListEvent.REMOVED, index ) );
        return o;
    }
    
    public boolean removeAll( Collection c )
    {
        boolean b = m_collection.removeAll( c );
        if
            ( b )
        {
            for ( Object o : c ) unlistenTo( o );
            fireValueChanged( new ObservableListEvent<T>( this,  "items removed: " + c, null, ObservableCollectionEvent.REMOVED, -1 ) ); // PENDING: send c
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
            fireValueChanged( new ObservableListEvent<T>( this,  "items retained: " + c, null, ObservableCollectionEvent.REMOVED, -1 ) ); // PENDING: send c'
        }
        return b;
    }
    
    public T set( int index, T element )
    {
        T o = ( (List<T>) m_collection ).set( index, element );
        unlistenTo( o );
        listenTo( element );
        fireValueChanged( new ObservableListEvent<T>( this, "item set at " + index +": " + element, o, element, index ) );
        return o;
    }
    



    
    static final long serialVersionUID = 7950292058978163757L;

}
