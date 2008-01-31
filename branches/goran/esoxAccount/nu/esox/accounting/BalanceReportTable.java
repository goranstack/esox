package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.model.*;


public class BalanceReportTable extends JTable
{
    public BalanceReportTable( ObservableList accounts )
    {
        super( new TableModel( accounts ) );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( Double.class, new AmountTableRenderer() );
        setShowGrid( false );
    }

    public BalanceReportTable( Account total )
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
                        
                new Column( "IB", Double.class, false )
                {
                    public Object getValue( Object target ) { return getAccount( target ).getIb(); }
                },
                        
                new Column( "Förändring", Double.class, false )
                {
                    public Object getValue( Object target ) { return getAccount( target ).getAmount() - getAccount( target ).getIb(); }
                },
                        
                new Column( "UB", Double.class, false )
                {
                    public Object getValue( Object target ) { return getAccount( target ).getAmount(); }
                },
            };
    }



}
