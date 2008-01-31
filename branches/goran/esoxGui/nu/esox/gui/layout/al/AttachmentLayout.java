package nu.esox.gui.layout.al;

import java.awt.*;
import java.awt.geom.*;
import nu.esox.al.*;

/**
 * Multi purpose layout manager based on com.bluebrim.al.AttachableContainer.
 * 
   * @author  Dennis Malmström
 * @version 2.0
 */
public class AttachmentLayout extends AttachableContainer implements LayoutManager2
{
    private static final Dimension m_minimumSize = new Dimension( 0, 0 );
    private static final Dimension m_maximumSize = new Dimension( Short.MAX_VALUE, Short.MAX_VALUE );
	

    public void addLayoutComponent( Component component, Object constraint )
    {
        add( new ComponentAttachable( component ), (Constraint) constraint );
    }

    public void addLayoutComponent( String name, Component component )
    {
        add( new ComponentAttachable( component ), null );
    }

    public float getLayoutAlignmentX( Container parent )
    {
        return 0;
    }

    public float getLayoutAlignmentY( Container parent )
    {
        return 0;
    }

    public void invalidateLayout( Container parent )
    {
        invalidateLayoutAndSize();
    }

    public void layoutContainer( Container parent )
    {
        layout();
    }

    public Dimension maximumLayoutSize( Container parent )
    {
        return m_maximumSize;
    }

    public Dimension minimumLayoutSize( Container parent )
    {
        return m_minimumSize;
    }

    public Dimension preferredLayoutSize( Container parent )
    {
        Dimension2D d = getPreferredLayoutSize();
        Insets insets = parent.getInsets();
        return new Dimension( (int) d.getWidth() + insets.left + insets.right,
                              (int) d.getHeight() + insets.top + insets.bottom );
    }

    public void removeLayoutComponent( Component component )
    {
        remove( indexOf( component ) );
    }



    Constraint getConstraintFor( Component c )
    {
        int n = indexOf( c );
        if
            ( n == -1 )
        {
            return null;
        } else {
            return (Constraint) getConstraint( n );
        }
    }

    private int indexOf( Component c )
    {
        int I = getConstraintCount();
        for
            ( int i = 0; i < I; i++ )
        {
            
            if
                ( c == ( (ComponentAttachable) ( (Constraint) getConstraint( i ) ).getAttachable() ).getComponent() )
            {
                return i;
            }
        }

        return -1;
    }

    protected void invalidateAttachments()
    {
        super.invalidateAttachments();
    }

}
