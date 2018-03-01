package nu.esox.gui.aspect;


import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;


public class ListAdapter extends AbstractAdapter implements ListSelectionListener
{
    private final JList m_list;
    private transient boolean m_isUpdating = false;

    
    public ListAdapter( JList list,
                        ModelOwnerIF modelOwner,
                        Class modelClass,
                        String getAspectMethodName,
                        String setAspectMethodName,
                        String aspectName,
                        Class aspectClass )
    {
        super( modelOwner, modelClass, getAspectMethodName, setAspectMethodName, aspectClass, aspectName, null, null );

        m_list = list;
        m_list.getSelectionModel().addListSelectionListener( this );
        update();
    }

    
    protected Object getItemOfRow( JList t, int row ) { return null; }
    protected int getRowOfItem( JList t, Object item ) { return -1; }
    

    public void valueChanged( ListSelectionEvent ev )
    {
        if ( m_isUpdating ) return;

        if
            ( m_list.getSelectedIndex() != -1 )
        {
            setAspectValue( getItemOfRow( m_list, m_list.getSelectedIndex() ) );
        } else {
            setAspectValue( null );
        }

    }

    protected void update( Object projectedValue )
    {
        m_isUpdating = true;

        int n = -1;
        if ( projectedValue != null ) n = getRowOfItem( m_list, projectedValue );
        
        if
            ( n == -1 )
        {
            m_list.clearSelection();
        } else {
            m_list.getSelectionModel().setSelectionInterval( n, n );
        }
        
        m_isUpdating = false;
    }
}





    
