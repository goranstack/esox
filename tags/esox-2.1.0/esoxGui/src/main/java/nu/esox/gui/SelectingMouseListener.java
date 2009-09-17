package nu.esox.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//PENDING: keep aspect rato

public class SelectingMouseListener implements MouseListener, MouseMotionListener
{
    private JScrollPane m_scrollPane;
    private Graphics m_graphics;

    private final int m_cornerModifierMask;
    private final int m_centerModifierMask;
    private final Cursor m_cursor;

    private boolean m_isSelecting;
    private boolean m_isCenterSelection;
    private int m_x0;
    private int m_y0;
    private int m_x;
    private int m_y;
    private Cursor m_originalCursor;

    
    public SelectingMouseListener( Cursor cursor, int cornerModifierMask, int centerModifierMask )
    {
        this( null, cursor, cornerModifierMask, centerModifierMask );
    }
    
    public SelectingMouseListener( JScrollPane scrollPane, Cursor cursor, int cornerModifierMask, int centerModifierMask )
    {
        m_cornerModifierMask = cornerModifierMask;
        m_centerModifierMask = centerModifierMask;
        m_cursor = cursor;
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


    protected void start( MouseEvent ev, int x, int y )
    {
    }

    protected void selecting( MouseEvent ev, int x, int y, int w, int h )
    {
    }
    
    protected void selected( MouseEvent ev, int x, int y, int w, int h )
    {
    }

    
    public void mousePressed( MouseEvent ev )
    {
        m_isCenterSelection = ( ev.getModifiers() & m_centerModifierMask ) == m_centerModifierMask;
        m_isSelecting = m_isCenterSelection || ( ev.getModifiers() & m_cornerModifierMask ) == m_cornerModifierMask;

        if
            ( m_isSelecting )
        {
            if
                ( m_cursor != null )
            {
                m_originalCursor = m_scrollPane.getViewport().getView().getCursor();
                m_scrollPane.getViewport().getView().setCursor( m_cursor );
            }
            
            m_graphics = m_scrollPane.getViewport().getView().getGraphics();
            m_graphics.setColor( m_scrollPane.getViewport().getView().getBackground() );
            m_graphics.setXORMode( m_scrollPane.getViewport().getView().getForeground() );
            m_x0 = m_x = ev.getX();
            m_y0 = m_y = ev.getY();
            draw();
            start( ev, m_x0, m_y0 );
        }
    }

    public void mouseDragged( MouseEvent ev )
    {
        if
            ( m_isSelecting )
        {
            draw();
            m_x = ev.getX();
            m_y = ev.getY();
            draw();

            if
                ( m_isCenterSelection )
            {
                int dx = m_x0 - m_x;
                int dy = m_y0 - m_y;
                selecting( ev, m_x0 - dx, m_y0 - dy, dx * 2, dy * 2 );
            } else {
                selecting( ev,
                           Math.min( m_x0, m_x ),
                           Math.min( m_y0, m_y ),
                           Math.abs( m_x0 - m_x ),
                           Math.abs( m_y0 - m_y ) );
            }
        }
    }

    public void mouseReleased( MouseEvent ev )
    {
        if
            ( m_isSelecting )
        {
            draw();
            m_graphics = null;
            m_isSelecting = false;
            if
                ( m_originalCursor != null )
            {
                m_scrollPane.getViewport().getView().setCursor( m_originalCursor );
                m_originalCursor = null;
            }

            if
                ( m_isCenterSelection )
            {
                int dx = m_x0 - m_x;
                int dy = m_y0 - m_y;
                selected( ev, m_x0 - dx, m_y0 - dy, dx * 2, dy * 2 );
            } else {
                selected( ev,
                          Math.min( m_x0, m_x ),
                          Math.min( m_y0, m_y ),
                          Math.abs( m_x0 - m_x ),
                          Math.abs( m_y0 - m_y ) );
            }
        }
    }

    public void mouseMoved( MouseEvent ev ) {}
    public void mouseClicked( MouseEvent ev ) {}
    public void mouseEntered( MouseEvent ev ) {}
    public void mouseExited( MouseEvent ev ) {}


    private void draw()
    {
        if
            ( m_isCenterSelection )
        {
            int dx = Math.abs( m_x0 - m_x );
            int dy = Math.abs( m_y0 - m_y );
            m_graphics.drawRect( m_x0 - dx, m_y0 - dy, dx * 2, dy * 2 );
        } else {
            m_graphics.drawRect( Math.min( m_x0, m_x ),
                                 Math.min( m_y0, m_y ),
                                 Math.abs( m_x0 - m_x ),
                                 Math.abs( m_y0 - m_y ) );
        }
    }


}
