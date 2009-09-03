package nu.esox.gui.layout.al;

import java.awt.*;
import nu.esox.al.*;

  /**
   * Implementation of com.bluebrim.al.Attachable used for laying out awt and swing components.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */

class ComponentAttachable implements Attachable
{
    private final Component m_component;

    public ComponentAttachable( Component component )
    {
        if ( component == null ) throw new NullPointerException();
        m_component = component;
    }


    public Component getComponent()
    {
        return m_component;
    }
    
    public void setBounds( double x, double y, double w, double h )
    {
        Insets i = m_component.getParent().getInsets();
        m_component.setBounds( (int) Math.floor( x ) + i.left,
                               (int) Math.floor( y ) + i.top,
                               (int) Math.ceil( w ),
                               (int) Math.ceil( h ) );
    }
    
    public String getId()
    {
        return "\"" + m_component.getName() + "\" (" + m_component.getClass() + ")";
    }
    
    public double getContainerWidth()
    {
        Container parent = m_component.getParent();
        Insets i = parent.getInsets();
        return parent.getWidth() - i.left - i.right;
    }
    
    public double getContainerHeight()
    {
        Container parent = m_component.getParent();
        Insets i = parent.getInsets();
        return parent.getHeight() - i.top - i.bottom;
    }

    public double getPreferredWidth()
    {
          return m_component.getPreferredSize().getWidth();
    }

    public double getPreferredHeight()
    {
          return m_component.getPreferredSize().getHeight();
    }

    public double getX()
    {
        return m_component.getX() - m_component.getParent().getInsets().left;
    }
    
    public double getY()
    {
        return m_component.getY() - m_component.getParent().getInsets().right;
    }

    public void invalidate()
    {
        AttachmentLayout l = (AttachmentLayout) m_component.getParent().getLayout();
	
        l.invalidateLayout( m_component.getParent() );
        l.invalidateAttachments();
    }

    public boolean equals( Object o )
    {
        if ( o == this ) return true;

        if
            ( o instanceof ComponentAttachable )
        {
            return m_component == ( (ComponentAttachable) o ).m_component;
        }

        return false;
    }
}

