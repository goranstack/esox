package nu.esox.gui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ScrollingMouseListener implements MouseListener, MouseMotionListener
{
    private JScrollPane m_scrollPane;

    private final int m_modifierMask;

    private boolean m_isScrolling;
    private int m_x;
    private int m_y;

    
    public ScrollingMouseListener( int modifierMask )
    {
        this( null, modifierMask );
    }
    
    public ScrollingMouseListener( JScrollPane scrollPane, int modifierMask )
    {
        m_modifierMask = modifierMask;
        setScrollPane( scrollPane );
    }

    
    public void setScrollPane( JScrollPane scrollPane )
    {
        if
            ( m_scrollPane != null )
        {
            m_scrollPane.getViewport().getView().removeMouseMotionListener( this );
            m_scrollPane.getViewport().getView().removeMouseListener( this );
        }
        
        m_scrollPane = scrollPane;
        
        if
            ( m_scrollPane != null )
        {
            m_scrollPane.getViewport().getView().addMouseListener( this );
            m_scrollPane.getViewport().getView().addMouseMotionListener( this );
        }
    }


    public void mousePressed( MouseEvent ev )
    {
        m_isScrolling = ( ev.getModifiers() & m_modifierMask ) == m_modifierMask;

        if
            ( m_isScrolling )
        {
            m_x = ev.getX();
            m_y = ev.getY();
            m_scrollPane.getHorizontalScrollBar().setValueIsAdjusting( true );
            m_scrollPane.getVerticalScrollBar().setValueIsAdjusting( true );
        }
    }

    public void mouseDragged( MouseEvent ev )
    {
        if
            ( m_isScrolling )
        {
            int dx = m_x - ev.getX();
            int dy = m_y - ev.getY();

            {
                JScrollBar sb = m_scrollPane.getHorizontalScrollBar();
                sb.setValue( sb.getValue() + dx );
            }

            {
                JScrollBar sb = m_scrollPane.getVerticalScrollBar();
                sb.setValue( sb.getValue() + dy );
            }
            
            m_x = ev.getX() + dx;
            m_y = ev.getY() + dy;
        }
    }

    public void mouseReleased( MouseEvent ev )
    {
        if
            ( m_isScrolling )
        {
            m_scrollPane.getHorizontalScrollBar().setValueIsAdjusting( false );
            m_scrollPane.getVerticalScrollBar().setValueIsAdjusting( false );
            m_isScrolling = false;
        }
    }

    public void mouseMoved( MouseEvent ev ) {}
    public void mouseClicked( MouseEvent ev ) {}
    public void mouseEntered( MouseEvent ev ) {}
    public void mouseExited( MouseEvent ev ) {}


    



    public static void main( String [] args )
    {
        JFrame f = new JFrame();
        JTextArea t = new JTextArea( 40, 40 );
        JScrollPane s = new JScrollPane( t );
        f.getContentPane().add( s );

        new ScrollingMouseListener( s, InputEvent.CTRL_MASK );
        new SelectingMouseListener( s, Cursor.getPredefinedCursor( Cursor.CROSSHAIR_CURSOR ), InputEvent.BUTTON3_MASK, InputEvent.BUTTON2_MASK );
        
        f.pack();
        f.show();
    }
}
