package nu.esox.gui.layout;


import java.awt.*;
//import javax.swing.SizeRequirements;


/*
	A layout manager that places components in two columns.
	The figure below illustrates how the components are laid out.

					column 1			column 2
	row 0		component 0		component 1
	row 1		component 2		component 3
	row 2		component 4		component 5
	...

	The width of each column is the same as the widest component in the column.
	The height of each row is the same as the highest of the two componenta in the row.

	The configuration possibilities are:
	Sticky - If set all components in column 2 are stretched to the width of the widest one (default = false).
	horizontalGap - Spacing between the rows (default = 0).
	verticalGap - Spacing between the columns (default = 0).

	BUG: Will throw an exception if the number of components is odd.
*/

public class FormLayout implements LayoutManager2
{
    protected int m_horizontalGap;
    protected int m_verticalGap;
    protected boolean m_sticky;
	
    protected transient SizeRequirements[] m_yChildren;
    protected transient SizeRequirements m_x1Total;
    protected transient SizeRequirements m_x2Total;
    protected transient SizeRequirements m_yTotal;

    private static final boolean TRACE = false;


    public FormLayout()
    {
        this( 0, 0 );
    }
    
    public FormLayout( int horizontalGap, int verticalGap )
    {
        this( horizontalGap, verticalGap, true );
    }
    
    public FormLayout( int horizontalGap, int verticalGap, boolean sticky )
    {
        m_horizontalGap = horizontalGap;
        m_verticalGap = verticalGap;
        m_sticky = sticky;
    }

    public FormLayout( boolean sticky )
    {
        this( 0, 0, sticky );
    }


    public void addLayoutComponent( Component comp, Object constraints )
    {
    }

    public void addLayoutComponent( String name, Component comp )
    {
    }

    void checkRequests( Container target )
    {
        if
            ( m_yChildren == null )
        {
            int I = target.getComponentCount();
            int N = (int) Math.ceil( I / 2.0 );

            m_x1Total = new SizeRequirements();
            m_x2Total = new SizeRequirements();
            m_yTotal = new SizeRequirements();
            m_yChildren = new SizeRequirements[ N ];
            for
                ( int i = 0; i < N; i++ )
            {
                m_yChildren[ i ] = new SizeRequirements();
            }

            boolean firstColumn = true;
            
            for
                ( int i = 0; i < I; i++ )
            {
                Component c = target.getComponent( i );
			
                Dimension min = c.getMinimumSize();
                Dimension typ = c.getPreferredSize();
                Dimension max = c.getMaximumSize();

                int z = m_yChildren[ i / 2 ].minimum;
                m_yChildren[ i / 2 ].minimum =   (int) Math.max( (long) m_yChildren[ i / 2 ].minimum,   min.height );
                m_yChildren[ i / 2 ].preferred = (int) Math.max( (long) m_yChildren[ i / 2 ].preferred, typ.height );
                m_yChildren[ i / 2 ].maximum =   (int) Math.max( (long) m_yChildren[ i / 2 ].maximum,   max.height );

                if
                    ( firstColumn )
                {
                    m_x1Total.minimum =   (int) Math.max( (long) m_x1Total.minimum,   min.width );
                    m_x1Total.preferred = (int) Math.max( (long) m_x1Total.preferred, typ.width );
                    m_x1Total.maximum =   (int) Math.max( (long) m_x1Total.maximum,   max.width );
                } else {
                    m_x2Total.minimum =   (int) Math.max( (long) m_x2Total.minimum,   min.width );
                    m_x2Total.preferred = (int) Math.max( (long) m_x2Total.preferred, typ.width );
                    m_x2Total.maximum =   (int) Math.max( (long) m_x2Total.maximum,   max.width );
                }

                if
                    ( ! firstColumn || ( i == I - 1 ) )
                {
                    m_yTotal.minimum +=   m_yChildren[ i / 2 ].minimum;
                    m_yTotal.preferred += m_yChildren[ i / 2 ].preferred;
                    m_yTotal.maximum +=   m_yChildren[ i / 2 ].maximum;

                    if ( m_yTotal.minimum < 0 ) m_yTotal.minimum = Integer.MAX_VALUE;
                    if ( m_yTotal.preferred < 0 ) m_yTotal.preferred = Integer.MAX_VALUE;
                    if ( m_yTotal.maximum < 0 ) m_yTotal.maximum = Integer.MAX_VALUE;
                }
                
                firstColumn = ! firstColumn;
            }
        }
    }

    public float getLayoutAlignmentX( Container target )
    {
        checkRequests( target );
        return 0;//m_x1Total.alignment;
    }

    public float getLayoutAlignmentY( Container target )
    {
        checkRequests( target );
        return 0;//m_yTotal.alignment;
    }

    public void invalidateLayout( Container target )
    {
        m_yChildren = null;
        m_x1Total = null;
        m_x2Total = null;
        m_yTotal = null;
    }

    public void layoutContainer( Container target )
    {
        checkRequests( target );

        int I = target.getComponentCount();

          // determine the child placements
        Dimension alloc = target.getSize();

        Insets in = target.getInsets();

        long Y = 0;
        for
            ( int i = 0; i < I; i++ )
        {
            Component c = target.getComponent( i );

            int x;
            int y;
            int w;
            int h;

            if
                ( ( ( i / 2 ) * 2 ) == i )
            {
                x = (int) Math.min( (long) in.left, Integer.MAX_VALUE );
                y = (int) Math.min( Y + (long) in.top, Integer.MAX_VALUE );
                w = (int) Math.min( (long) m_x1Total.preferred, Integer.MAX_VALUE );
                h = (int) Math.min( (long) m_yChildren[ i / 2 ].preferred, Integer.MAX_VALUE );
            } else {
                x = (int) Math.min( (long) m_horizontalGap + (long) m_x1Total.preferred + (long) in.left, Integer.MAX_VALUE );
                y = (int) Math.min( Y + (long) in.top, Integer.MAX_VALUE );
                w = (int) Math.min( (long) m_x2Total.preferred, Integer.MAX_VALUE );
                w = (int) Math.min( (long) c.getPreferredSize().width, Integer.MAX_VALUE );
                h = (int) Math.min( (long) m_yChildren[ i / 2 ].preferred, Integer.MAX_VALUE );
                Y += h + m_verticalGap;

                if
                    ( m_sticky )
                {
                    w = alloc.width - in.right - x;
                }
            }

            if
                ( TRACE )
            {
                System.err.print( x );
                System.err.print( " " );
                System.err.print( y );
                System.err.print( " " );
                System.err.print( w );
                System.err.print( " " );
                System.err.print( h );
                System.err.print( " ::: " );
                System.err.println( c );
            }

            c.setBounds( x, y, w, h );
        }
    }



    protected int preferredContentWidth()
    {      
        return m_horizontalGap + m_x1Total.preferred + m_x2Total.preferred;
    }
           
    
    protected int minimumContentWidth()
    {
        return m_horizontalGap + m_x1Total.minimum + m_x2Total.minimum;
    }
           
    
    protected int maximumContentWidth()
    {
        return m_horizontalGap + m_x1Total.maximum + m_x2Total.maximum;
    }

    
    public Dimension maximumLayoutSize( Container target )
    {
        checkRequests( target );
        Dimension size = new Dimension( maximumContentWidth(), m_yTotal.maximum );
        if
            ( m_yChildren.length > 0 )
        {
            size.height += m_verticalGap * ( m_yChildren.length - 1 );
        }

        Insets insets = target.getInsets();
	
        size.width = (int) Math.min( size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
	
        return size;
    }
    
    public Dimension minimumLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( minimumContentWidth(), m_yTotal.minimum );
        if
            ( m_yChildren.length > 0 )
        {
            size.height += m_verticalGap * ( m_yChildren.length - 1 );
        }

        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
	
        return size;
    }
    
    public Dimension preferredLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( preferredContentWidth(), m_yTotal.preferred );
        if
            ( m_yChildren.length > 0 )
        {
            size.height += m_verticalGap * ( m_yChildren.length - 1 );
        }

        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
	
        return size;
    }

    public void removeLayoutComponent( Component comp )
    {
    }
}
