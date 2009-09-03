package nu.esox.gui.aspect;


import java.util.*;
import nu.esox.util.*;
import javax.swing.*;


public class EnableAdapter extends AbstractBooleanAdapter
{
    private final EnableableCollection m_enableables = new EnableableCollection();

    
    public EnableAdapter( JComponent trueComponent,
                          JComponent falseComponent,
                          Action trueAction,
                          Action falseAction,
                          ModelOwnerIF modelOwner,
                          Class modelClass,
                          String getAspectMethodName,
                          String aspectName,
                          Object trueValue,
                          Object falseValue,
                          Object nullValue,
                          Object undefinedValue )
    {
        this( ( trueComponent == null ) ? null : new JComponent [] { trueComponent },
              ( falseComponent == null ) ? null : new JComponent [] { falseComponent },
              ( trueAction == null ) ? null : new Action [] { trueAction },
              ( falseAction == null ) ? null : new Action [] { falseAction },
              modelOwner,
              modelClass,
              getAspectMethodName,
              aspectName,
              trueValue,
              falseValue,
              nullValue,
              undefinedValue );
    }
    
     public EnableAdapter( JComponent [] trueComponents,
                           JComponent [] falseComponents,
                           Action [] trueActions,
                           Action [] falseActions,
                           ModelOwnerIF modelOwner,
                           Class modelClass,
                           String getAspectMethodName,
                           String aspectName,
                           Object trueValue,
                           Object falseValue,
                           Object nullValue,
                           Object undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, null, null, aspectName, trueValue, falseValue, nullValue, undefinedValue );

        m_enableables.add( trueComponents, falseComponents, trueActions, falseActions );
        update();
    }


//     public final EnableableCollection getEnableables() { return m_enableables; }
        
    public void addTrue( JComponent c ) { m_enableables.addTrue( c ); update(); }
    public void addFalse( JComponent c ) { m_enableables.addFalse( c ); update(); }
    public void addTrue( Action a ) { m_enableables.addTrue( a ); update(); }
    public void addFalse( Action a ) { m_enableables.addFalse( a ); update(); }

    public void addTrue( JComponent [] c ) { m_enableables.addTrue( c ); update(); }
    public void addFalse( JComponent[]  c ) { m_enableables.addFalse( c ); update(); }
    public void addTrue( Action [] a ) { m_enableables.addTrue( a ); update(); }
    public void addFalse( Action [] a ) { m_enableables.addFalse( a ); update(); }

    protected void update( Object projectedValue )
    {
        m_enableables.apply( (Boolean) projectedValue );
    }
}
