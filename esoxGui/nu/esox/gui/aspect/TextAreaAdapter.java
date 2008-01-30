package nu.esox.gui.aspect;

import java.awt.event.*;
import nu.esox.util.*;
import javax.swing.*;
import javax.swing.event.*;


public class TextAreaAdapter extends AbstractAdapter implements FocusListener, ActionListener, DocumentListener
{
    private final JTextArea m_textArea;
    private final int m_commitDelay;
    private long m_t0 = 0;
    private Timer m_timer = null;

    
    public TextAreaAdapter( JTextArea textArea, int commitDelay, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, String aspectName )
    {
        this( textArea, commitDelay, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, String.class, aspectName, "", "" );
    }
    
    public TextAreaAdapter( JTextArea textArea, int commitDelay, ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, Class aspectClass, String aspectName )
    {
        this( textArea, commitDelay, modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, "", "" );
    }
    
    public TextAreaAdapter( JTextArea textArea,
                            int commitDelay,
                            ModelOwnerIF modelOwner,
                            Class modelClass,
                            String getAspectMethodName,
                            String setAspectMethodName,
                            Class aspectClass,
                            String aspectName,
                            String nullValue,
                            String undefinedValue )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, nullValue, undefinedValue );

        assert nullValue != null;
        assert undefinedValue != null;

        m_commitDelay = commitDelay;
        m_textArea = textArea;
        m_textArea.addFocusListener( this );
        m_textArea.setEditable( setAspectMethodName != null );
        update();

        m_textArea.getDocument().addDocumentListener( this );
    }
    
            
    public final void focusGained( FocusEvent ev )
    {
    }
            
    public final void focusLost( FocusEvent ev )
    {
        setAspectValue( m_textArea.getText() );
    }

    protected void update( Object projectedValue )
    {
        m_textArea.setText( projectedValue.toString() );
    }


    public void changedUpdate( DocumentEvent ev ) {}

    
    public void insertUpdate( DocumentEvent ev )
    {
        if ( ! m_textArea.isEditable() ) return;
        
        if
            ( m_commitDelay > 0 )
        {
            m_t0 = System.currentTimeMillis();
            if ( m_timer == null ) m_timer = new Timer( m_commitDelay, this );
            m_timer.start();
        }
//         try
//         {
//             String tmp = ev.getDocument().getText( ev.getOffset(), ev.getLength() );
//             if ( tmp.indexOf( '\n' ) != -1 ) setAspectValue( m_textArea.getText() );
//         }
//         catch ( javax.swing.text.BadLocationException ignore ) {}
    }
    
    public void removeUpdate( DocumentEvent ev )
    {
        if
            ( m_commitDelay > 0 )
        {
            m_t0 = System.currentTimeMillis();
            if ( m_timer == null ) m_timer = new Timer( m_commitDelay, this );
            m_timer.start();
        }
    }

    public void actionPerformed( ActionEvent ev )
    {
        if
            ( m_t0 == 0 )
        {
            m_timer.stop();
            m_timer = null;
        } else {
            long t = System.currentTimeMillis();
            if
                ( t - m_t0 > m_commitDelay )
            {
                m_timer.stop();
                m_timer = null;
                setAspectValue( m_textArea.getText() );
                m_t0 = 0;
            }
        }
    }
}
