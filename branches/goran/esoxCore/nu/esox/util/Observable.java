package nu.esox.util;

import javax.swing.event.EventListenerList;

import java.io.*;
import java.util.*;


public class Observable implements ObservableIF, Serializable, ObservableListener
{
    public boolean OBJECT_TRACE = false;
    public static boolean TRACE = false;
    private static int m_level = 0;
    
    private transient EventListenerList m_listeners; // [ ObservableListener ]

    private transient boolean m_transactionInProgress;
    private transient ObservableTransactionEvent m_transactionEvent;
    private transient boolean m_dirty;
    
    
    public Observable()
    {
        init();
    }

    private void init()
    {
        if ( m_listeners == null ) m_listeners = new EventListenerList();
        m_transactionInProgress = false;
        m_dirty = false;
    }
    
        
    public final void addObservableListener( ObservableListener l )
    {
        if
            ( m_listeners.getListenerCount( ObservableListener.class ) == 0 )
        {
            addingFirstListener();
        }
        
        m_listeners.add( ObservableListener.class, l );
    }

    public final void removeObservableListener( ObservableListener l )
    {
        m_listeners.remove( ObservableListener.class, l );

        if
            ( m_listeners.getListenerCount( ObservableListener.class ) == 0 )
        {
            removedLastListener();
        }
    }


    
    public final void listenTo( ObservableIF o )
    {
        o.addObservableListener( this );
    }

    public final void unlistenTo( ObservableIF o )
    {
        o.removeObservableListener( this );
    }


    
    public final void clearObservableListeners()
    {
        m_listeners = new EventListenerList();
        removedLastListener();
    }

    protected void addingFirstListener()
    {
    }

    protected void removedLastListener()
    {
    }
    
    protected final boolean isObserved()
    {
        return m_listeners.getListenerCount() > 0;
    }

    
    public final boolean beginTransaction()
    {
        return beginTransaction( true );
    }
    
    public final boolean beginTransaction( boolean collectTransaction )
    {
        if
            ( checkForThenSetTransactionInProgress() )
        {
            return false;
        }
        
//         if ( m_transactionInProgress ) return false;
//         m_transactionInProgress = true;

        if ( collectTransaction ) m_transactionEvent = createObservableTransactionEvent();
        m_dirty = false;

        return true;
    }

    
    protected boolean checkForThenSetTransactionInProgress()
    {
        if ( m_transactionInProgress ) return true; // transaction was allready in progress
        
        m_transactionInProgress = true;
        return false;
    }

    
    public final void endTransaction( String info, Object data )
    {
        m_transactionInProgress = false;
        if
            ( true || m_dirty ) // Y true ???   !?:Forcing notification without actuall change?
        {
            if
                ( m_transactionEvent == null )
            {
                fireValueChanged( info, data );
            } else {
                m_transactionEvent.setInfo( info );
                fireValueChanged( m_transactionEvent );
            }
        }
        m_dirty = false;
        m_transactionEvent = null;
    }

    
    public final void fireValueChanged( String info, Object data )
    {
        fireValueChanged( createObservableEvent( info, data ) );
    }


    
    protected ObservableEvent createObservableEvent( String info, Object data )
    {
        return ObservableEvent.create( this, info, data );
    }

    protected ObservableTransactionEvent createObservableTransactionEvent()
    {
        return new ObservableTransactionEvent( this );
    }

    
    protected final void fireValueChanged( ObservableEvent e )
    {
        if
            ( m_transactionInProgress )
        {
            m_dirty = true;
            if ( m_transactionEvent != null ) m_transactionEvent.add( e );
        } else {

            if ( ! isObserved() ) return;
            
            Object[] listeners = m_listeners.getListenerList();

            if
                ( TRACE || OBJECT_TRACE )
            {
                for ( int i = 0; i < m_level; i++ ) System.err.print( "  " );
                System.err.println( "### fireValueChanged " + getClass() + " '" + toString() + "' " + e.getInfo() );
            }
            for
                ( int i = listeners.length - 2; i >= 0; i -= 2 )
            {
                if
                    ( listeners[ i ] == ObservableListener.class )
                {
                    if
                        ( TRACE || OBJECT_TRACE )
                    {
                        for ( int q = 0; q < m_level; q++ ) System.err.print( "  " );
                        System.err.println( "-> " + listeners[ i + 1 ] + "  (" + listeners[ i + 1 ].getClass() + ")");
                        m_level++;
                    }
                    ( (ObservableListener) listeners[ i + 1 ] ).valueChanged( e );
                    if
                        ( TRACE || OBJECT_TRACE )
                    {
                        m_level--;
                    }
                }	       
            }
            if
                ( TRACE || OBJECT_TRACE )
            {
                for ( int i = 0; i < m_level; i++ ) System.err.print( "  " );
                System.err.println( "###" );
            }
        }
    }


    
    public void valueChanged( ObservableEvent ev )
    {
        fireValueChanged( ev.getInfo(), ev.getData() );
    }

    

    private void writeObject( ObjectOutputStream stream ) throws IOException
    {
        try
        {
            stream.defaultWriteObject();
            List<PersistantObservableListener> tmp = new ArrayList<PersistantObservableListener>();
            Object[] listeners = m_listeners.getListenerList();
            for
                ( int i = listeners.length - 2; i >= 0; i -= 2 )
            {
                if
                    ( listeners[ i ] == ObservableListener.class )
                {
                    if
                        ( listeners[ i + 1 ] instanceof PersistantObservableListener )
                    {
                        tmp.add( (PersistantObservableListener) listeners[ i + 1 ] );
                    }
                }	       
            }

            stream.writeObject( tmp );
        }
        catch ( IOException ex )
        {
        }
    }

    

    @SuppressWarnings("unchecked")
        private List<ObservableListener> readListeners( ObjectInputStream stream ) throws IOException, ClassNotFoundException
    {
        return (List<ObservableListener>) stream.readObject();
    }
    
    private void readObject( ObjectInputStream stream ) throws IOException
    {
        try
        {
            stream.defaultReadObject();
            init();

            try
            {
                List<ObservableListener> tmp = readListeners( stream );

                for ( ObservableListener l : tmp ) m_listeners.add( ObservableListener.class, (PersistantObservableListener) l );
            }
            catch ( IOException ex )
            {
                  // migration problem, never mind
            }
        }
        catch ( ClassNotFoundException e )
        {
        }
    }


    static final long serialVersionUID = -4590833817237017531L;
}
