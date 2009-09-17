package nu.esox.util;


import java.lang.reflect.*;


public class PredicateAdapter extends Predicate
{
    private final Method m_get;
    private final ObservableIF m_model;

    private static final Class [] GET_ARG_TYPES = new Class [] {};
    private static final Object [] GET_ARGS = new Object [ 0 ];


    public PredicateAdapter( ObservableIF model, String getMethodName )
    {
        m_model = model;

        Method m = null;
        try
        {
            m = model.getClass().getMethod( getMethodName, GET_ARG_TYPES );
        }
        catch ( NoSuchMethodException ex )
        {
            assert false : "Get method not found " + model.getClass() + "." + getMethodName;
        }
		
        m_get = m;
        m_model.addObservableListener( this );
    }
	
    protected boolean derive( Object o ) { return ( (Boolean) o ).booleanValue(); }
    
    public void valueChanged( ObservableEvent ev )
    {
        assert m_model != null;
        assert m_get != null;
        
        try
        {
            set( derive( m_get.invoke( m_model, GET_ARGS ) ) );
        }
        catch ( Exception ex )
        {
            System.err.println( ex );
            ex.printStackTrace();
        }
    }

    static final long serialVersionUID = 5806067845682464164L;
}
