package nu.esox.gui.layout.al;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import nu.esox.al.*;
import javax.swing.*;

  /**
   * Panel for testing and debugging attachment layout configurations.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */
public class AttachmentPanel extends JPanel
{
    private boolean m_showSize = false;
    private boolean m_showAttachments = false;
    private boolean m_showBounds = false;
    private int m_gridX = 0;
    private int m_gridY = 0;

    private JPopupMenu m_menu;
    private JTextField m_textField;
    private JWindow m_textFieldWindow;

    protected TextFieldListener m_textFieldListener;


    interface TextFieldListener
    {
        void valueEntered( ActionEvent e );
    }


    public AttachmentPanel()
    {
        super( new AttachmentLayout() );

        init();
    }

    public AttachmentPanel( boolean isDoubleBuffered )
    {
        super( new AttachmentLayout(), isDoubleBuffered );

        init();
    }


    private void createMenu()
    {
        m_menu = new JPopupMenu();
	
        abstract class PropertyActionListener implements ActionListener, TextFieldListener
        {
            public final void actionPerformed( ActionEvent e )
            {
                postTextField( format( e ), this );
            }
		
            public abstract String format( ActionEvent e );
        };


        m_menu.add( new JMenuItem( "pack" ) ).addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    Container top = getTopLevelAncestor();
                    if ( top instanceof Window ) ( (Window) top ).pack();
                }
            } );

        m_menu.add( new JMenuItem( ( getShowSize() ? "hide" : "show" ) + " size" ) ).addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    setShowSize( ! getShowSize() );
                    ( (JMenuItem) e.getSource() ).setText( ( getShowSize() ? "hide" : "show" ) + " size" );
                }
            } );

        m_menu.add( new JMenuItem( ( getShowAttachments() ? "hide" : "show" ) + " attachments" ) ).addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    setShowAttachments( ! getShowAttachments() );
                    ( (JMenuItem) e.getSource() ).setText( ( getShowAttachments() ? "hide" : "show" ) + " attachments" );
                }
            } );

        m_menu.add( new JMenuItem( ( getShowBounds() ? "hide" : "show" ) + " bounds" ) ).addActionListener( new ActionListener()
            {
                public void actionPerformed( ActionEvent e )
                {
                    setShowBounds( ! getShowBounds() );
                    ( (JMenuItem) e.getSource() ).setText( ( getShowBounds() ? "hide" : "show" ) + " bounds" );
                }
            } );

        m_menu.add( new JMenuItem( "grid..." ) ).addActionListener( new PropertyActionListener()
            {
                public String format( ActionEvent e )
                {
                    return getGridX() + " " + getGridY();
                    
                }
                
                public void valueEntered( ActionEvent e )
                {
                    int[] i = parseInts( e.getActionCommand(), 2 );
                    if
                        ( i != null )
                    {
                        setGridX( i[ 0 ] );
                        setGridY( i[ 1 ] );
                    }
                }
            } );
        
        m_menu.add( new JMenuItem( "insets..." ) ).addActionListener( new PropertyActionListener()
            {
                public String format( ActionEvent e )
                {
                    Insets i = getInsets();
                    return i.top + " " + i.right + " " + i.bottom + " " + i.left;
                }
                
                public void valueEntered( ActionEvent e )
                {
                    int[] i = parseInts( e.getActionCommand(), 4 );
                    int t, r, b, l;
                    
                    if
                        ( i != null )
                    {
                        t = i[ 0 ];
                        l = i[ 3 ];
                        b = i[ 2 ];
                        r = i[ 1 ];
                    } else if
                        ( ( i = parseInts( e.getActionCommand(), 1 ) ) != null )
                    {
                        t = l = b = r = i[ 0 ];
                    } else {
                        return;
                    }
                    
                    Insets I = getInsets();
                    int dw = l + r - I.left - I.right;
                    int dh = t + b - I.top - I.bottom;
                    setBorder( BorderFactory.createMatteBorder( t, l, b, r, Color.green ) );
                    Container top = getTopLevelAncestor();
                    top.setSize( top.getWidth() + dw, top.getHeight() + dh );
                    top.validate();
                }
            } );
    }

    protected MouseListener createMouseListener()
    {
        return
            new MouseListener()
            {
                public void mousePressed( MouseEvent e ) { if ( e.isPopupTrigger() ) postMenu( e ); }
                public void mouseReleased( MouseEvent e ) { if ( e.isPopupTrigger() ) postMenu( e ); }
                public void mouseClicked( MouseEvent e ) { if ( e.isPopupTrigger() ) postMenu( e ); }
                public void mouseEntered( MouseEvent e ) {}
                public void mouseExited( MouseEvent e ) {}
            };
    }

    private void drawArrowX( Component c, Graphics g, int x0, int y0, int s0, SiblingAttachment ca )
    {
        Rectangle r = ( (ComponentAttachable) ca.getAttachedConstraints()[ 0 ].getAttachable() ).getComponent().getBounds(); // PENDING: can't handle multiple attachments yet

        int x = c.getX();
        int y = c.getY();

        int y1 = r.y - y + r.height / 2;
        int x1 = r.x - x;
        int s1 = -1;
        if
            ( ca.getAttachedEdge() == Edge.RIGHT )
        {
            s1 = 1;
            x1 += r.width;
        }

        if ( y0 >= y1 ) y1 += 10; else y1 -= 10;
        if ( y1 < r.y - y ) y1 = r.y - y + 1;
        if ( y1 > r.y - y + r.height ) y1 = r.y - y + r.height - 1;

        g.drawLine( x0 - s0 * 3, y0,     x0 + s0 * 15, y0 );
        g.drawLine( x0 + s0 * 15, y0,    x1 + s1 * 10, y1 );	
        g.drawLine( x1 + s1 * 10, y1,    x1, y1 );
        g.drawLine( x1, y1,              x1 + s1 * 3, y1 + s1 * 3 );
        g.drawLine( x1, y1,              x1 + s1 * 3, y1 - s1 * 3 );
    }

    private void drawArrowY( Component c, Graphics g, int x0, int y0, int s0, SiblingAttachment ca )
    {
        Rectangle r = ( (ComponentAttachable) ca.getAttachedConstraints()[ 0 ].getAttachable() ).getComponent().getBounds(); // PENDING: can't handle multiple attachments yet

        int x = c.getX();
        int y = c.getY();
	
        int y1 = r.y - y;
        int x1 = r.x - x + r.width / 2;
        int s1 = -1;
        if
            ( ca.getAttachedEdge() == Edge.BOTTOM )
        {
            s1 = 1;
            y1 += r.height;
        }

        if ( x0 >= x1 ) x1 += 20; else x1 -= 20;
        if ( x1 < r.x - x ) x1 = r.x - x + 1;
        if ( x1 > r.x - x + r.width ) x1 = r.x - x + r.width - 1;
	
        g.drawLine( x0, y0 - s0 * 3,     x0, y0 + s0 * 15 );
        g.drawLine( x0, y0 + s0 * 15,    x1, y1 + s1 * 10 );		
        g.drawLine( x1, y1 + s1 * 10,    x1, y1 );
        g.drawLine( x1, y1,              x1 + s1 * 3,   y1 + s1 * 3 );
        g.drawLine( x1, y1,              x1 - s1 * 3,   y1 + s1 * 3 );
    }

    public int getGridX()
    {
        return m_gridX;
    }

    public int getGridY()
    {
        return m_gridY;
    }

    public boolean getShowAttachments()
    {
        return m_showAttachments;
    }

    public boolean getShowBounds()
    {
        return m_showBounds;
    }

    public boolean getShowSize()
    {
        return m_showSize;
    }

    protected String getStringFor( Attachment a )
    {
        if
            ( a instanceof RelativeAttachment )
        {
            RelativeAttachment ra = (RelativeAttachment) a;
            int pos = (int) ( 100 * ra.getPosition() );
            double off = ra.getOffset();
            if      ( off == 0 ) return pos + "%";
            else if ( off < 0 )  return pos + "% " + ra.getOffset();
            else                 return pos + "% +" + ra.getOffset();
        } else if
            ( a instanceof ContainerAttachment )
        {
            ContainerAttachment ca = (ContainerAttachment) a;
            return ca.getOffset() + "";
        } else if
            ( a instanceof SiblingAttachment )
        {
            SiblingAttachment ca = (SiblingAttachment) a;
            return ( ca.getOffset() == 0 ) ? null : ca.getOffset() + "";
        } else {
            return null;
        }
    }

    private void init()
    {
        addMouseListener( createMouseListener() );
    }

    public void paint( Graphics g )
    {
        super.paint( g );

        paintGridAndSize( g );

        paintAttachmentsAndBounds( g );
    }

    private void paintAttachmentsAndBounds( Constraint constraint, Graphics2D g )
    {
        Component c = ( (ComponentAttachable) constraint.getAttachable() ).getComponent();

	
        g.setFont( new Font( "dialog", 0, 10 ) );
        g.setClip( null );

        int x = c.getX();
        int y = c.getY();
        int h = c.getHeight();
        int w = c.getWidth();

        g.translate( x, y );
	
        int wd2 = w / 2;
        int hd2 = h / 2;
        int fontHeight = g.getFontMetrics().getHeight();

        if
            ( m_showBounds )
        {
            g.setColor( Color.blue );
            g.drawString( x + " " + y + " " + w + " " + h, 2, - 2 );
            g.drawString( (int) c.getPreferredSize().getWidth() + " " + (int) c.getPreferredSize().getHeight(), 2, h + fontHeight - 5 );
        }

        if
            ( m_showAttachments )
        {
            g.setColor( Color.red );
		
            String str = getStringFor( constraint.getLeft() );
            if
                ( str != null )
            {
                Rectangle2D d = g.getFont().getStringBounds( str, g.getFontRenderContext() );
                g.drawString( str, - (float) ( 1 + d.getWidth() ), hd2 );
            }

            str = getStringFor( constraint.getRight() );
            if ( str != null ) g.drawString( str, w + 2, hd2 );
	
            str = getStringFor( constraint.getTop() );
            if ( str != null ) g.drawString( str, wd2 + 2, -1 );

            str = getStringFor( constraint.getBottom() );
            if ( str != null ) g.drawString( str, wd2 + 2, h + fontHeight - 7 );

            g.setColor( Color.red );
            if ( constraint.getLeft()   instanceof SiblingAttachment ) drawArrowX( c, g, 0,   hd2, -1, (SiblingAttachment) constraint.getLeft() );
            if ( constraint.getRight()  instanceof SiblingAttachment ) drawArrowX( c, g, w,   hd2,  1, (SiblingAttachment) constraint.getRight() );
            if ( constraint.getTop()    instanceof SiblingAttachment ) drawArrowY( c, g, wd2, 0,   -1, (SiblingAttachment) constraint.getTop() );
            if ( constraint.getBottom() instanceof SiblingAttachment ) drawArrowY( c, g, wd2, h,    1, (SiblingAttachment) constraint.getBottom() );
        }

        g.translate( -x, -y );
    }

    private void paintAttachmentsAndBounds( Graphics g )
    {
        if
            ( m_showAttachments || m_showBounds )
        {
            Color c = g.getColor();
            Font f = g.getFont();
		
            Graphics2D G = (Graphics2D) g;

            g.setFont( new Font( "dialog", 0, 10 ) );
            g.setClip( null );

            Iterator i = ( (AttachmentLayout) getLayout() ).getConstraints().iterator();
            while
                ( i.hasNext() )
            {
                paintAttachmentsAndBounds( (Constraint) i.next(), G );
            }

            g.setFont( f );
            g.setColor( c );
        }
    }

    private void paintGridAndSize( Graphics g )
    {
        Color c = g.getColor();

        int W = getWidth();
        int H = getHeight();
	
        if
            ( m_gridX > 0 || m_gridY > 0 )
        {
            g.setColor( Color.black );
            Insets i = getInsets();
            int x0 = i.left;
            int y0 = i.top;
            int w = W - i.left - i.right;
            int h = H - i.top - i.bottom;
            int x1 = x0 + w - 1;
            int y1 = y0 + h - 1;

            float k = 1 / (float) m_gridX;
            for
                ( int n = 1; n < m_gridX; n++ )
            {
                int x = x0 + (int) ( w * k * n );
                g.drawLine( x, y0, x, y1 );
            }

            k = 1 / (float) m_gridY;
            for
                ( int n = 1; n < m_gridY; n++ )
            {
                int y = y0 + (int) ( h * k * n );
                g.drawLine( x0, y, x1, y );
            }

        }

        if
            ( m_showSize )
        {
            g.setColor( Color.red );
            g.drawString( W + ", " + H, 0, H - 3 );
        }

        g.setColor( c );
    }

    protected int[] parseInts( String str, int count )
    {
        StringTokenizer t = new StringTokenizer( str, " ,;\t%+" );

        int [] ints = new int[ count ];

        for
            ( int i = 0; i < count; i++ )
        {
            if
                ( ! t.hasMoreTokens() )
            {
                return null;
            }

            String token = t.nextToken();

            try
            {
                ints[ i ] = Integer.parseInt( token );
            }
            catch ( NumberFormatException ex )
            {
                return null;
            }
        }

        return ints;
    }

    private void postMenu( MouseEvent e )
    {
        if
            ( m_menu == null )
        {
            createMenu();
        }

        m_menu.show( this, e.getX(), e.getY() );
    }

    protected void postTextField( String str, TextFieldListener textFieldListener )
    {
        m_textFieldListener = textFieldListener;
	
        Container top = getTopLevelAncestor();
	
        if
            ( m_textField == null )
        {
            if
                ( top instanceof Window )
            {
                m_textFieldWindow = new JWindow( (Window) top );
            } else {
                m_textFieldWindow = new JWindow();
            }
            m_textField = new JTextField( 20 );
            m_textFieldWindow.getContentPane().add( m_textField );
            m_textField.addActionListener(
                                          new ActionListener()
                                          {
                                              public final void actionPerformed( ActionEvent e )
                                              {
                                                  m_textFieldWindow.setVisible( false );
                                                  if ( m_textFieldListener != null ) m_textFieldListener.valueEntered( e );
                                              }
                                          }
                                          );

            m_textFieldWindow.pack();
        }


        Point p = getLocationOnScreen();
        m_textFieldWindow.setLocation( (int) p.getX() + getWidth() / 2, (int) p.getY() + getHeight() / 2 );

        m_textField.setText( str );
        m_textField.selectAll();
        m_textFieldWindow.setVisible( true );
        m_textField.requestFocus();
    }

    public void setGridX( int gridX )
    {
        if
            ( gridX != m_gridX )
        {
            m_gridX = gridX;
            repaint();
        }
    }

    public void setGridY( int gridY )
    {
        if
            ( gridY != m_gridY )
        {
            m_gridY = gridY;
            repaint();
        }
    }

    public void setShowAttachments( boolean showAttachments )
    {
        if
            ( showAttachments != m_showAttachments )
        {
            m_showAttachments = showAttachments;
            repaint();
        }
    }

    public void setShowBounds( boolean showBounds )
    {
        if
            ( showBounds != m_showBounds )
        {
            m_showBounds = showBounds;
            repaint();
        }
    }

    public void setShowSize( boolean showSize )
    {
        if
            ( showSize != m_showSize )
        {
            m_showSize = showSize;
            repaint();
        }
    }

    static final long serialVersionUID = 42;
}
