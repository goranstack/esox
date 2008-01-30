package nu.esox.accounting;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;


public class AccountPopulationEditor extends ModelPanel
{
    private final AccountPopulationTable m_table =
        new AccountPopulationTable( "Nytt", "Ta bort" )
        {
            {
                SwingPrefs.add( "account-table", this );
            }
            
            protected void add()
            {
                AccountPopulationEditor.this.add();
            }
        };

    
    private final AccountEditor m_editor =
        new AccountEditor()
        {
            protected void verifyNumber( int value ) throws ParseException
            {
                super.verifyNumber( value );
                Account a = getAccountPopulation().getAccount( value );
                if ( a == null ) return;
                if ( a == getAccount() ) return;
                throw new ParseException( "", 0 );
            }
        };


    
    public AccountPopulationEditor()
    {
        setLayout( new BorderLayout( 5, 5 ) );
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
 
        m_table.setPreferredScrollableViewportSize( new Dimension( (int) m_table.getPreferredSize().getWidth(), 0 ) );
        JPanel center = new JPanel( new ColumnLayout( 5, true, true ) );
        center.add( new JScrollPane( m_table ), ColumnLayout.FILL );

        {
            JPanel buttons = new JPanel( new RowLayout( 5 ) );
            buttons.add( new JButton( m_table.getAddAction() ) );
            buttons.add( new JButton( m_table.getDeleteAction() ) );
            center.add( buttons );
        }
        JPanel east = new JPanel( new ColumnLayout( 5, true, true ) );
        east.add( m_editor );

        final TransactionSetTable tst = new TransactionSetTable( false );
        SwingPrefs.add( "account-transaction-table", tst );
        east.add( new JScrollPane( tst ), ColumnLayout.FILL );

        add( center, BorderLayout.CENTER );
        add( east, BorderLayout.EAST );


        new SubModelAdapter( m_table, "setAccountPopulation", AccountPopulation.class, this, null );//, AccountPopulation.class, "getAccountPopulation" );

        ListSelectionListener l =
            new ListSelectionListener()
            {
                public void valueChanged( ListSelectionEvent ev )
                {
                    Account a = ( m_table.getSelectedRowCount() != 1 ) ? null : (Account) getAccountPopulation().get( m_table.getSelectedRow() );
                    m_editor.setAccount( a );
                    tst.setTransactionSet( ( a == null ) ? null : a.getTransactions() );
                }
            };
        
        m_table.getSelectionModel().addListSelectionListener( l );
    }


    public AccountPopulation getAccountPopulation() { return (AccountPopulation) getModel(); }
    public void setAccountPopulation( AccountPopulation p ) { setModel( p ); }


    
    private void add()
    {
        Account a = new Account( 0 );
        m_editor.setAccount( a );
        new NewAccountMonitor( a );
    }



    
    private class NewAccountMonitor implements ObservableListener, PropertyChangeListener
    {
        private final Account m_account;
        
        NewAccountMonitor( Account account )
        {
            m_account = account;
            m_account.addObservableListener( this );
            m_editor.addPropertyChangeListener( this );
        }
        
        public void valueChanged( ObservableEvent ev )
        {
            if
                ( ev.getInfo() == Account.PROPERTY_NUMBER )
            {
                getAccountPopulation().add( m_account );
                int i = getAccountPopulation().indexOf( m_account );
                m_table.getSelectionModel().setSelectionInterval( i, i );
                unlisten();
            }
        }
        
        public void propertyChange( PropertyChangeEvent ev )
        {
            if ( ev.getPropertyName().equals( "model-property" ) ) unlisten();
        }
        
        private void unlisten()
        {
            m_account.removeObservableListener( this );
            m_editor.removePropertyChangeListener( this );
        }
    }

}

