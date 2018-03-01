package nu.esox.gui.aspect;


import javax.swing.*;
import javax.swing.event.*;
import java.lang.reflect.*;
import nu.esox.util.*;


public class TableSelectionAdapter implements ListSelectionListener, ModelOwnerIF.Listener
{
    private final JTable m_table;
    private final ModelOwnerIF m_modelOwner;
    private boolean m_isUpdating = false;


    public TableSelectionAdapter( JTable table, ModelOwnerIF modelOwner )
    {
        m_table = table;
        m_table.getSelectionModel().addListSelectionListener( this );

        m_modelOwner = modelOwner;
        m_modelOwner.addListener( this );
    }

    public void modelAssigned( ObservableIF oldModel, ObservableIF newModel )
    {
        if ( m_isUpdating ) return;
        m_isUpdating = true;

        int i = ( newModel == null ) ? -1 : m_table.convertRowIndexToView( getRowOfItem( newModel ) );
        
        if
            ( i == -1 )
        {
            m_table.clearSelection();
        } else {
            m_table.getSelectionModel().setSelectionInterval( i, i );
        }
        m_isUpdating = false;
    }
    
    public void valueChanged( ListSelectionEvent ev )
    {
        if ( ev.getValueIsAdjusting() ) return;

        if ( m_isUpdating ) return;
        m_isUpdating = true;
        
        if
            ( m_table.getSelectedRowCount() != 1 )
        {
            m_modelOwner.setModel( null );
        } else {
            m_modelOwner.setModel( getItemOfRow( m_table.convertRowIndexToModel( m_table.getSelectedRow() ) ) );
        }
        m_isUpdating = false;
    }

    protected ObservableIF getItemOfRow( int row ) { return null; }
    protected int getRowOfItem( ObservableIF item ) { return -1; }
}
