package nu.esox.gui.aspect;


import java.util.*;
import nu.esox.util.*;
import javax.swing.*;

public class EnableableCollection extends ComponentAndActionCollection
{
    protected void apply( JComponent c, boolean enabled )
    {
        c.setEnabled( enabled );
    }
    
    protected void apply( Action a, boolean enabled )
    {
        a.setEnabled( enabled );
    }
}
