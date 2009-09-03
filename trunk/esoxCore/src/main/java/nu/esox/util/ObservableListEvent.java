package nu.esox.util;


public class ObservableListEvent<T> extends ObservableCollectionEvent<T>
{
    private int m_index = -1;
    
    
    protected ObservableListEvent( ObservableIF source, String info, T item, int operation, int index )
    {
        super( source, info, item, operation );

        m_index = index;
    }
    
    protected ObservableListEvent( Observable source, String info, T item, T oldItem, int index )
    {
        super( source, info, item, oldItem );

        m_index = index;
    }

    
    public int getIndex()
    {
        return m_index;
    }
    
    public String toString()
    {
        return super.toString() + " " + m_index;
    }
    
    
//     public static ObservableListEvent create( ObservableIF source, String info, Object item, int operation, int index )
//     {
//         return new ObservableListEvent( source, info, item, operation, index );
//     }
    
//     public static ObservableListEvent create( Observable source, String info, Object item, Object oldItem, int index )
//     {
//         return new ObservableListEvent( source, info, item, oldItem, index );
//     }


    static final long serialVersionUID = -4838665810702024483L;
}
