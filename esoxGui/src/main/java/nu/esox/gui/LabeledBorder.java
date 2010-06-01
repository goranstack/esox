package nu.esox.gui;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.*;


public class LabeledBorder extends AbstractBorder implements SwingConstants
{
    protected String m_title;
    protected int    m_titleJustification;
    protected int    m_titleOffset;
    protected Font   m_titleFont;
    protected Color  m_titleColor;
    protected int m_titlePosition;

    private Point m_textLoc = new Point();

      /**
       * Use the default justification for the title text.
       */
    static public final int     DEFAULT_JUSTIFICATION   = 0;
    
      /** Position title text at the left side of the border line. */
    static public final int     LEFT    = 1;
    static public final int     TOP    = 1;
    
      /** Position title text in the center of the border line. */
    static public final int     CENTER  = 2;
    
      /** Position title text at the right side of the border line. */
    static public final int     RIGHT   = 3;
    static public final int     BOTTOM   = 3;
    
      /** Position title text at the left side of the border line
       *  for left to right orientation, at the right side of the 
       *  border line for right to left orientation.
       */
    static public final int     LEADING = 4;
    
      /** Position title text at the right side of the border line
       *  for left to right orientation, at the left side of the 
       *  border line for right to left orientation.
       */
    static public final int     TRAILING = 5;
    

    
      /**
       * Creates a LabeledBorder instance.
       * 
       * @param title  the title the border should display
       */
    public LabeledBorder( String title )
    {
        this( title, LEADING, NORTH, 0, null, null );
    }

      /**
       * Creates a LabeledBorder instance with the specified border
       * and an empty title.
       * 
       * @param border  the border
       */
    public LabeledBorder()
    {
        this( "", LEADING, NORTH, 0, null, null );
    }

      /**
       * Creates a LabeledBorder instance with the specified border,
       * title, title-justification, and title-position.
       * 
       * @param border  the border
       * @param title  the title the border should display
       * @param titleJustification the justification for the title
       * @param titlePosition the position for the title
       */
    public LabeledBorder( String title, int titleJustification )
    {
        this( title, titleJustification, NORTH, 0, null, null );
    }

    public LabeledBorder( String title, int titleJustification, int titlePosition )
    {
        this( title, titleJustification, titlePosition, 0, null, null );
    }
    
      /**
       * Creates a LabeledBorder instance with the specified border,
       * title, title-justification, title-position, and title-font.
       * 
       * @param border  the border
       * @param title  the title the border should display
       * @param titleJustification the justification for the title
       * @param titlePosition the position for the title
       * @param titleFont the font for rendering the title
       */
    public LabeledBorder( String title, int titleJustification, Font titleFont )
    {
        this( title, titleJustification, NORTH, 0, titleFont, null );
    }

    public LabeledBorder( String title, Font titleFont )
    {
        this( title, LEFT, NORTH, 0, titleFont, null );
    }

      /**
       * Creates a LabeledBorder instance with the specified border,
       * title, title-justification, title-position, title-font, and
       * title-color.
       * 
       * @param border  the border
       * @param title  the title the border should display
       * @param titleJustification the justification for the title
       * @param titlePosition the position for the title
       */
    public LabeledBorder( String title, int titleJustification, int titlePosition, int titleOffset )
    {
        this( title, titleJustification, titlePosition, titleOffset, null, null );
    }

      /**
       * Creates a LabeledBorder instance with the specified border,
       * title, title-justification, title-position, title-font, and
       * title-color.
       * 
       * @param border  the border
       * @param title  the title the border should display
       * @param titleJustification the justification for the title
       * @param titlePosition the position for the title
       * @param titleOffset offset between component and title
       * @param titleFont the font of the title
       * @param titleColor the color of the title
       */
    public LabeledBorder( String title, int titleJustification, int titlePosition, int titleOffset, Font titleFont, Color titleColor )
    {
        m_title = title;
        m_titleFont = titleFont;
        m_titleColor = titleColor;
        setTitleJustification( titleJustification );
        setTitlePosition( titlePosition );
        setTitleOffset( titleOffset );
    }


    public void addTo( JComponent c )
    {
        c.setBorder( BorderFactory.createCompoundBorder( this, c.getBorder() ) );
    }
    
    private String getTitle( Component c )
    {
        return ( getTitle() == null ) ? c.getName() : getTitle();
    }
    
      /**
       * Paints the border for the specified component with the 
       * specified position and size.
       * @param c the component for which this border is being painted
       * @param g the paint graphics
       * @param x the x position of the painted border
       * @param y the y position of the painted border
       * @param width the width of the painted border
       * @param height the height of the painted border
       */
    public void paintBorder( Component c, Graphics g, int x, int y, int width, int height )
    {
        String title = getTitle( c );

        if
            ( title == null || title.equals( "" ) )
        {
            return;
        }

        Rectangle grooveRect = new Rectangle( x, y, width, height );
        Font font = g.getFont();
        Color color = g.getColor();

        g.setFont( getFont( c ) );

        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();
        int descent = fm.getDescent();
        int ascent = fm.getAscent();
        int stringWidth = fm.stringWidth( title );

        switch
            ( m_titlePosition )
        {
        case WEST:
            grooveRect.x += stringWidth;
            grooveRect.width -= stringWidth;
//            m_textLoc.y = ( grooveRect.y - descent ) + ( ascent + descent ) / 2;
            break;

        case EAST:
            grooveRect.width -= stringWidth;
            m_textLoc.x = grooveRect.width;
            break;

        case NORTH:
            {
                int diff = Math.max( 0, ascent / 2 );
                grooveRect.y += diff;
                grooveRect.height -= diff;
                m_textLoc.y = ( grooveRect.y - descent ) + ( ascent + descent ) / 2;
                break;
            }

        case SOUTH:
            {
                int diff = Math.max( 0, ascent / 2 );
                grooveRect.height -= diff;
                m_textLoc.y = ( grooveRect.y + grooveRect.height - descent ) + ( ascent + descent ) / 2;
                break;
            }
        }
        
//         if
//             ( m_titlePosition == WEST )
//         {
//             grooveRect.x += stringWidth;
//             grooveRect.width -= stringWidth;
// //            m_textLoc.y = (grooveRect.y - descent) + (ascent + descent)/2;
//         } else {
//             int diff = Math.max( 0, ascent / 2 );
//             grooveRect.y += diff;
//             grooveRect.height -= diff;
//             m_textLoc.y = ( grooveRect.y - descent ) + ( ascent + descent ) / 2;
//         }
        
        int justification = getTitleJustification();
        if
            ( c.getComponentOrientation().isLeftToRight() )
        {
            if
                ( justification==LEADING || justification == DEFAULT_JUSTIFICATION )
            {
                justification = LEFT;
            } else if
                ( justification==TRAILING )
            {
                justification = RIGHT;
            }
        } else {
            if
                ( justification==LEADING || justification == DEFAULT_JUSTIFICATION )
            {
                justification = RIGHT;
            } else if
                ( justification==TRAILING )
            {
                justification = LEFT;
            }
        }

        if
            ( m_titlePosition == WEST || m_titlePosition == EAST )
        {
            switch
                ( justification )
            {
            case TOP:
                m_textLoc.y = grooveRect.y + ( ascent + 0*descent );
                break;
            case BOTTOM:
                m_textLoc.y = grooveRect.y + grooveRect.height;
                break;
            case CENTER:
                m_textLoc.y = grooveRect.y + ( grooveRect.height + ( ascent + descent ) / 2 ) / 2;
                break;
            }
        } else {
            switch
                ( justification )
            {
            case LEFT:
                m_textLoc.x = grooveRect.x;
                break;
            case RIGHT:
                m_textLoc.x = ( grooveRect.x + grooveRect.width ) - stringWidth;
                break;
            case CENTER:
                m_textLoc.x = grooveRect.x + ( ( grooveRect.width - stringWidth ) / 2 );
                break;
            }
        }

//         g.setColor( Color.green );
//         g.fillRect( x, y, width, ascent + descent + m_titleOffset );
        
        g.setColor( getTitleColor() );
        g.drawString( title, m_textLoc.x, m_textLoc.y );

        g.setFont( font );
        g.setColor( color );
    }

      /**
       * Returns the insets of the border.
       * @param c the component for which this border insets value applies
       */
    public Insets getBorderInsets(Component c)
    {
        return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }

      /** 
       * Reinitialize the insets parameter with this Border's current Insets. 
       * @param c the component for which this border insets value applies
       * @param insets the object to be reinitialized
       */
    public Insets getBorderInsets(Component c, Insets insets)
    {
        insets.left = insets.top = insets.right = insets.bottom = 0;

        String title = getTitle( c );
        if
            ( c == null || title == null || title.equals( "" ) )
        {
            return insets;
        }

        Font font = getFont( c );

        FontMetrics fm = c.getFontMetrics( font );
        int         descent = 0;
        int         ascent = 16;

        switch
            ( m_titlePosition )
        {
        case WEST:
            insets.left += fm.stringWidth( title ) + m_titleOffset;
            break;

        case EAST:
            insets.right += fm.stringWidth( title ) + m_titleOffset;
            break;

        case NORTH:
            if
                ( fm != null )
            {
                descent = fm.getDescent();
                ascent = fm.getAscent();
            }
            
            insets.top += ascent + descent + m_titleOffset;
            break;

        case SOUTH:
            if
                ( fm != null )
            {
                descent = fm.getDescent();
                ascent = fm.getAscent();
            }
            
            insets.bottom += ascent + descent + m_titleOffset;
            break;
        }

        return insets;
    }

      /**
       * Returns whether or not the border is opaque.
       */
    public boolean isBorderOpaque() { return false; }

      /**
       * Returns the title of the titled border.
       */
    public String getTitle()        {       return m_title;   }

      /**
       * Returns the title-justification of the titled border.
       */
    public int getTitleJustification()      {       return m_titleJustification;      }

      /**
       * Returns the title-offset of the titled border.
       */
    public int getTitleOffset()      {       return m_titleOffset;      }

      /**
       * Returns the title-position of the titled border.
       */
    public int getTitlePosition()      {       return m_titlePosition;      }

      /**
       * Returns the title-font of the titled border.
       */
    public Font getTitleFont()
    {       
        Font f = m_titleFont;
        if (f == null) f = UIManager.getFont("TitledBorder.font");
        return f;       
    }

      /**
       * Returns the title-color of the titled border.
       */
    public Color getTitleColor()
    {       
        Color c = m_titleColor;
        if (c == null) c = UIManager.getColor("TitledBorder.titleColor");
        return c;  
    }


      // REMIND(aim): remove all or some of these set methods?

      /**
       * Sets the title of the titled border.
       * param title the title for the border
       */
    public void setTitle(String title)      {       m_title = title;     }

      /**
       * Sets the title-justification of the titled border.
       * @param titleJustification the justification for the border
       */
    public void setTitleJustification(int titleJustification)
    {
        switch
            (titleJustification)
        {
        case DEFAULT_JUSTIFICATION:
        case LEFT:
        case CENTER:
        case RIGHT:
        case LEADING:
        case TRAILING:
            m_titleJustification = titleJustification;
            break;
        default:
            throw new IllegalArgumentException(titleJustification + " is not a valid title justification.");
        }
    }

      /**
       * Sets the title-position of the titled border.
       * @param titlePosition the position for the border
       */
    public void setTitlePosition(int titlePosition)
    {
        switch
            (titlePosition)
        {
        case NORTH:
        case WEST:
        case SOUTH:
        case EAST:
            m_titlePosition = titlePosition;
            break;
        default:
            throw new IllegalArgumentException(titlePosition + " is not a valid title position.");
        }
    }

      /**
       * Sets the title-offset of the titled border.
       * @param titleOffset the offset for the border title
       */
    public void setTitleOffset( int titleOffset )
    {       
        m_titleOffset = titleOffset;     
    }

      /**
       * Sets the title-font of the titled border.
       * @param titleFont the font for the border title
       */
    public void setTitleFont(Font titleFont)
    {       
        m_titleFont = titleFont;     
    }

      /**
       * Sets the title-color of the titled border.
       * @param titleColor the color for the border title
       */
    public void setTitleColor(Color titleColor)
    {       
        m_titleColor = titleColor;   
    }

      /**
       * Returns the minimum dimensions this border requires
       * in order to fully display the border and title.
       * @param c the component where this border will be drawn
       */
    public Dimension getMinimumSize(Component c)
    {
        Insets insets = getBorderInsets(c);
        Dimension minSize = new Dimension(insets.right+insets.left, insets.top+insets.bottom);
        Font font = getFont(c);
        FontMetrics fm = c.getFontMetrics(font);
        minSize.width = Math.max(fm.stringWidth(getTitle( c )), minSize.width);
        return minSize;       
    }

    protected Font getFont(Component c)
    {
        Font font;
        if
            ((font = getTitleFont()) != null)
        {
        } else if
            (c != null && (font = c.getFont()) != null)
        {
        } else {
            font = new Font("Dialog", Font.PLAIN, 12);
        }

          //        if ( m_fontSizeOffset != 0 ) font = font.deriveFont( (float) ( font.getSize() + m_fontSizeOffset ) );
        return font;
    }  

/*

    public static void main( String [] args )
    {
        JFrame f = new JFrame();
        JPanel p = new JPanel( new RowLayout( 10, false ) );
        f.getContentPane().add( p );

        JCheckBox cb = null;

        String [] TITLE = new String [] { "North", "South", "West", "East" };
        int [] POS = new int [] { LabeledBorder.NORTH, LabeledBorder.SOUTH, LabeledBorder.WEST, LabeledBorder.EAST };
        int [] OFFSET = new int [] { 0, 0, 5, 5 };
        int [] JUST = new int [] { LabeledBorder.LEFT, LabeledBorder.CENTER, LabeledBorder.RIGHT };

        JPanel p2 = new JPanel( new ColumnLayout( 10, false ) );
        p.add( p2 );
        for
            ( int j = 0; j < JUST.length; j++ )
        {
            cb = new JCheckBox( (String) null );
            cb.setBorderPainted( true );
            cb.setOpaque( true );
            cb.setBackground( Color.cyan );
            cb.setBorder( null );
            p2.add( cb );
        }

        for
            ( int i = 0; i < POS.length; i++ )
        {
            p2 = new JPanel( new ColumnLayout( 10, false ) );
            p.add( p2 );

            for
                ( int j = 0; j < JUST.length; j++ )
            {
                cb = new JCheckBox( (String) null );
//                cb.setPreferredSize( new Dimension( cb.getPreferredSize().width, 200 ) );
                cb.setBorderPainted( true );
                cb.setOpaque( true );
                cb.setBackground( Color.cyan );
                cb.setBorder( new LabeledBorder( TITLE[ i ], JUST[ j ], POS[ i ], OFFSET[ i ] ) );
                p2.add( cb );
            }
        }

        f.pack();
        f.show();
    }
*/

    static final long serialVersionUID = 42;
}
