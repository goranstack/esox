package nu.esox.gui.example.indexedsubmodeladapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nu.esox.gui.aspect.AbstractAdapter;
import nu.esox.gui.aspect.Aspect;
import nu.esox.gui.aspect.AspectIF;
import nu.esox.gui.aspect.ModelOwnerIF;

/**
 * This class is a proposal for changes in the SubModelAdaptor. The request is to
 * change SubModelAdaptor in a way that makes it possible to use a customized
 * aspect object. The present design has no constructor that permits customized
 * aspect. Need help to change the SubModelAdaptor without duplicating the
 * initializing of m_setSubModelMethod or the instantiation of the aspect object.
 * 
 * @author G Stack
 *
 */
public class XSubModelAdapter extends AbstractAdapter
{
    private final Object m_subModelTarget;
    private final Method m_setSubModelMethod;

    
    public XSubModelAdapter( Object subModelTarget,
            String setSubModelMethodName,
            Class subModelClass,
            ModelOwnerIF modelOwner,
            AspectIF aspect,
            String aspectName)
    {
    	super(modelOwner, aspect, aspectName, null, null);
        m_subModelTarget = subModelTarget;
        try
        {
            m_setSubModelMethod = m_subModelTarget.getClass().getMethod( setSubModelMethodName, subModelClass );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new Error( "No such method: " + m_subModelTarget.getClass() + "." + setSubModelMethodName + "( " + subModelClass + " )" );
        }
        update();

    }
    
    public XSubModelAdapter( Object subModelTarget,
                            String setSubModelMethodName,
                            Class subModelClass,
                            ModelOwnerIF modelOwner,
                            String aspectName )
    {
        this( subModelTarget, setSubModelMethodName, subModelClass, modelOwner, null, null, aspectName );
    }
    
    public XSubModelAdapter( Object subModelTarget,
                            String setSubModelMethodName,
                            Class subModelClass,
                            ModelOwnerIF modelOwner,
                            Class modelClass,
                            String getAspectMethodName,
                            String aspectName )
    {
    	this(subModelTarget, setSubModelMethodName, subModelClass, modelOwner, new Aspect(modelClass, getAspectMethodName, null, subModelClass), aspectName);
    }
    
    protected void update( Object projectedValue )
    {
        boolean tmp = m_setSubModelMethod.isAccessible();
        try
        {
            m_setSubModelMethod.setAccessible( true );
            m_setSubModelMethod.invoke( m_subModelTarget, projectedValue );
        }
        catch ( IllegalAccessException ex )
        {
            throw new Error( "Method not accessible: " + m_setSubModelMethod );
        }
        catch ( InvocationTargetException ex )
        {
            throw new Error( "Invovation failure: " + m_setSubModelMethod, ex );
        }
        finally
        {
            m_setSubModelMethod.setAccessible( tmp );
        }
    }
}
