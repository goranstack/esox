package nu.esox.gui.layout;


import java.awt.*;

/*
	A layout manager that places components left justified in a single column.
	The configuration possibilities are:
	Sticky - If set all components are stretched to the width of the widest one (default = false).
	Spacing - Spacing between the components (default = 0).
	Fill - If the constant FILL is passed as layout constraint ( in the call to Container.add( Component, Object ) )
	       then that particular component will be stretched verticaly to fill the container (default = none).
	       If the constant FILL is passed as layout constraint for several components in the same container
	       the last call will overwrite the state set by the previous ones.
*/

public class ColumnLayout implements LayoutManager2
{
    public static final String FILL = "FILL";
    public static final double TOP = 0.0;
    public static final double CENTER = 0.5;
    public static final double BOTTOM = 1.0;
	
    private static final boolean DEBUG = false;

    private int m_spacing;
    private boolean m_sticky = false;
    private boolean m_stretch = false;
    private double m_justification;
    private double m_alignment;
    private Component m_fillComponent;

    private transient SizeRequirements[] m_h;
    private transient SizeRequirements m_W;
    private transient SizeRequirements m_H;

    
    public ColumnLayout()
    {
        this( 0 );
    }
    
    public ColumnLayout( int spacing )
    {
        this( spacing, false );
    }
    
    public ColumnLayout( int spacing, boolean sticky )
    {
        this( spacing, sticky, TOP );
    }
    
    public ColumnLayout( int spacing, boolean sticky, boolean stretch )
    {
        this( spacing, sticky, stretch, TOP, 0 );
    }
    
    public ColumnLayout( boolean sticky )
    {
        this( 0, sticky );
    }
    
    public ColumnLayout( boolean sticky, boolean stretch )
    {
        this( 0, sticky, stretch );
    }
    
    public ColumnLayout( boolean sticky, double justification )
    {
        this( 0, sticky, justification );
    }
    
    public ColumnLayout( int spacing, boolean sticky, double justification )
    {
        this( spacing, sticky, false, justification, 0 );
    }
    
    public ColumnLayout( int spacing, boolean sticky, boolean stretch, double justification, double alignment )
    {
        m_spacing = spacing;
        m_sticky = sticky;
        m_stretch = stretch;
        m_justification = justification;
        m_alignment = alignment;
    }

    
    public void addLayoutComponent( Component comp, Object constraints )
    {
        if
            ( constraints == FILL )
        {
            m_fillComponent = comp;
        }
    }
    
    public void addLayoutComponent(String name, Component comp)
    {
        if
            ( name == FILL )
        {
            m_fillComponent = comp;
        }
    }
    
    void checkRequests( Container target )
    {
        if
            ( m_H == null )
        {
            int I = target.getComponentCount();

            m_W = new SizeRequirements();
            m_H = new SizeRequirements();
            m_h = new SizeRequirements[ I ];
            for
                ( int i = 0; i < I; i++ )
            {
                m_h[ i ] = new SizeRequirements();
            }
		
            for
                ( int i = 0; i < I; i++ )
            {
                Component c = target.getComponent( i );
			
                Dimension min = c.getMinimumSize();
                Dimension typ = c.getPreferredSize();
                Dimension max = c.getMaximumSize();
			
                m_h[ i ].minimum =   min.height;
                m_h[ i ].preferred = typ.height;
                m_h[ i ].maximum =   max.height;

                m_W.minimum =   (int) Math.max( (long) m_W.minimum,   min.width );
                m_W.preferred = (int) Math.max( (long) m_W.preferred, typ.width );
                m_W.maximum =   (int) Math.max( (long) m_W.maximum,   max.width );

                m_H.minimum += m_h[ i ].minimum + ( ( i == 0 ) ? 0 : m_spacing );
                m_H.preferred += m_h[ i ].preferred + ( ( i == 0 ) ? 0 : m_spacing );
                m_H.maximum += m_h[ i ].maximum + ( ( i == 0 ) ? 0 : m_spacing );

                if ( m_H.minimum < 0 ) m_H.minimum = Integer.MAX_VALUE;
                if ( m_H.preferred < 0 ) m_H.preferred = Integer.MAX_VALUE;
                if ( m_H.maximum < 0 ) m_H.maximum = Integer.MAX_VALUE;
            }
        }
    }
    
    public float getLayoutAlignmentX(Container target)
    {
        checkRequests(target);
        return 0;//m_W.alignment;
    }
    
    public float getLayoutAlignmentY(Container target)
    {
        checkRequests(target);
        return 0;//m_H.alignment;
    }
    
    public void invalidateLayout(Container target)
    {
        m_H = null;
        m_h = null;
        m_W = null;
    }
    
    public void layoutContainer( Container target )
    {
        checkRequests( target );

        Insets in = target.getInsets();

        int W =
            m_stretch
            ?
            ( target.getWidth() - ( in.left + in.right ) )
            :
            ( Math.min( m_W.preferred, target.getWidth() - ( in.left + in.right ) ) );
                
        int Y = ( m_fillComponent != null ) ? 0 : (int) ( m_justification * ( target.getHeight() - ( in.top + in.bottom ) - m_H.preferred ) );
        Y += in.top;
        
        int fillComponentIndex = -1;
        int X = Math.min( in.left, Short.MAX_VALUE );

        if
            ( DEBUG && target.getName() != null )
        {
            System.err.println( target.getName() + ": " + target.getHeight() + "   " + in );
        }
        
        int I = target.getComponentCount();
        for
            ( int i = 0; i < I; i++ )
        {
            Component c = target.getComponent( i );
            if ( c == m_fillComponent ) fillComponentIndex = i;

            int y = Math.min( Y, Short.MAX_VALUE );
            int w = m_sticky ? Math.min( W, Short.MAX_VALUE ) : c.getPreferredSize().width;
            int h = Math.min( m_h[ i ].preferred, Short.MAX_VALUE );
            Y += h + m_spacing;
            int x = (int) ( X + getAlignmentfor( c ) * ( W - w ) );

            if
                ( DEBUG && target.getName() != null )
            {
                System.err.println( x + " " + y + " " + w + " " + h + " ::: " + c.getName() + " (" + c.getClass() + ")" );
            }

            c.setBounds( x, y, w, h );
        }

        if
            ( m_fillComponent != null )
        {
            Y -= m_spacing;
            int H = target.getHeight() - in.bottom;
            if
                ( H > Y )
            {
                Dimension d = m_fillComponent.getSize();
                int dH = H - Y;
                d.height += dH;
                m_fillComponent.setSize( d );

                if
                    ( DEBUG && target.getName() != null )
                {
                    System.err.println( "FILL " + d.height + " ::: " + m_fillComponent.getName() + " (" + m_fillComponent.getClass() + ")" );
                }
                
                for
                    ( int i = fillComponentIndex + 1; i < I; i++ )
                {
                    Component c = target.getComponent( i );
                    Point p = c.getLocation();
                    p.y += dH;
                    c.setLocation( p );

                    if
                        ( DEBUG && target.getName() != null )
                    {
                        System.err.println( "FILL " + p.y + " ::: " + c.getName() + " (" + c.getClass() + ")" );
                    }
                }
            }
        }
    }

    private double getAlignmentfor( Component c )
    {
        return Double.isNaN( m_alignment ) ? c.getAlignmentX() : m_alignment;
    }
    
    public Dimension maximumLayoutSize( Container target )
    {
        checkRequests( target );
        Dimension size = new Dimension( m_W.maximum, m_H.maximum );
        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
        return size;
    }
    
    public Dimension minimumLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( m_W.minimum, m_H.minimum );
        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
        return size;
    }
    
    public Dimension preferredLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( m_W.preferred, m_H.preferred );
        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Short.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Short.MAX_VALUE );
        return size;
    }
    
    public void removeLayoutComponent(Component comp)
    {
        if ( m_fillComponent == comp ) m_fillComponent = null;
    }
}
