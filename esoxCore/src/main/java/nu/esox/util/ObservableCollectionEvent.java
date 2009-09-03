package nu.esox.util;


public class ObservableCollectionEvent<T> extends ObservableEvent
{
    public static final int ADDED = 0;
    public static final int REMOVED = 1;
    public static final int REPLACED = 2;
    public static final int ITEM_CHANGED = 3;
    
    private T m_item;
    private T m_oldItem;
    private int m_operation;
    
    public String toString()
    {
        String s = super.toString() + " : ";
        if ( m_operation == ADDED ) s += "ADDED ";
        if ( m_operation == REMOVED ) s += "REMOVED ";
        if ( m_operation == REPLACED ) s += "REPLACED ";
        if ( m_operation == ITEM_CHANGED ) s += "ITEM_CHANGED ";
        s += m_item + " " + m_oldItem;
        return s;
    }
   
    protected ObservableCollectionEvent( ObservableIF source, String info, T item, int operation )
    {
        super( source, info, null );
        
        m_item = item;
        m_operation = operation;
    }
    
    protected ObservableCollectionEvent( Observable source, String info, T item, T oldItem )
    {
        super( source, info, null );

        m_item = item;
        m_oldItem = oldItem;
        m_operation = REPLACED;
    }

    
    public T getItem()
    {
        return m_item;
    }

    public int getOperation()
    {
        return m_operation;
    }
    
    public T getOldItem()
    {
        return m_oldItem;
    }


    static final long serialVersionUID = -7577320995222980806L;



//     public static ObservableCollectionEvent create( ObservableIF source, String info, Object item, int operation )
//     {
//         return new ObservableCollectionEvent( source, info, item, operation );
//     }

//     public static ObservableCollectionEvent create( Observable source, String info, Object item, Object oldItem )
//     {
//         return new ObservableCollectionEvent( source, info, item, oldItem );
//     }
}
