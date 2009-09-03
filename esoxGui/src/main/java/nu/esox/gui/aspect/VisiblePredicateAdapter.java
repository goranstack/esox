package nu.esox.gui.aspect;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import nu.esox.util.*;
    


public class VisiblePredicateAdapter implements ObservableListener
{
    private final LinkedList<JComponent> m_trueComponents = new LinkedList<JComponent>();
    private final LinkedList<JComponent> m_falseComponents = new LinkedList<JComponent>();
    
    private boolean m_isTrue = false;
    
    
    public VisiblePredicateAdapter( PredicateIF p )
    {
        this( new JComponent [] {  },
              new JComponent [] {  },
              p );
    }
    
    public VisiblePredicateAdapter( JComponent trueComponent,
                                    JComponent falseComponent,
                                    PredicateIF p )
    {
        this( ( trueComponent == null ) ? null : new JComponent [] { trueComponent },
              ( falseComponent == null ) ? null : new JComponent [] { falseComponent },
              p );
    }
    
    public VisiblePredicateAdapter( JComponent [] trueComponents,
                                    JComponent [] falseComponents,
                                    PredicateIF p )
    {
        for ( JComponent c : trueComponents ) m_trueComponents.add( c );
        for ( JComponent c : falseComponents ) m_falseComponents.add( c );
        p.addObservableListener( this );
        m_isTrue = p.isTrue();
        update( m_isTrue );
    }
    
    public void addTrue( JComponent c ) { m_trueComponents.add( c ); update( m_isTrue ); }
    public void addFalse( JComponent c ) { m_falseComponents.add( c ); update( m_isTrue ); }
    
    public void valueChanged( ObservableEvent ev )
    {
        update( ( (PredicateIF) ev.getSource() ).isTrue() );
    }
    
    private void update( boolean enabled )
    {
        m_isTrue = enabled;
        
        for ( JComponent c : m_trueComponents ) c.setVisible( enabled );
        
        enabled = ! enabled;
        
        for ( JComponent c : m_falseComponents ) c.setVisible( enabled );
    }
}
