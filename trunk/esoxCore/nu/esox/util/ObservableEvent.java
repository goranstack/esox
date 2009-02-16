package nu.esox.util;


import java.util.EventObject;


public class ObservableEvent extends EventObject
{
    protected String m_info;
    protected Object m_data;

    
    protected ObservableEvent( ObservableIF source, String info, Object data )
    {
        super( source );
        m_info = info;
        m_data = data;
    }

    
    public ObservableIF getObservable()
    {
        return (ObservableIF) getSource();
    }

    public String getInfo()
    {
        return m_info;
    }

    public Object getData()
    {
        return m_data;
    }

    public String toString() { return "ObservableEvent: " + m_info + ", " + m_data; }

    static final long serialVersionUID = -3304174174040200488L;



    public static ObservableEvent create( ObservableIF source, String info, Object data )
    {
        return new ObservableEvent( source, info, data );
    }
}

