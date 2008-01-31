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
        setDefaultRenderer( Double.class, new AmountTableRenderer() );

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
                                                          new ListSelectionPredicate( getSelectionModel(), ListSelectionPredicate.TEST_SOME ) ) );  // todo: Test that all are unlocked
            getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), m_deleteAction.getValue( Action.NAME ) );
            getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), m_deleteAction.getValue( Action.NAME ) );
            getActionMap().put( m_deleteAction.getValue( Action.NAME ), m_deleteAction );
        } else {
            m_deleteAction = null;
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



    
    private static class TableModel extends ObservableListTableModel
    {
        TableModel()
        {
            super( null );
        }
        
        protected Column [] getColumns() { return m_columns; }

        private static Account getAccount( Object o ) { return (Account) o; }
        
        private static Column [] m_columns =
            new Column []
            {
                new Column( "Konto", Account.class, false )
                {
                    public Object getValue( Object target ) { return getAccount( target ); }
                },
                new Column( "Saldo", Double.class, false )
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
