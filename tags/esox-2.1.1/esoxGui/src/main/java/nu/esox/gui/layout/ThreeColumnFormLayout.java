package nu.esox.gui.layout;


import java.awt.*;
//import javax.swing.*;



public class ThreeColumnFormLayout extends FormLayout
{
    private static final boolean DEBUG = false;

    
    protected transient SizeRequirements m_x3Total;

    
    public ThreeColumnFormLayout()
    {
        super();
    }
    
    public ThreeColumnFormLayout( int horizontalGap, int verticalGap )
    {
        super( horizontalGap, verticalGap );
    }
    
    public ThreeColumnFormLayout( int horizontalGap, int verticalGap, boolean sticky )
    {
        super( horizontalGap, verticalGap, sticky );
    }


    
    public void invalidateLayout( Container target )
    {
        super.invalidateLayout( target );
        m_x3Total = null;
    }
    
      
    protected int preferredContentWidth()
    {      
        return super.preferredContentWidth() + m_horizontalGap + m_x3Total.preferred;
    }
           
    
    protected int minimumContentWidth()
    {
        return super.minimumContentWidth() + m_horizontalGap + m_x3Total.minimum;
    }
           
    
    protected int maximumContentWidth()
    {
        return super.maximumContentWidth() + m_horizontalGap + m_x3Total.maximum;
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
                ( i % 3 == 0 )
            {
                x = (int) Math.min( (long) in.left, Short.MAX_VALUE );
                y = (int) Math.min( Y + (long) in.top, Short.MAX_VALUE );
                w = (int) Math.min( (long) m_x1Total.preferred, Short.MAX_VALUE );
                h = (int) Math.min( (long) m_yChildren[ i / 3 ].preferred, Short.MAX_VALUE );
            } else if
                ( i % 3 == 1 )
            {
                x = (int) Math.min( (long) m_horizontalGap + (long) m_x1Total.preferred + (long) in.left, Short.MAX_VALUE );
                y = (int) Math.min( Y + (long) in.top, Short.MAX_VALUE );
                w = (int) Math.min( (long) c.getPreferredSize().width, Short.MAX_VALUE );
                h = (int) Math.min( (long) m_yChildren[ i / 3 ].preferred, Short.MAX_VALUE );
            } else {
                x = (int) Math.min( (long) m_horizontalGap + (long) m_x1Total.preferred + (long) m_horizontalGap + (long) m_x2Total.preferred + (long) in.left, Short.MAX_VALUE );
                y = (int) Math.min( Y + (long) in.top, Short.MAX_VALUE );
                w = (int) Math.min( (long) m_x3Total.preferred, Short.MAX_VALUE );
                h = (int) Math.min( (long) m_yChildren[ i / 3 ].preferred, Short.MAX_VALUE );

                if
                    ( m_sticky )
                {
                    w = alloc.width - in.right - x;
                }

                Y += h + m_verticalGap;
            }

            if
                ( DEBUG )
            {
                System.err.print( i );
                System.err.print( " " );
                System.err.print( x );
                System.err.print( " " );
                System.err.print( y );
                System.err.print( " " );
                System.err.print( w );
                System.err.print( " " );
                System.err.print( h );
                System.err.print( " ::: " );
                System.err.println( c.getClass() );
            }

            c.setBounds( x, y, w, h );
        }
    }




    void checkRequests( Container target )
    {
        if
            ( m_yChildren == null )
        {
            int I = target.getComponentCount();

            m_x1Total = new SizeRequirements();
            m_x2Total = new SizeRequirements();
            m_x3Total = new SizeRequirements();
            m_yTotal = new SizeRequirements();
            m_yChildren = new SizeRequirements[ I / 3 ];
            for
                ( int i = 0; i < I / 3; i++ )
            {
                m_yChildren[ i ] = new SizeRequirements();
            }
            
            for
                ( int i = 0; i < I; i++ )
            {
                Component c = target.getComponent( i );
                
                Dimension min = c.getMinimumSize();
                Dimension typ = c.getPreferredSize();
                Dimension max = c.getMaximumSize();
                
                m_yChildren[ i / 3 ].minimum =   (int) Math.max( (long) m_yChildren[ i / 3 ].minimum,   min.height );
                m_yChildren[ i / 3 ].preferred = (int) Math.max( (long) m_yChildren[ i / 3 ].preferred, typ.height );
                m_yChildren[ i / 3 ].maximum =   (int) Math.max( (long) m_yChildren[ i / 3 ].maximum,   max.height );

                if
                    ( i % 3 == 0 )
                {
                    m_x1Total.minimum =   (int) Math.max( (long) m_x1Total.minimum,   min.width );
                    m_x1Total.preferred = (int) Math.max( (long) m_x1Total.preferred, typ.width );
                    m_x1Total.maximum =   (int) Math.max( (long) m_x1Total.maximum,   max.width );
                } else if
                    ( i % 3 == 1 )
                {
                    m_x2Total.minimum =   (int) Math.max( (long) m_x2Total.minimum,   min.width );
                    m_x2Total.preferred = (int) Math.max( (long) m_x2Total.preferred, typ.width );
                    m_x2Total.maximum =   (int) Math.max( (long) m_x2Total.maximum,   max.width );
                } else {
                    m_x3Total.minimum =   (int) Math.max( (long) m_x3Total.minimum,   min.width );
                    m_x3Total.preferred = (int) Math.max( (long) m_x3Total.preferred, typ.width );
                    m_x3Total.maximum =   (int) Math.max( (long) m_x3Total.maximum,   max.width );
                    m_yTotal.minimum +=   m_yChildren[ i / 3 ].minimum;
                    m_yTotal.preferred += m_yChildren[ i / 3 ].preferred;
                    m_yTotal.maximum +=   m_yChildren[ i / 3 ].maximum;
                }
            }
        }
    }


}
