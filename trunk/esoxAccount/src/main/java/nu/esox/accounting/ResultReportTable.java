package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.model.*;


@SuppressWarnings( "serial" )
public class ResultReportTable extends JTable
{
    public ResultReportTable( ObservableList<Account> accounts )
    {
        super( new TableModel( accounts ) );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( Double.class, new NegativeAmountTableRenderer() );
        setShowGrid( false );
    }

    public ResultReportTable( Account total )
    {
        this( new ObservableList<Account>() );
        ( (TableModel) getModel() ).getData().add( total );
    }


    public final AccountPopulation getAccountPopulation()
    {
        return (AccountPopulation) ( (FilteredObservableList<Account>) ( (TableModel) getModel() ).getData() ).getSource();
    }

    public final void setAccountPopulation( AccountPopulation ap )
    {
        if ( ap == getAccountPopulation() ) return;
        ( (FilteredObservableList<Account>) ( (TableModel) getModel() ).getData() ).setSource( ap );
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



     
    private static class TableModel extends ObservableListTableModel<Account>
    {
        TableModel( ObservableList<Account> accounts )
        {
            super( accounts );
        }
        
        protected Column<Account> [] getColumns() { return m_columns; }

        
        private static AccountColumn [] m_columns =
            new AccountColumn []
            {
                new AccountColumn( "Konto", String.class )
                {
                    public Object getValue( Account a )
                    {
                        int nr = a.getNumber();
                        return ( nr == 0 ) ? a.getName() : NumberTextField.FORMAT.format( nr ) + " " + a.getName();
                    }
                },
                        
                new AccountColumn( "Summa", Double.class )
                {
                    public Object getValue( Account a ) { return a.getAmount(); }
                },
                        
                new AccountColumn( "Budget", Double.class )
                {
                    public Object getValue( Account a ) { return a.hasBudget() ? a.getBudget() : null; }
                },
            };
    }


    static abstract class AccountColumn extends ObservableListTableModel.Column<Account>
    {
        AccountColumn( String name, Class type ) { super( name, type, false ); }
    }


}
