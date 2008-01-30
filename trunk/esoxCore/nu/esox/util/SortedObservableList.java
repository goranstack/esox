package nu.esox.util;

import java.util.*;


public class SortedObservableList<T> extends ObservableList<T> implements ObservableListener
{
    private final ObservableListIF<T> m_source;
    private Comparator<T> m_comparator = createDefaultComparator();
    
    public SortedObservableList()
    {
        this( new ObservableList<T>() );
    }
    
    public SortedObservableList( ObservableListIF<T> source )
    {
        super( new ArrayList<T>() );

        m_source = source;
        m_source.addObservableListener( this );

        resort();
    }


    public ObservableListIF<T> getSource()
    {
        return m_source;
    }
    
    public Comparator<T> getComparator()
    {
        return m_comparator;
    }

    public void setComparator( Comparator<T> c )
    {
        if ( c == null ) c = createDefaultComparator();
        if ( c == m_comparator ) return;

        if ( m_comparator instanceof ObservableIF ) ( (ObservableIF) m_comparator ).removeObservableListener( this );
        
        m_comparator = c;

        if ( m_comparator instanceof ObservableIF ) ( (ObservableIF) m_comparator ).addObservableListener( this );

        comparatorChanged();
    }


    public void valueChanged( ObservableEvent ev )
    {
        if
            ( ev instanceof ObservableTransactionEvent )
        {
            for ( ObservableEvent e : ( (ObservableTransactionEvent) ev ).getEvents() ) valueChanged( e );
//             Iterator i = ( (ObservableTransactionEvent) ev ).getEvents().iterator();
//             while
//                 ( i.hasNext() )
//             {
//                 sourceChanged( (ObservableListEvent<T>) i.next() );
//             }
        } else {
            if
                ( ev.getObservable() == m_comparator )
            {
                comparatorChanged();
            } else if
                ( ev instanceof ObservableListEvent )
            {
                sourceChanged( (ObservableListEvent<T>) ev );
            } else {
                beginTransaction( true );
                resort();
                endTransaction( "source changed", null );
  }
        }
    }

    private void comparatorChanged()
    {
        beginTransaction( false );
        resort();
        endTransaction( "setComparator", null );
    }
    
    private void sourceChanged( ObservableListEvent<T> ev )
    {
        switch
            ( ev.getOperation() )
        {
        	case ObservableListEvent.ADDED:
              T o = ev.getItem();
              if
                  ( o == null )
              {
                  beginTransaction( true );
                  resort();
                  endTransaction( "added", null );
              } else {
                  if
                      ( size() == 0 )
                  {
                      add( o );
                  } else {
                      int i = Collections.binarySearch( this, o, m_comparator );
                      if ( i < 0 ) i = - ( 1 + i );
                      add( i, o );
                  }
              }
              break;
              
        	case ObservableListEvent.REMOVED:
              o = ev.getItem();
              if
                  ( o == null )
              {
                  beginTransaction( true );
                  resort();
                  endTransaction( "removeded", null );
              } else {
                  remove( o );
              }
              break;
              
        	case ObservableListEvent.REPLACED:
              Object newItem = ev.getItem();
              Object oldItem = ev.getOldItem();
              if
                  ( newItem == null )
              {
                  beginTransaction( false );
                  resort();
                  endTransaction( "replaced", null );
              } else {
                  boolean doResort = false;
                  int i = indexOf( oldItem );

                  if
                      ( size() > 1 )
                  {
                      if
                          ( i == 0 )
                      {
                          doResort = m_comparator.compare( get( 0 ), get( 1 ) ) > 0;
                      } else if
                          ( i == size() - 1 )
                      {
                          doResort = m_comparator.compare( get( i - 1 ), get( i ) ) > 0;
                      } else {
                          doResort = ( m_comparator.compare( get( i - 1 ), get( i ) ) > 0 ) || ( m_comparator.compare( get( i ), get( i + 1 ) ) > 0 );
                      }
                  }
                  
                  if
                      ( doResort )
                  {
                      beginTransaction( false );
                      resort();
                      endTransaction( "replaced", null );
                  } else {
                      fireValueChanged( "replaced", null );
                  }
              }
              break;
              
        	case ObservableListEvent.ITEM_CHANGED:
              o = ev.getItem();
              if
                  ( o == null )
              {
                  beginTransaction( true );
                  resort();
                  endTransaction( "items changed", null );
              } else {
                  boolean doResort = false;
                  int I = size();
                  int i = 0;
                  for ( ; i < I; i++ ) if ( get( i ) == o ) break;
                  
                  if
                      ( I > 1 )
                  {
                      if
                          ( i == 0 )
                      {
                          doResort = m_comparator.compare( get( 0 ), get( 1 ) ) > 0;
                      } else if
                          ( i == size() - 1 )
                      {
                          doResort = m_comparator.compare( get( i - 1 ), get( i ) ) > 0;
                      } else {
                          doResort = ( m_comparator.compare( get( i - 1 ), get( i ) ) > 0 ) || ( m_comparator.compare( get( i ), get( i + 1 ) ) > 0 );
                      }
                  }
                  
                  if
                      ( doResort )
                  {
                      beginTransaction( false );
                      resort();
                      endTransaction( "replaced", null );
                  } else {
                      fireValueChanged( "item changed", null );
                  }
              }
              break;
        }
    }


    private Comparator<T> createDefaultComparator()
    {
        return
            new Comparator<T>()
            {
                public int compare( T o1, T o2 )
                {
                    if ( o1 instanceof Comparable ) return ( (Comparable) o1 ).compareTo( (Comparable) o2 );
                    return 0;
                }
            };
    }
    

    protected void resort()
    {
        clear();
        addAll( m_source );
        if
            ( m_comparator != null )
        {
            Collections.sort( this, m_comparator );
        }
    }


    static final long serialVersionUID = 5806067845682464164L;
}
 
