package nu.esox.gui.list;


import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;


public class ListSelectionHelper implements ListDataListener, ListSelectionListener, PropertyChangeListener
{
    public static boolean TRACE = false;
    public static int m_count = 0;
    public int m_id = m_count++;
    
    private final JList m_list;
    private List m_selectedItems;
    private final Comparator m_comparator;

    public interface Comparator
    {
        boolean equals( Object o1, Object o2 );
    }

    public static final Comparator IDENTITY = new Comparator() { public boolean equals( Object o1, Object o2 ) { return o1 == o2; } };
    

    public ListSelectionHelper( JList l )
    {
        this( l, IDENTITY  );
    }

    public ListSelectionHelper( JList l, Comparator c )
    {
        m_list = l;
        m_comparator = c;

        l.addPropertyChangeListener( this );
        l.getModel().addListDataListener( this );
        l.getSelectionModel().addListSelectionListener( this );
    }


      // PropertyChangeListener
    public void propertyChange( PropertyChangeEvent ev )
    {
          // update model and selection model listeners
        if
            ( ev.getPropertyName().equals( "model" ) )
        {
            if ( ev.getOldValue() != null ) ( (ListModel) ev.getOldValue() ).removeListDataListener( this );
            if ( ev.getNewValue() != null ) ( (ListModel) ev.getNewValue() ).addListDataListener( this );
        } else if
            ( ev.getPropertyName().equals( "selectionModel" ) )
        {
            if ( ev.getOldValue() != null ) ( (ListSelectionModel) ev.getOldValue() ).removeListSelectionListener( this );
            if ( ev.getNewValue() != null ) ( (ListSelectionModel) ev.getNewValue() ).addListSelectionListener( this );
        }
    }

      // ListSelectionListener
    public void valueChanged( ListSelectionEvent ev )
    {
          // keep track of selected objects
        if
            ( ! ev.getValueIsAdjusting() )
        {
            m_selectedItems = Arrays.asList( m_list.getSelectedValues() );
            if ( TRACE ) System.err.println( m_id + " SELECTION CHANGED " + m_selectedItems );
        }
    }


      // ListDataListener
    public void intervalAdded( ListDataEvent ev )
    {
        if ( TRACE ) System.err.println( m_id + " intervalAdded" );
        restoreSelection();
    }

    public void intervalRemoved( ListDataEvent ev )
    {
        if ( TRACE ) System.err.println( m_id + " intervalRemoved" );
        restoreSelection();
    }

    public void contentsChanged( ListDataEvent ev )
    {
        if ( TRACE ) System.err.println( m_id + " contentsChanged" );
        restoreSelection();
    }


    
    private boolean contains( List l, Object element )
    {
        if ( m_comparator == null ) return l.contains( element );
        
        int I = l.size();
        for
            ( int i = 0; i < I; i++ )
        {
            if ( m_comparator.equals( l.get( i ), element ) ) return true;
        }

        return false;
    }
    
    private void restoreSelection()
    {
          // restore selection
        if ( TRACE ) System.err.println( m_id + " RESTORE " + m_selectedItems );
        
        m_list.getSelectionModel().setValueIsAdjusting( true );
        m_list.getSelectionModel().clearSelection();

        if
            ( m_selectedItems != null && ! m_selectedItems.isEmpty() )
        {
            int I = m_list.getModel().getSize();
            for
                ( int i = 0; i < I; i++ )
            {
                if
                    ( contains( m_selectedItems, m_list.getModel().getElementAt( i ) ) )
                {
                    if ( TRACE ) System.err.println( m_id + " " + i + " " + m_list.getModel().getElementAt( i ) );
                    m_list.getSelectionModel().addSelectionInterval( i, i );
                }
            }
        }
        if ( TRACE ) System.err.println( m_id + " DONE " );
        m_list.getSelectionModel().setValueIsAdjusting( false );
        if ( TRACE ) System.err.println( m_id + " DONE " );
        
        m_selectedItems = Arrays.asList( m_list.getSelectedValues() );

        if ( TRACE ) System.err.println( m_id + " RESTORED " + m_selectedItems );
    }
}
