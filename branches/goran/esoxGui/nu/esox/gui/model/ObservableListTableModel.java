package nu.esox.gui.model;

import javax.swing.event.*;
import javax.swing.table.*;
import nu.esox.util.*;


public abstract class ObservableListTableModel extends AbstractTableModel implements ObservableListener
{
    private ObservableListIF m_data;
    private boolean m_isEditable;

    
    public ObservableListTableModel( ObservableListIF data, boolean isEditable )
    {
        setData( data );
        m_isEditable = isEditable;
    }
    
    public ObservableListTableModel( ObservableListIF data )
    {
        this( data, true );
    }

    
    public java.util.List getData()
    {
        return m_data;
    }

    public void setData( ObservableListIF data )
    {
        if ( m_data != null ) m_data.removeObservableListener( this );
        m_data = data;
        if ( ( m_data != null ) && ( getTableModelListeners().length > 0 ) ) m_data.addObservableListener( this );
        fireTableDataChanged();
    }


    public void addTableModelListener( TableModelListener l )
    {
        if ( ( m_data != null ) && ( getTableModelListeners().length == 0 ) ) m_data.addObservableListener( this );
        super.addTableModelListener( l );
    }

    public void removeTableModelListener( TableModelListener l )
    {
        super.removeTableModelListener( l );
        if ( ( m_data != null ) && ( getTableModelListeners().length == 0 ) ) m_data.removeObservableListener( this );
    }


    
    public void valueChanged( ObservableEvent e )
    {
        if
            ( e instanceof ObservableListEvent )
        {
            ObservableListEvent ev = (ObservableListEvent) e;
            switch
                ( ev.getOperation() )
            {
         		 		case ObservableCollectionEvent.ADDED:
                    if    
                        ( ev.getItem() == null )
                    {     
                        fireTableStructureChanged();
                    } else {
                        int i = ev.getIndex();
                        fireTableRowsInserted( i, i );
                    }     
                    break;
                    
		            case ObservableCollectionEvent.REMOVED:
                    if    
                        ( ev.getItem() == null )
                    {     
                        fireTableStructureChanged();
                    } else {
                        int i = ev.getIndex();
                        fireTableRowsDeleted( i, i );
                    }     
                    break;
                    
         		 		case ObservableCollectionEvent.ITEM_CHANGED:
            		case ObservableCollectionEvent.REPLACED:
                    if    
                        ( ev.getItem() == null )
                    {     
                        fireTableDataChanged();
                    } else {
                        int i = ev.getIndex();
                        fireTableRowsUpdated( i, i );
                    }     
                    break;
            }
        } else {
            fireTableDataChanged();
        }
            
    }


    
    public static abstract class Column
    {
        private String m_name;
        private final Class m_class;
        private final boolean m_isEditable;

        public Column( String name, Class c, boolean isEditable )
        {
            m_name = name;
            m_class = c;
            m_isEditable = isEditable;
        }

        public String getName() { return m_name; }
        public final Class getColumnClass() { return m_class; }
        public boolean isEditable() { return m_isEditable; }
        
        public abstract Object getValue( Object target );
        public void setValue( Object target, Object value )
        {
            assert false;
        }
    };



    
    protected abstract Column [] getColumns();

    



    
    public int getRowCount()
    {
        return ( m_data == null ) ? 0 : m_data.size();
    }
    
    public Object getValueAt( int row, int column )
    {
        return ( m_data == null ) ? null : getColumns()[ column ].getValue( m_data.get( row ) );
    }

    public String getColumnName( int column )
    {
        return getColumns()[ column ].getName();
    }
    
    public int getColumnCount()
    {
        return getColumns().length;
    }

    public Class getColumnClass( int column )
    {
        return getColumns()[ column ].getColumnClass();
    }
    
    public boolean isCellEditable( int row, int column )
    {
        return m_isEditable && getColumns()[ column ].isEditable();
    }

    public void setValueAt( Object value, int row, int column )
    {
        getColumns()[ column ].setValue( m_data.get( row ), value );
    }
}
