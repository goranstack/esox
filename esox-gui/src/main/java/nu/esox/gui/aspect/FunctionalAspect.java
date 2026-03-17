package nu.esox.gui.aspect;

import java.util.function.BiConsumer;
import java.util.function.Function;


public class FunctionalAspect<M, V> implements AspectIF
{
    private final Function<M, V> m_getter;
    private final BiConsumer<M, V> m_setter;

    public FunctionalAspect( Function<M, V> getter, BiConsumer<M, V> setter )
    {
        m_getter = getter;
        m_setter = setter;
    }

    @SuppressWarnings( "unchecked" )
    public Object getAspectValue( Object model )
    {
        if ( m_getter == null ) return model;
        return m_getter.apply( (M) model );
    }

    @SuppressWarnings( "unchecked" )
    public void setAspectValue( Object model, Object aspectValue )
    {
        if ( m_setter == null ) return;
        m_setter.accept( (M) model, (V) aspectValue );
    }

    public boolean hasSetter()
    {
        return m_setter != null;
    }
}
