package nu.esox.gui.model;

import javax.swing.event.*;
import javax.swing.table.*;
import nu.esox.util.*;


public abstract class ObservableListTableModel<T> extends AbstractTableModel implements ObservableListener
{
    private ObservableListIF<T> m_data;
    private boolean m_isEditable;

    
    public ObservableListTableModel( ObservableListIF<T> data, boolean isEditable )
    {
        setData( data );
        m_isEditable = isEditable;
    }
    
    public ObservableListTableModel( ObservableListIF<T> data )
    {
        this( data, true );
    }

    
    public ObservableListIF<T> getData()
    {
        return m_data;
    }

    public void setData( ObservableListIF<T> data )
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


    
    public static abstract class Column<T>
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
        
        public abstract Object getValue( T target );
        public void setValue( T target, Object value )
        {
            assert false;
        }
    };



    
    protected abstract Column<T> [] getColumns();

    



    
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
