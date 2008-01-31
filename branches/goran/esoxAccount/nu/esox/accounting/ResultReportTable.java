package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.model.*;


public class ResultReportTable extends JTable
{
    public ResultReportTable( ObservableList accounts )
    {
        super( new TableModel( accounts ) );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( Double.class, new NegativeAmountTableRenderer() );
        setShowGrid( false );
    }

    public ResultReportTable( Account total )
    {
        this( new ObservableList() );
        ( (TableModel) getModel() ).getData().add( total );
    }


    public final AccountPopulation getAccountPopulation()
    {
        return (AccountPopulation) ( (FilteredObservableList) ( (TableModel) getModel() ).getData() ).getSource();
    }

    public final void setAccountPopulation( AccountPopulation ap )
    {
        if ( ap == getAccountPopulation() ) return;
        ( (FilteredObservableList) ( (TableModel) getModel() ).getData() ).setSource( ap );
    }
    
    private static class NegativeAmountTableRenderer extends AmountTableRenderer
    {
        public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
        {
            if
                ( ( value != null ) && ( column == 1 ) )
            {
                double d = ( (Number) value ).doubleValue();
                if ( d != 0 ) d = -d;
                value = new Double( d );
            }
            return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
        }
    }



     
    private static class TableModel extends ObservableListTableModel
    {
        TableModel( ObservableList accounts )
        {
            super( accounts );
        }
        
        protected Column [] getColumns() { return m_columns; }

        private static Account getAccount( Object o ) { return (Account) o; }
        
        private static Column [] m_columns =
            new Column []
            {
                new Column( "Konto", String.class, false )
                {
                    public Object getValue( Object target )
                    {
                        int nr = getAccount( target ).getNumber();
                        return ( nr == 0 ) ? getAccount( target ).getName() : NumberTextField.FORMAT.format( nr ) + " " + getAccount( target ).getName();
                    }
                },
                        
                new Column( "Summa", Double.class, false )
                {
                    public Object getValue( Object target ) { return getAccount( target ).getAmount(); }
                },
                        
                new Column( "Budget", Double.class, false )
                {
                    public Object getValue( Object target ) { return getAccount( target ).hasBudget() ? getAccount( target ).getBudget() : null; }
                },
            };
    }



}
