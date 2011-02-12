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
public class BalanceReportTable extends JTable
{
    private BalanceReportTable( ObservableList<Account> accounts, String format )
    {
        super( new TableModel( accounts ) );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( String.class, new FormattedTableCellRenderer( format ) );
        setDefaultRenderer( Double.class, new AmountTableRenderer( format ) );
        setShowGrid( false );
    }

    public BalanceReportTable( ObservableList<Account> accounts )
    {
        this( accounts, "%s" );
    }

    public BalanceReportTable( Account total )
    {
        this( new ObservableList<Account>(), "<html><b>%s</html>" );
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
                        
                new AccountColumn( "IB", Double.class )
                {
                    public Object getValue( Account a ) { return a.getIb(); }
                },
                        
                new AccountColumn( "Förändring", Double.class )
                {
                    public Object getValue( Account a ) { return a.getAmount() - a.getIb(); }
                },
                        
                new AccountColumn( "UB", Double.class )
                {
                    public Object getValue( Account a ) { return a.getAmount(); }
                },
            };
    }


    static abstract class AccountColumn extends ObservableListTableModel.Column<Account>
    {
        AccountColumn( String name, Class type ) { super( name, type, false ); }
    }

}
