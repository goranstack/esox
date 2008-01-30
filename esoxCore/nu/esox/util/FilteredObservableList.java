package nu.esox.util;

import java.util.*;
import nu.esox.util.Observable;



public class FilteredObservableList<T> extends ObservableList<T> implements ObservableListener// implements Serializable
{
    public interface Filter<T>// extends ObservableIF
    {
        boolean pass( T o );
    };

    public static abstract class AbstractFilter<T> extends Observable implements Filter<T>
    {
    };

    public static class ObservableListFilter<T> extends ObservableList<T> implements Filter<T>
    {
        public boolean pass( T o )
        {
            return contains( o );
        }

        static final long serialVersionUID = 5806067845682464164L;
    };

    
    private ObservableListIF<T> m_source;
    private Filter<T> m_filter;
    
    
    public FilteredObservableList()
    {
        this( null );
    }
    
    public FilteredObservableList( ObservableListIF<T> source )
    {
        super( new ArrayList<T>() );

        setSource( source );
    }
    
    public FilteredObservableList( ObservableListIF<T> source, Filter f )
    {
        super( new ArrayList<T>() );

        setSource( source );
        setFilter( f );
    }


    public ObservableListIF<T> getSource()
    {
        return m_source;
    }

    public void setSource( ObservableListIF<T> source )
    {
        if ( m_source != null ) m_source.removeObservableListener( this );
        m_source = source;
        if ( m_source != null ) m_source.addObservableListener( this );

        beginTransaction( false );
        rebuild();
        endTransaction( "setSource", null );
    }
    

    public Filter getFilter()
    {
        return m_filter;
    }

    public void setFilter( Filter f )
    {
        if ( f == m_filter ) return;

        if ( m_filter != null && m_filter instanceof ObservableIF ) ( (ObservableIF) m_filter ).removeObservableListener( this );
        m_filter = f;
        if ( m_filter != null && m_filter instanceof ObservableIF ) ( (ObservableIF) m_filter ).addObservableListener( this );

        beginTransaction( false );
        rebuild();
        endTransaction( "setFilter", null );
    }
    
    protected void itemChanged( ObservableEvent ev )
    {
          // will be notified by source, do nothing
    }

    public void valueChanged( ObservableEvent ev )
    {
        if
            ( ev.getObservable() == m_filter )
        {
            filterChanged();
        } else if
            ( ev instanceof ObservableTransactionEvent )
        {
            Iterator i = ( (ObservableTransactionEvent) ev ).getEvents().iterator();
            while
                ( i.hasNext() )
            {
                sourceChanged( (ObservableListEvent<T>) i.next() );
            }
        } else if
            ( ev instanceof ObservableListEvent )
        {
            sourceChanged( (ObservableListEvent<T>) ev );
        } else {
            beginTransaction( true );
            rebuild();
            endTransaction( "source changed", null );
        }
    }

    private void filterChanged()
    {
        beginTransaction( false );
        rebuild();
        endTransaction( "filter changed", null );
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
                  rebuild();
                  endTransaction( "added", null );
              } else {
                  if
                      ( pass( o ) )
                  {
                      beginTransaction( true );
                      rebuild();
                      endTransaction( "added", null );
                  }
              }
              break;
              
        	case ObservableListEvent.REMOVED:
              o = ev.getItem();
              if
                  ( o == null )
              {
                  beginTransaction( true );
                  rebuild();
                  endTransaction( "removed", null );
              } else {
                  if
                      ( pass( o ) )
                  {
                      beginTransaction( true );
                      int index = indexOf( o );
                      rebuild();
                      endTransaction( "removed", null );
                  }
              }
              break;
              
        	case ObservableListEvent.REPLACED:
              T newItem = ev.getItem();
              T oldItem = ev.getOldItem();
              if
                  ( newItem == null )
              {
                  beginTransaction( false );
                  rebuild();
                  endTransaction( "replaced", null );
              } else {
                  boolean wasIn = contains( oldItem );
                  boolean isIn = pass( newItem );
                  if
                      ( wasIn && isIn )
                  {
                      beginTransaction( true );
                      int index = indexOf( oldItem );
                      rebuild();
                      endTransaction( "replaced", null );
                  } else if
                      ( wasIn && ! isIn )
                  {
                      beginTransaction( true );
                      int index = indexOf( oldItem );
                      rebuild();
                      endTransaction( "removed", null );
                  } else if
                      ( ! wasIn && isIn )
                  {
                      beginTransaction( true );
                      rebuild();
                      endTransaction( "added", null );
                  }
              }
              break;
              
        	case ObservableListEvent.ITEM_CHANGED:
              o = ev.getItem();
              if
                  ( o == null )
              {
                  beginTransaction( true );
                  rebuild();
                  endTransaction( "items changed", null );
              } else {
                  boolean wasIn = contains( o );
                  boolean isIn = pass( o );
                  if
                      ( wasIn && isIn )
                  {
                      beginTransaction( true );
                      endTransaction( "removed", null );
                  } else if
                      ( wasIn && ! isIn )
                  {
                      beginTransaction( true );
                      int index = indexOf( o );
                      rebuild();
                      endTransaction( "removed", null );
                  } else if
                      ( ! wasIn && isIn )
                  {
                      beginTransaction( true );
                      rebuild();
                      endTransaction( "added", null );
                  }
              }
              break;
        }
    }


    protected void rebuild()
    {
        clear();

        if ( m_source == null ) return;
        
        if
            ( m_filter == null )
        {
            addAll( m_source );
        } else {
            Iterator<T> i = m_source.iterator();
            while
                ( i.hasNext() )
            {
                T o = i.next();
                if ( m_filter.pass( o ) ) add( o );
            }
        }
    }

    private boolean pass( T o )
    {
        if
            ( m_filter == null )
        {
            return true;
        } else {
            return m_filter.pass( o );
        }
    }

    static final long serialVersionUID = 7648287867714422984L;
}
 
