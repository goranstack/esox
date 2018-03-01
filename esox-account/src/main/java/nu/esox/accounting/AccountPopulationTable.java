package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.model.*;
import nu.esox.gui.list.*;


@SuppressWarnings( "serial" )
public class AccountPopulationTable extends JTable
{
    private final Action m_addAction;
    private final Action m_deleteAction;
    private final Predicate m_hasModel = new Predicate();


    public AccountPopulationTable( String addLabel, String deleteLabel )
    {
        super( new TableModel() );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( Account.class, new AccountTableRenderer() );
        setDefaultRenderer( Double.class, new AmountTableRenderer( "%s" ) );

        if
            ( addLabel != null )
        {
            m_addAction = new AbstractAction( addLabel ) { public void actionPerformed( ActionEvent ev ) { add(); } };
            new EnablePredicateAdapter( this,
                                        null,
                                        m_addAction,
                                        null,
                                        m_hasModel );
            getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_INSERT, 0 ), m_addAction.getValue( Action.NAME ) );
            getActionMap().put( m_addAction.getValue( Action.NAME ), m_addAction );
        } else {
            m_addAction = null;
        }
        
        if
            ( deleteLabel != null )
        {
            m_deleteAction = new AbstractAction( deleteLabel ) { public void actionPerformed( ActionEvent ev ) { delete(); } };
            new EnablePredicateAdapter( null,
                                        null,
                                        m_deleteAction,
                                        null,
                                        new AndPredicate( m_hasModel,
                                                          new ListSelectionPredicate( getSelectionModel(), new Xxx() ) ) );
            getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), m_deleteAction.getValue( Action.NAME ) );
            getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), m_deleteAction.getValue( Action.NAME ) );
            getActionMap().put( m_deleteAction.getValue( Action.NAME ), m_deleteAction );
        } else {
            m_deleteAction = null;
        }

    }



    private class Xxx extends ListSelectionPredicate.CountTest // fixit: observe selected accounts
    {
        Xxx()
        {
            super( 1, Integer.MAX_VALUE );
        }

        public boolean test( ListSelectionModel selectionModel )
        {
            if ( ! super.test( selectionModel ) ) return false;
            for
                ( int i : getSelectedRows() )
            {
                Account ac = getAccountPopulation().get( i );
                if ( ac.isLocked() ) return false;
                if ( ! ac.getTransactions().isEmpty() ) return false;
            }
            return true;
        }
    }



    
    public final AccountPopulation getAccountPopulation()
    {
        return (AccountPopulation) ( (TableModel) getModel() ).getData();
    }

    public final void setAccountPopulation( AccountPopulation ap )
    {
        if ( ap == ( (TableModel) getModel() ).getData() ) return;
        ( (TableModel) getModel() ).setData( ap );
        m_hasModel.set( ap != null );
    }




    public Action getAddAction() { return m_addAction; }
    public Action getDeleteAction() { return m_deleteAction; }

    
    protected void add() {}


    private void delete()
    {
        int [] rows = getSelectedRows();
        clearSelection();
        
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            getAccountPopulation().remove( rows[ i ] );
        }

        if
            ( rows[ rows.length - 1 ] < getAccountPopulation().size() )
        {
            int i = rows[ rows.length - 1 ];
            setRowSelectionInterval( i, i );
        } else if
              ( ! getAccountPopulation().isEmpty() )
        {
            int i = getRowCount() - 1;
            setRowSelectionInterval( i, i );
        }
    }



    
    private static class TableModel extends ObservableListTableModel<Account>
    {
        TableModel()
        {
            super( null );
        }
        
        protected Column<Account> [] getColumns() { return m_columns; }

        private static AccountColumn [] m_columns =
            new AccountColumn []
            {
                new AccountColumn( "Konto", Account.class )
                {
                    public Object getValue( Account a ) { return a; }
                },
                new AccountColumn( "Saldo", Double.class )
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
