package nu.esox.gui;


import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;


public class TextFieldFocusHandler implements FocusListener, DocumentListener
{
    public static final String FOCUS_LOST = "FOCUS_LOST";

    
    private final List<Document> m_dirtyDocs = new ArrayList<Document>();

    
    private TextFieldFocusHandler()
    {
    }

    private static final TextFieldFocusHandler m_instance = new TextFieldFocusHandler();


    
    public static void add( JTextField t )
    {
        t.addFocusListener( m_instance );
    }
    
    public static void remove( JTextField t )
    {
        t.removeFocusListener( m_instance );
    }


    
    public void focusGained( FocusEvent e )
    {
        JTextField t = (JTextField) e.getSource();
        t.selectAll();
        t.getDocument().addDocumentListener( this );
    }
    
    public void focusLost( FocusEvent e )
    {
        if ( e.isTemporary() ) return;
        
        JTextField t = (JTextField) e.getSource();
        t.getDocument().removeDocumentListener( this );

        t.select( 0, 0 );

        Document d = t.getDocument();
        if
            ( m_dirtyDocs.contains( d ) )
        {
            m_dirtyDocs.remove( d );

            t.setActionCommand( FOCUS_LOST ); 
            t.postActionEvent();
            t.setActionCommand( null ); 
        }
    }
    
    
    public void changedUpdate( DocumentEvent e )
    {
        markDirty( e.getDocument() );
    }
    
    public void insertUpdate( DocumentEvent e )
    {
        markDirty( e.getDocument() );
    }
    
    public void removeUpdate( DocumentEvent e )
    {
        markDirty( e.getDocument() );
    }

    private void markDirty( Document d )
    {
        if ( ! m_dirtyDocs.contains( d ) ) m_dirtyDocs.add( d );
        d.removeDocumentListener( this );
    }
}
