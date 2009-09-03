package nu.esox.util;


public class ObservableImpl extends Observable
{
    private final ObservableIF m_observable;


    public ObservableImpl( ObservableIF o )
    {
        m_observable = o;
    }

    
    protected ObservableEvent createObservableEvent( String info, Object data )
    {
        return ObservableEvent.create( m_observable, info, data );
    }

    protected ObservableTransactionEvent createObservableTransactionEvent()
    {
        return new ObservableTransactionEvent( m_observable );
    }


/*
  Usage:
  
    public class X extends Y implements ObservableIF
    {
        private final ObservableImpl m_observableImpl = new ObservableImpl( this );

        public void addObservableListener( ObservableListener l ) { m_observableImpl.addObservableListener( l ); }
        public void removeObservableListener( ObservableListener l ) { m_observableImpl.removeObservableListener( l ); }
        public void clearObservableListeners() { m_observableImpl.clearObservableListeners(); }
        public boolean beginTransaction() { return m_observableImpl.beginTransaction(); }
        public boolean beginTransaction( boolean collectTransaction ) { return m_observableImpl.beginTransaction( collectTransaction ); }
        public void endTransaction( String info, Object data ) { m_observableImpl.endTransaction( info, data ); }

        
//         public void setZ( Z z )
//         {
//             m_z = z;
//             m_observableImpl.fireValueChanged( "Z changed" );
//         }

    }
*/

    static final long serialVersionUID = 3381417880293935981L;
}

  
