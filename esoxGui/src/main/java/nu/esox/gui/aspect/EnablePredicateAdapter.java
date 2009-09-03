package nu.esox.gui.aspect;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import nu.esox.util.*;
    


public class EnablePredicateAdapter implements ObservableListener
{
    private final EnableableCollection m_enableables = new EnableableCollection();
    
    private boolean m_isTrue = false;
    
    
    public EnablePredicateAdapter( PredicateIF p )
    {
        this( new JComponent [] {  },
              new JComponent [] {  },
              new Action [] {  },
              new Action [] {  },
              p );
    }
    
    public EnablePredicateAdapter( JComponent trueComponent,
                                   JComponent falseComponent,
                                   Action trueAction,
                                   Action falseAction,
                                   PredicateIF p )
    {
        this( ( trueComponent == null ) ? null : new JComponent [] { trueComponent },
              ( falseComponent == null ) ? null : new JComponent [] { falseComponent },
              ( trueAction == null ) ? null : new Action [] { trueAction },
              ( falseAction == null ) ? null : new Action [] { falseAction },
              p );
    }
    
    public EnablePredicateAdapter( JComponent [] trueComponents,
                                   JComponent [] falseComponents,
                                   Action [] trueActions,
                                   Action [] falseActions,
                                   PredicateIF p )
    {
        m_enableables.add( trueComponents, falseComponents, trueActions, falseActions );
        p.addObservableListener( this );
        m_isTrue = p.isTrue();
        update( m_isTrue );
    }
    
    public void addTrue( JComponent c ) { m_enableables.addTrue( c ); update( m_isTrue ); }
    public void addFalse( JComponent c ) { m_enableables.addFalse( c ); update( m_isTrue ); }
    public void addTrue( Action a ) { m_enableables.addTrue( a ); update( m_isTrue ); }
    public void addFalse( Action a ) { m_enableables.addFalse( a ); update( m_isTrue ); }

    public void addTrue( JComponent [] c ) { m_enableables.addTrue( c ); update( m_isTrue ); }
    public void addFalse( JComponent[]  c ) { m_enableables.addFalse( c ); update( m_isTrue ); }
    public void addTrue( Action [] a ) { m_enableables.addTrue( a ); update( m_isTrue ); }
    public void addFalse( Action [] a ) { m_enableables.addFalse( a ); update( m_isTrue ); }

 
//    public final EnableableCollection getEnableables() { return m_enableables; }
    
    public void valueChanged( ObservableEvent ev )
    {
        update( ( (PredicateIF) ev.getSource() ).isTrue() );
    }
    
    private void update( boolean enabled )
    {
        m_isTrue = enabled;
        m_enableables.apply( m_isTrue );
    }
}
