package nu.esox.gui.aspect;


import javax.swing.*;
import javax.swing.event.*;
import nu.esox.util.*;


public class ListSelectionAdapter implements ListSelectionListener, ModelOwnerIF.Listener
{
    private final JList m_list;
    private final ModelOwnerIF m_modelOwner;
    private boolean m_isUpdating = false;

    
    public ListSelectionAdapter( JList list, ModelOwnerIF modelOwner )
    {
        m_list = list;
        m_list.addListSelectionListener( this );

        m_modelOwner = modelOwner;
        m_modelOwner.addListener( this );
    }

    public void modelAssigned( ObservableIF oldModel, ObservableIF newModel )
    {
        if ( m_isUpdating ) return;
        m_isUpdating = true;

        if
            ( newModel == null )
        {
            m_list.clearSelection();
        } else {
            m_list.setSelectedValue( newModel, true );
        }
        m_isUpdating = false;
    }

    public void valueChanged( ListSelectionEvent ev )
    {
        if ( ev.getValueIsAdjusting() ) return;
        
        if ( m_isUpdating ) return;
        m_isUpdating = true;

        Object [] selected = m_list.getSelectedValues();
        if
            ( selected.length == 1 )
        {
            m_modelOwner.setModel( (ObservableIF) selected[ 0 ] );
        } else {
            m_modelOwner.setModel( null );
        }
        m_isUpdating = false;
    }
}
