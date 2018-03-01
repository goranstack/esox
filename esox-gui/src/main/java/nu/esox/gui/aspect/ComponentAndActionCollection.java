package nu.esox.gui.aspect;


import java.util.*;
import nu.esox.util.*;
import javax.swing.*;

//fixit: traverse tree ???
//fixit: added callback ???
public abstract class ComponentAndActionCollection
{
    private final LinkedList<JComponent> m_trueComponents = new LinkedList<JComponent>();
    private final LinkedList<JComponent> m_falseComponents = new LinkedList<JComponent>();
    private final LinkedList<Action> m_trueActions = new LinkedList<Action>();
    private final LinkedList<Action> m_falseActions = new LinkedList<Action>();

    
    public ComponentAndActionCollection() {}
    {
    }
    
    public ComponentAndActionCollection( JComponent trueComponent,
                                         JComponent falseComponent,
                                         Action trueAction,
                                         Action falseAction )
    {
        add( trueComponent, falseComponent, trueAction, falseAction );
    }
    
     public ComponentAndActionCollection( JComponent [] trueComponents,
                                          JComponent [] falseComponents,
                                          Action [] trueActions,
                                          Action [] falseActions )
    {
        add( trueComponents, falseComponents, trueActions, falseActions );
    }

    
    public void add( JComponent trueComponent,
                     JComponent falseComponent,
                     Action trueAction,
                     Action falseAction )
    {
        add( ( trueComponent == null ) ? null : new JComponent [] { trueComponent },
             ( falseComponent == null ) ? null : new JComponent [] { falseComponent },
             ( trueAction == null ) ? null : new Action [] { trueAction },
             ( falseAction == null ) ? null : new Action [] { falseAction } );
    }
    
    public void add( JComponent [] trueComponents,
                     JComponent [] falseComponents,
                     Action [] trueActions,
                     Action [] falseActions )
    {
        addTrue( trueComponents );
        addTrue( trueActions );
        addFalse( falseComponents );
        addFalse( falseActions );
    }

    public void addTrue( JComponent c ) { m_trueComponents.add( c ); }
    public void addFalse( JComponent c ) { m_falseComponents.add( c ); }
    public void addTrue( Action a ) { m_trueActions.add( a ); }
    public void addFalse( Action a ) { m_falseActions.add( a ); }
    
    public void addTrue( JComponent [] cs ) { if ( cs != null ) for ( JComponent c : cs ) addTrue( c ); }
    public void addFalse( JComponent [] cs ) { if ( cs != null ) for ( JComponent c : cs ) addFalse( c ); }
    public void addTrue( Action [] as ) { if ( as != null ) for ( Action a : as ) addTrue( a ); }
    public void addFalse( Action [] as ) { if ( as != null ) for ( Action a : as ) addFalse( a ); }
    
    protected void apply( boolean enabled )
    {
        for ( JComponent c : m_trueComponents ) apply( c, enabled );
        for ( Action a : m_trueActions ) apply( a, enabled );
        
        enabled = ! enabled;
        
        for ( JComponent c : m_falseComponents ) apply( c, enabled );
        for ( Action a : m_falseActions ) apply( a, enabled );
    }
    
    protected abstract void apply( JComponent c, boolean enabled );
    protected abstract void apply( Action a, boolean enabled );
}
