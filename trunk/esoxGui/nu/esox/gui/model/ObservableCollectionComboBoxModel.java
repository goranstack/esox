package nu.esox.gui.model;

import javax.swing.*;
import nu.esox.util.*;


public class ObservableCollectionComboBoxModel extends ObservableCollectionListModel implements ComboBoxModel
{
    private Object m_selection;
    
    
    public ObservableCollectionComboBoxModel( ObservableCollectionIF data )
    {
        super( data );
    }


    public void valueChanged( ObservableEvent ev )
    {
        Object old = m_selection;

        super.valueChanged( ev );

        restore( old );
    }

    
    public void setData( ObservableCollectionIF data )
    {
        Object old = m_selection;

        super.setData( data );

        restore( old );
    }

    
    private void restore( Object old )
    {
        if
            ( m_data == null )
        {
            setSelectedItem( null );
        } else if
            ( old != null )
        {
            if
                ( m_data.contains( old ) )
            {
                setSelectedItem( old );
            } else if
                ( ! m_data.isEmpty() )
            {
                setSelectedItem( m_data.iterator().next() );
            } else {
                setSelectedItem( null );
            }
        } else {
            if
                ( ! m_data.isEmpty() )
            {
                setSelectedItem( m_data.iterator().next() );
            }
        }
    }

    
    public Object getSelectedItem()
    {
        return m_selection;
    }
    
    public void setSelectedItem( Object o )
    {
        m_selection = o;
        fireContentsChanged( this, -1, -1 );
    }
    
}
