package nu.esox.gui.model;

import javax.swing.*;
import javax.swing.event.*;
import nu.esox.util.*;


public class ObservableCollectionListModel<T> implements ListModel, ObservableListener
{
    protected EventListenerList m_listenerList = new EventListenerList();

    protected ObservableCollectionIF<T> m_data;
    protected Object [] m_items;
    
    
    public ObservableCollectionListModel( ObservableCollectionIF<T> data )
    {
        setData( data );
    }

    
    public ObservableCollectionIF<T> getData()
    {
        return m_data;
    }

    public void setData( ObservableCollectionIF <T>data )
    {
        if
            ( m_data != null )
        {
            m_data.removeObservableListener( this );
        }

        fireIntervalRemoved( this, 0, getSize() );
        
        m_data = data;
        refresh();
        
        if
            ( ( m_data != null ) && ( m_listenerList.getListenerCount() > 0 ) )
        {
            m_data.addObservableListener( this );
        }
        
        fireIntervalAdded( this, 0, getSize() );
    }


    
    private void refresh()
    {
        m_items = ( m_data == null ) ? null : m_data.toArray();
    }

    
    public void valueChanged( ObservableEvent e )
    {
        if
            ( ! ( e instanceof ObservableListEvent ) )
        {
            refresh();
            fireContentsChanged( this, -1, -1 );
        } else {
            ObservableListEvent ev = (ObservableListEvent) e;

            if
                ( ev.getIndex() == -1 )
            {
                refresh();
                fireContentsChanged( this, -1, -1 );
            } else {
                refresh();
                
                switch
                    ( ev.getOperation() )
                {
		        				case ObservableListEvent.ADDED:
                        fireIntervalAdded( this, ev.getIndex(), ev.getIndex() );
                        break;
                        
				        		case ObservableListEvent.REMOVED:
                        fireIntervalRemoved( this, ev.getIndex(), ev.getIndex() );
                        break;
                    
						        case ObservableListEvent.REPLACED:
                        fireContentsChanged( this, ev.getIndex(), ev.getIndex() );
                        break;
                
						        case ObservableListEvent.ITEM_CHANGED:
                        fireContentsChanged( this, ev.getIndex(), ev.getIndex() );
                        break;
                }
            }
        }

        postValueChanged( e );
    }

    protected void postValueChanged( ObservableEvent e )
    {
    }
    
    
    public int getSize()
    {
        return ( m_items == null ) ? 0 : m_items.length;
    }

    public Object getElementAt( int index )
    {
        return ( m_items == null ) ? null : m_items[ index ];
    }

    
    public void addListDataListener( ListDataListener l )
    {
        if
            ( ( m_data != null ) && ( m_listenerList.getListenerCount() == 0 ) )
        {
            m_data.addObservableListener( this );
            refresh();
        }
        
        m_listenerList.add( ListDataListener.class, l );
    }

    public void removeListDataListener( ListDataListener l )
    {
        m_listenerList.remove( ListDataListener.class, l );

        if
            ( ( m_data != null ) && ( m_listenerList.getListenerCount() == 0 ) )
        {
            m_data.removeObservableListener( this );
        }
    }




    protected void fireContentsChanged(Object source, int index0, int index1)
    {
        Object[] listeners = m_listenerList.getListenerList();
        ListDataEvent e = null;
        
        for
            (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if
                (listeners[i] == ListDataListener.class)
            {
                if (e == null) e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
                ((ListDataListener)listeners[i+1]).contentsChanged(e);
            }	       
        }
    }
    
    protected void fireIntervalAdded(Object source, int index0, int index1)
    {
        Object[] listeners = m_listenerList.getListenerList();
        ListDataEvent e = null;
        
        for
            (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if
                (listeners[i] == ListDataListener.class)
            {
                if (e == null) e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
                ((ListDataListener)listeners[i+1]).intervalAdded(e);
            }	       
        }
    }
    
    protected void fireIntervalRemoved(Object source, int index0, int index1)
    {
        Object[] listeners = m_listenerList.getListenerList();
        ListDataEvent e = null;
        
        for
            (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if
                (listeners[i] == ListDataListener.class)
            {
                if (e == null) e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                ((ListDataListener)listeners[i+1]).intervalRemoved(e);
            }	       
        }
    }
    
}
