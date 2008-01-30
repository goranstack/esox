package nu.esox.gui.layout.al;

import java.awt.*;
import java.awt.event.*;
import nu.esox.al.*;
import javax.swing.*;

  /**
   * Adds attachment editing capability to a AttachmentPanel.
   * Right button drag starts attachment assignment.
   * Release on background sets container attachment.
   * Ctrl + release on background sets relative attachment.
   * Release on sibling edge sets component attachment.
   * Delete, backspace, ctrl-x or ctrl-d sets null attachment.
   * Right button click edits attachment.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */
public class AttachmentPanelEditor implements ContainerListener, MouseMotionListener, MouseListener
{
    private AttachmentPanel m_panel;

    private Edge m_hitEdge = null;
    private Component m_hitComponent = null;

    private boolean m_isDragging = false;
    private int m_x0;
    private int m_y0;
    private int m_x;
    private int m_y;
    private String m_dragFeedback = null;

    private AttachmentTracker m_setAttachment = new SetAttachmentTracker();

    
    private AttachmentTracker m_trackAttachment =
        new AttachmentTracker()
        {
            public Object handleSameAttachment( Constraint constraint, Edge e )
            {
                return null;
            }
			
            public Object handleContainerAttachment( Constraint constraint, Edge e, int offset )
            {
                return "" + offset;
            }
			
            public Object handleComponentAttachment( Constraint constraint, Edge e, Component c, Edge e2, int offset )
            {
                return null;
            }
			
            public Object handleRelativeAttachment( Constraint constraint, Edge e, float position, int offset )
            {
                return (int) ( 100 * position ) + "%";
            }
			
            public Object doNothing( Constraint constraint, Edge e )
            {
                return null;
            }
        };

    private AttachmentTextListener m_attachmentTextListener = new AttachmentTextListener();


    
    public AttachmentPanelEditor( AttachmentPanel p )
    {
        super();

        init( p );
    }


    private void attachmentTextEntered( Attachment a, String text )
    {
        boolean dirty = false;
	
        if
            ( a instanceof ContainerAttachment )
        {
            int [] i = m_panel.parseInts( text, 1 );
            if ( i == null ) return;
            dirty |= EditorOperations.setOffset( (ContainerAttachment) a, i[ 0 ] );
        } else if
            ( a instanceof SiblingAttachment )
        {
            int [] i = m_panel.parseInts( text, 1 );
            if ( i == null ) return;
            dirty |=  EditorOperations.setOffset( (SiblingAttachment) a, i[ 0 ] );
        } else if
            ( a instanceof RelativeAttachment )
        {
            int [] i = m_panel.parseInts( text, 2 );
            if
                ( i == null )
            {
                i = m_panel.parseInts( text, 1 );
            }
            if ( i == null ) return;
            dirty |= EditorOperations.setPosition( (RelativeAttachment) a, i[ 0 ] / 100f );
            dirty |= EditorOperations.setOffset( (RelativeAttachment) a, ( i.length > 1 ) ? i[ 1 ] : 0 );
        }

        if
            ( dirty )
        {
            AttachmentLayout l = (AttachmentLayout) m_panel.getLayout();
	
            l.invalidateLayout( m_panel.getParent() );
            l.invalidateAttachments();
            m_panel.invalidate();	
            m_panel.validate();
            m_panel.repaint();
        }
    }

    private void componentAdded( Component c )
    {
        c.addMouseMotionListener( this );
        c.addMouseListener( this );
    }

    public void componentAdded( ContainerEvent e )
    {
        componentAdded( e.getChild() );
    }

    private void componentRemoved( Component c )
    {
        c.removeMouseMotionListener( this );
        c.removeMouseListener( this );
    }

    public void componentRemoved( ContainerEvent e )
    {
        componentRemoved( e.getChild() );
    }

    private void dettachEdge()
    {
        if
            ( m_hitComponent != null && m_hitEdge != null )
        {
            Constraint constraint = ( (AttachmentLayout) m_panel.getLayout() ).getConstraintFor( m_hitComponent );
            EditorOperations.unsetAttachment( constraint, m_hitEdge );
            m_panel.invalidate();	
            m_panel.validate();
            m_panel.repaint();
        }
    }

    private void editAttachment()
    {
        Attachment a = getCurrentAttachment( m_hitComponent );

        if ( EditorOperations.isNullAttachment( a ) ) return;
	
        m_panel.postTextField( m_panel.getStringFor( a ), m_attachmentTextListener.prepare( a ) );
    }

    private Attachment getCurrentAttachment( Component c )
    {
        if
            ( m_hitEdge == null )
        {
            return null;
        } else {
            return m_hitEdge.getAttachmentFor( ( (AttachmentLayout) m_panel.getLayout() ).getConstraintFor( c ) );
        }
    }

    private Edge getEdge( Edge e, Rectangle r, int x, int y, int [] X, int [] Y )
    {
        Edge hit = null;

        if
            ( ( e == Edge.LEFT ) || ( e == Edge.RIGHT ) )
        {
            hit = ( x < r.width / 2 ) ? Edge.LEFT : Edge.RIGHT;
        } else if
            ( ( e == Edge.TOP ) || ( e == Edge.BOTTOM ) )
        {
            hit = ( y < r.height / 2 ) ? Edge.TOP : Edge.BOTTOM;
        } else {
		
            if
                ( y < ( r.height / (float) r.width ) * x )
            {
                  // top or right
                if
                    ( y < r.height - x * ( r.height / (float) r.width ) )
                {
                      // top
                    hit = Edge.TOP;
                } else {
                      // right
                    hit = Edge.RIGHT;
                }
            }	else {
                  // left or bottom
                if
                    ( y < r.height - x * ( r.height / (float) r.width ) )
                {
                      // left
                    hit = Edge.LEFT;
                } else {
                      // bottom
                    hit = Edge.BOTTOM;
                }
            }
        }

        if
            ( X != null )
        {
            X[ 0 ] = r.width / 2;
            Y[ 0 ] = r.height / 2;
	
            if
                ( hit == Edge.LEFT )
            {
                X[ 1 ] = 0;
                Y[ 1 ] = r.height;
                X[ 2 ] = 0;
                Y[ 2 ] = 0;
            } else if
                ( hit == Edge.RIGHT )
            {
                X[ 1 ] = r.width;
                Y[ 1 ] = 0;
                X[ 2 ] = r.width;
                Y[ 2 ] = r.height;
            } else if
                ( hit == Edge.TOP )
            {
                X[ 1 ] = r.width;
                Y[ 1 ] = 0;
                X[ 2 ] = 0;
                Y[ 2 ] = 0;
            } else if
                ( hit == Edge.BOTTOM )
            {
                X[ 1 ] = 0;
                Y[ 1 ] = r.height;
                X[ 2 ] = r.width;
                Y[ 2 ] = r.height;
            }
        }

        return hit;
    }

    private void init( AttachmentPanel p )
    {
        m_panel = p;
	
        ActionListener al =
            new ActionListener()
            {
                public void actionPerformed( ActionEvent ev )
                {
                    dettachEdge();
                }
            };
		
        m_panel.registerKeyboardAction( al, KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
        m_panel.registerKeyboardAction( al, KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
        m_panel.registerKeyboardAction( al, KeyStroke.getKeyStroke( KeyEvent.VK_D, KeyEvent.CTRL_MASK ), JComponent.WHEN_IN_FOCUSED_WINDOW );
        m_panel.registerKeyboardAction( al, KeyStroke.getKeyStroke( KeyEvent.VK_X, KeyEvent.CTRL_MASK ), JComponent.WHEN_IN_FOCUSED_WINDOW );

        int I = m_panel.getComponentCount();
        for
            ( int i = 0; i < I; i++ )
        {
            componentAdded( m_panel.getComponent( i ) );
        }

        m_panel.addContainerListener( this );
    }

    public void mouseClicked( MouseEvent e )
    {
    }

    public void mouseDragged( MouseEvent e )
    {
        Graphics g = m_panel.getGraphics();
        g.setXORMode( m_panel.getBackground() );
        g.setColor( Color.red );
        g.setFont( new Font( "dialog", Font.BOLD, 14 ) );

        g.drawLine( m_x0, m_y0, m_x, m_y );
        if ( m_dragFeedback != null ) g.drawString( m_dragFeedback, m_x, m_y );

        Component c = (Component) e.getSource();
        m_x = e.getX() + c.getX();
        m_y = e.getY() + c.getY();
	
        g.drawLine( m_x0, m_y0, m_x, m_y );

        m_dragFeedback = (String) trackAttachment( m_trackAttachment, e, m_x, m_y );
        if ( m_dragFeedback != null ) g.drawString( m_dragFeedback, m_x, m_y );
    }

    public void mouseEntered( MouseEvent e )
    {
        if ( m_isDragging ) return;
	
        m_hitEdge = null;
        m_hitComponent = (Component) e.getComponent();
    }

    public void mouseExited( MouseEvent e )
    {
        if ( m_isDragging ) return;

        m_hitComponent = (Component) e.getSource();
        Graphics g = m_panel.getGraphics();
        g.setClip( m_hitComponent.getBounds() );
        m_panel.paint( g );
    }

    public void mouseMoved( MouseEvent e )
    {
        Component c = (Component) e.getSource();

        Rectangle r = c.getBounds();

        int x = e.getX();
        int y = e.getY();

        int [] X = new int [ 3 ];
        int [] Y = new int [ 3 ];
        Edge hit = getEdge( null, r, x, y, X, Y );
	
        if
            ( m_hitEdge != hit )
        {
            Graphics g = m_panel.getGraphics();
            g.setClip( c.getBounds() );
            m_panel.paint( g );
		
            g = c.getGraphics();
            g.setColor( Color.black );
            g.fillPolygon( X, Y, 3 );
        }

        m_hitEdge = hit;
    }

    public void mousePressed( MouseEvent e )
    {
        m_isDragging = true;

        Component c = (Component) e.getSource();
        m_x = e.getX() + c.getX();
        m_y = e.getY() + c.getY();
        m_x0 = m_x;
        m_y0 = m_y;
	
        Graphics g = m_panel.getGraphics();
        g.setXORMode( m_panel.getBackground() );
        g.setColor( Color.red );
        g.setFont( new Font( "dialog", Font.BOLD, 14 ) );
	
        g.drawLine( m_x0, m_y0, m_x, m_y );

        m_dragFeedback = (String) trackAttachment( m_trackAttachment, e, m_x, m_y );
        if ( m_dragFeedback != null ) g.drawString( m_dragFeedback, m_x, m_y );
    }

    public void mouseReleased( MouseEvent e )
    {
        Graphics g = m_panel.getGraphics();
        g.setXORMode( m_panel.getBackground() );
        g.setColor( Color.red );
        g.setFont( new Font( "dialog", Font.BOLD, 14 ) );

        Component component = (Component) e.getSource();
        m_x = e.getX() + component.getX();
        m_y = e.getY() + component.getY();
	
        g.drawLine( m_x0, m_y0, m_x, m_y );
        if ( m_dragFeedback != null ) g.drawString( m_dragFeedback, m_x, m_y );

        m_isDragging = false;

        trackAttachment( m_setAttachment, e, m_x, m_y );

        m_panel.invalidate();	
        m_panel.validate();
        m_panel.repaint();
    }

    private Object trackAttachment( AttachmentTracker t, MouseEvent e, int x, int y )
    {
        Component component = (Component) e.getSource();

        Rectangle parentBounds = m_panel.getBounds();
        {
            Insets i = m_panel.getInsets();
            parentBounds.x += i.left;
            parentBounds.y += i.top;
            parentBounds.width -= i.left + i.right;
            parentBounds.height -= i.top + i.bottom;
        }
	
        Constraint constraint = ( (AttachmentLayout) m_panel.getLayout() ).getConstraintFor( component );

        if
            ( ! parentBounds.contains( x, y ) )
        {
              // outide parent -> noop
			
        } else {
            Component c = null;
		
            int I = m_panel.getComponentCount();
            for
                ( int i = 0; i < I; i++ )
            {
                if
                    ( m_panel.getComponent( i ).getBounds().contains( x, y ) )
                {
                    c = m_panel.getComponent( i );
                    break;
                }
            }

            if
                ( c == null )
            {
                if
                    ( ( e.getModifiers() & InputEvent.CTRL_MASK ) != 0 )
                {
                      // relative
                    if
                        ( ( m_hitEdge == Edge.LEFT ) || ( m_hitEdge == Edge.RIGHT ) )
                    {
                        return t.handleRelativeAttachment( constraint, m_hitEdge, Math.round( 100 * ( x - parentBounds.x ) / (float) parentBounds.width ) / 100f, 0 );
                    } else {
                        return t.handleRelativeAttachment( constraint, m_hitEdge, Math.round( 100 * ( y - parentBounds.y ) / (float) parentBounds.height ) / 100f, 0 );
                    }
                } else {
                      // container
                    if      ( m_hitEdge == Edge.LEFT )   return t.handleContainerAttachment( constraint, m_hitEdge, x - parentBounds.x );
                    else if ( m_hitEdge == Edge.RIGHT )  return t.handleContainerAttachment( constraint, m_hitEdge, x - parentBounds.x - parentBounds.width );
                    else if ( m_hitEdge == Edge.TOP )    return t.handleContainerAttachment( constraint, m_hitEdge, y - parentBounds.y );
                    else if ( m_hitEdge == Edge.BOTTOM ) return t.handleContainerAttachment( constraint, m_hitEdge, y - parentBounds.y - parentBounds.height );
				
                }
            } else if
                ( c == component )
            {
                  // same
                return t.handleSameAttachment( constraint, m_hitEdge );
            } else {
                  // on sibling -> component
                return t.handleComponentAttachment( constraint, m_hitEdge, c, getEdge( m_hitEdge, c.getBounds(), x - c.getX(), y - c.getY(), null, null ), 0 );
            }
        }

        return t.doNothing( constraint, m_hitEdge );
    }



    private interface AttachmentTracker
    {
        Object handleSameAttachment( Constraint constraint, Edge e );
        Object handleContainerAttachment( Constraint constraint, Edge e, int offset );
        Object handleComponentAttachment( Constraint constraint, Edge e, Component c, Edge e2, int offset );
        Object handleRelativeAttachment( Constraint constraint, Edge e, float position, int offset );
        Object doNothing( Constraint constraint, Edge e );
    }

    private class SetAttachmentTracker implements AttachmentTracker
    {
        public Object handleSameAttachment( Constraint constraint, Edge e )
        {
            AttachmentPanelEditor.this.editAttachment();
            return null;
        }
		
        public Object handleContainerAttachment( Constraint constraint, Edge e, int offset )
        {
            EditorOperations.setAttachment( constraint, ContainerAttachment.create( e, offset ) );
            return null;
        }
		
        public Object handleComponentAttachment( Constraint constraint, Edge e, Component c, Edge e2, int offset )
        {
            EditorOperations.setAttachment( constraint, ComponentAttachment.create( e, c, e2, offset ) );
            return null;
        }
		
        public Object handleRelativeAttachment( Constraint constraint, Edge e, float position, int offset )
        {
            EditorOperations.setAttachment( constraint, RelativeAttachment.create( e, position, offset ) );
            return null;
        }
		
        public Object doNothing( Constraint constraint, Edge e )
        {
            return null;
        }
    };



    private class AttachmentTextListener implements AttachmentPanel.TextFieldListener
    {
        private Attachment m_attachment;

        public AttachmentTextListener prepare( Attachment a )
        {
            m_attachment = a;
            return this;
        }
		
        public void valueEntered( ActionEvent e )
        {
            AttachmentPanelEditor.this.attachmentTextEntered( m_attachment, e.getActionCommand() );			
        }
    }


}
