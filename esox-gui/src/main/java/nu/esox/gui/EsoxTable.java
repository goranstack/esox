package nu.esox.gui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import nu.esox.gui.model.*;


/**
 * Table that retains selection over model (must be ObservableListTableModel) changes.
 *
 * @author Dennis Malmstrom
 */
public class EsoxTable extends JTable
{
    private List<Object> m_selected = new ArrayList<Object>();

    private ListSelectionListener m_listener =
        new ListSelectionListener()
        {
            public void valueChanged( ListSelectionEvent ev )
            {
                if ( ev.getValueIsAdjusting() ) return;
                if ( m_selected == null ) return;

                m_selected.clear();
                for ( int i : getSelectedRows() ) m_selected.add( ( (ObservableListTableModel) getModel() ).getData().get( i ) );
            }
        };


    public EsoxTable()
    {
    }
    
    public EsoxTable( ObservableListTableModel tableModel )
    {
        super( tableModel );
    }

    {
        getSelectionModel().addListSelectionListener( m_listener );
    }


    private class MyListSelectionModel extends DefaultListSelectionModel
    {
//         public boolean getValueIsAdjusting()
//         {
//             return ( m_selected == null ) ? true : super.getValueIsAdjusting();
//         }
        
//         public void setValueIsAdjusting( boolean isAdjusting )
//         {
//             if ( m_selected != null ) super.setValueIsAdjusting( isAdjusting );
//         }
        
//         protected void fireValueChanged(boolean isAdjusting)
//         {
//             super.fireValueChanged( isAdjusting );
//         }
        
        protected void fireValueChanged(int firstIndex, int lastIndex, boolean isAdjusting)
        {
            if ( m_selected == null ) return;
            super.fireValueChanged( firstIndex, lastIndex, isAdjusting );
        }
        
        
        public void fire( int lastIndex ) { fireValueChanged( 0, lastIndex, false ); }

        static final long serialVersionUID = 42;
    }

    protected ListSelectionModel createDefaultSelectionModel()
    {
        return new MyListSelectionModel();
    }

    public void tableChanged(TableModelEvent e)
    {
        List<Object> tmp = m_selected;
        m_selected = null;

        super.tableChanged( e );

        if ( tmp == null ) return;

        List modelData = ( (ObservableListTableModel) getModel() ).getData();
        if
            ( modelData != null )
        {
            ListSelectionModel sm = getSelectionModel();
            
            sm.clearSelection();
            int N = 0;
            for
                ( Object o : tmp )
            {
                int n = modelData.indexOf( o );
                if
                    ( n != -1 )
                {
                    sm.addSelectionInterval( n, n );
                    N = Math.max( n, N );
                }
            }
            
            m_selected = tmp;
            
            m_selected.retainAll( modelData );
            ( (MyListSelectionModel) sm ).fire( N );
        } else {
            m_selected = tmp;
        }
    }


    public void setSelectionModel( ListSelectionModel m )
    {
        if
            ( m_listener == null )
        {
            super.setSelectionModel( m );
        } else {
            if ( getSelectionModel() != null ) getSelectionModel().removeListSelectionListener( m_listener );
            super.setSelectionModel( m );
            if ( getSelectionModel() != null ) getSelectionModel().addListSelectionListener( m_listener );
        }
    }

    static final long serialVersionUID = 42;
}
