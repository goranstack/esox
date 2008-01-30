package nu.esox.gui.layout;


import java.awt.*;

/*
  PENDING: write something
*/

public class RowColumnLayout implements LayoutManager2
{
    public static final int LAST = -1;
    public static final int NONE = -2;
    public static final int ALL_EQUAL = -3;
    public static final int ALL_PROPORTIONAL = -4;
    
    protected final int m_count;
    protected final boolean m_isCountHorizontal;
    protected final boolean m_fillCellsX;
    protected final boolean m_fillCellsY;
    protected final int m_fillColumn;
    protected final int m_fillRow;
    protected final int m_horizontalGap;
    protected final int m_verticalGap;
	
    protected transient SizeRequirements[] m_widths;
    protected transient SizeRequirements[] m_heights;
    protected transient SizeRequirements m_width;
    protected transient SizeRequirements m_height;

    private static final boolean TRACE = false;


    public RowColumnLayout() // two column table
    {
        this( 2 );
    }
    

    public RowColumnLayout( int count ) // n column table with stretching last column
    {
        this( count, true, true, true, LAST, NONE, 0, 0 );
    }
    

    public RowColumnLayout( int count, boolean isCountHorizontal )
    {
        this( count, isCountHorizontal, true, true, NONE, NONE, 0, 0 );
    }
    
    public RowColumnLayout( int count, boolean isCountHorizontal, boolean fillCellsX, boolean fillCellsY, int fillColumn, int fillRow, int horizontalGap, int verticalGap )
    {
        m_count = count;
        m_isCountHorizontal = isCountHorizontal;
        m_fillCellsX = fillCellsX;
        m_fillCellsY = fillCellsY;
        m_fillColumn = fillColumn;
        m_fillRow = fillRow;
        m_horizontalGap = horizontalGap;
        m_verticalGap = verticalGap;
    }


    public void addLayoutComponent( Component comp, Object constraints )
    {
          // PENDING: constant FILL as argument sets fillColumn/fillRow
    }

    public void addLayoutComponent( String name, Component comp )
    {
        addLayoutComponent( comp, name );
    }

    void checkRequests( Container target )
    {
        if
            ( m_width == null )
        {
            int I = target.getComponentCount();

            m_width = new SizeRequirements();
            m_height = new SizeRequirements();
            if
                ( m_isCountHorizontal )
            {
                m_widths = new SizeRequirements[ m_count ];
                m_heights = new SizeRequirements[ (int) Math.ceil( (float) I / m_count ) ];
            } else {
                m_widths = new SizeRequirements[ (int) Math.ceil( (float) I / m_count ) ];
                m_heights = new SizeRequirements[ m_count ];
            }
            for
                ( int i = 0; i < m_widths.length; i++ )
            {
               m_widths[ i ] = new SizeRequirements();
            }
            for
                ( int i = 0; i < m_heights.length; i++ )
            {
               m_heights[ i ] = new SizeRequirements();
            }

            int r = 0;
            int c = 0;
            
            for
                ( int i = 0; i < I; i++ )
            {
                Component component = target.getComponent( i );
			
                Dimension min = component.getMinimumSize();
                Dimension typ = component.getPreferredSize();
                Dimension max = component.getMaximumSize();

                m_widths[ c ].minimum =   (int) Math.max( (long) m_widths[ c ].minimum,   min.width );
                m_widths[ c ].preferred = (int) Math.max( (long) m_widths[ c ].preferred, typ.width );
                m_widths[ c ].maximum =   (int) Math.max( (long) m_widths[ c ].maximum,   max.width );

                m_heights[ r ].minimum =   (int) Math.max( (long) m_heights[ r ].minimum,   min.height );
                m_heights[ r ].preferred = (int) Math.max( (long) m_heights[ r ].preferred, typ.height );
                m_heights[ r ].maximum =   (int) Math.max( (long) m_heights[ r ].maximum,   max.height );

                if
                    ( m_isCountHorizontal )
                {
                    c++;
                    if
                        ( c >= m_widths.length )
                    {
                        c = 0;
                        r++;
                    }
                } else {
                    r++;
                    if
                        ( r >= m_heights.length )
                    {
                        r = 0;
                        c++;
                    }
                }
            }

            if
                ( m_widths.length > 1 )
            {
                m_width.minimum = - m_horizontalGap;
                m_width.preferred = - m_horizontalGap;
                m_width.maximum = - m_horizontalGap;
            }
            for
                ( int i = 0; i < m_widths.length; i++ )
            {
                m_width.minimum += m_widths[ i ].minimum + m_horizontalGap;
                m_width.preferred += m_widths[ i ].preferred + m_horizontalGap;
                m_width.maximum += m_widths[ i ].maximum + m_horizontalGap;
            }
            
            if
                ( m_heights.length > 1 )
            {
                m_height.minimum = - m_verticalGap;
                m_height.preferred = - m_verticalGap;
                m_height.maximum = - m_verticalGap;
            }
            for
                ( int i = 0; i < m_heights.length; i++ )
            {
                m_height.minimum += m_heights[ i ].minimum + m_verticalGap;
                m_height.preferred += m_heights[ i ].preferred + m_verticalGap;
                m_height.maximum += m_heights[ i ].maximum + m_verticalGap;
            }

//             Insets in = target.getInsets();

//             m_width.minimum += in.left + in.right;
//             m_width.preferred += in.left + in.right;
//             m_width.maximum += in.left + in.right;
//             m_height.minimum += in.top + in.bottom;
//             m_height.preferred += in.top + in.bottom;
//             m_height.maximum += in.top + in.bottom;
        }
    }

    public float getLayoutAlignmentX( Container target )
    {
        checkRequests( target );
        return 0;//m_width.alignment;
    }

    public float getLayoutAlignmentY( Container target )
    {
        checkRequests( target );
        return 0;//m_height.alignment;
    }

    public void invalidateLayout( Container target )
    {
        m_width = null;
        m_height = null;
        m_widths = null;
        m_heights = null;
    }

    public void layoutContainer( Container target )
    {
        checkRequests( target );

        int I = target.getComponentCount();

        Insets in = target.getInsets();

        int x = in.left;
        int y = in.top;
        int W = target.getSize().width - in.left - in.right;
        int H = target.getSize().height - in.top - in.bottom;

        int r = 0;
        int c = 0;

        for
            ( int i = 0; i < I; i++ )
        {
            Component component = target.getComponent( i );

            int w = (int) Math.min( (long) component.getPreferredSize().width, Integer.MAX_VALUE );
            int h = (int) Math.min( (long) component.getPreferredSize().height, Integer.MAX_VALUE );

            if
                ( m_fillCellsX )
            {
                w = (int) Math.max( w, m_widths[ c ].preferred );
            }

            if
                ( m_fillCellsY )
            {
                h = (int) Math.max( h, m_heights[ r ].preferred );
            }


            int horizonalPadding = 0;
            int verticalPadding = 0;
            int extraSpace = W - m_width.preferred;
            if
                ( true || extraSpace > 0 )
            {
                if
                    ( c == m_fillColumn )
                {
                    horizonalPadding = extraSpace;
                } else if
                    ( ( m_fillColumn == LAST ) && ( c == m_widths.length - 1 ) )
                {
                    horizonalPadding = extraSpace;
                } else if
                    ( m_fillColumn == ALL_EQUAL )
                {
                    if
                        ( c == m_widths.length - 1 )
                    {
                        horizonalPadding = W - x - w;
                    } else {
                        horizonalPadding = extraSpace / m_widths.length;
                    }
                } else if
                    ( m_fillColumn == ALL_PROPORTIONAL )
                {
                    if
                        ( c == m_widths.length - 1 )
                    {
                        horizonalPadding = W - x - w;
                    } else {
                        horizonalPadding = (int) Math.round( extraSpace * (float) m_widths[ c ].preferred / m_width.preferred );
                    }
                }
                  // PENDING: consider maximum size
            } else {
                  // PENDING: consider minimum size
            }

            if
                ( ( r == m_fillRow ) || ( ( r == m_heights.length - 1 ) && ( m_fillRow == LAST ) ) )
            {
                verticalPadding = H - m_height.preferred;
            }

            w += horizonalPadding;
            h += verticalPadding;
            
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

            component.setBounds( x, y, w, h );

            if
                ( m_isCountHorizontal )
            {
                x += m_widths[ c ].preferred + m_horizontalGap + horizonalPadding;
                c++;
                if
                    ( c >= m_widths.length )
                {
                    c = 0;
                    x = in.left;
                    y += m_heights[ r ].preferred + m_verticalGap + verticalPadding;
                    r++;
                }
            } else {
                y += m_heights[ r ].preferred + m_verticalGap + verticalPadding;
                r++;
                if
                    ( r >= m_heights.length )
                {
                    r = 0;
                    y = in.top;
                    x += m_widths[ c ].preferred + m_horizontalGap + horizonalPadding;
                    c++;
                }
            }
        }
    }


   
    public Dimension maximumLayoutSize( Container target )
    {
        checkRequests( target );
        Dimension size = new Dimension( m_width.maximum, m_height.maximum );

        Insets insets = target.getInsets();
        size.width = (int) Math.min( size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
	
        return size;
    }
    
    public Dimension minimumLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( m_width.minimum, m_height.minimum );

        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
	
        return size;
    }
    
    public Dimension preferredLayoutSize( Container target )
    {      
        checkRequests( target );
        Dimension size = new Dimension( m_width.preferred, m_height.preferred );

        Insets insets = target.getInsets();
        size.width = (int) Math.min( (long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE );
        size.height = (int) Math.min( (long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE );
	
        return size;
    }

    public void removeLayoutComponent( Component comp )
    {
    }

}
