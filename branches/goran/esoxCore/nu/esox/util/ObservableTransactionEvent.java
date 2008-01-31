package nu.esox.util;


import java.util.List;
import java.util.ArrayList;


public class ObservableTransactionEvent extends ObservableEvent
{
    private final List<ObservableEvent> m_events = new ArrayList<ObservableEvent>();

     
    ObservableTransactionEvent( ObservableIF source )
    {
        super( source, null, null );
    }

    public void add( ObservableEvent ev )
    {
        m_events.add( ev );
    }

    public List<ObservableEvent> getEvents()
    {
        return m_events;
    }

    public void setInfo( String info )
    {
        m_info = info;
    }


    public void dispatch( ObservableListener l )
    {
        for ( ObservableEvent ev : m_events ) l.valueChanged( ev );
    }

    
//     public static ObservableTransactionEvent create( ObservableIF source )
//     {
//         return new ObservableTransactionEvent( source );
//     }

    static final long serialVersionUID = -8694576865704630656L;
}
