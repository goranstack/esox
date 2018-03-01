package nu.esox.gui.layout;

import java.awt.*;

    
public class ShrinkStretchLayout implements LayoutManager2
{
    private final boolean m_shrinkHorizontally;
    private final boolean m_stretchHorizontally;
    private final boolean m_shrinkVertically;
    private final boolean m_stretchVertically;
    
    private transient SizeRequirements m_w;
    private transient SizeRequirements m_h;

    public ShrinkStretchLayout( boolean shrinkHorizontally, boolean stretchHorizontally, boolean shrinkVertically, boolean stretchVertically )
    {
        m_shrinkHorizontally = shrinkHorizontally;
        m_stretchHorizontally = stretchHorizontally;
        m_shrinkVertically = shrinkVertically;
        m_stretchVertically = stretchVertically;
    }

    public void addLayoutComponent(Component comp, Object constraints)
    {
    }

    public void addLayoutComponent(String name, Component comp)
    {
    }

    void checkRequests( Container target )
    {
        if
            ( m_w == null )
        {
            m_w = new SizeRequirements();
            m_h = new SizeRequirements();
		
            Component c = target.getComponent( 0 );
			
            Dimension min = c.getMinimumSize();
            Dimension typ = c.getPreferredSize();
            Dimension max = c.getMaximumSize();
            
            m_w.minimum =   min.width;
            m_w.preferred = typ.width;
            m_w.maximum =   max.width;
            
            if ( m_w.minimum < 0 ) m_w.minimum = Integer.MAX_VALUE;
            if ( m_w.preferred < 0 ) m_w.preferred = Integer.MAX_VALUE;
            if ( m_w.maximum < 0 ) m_w.maximum = Integer.MAX_VALUE;
            
            m_h.minimum =   min.height;
            m_h.preferred = typ.height;
            m_h.maximum =   max.height;
            
            if ( m_h.minimum < 0 ) m_h.minimum = Integer.MAX_VALUE;
            if ( m_h.preferred < 0 ) m_h.preferred = Integer.MAX_VALUE;
            if ( m_h.maximum < 0 ) m_h.maximum = Integer.MAX_VALUE;
        }
    }

    public float getLayoutAlignmentX(Container target)
    {
        checkRequests(target);
        return 0;
    }

    public float getLayoutAlignmentY(Container target)
    {
        checkRequests(target);
        return 0;
    }

    public void invalidateLayout(Container target)
    {
        m_w = null;
        m_h = null;
    }

    public void layoutContainer( Container target )
    {
        checkRequests( target );

        Insets in = target.getInsets();

        int x = in.left;
        int y = in.top;

        int w = target.getWidth() - in.left - in.right;
        int h = target.getHeight() - in.left - in.right;
        
        if
            ( m_w.preferred < w )
        {
            if ( ! m_stretchHorizontally ) w = m_w.preferred;
        } else {
            if ( ! m_shrinkHorizontally ) w = m_w.preferred;
        }
        
        if
            ( m_h.preferred < h )
        {
            if ( ! m_stretchVertically ) h = m_h.preferred;
        } else {
            if ( ! m_shrinkVertically ) h = m_h.preferred;
        }
              
//         int w = ( m_doShrinkX && ( m_w.preferred < target.getWidth() - in.left - in.right ) ) ? m_w.preferred : target.getWidth() - in.left - in.right;
//         int h = ( m_doShrinkY && ( m_h.preferred < target.getHeight() - in.left - in.right ) ) ? m_h.preferred : target.getHeight() - in.top - in.bottom;

        target.getComponent( 0 ).setBounds( x, y, w, h );
    }

    private double getAlignmentfor( Component c )
    {
        return 0;
    }
    
    public Dimension maximumLayoutSize( Container target )
    {
        checkRequests( target );
        Dimension size = new Dimension( m_w.maximum, m_h.maximum );
        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
        return size;
    }

    public Dimension minimumLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( m_w.minimum, m_h.minimum );
        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
        return size;
    }

    public Dimension preferredLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( m_w.preferred, m_h.preferred );
        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
        return size;
    }

    public void removeLayoutComponent(Component comp)
    {
    }
}
