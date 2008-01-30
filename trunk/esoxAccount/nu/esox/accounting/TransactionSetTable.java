package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.model.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.list.*;


public class TransactionSetTable extends JTable
{
    private final Action m_upAction;
    private final Action m_downAction;


    public TransactionSetTable( boolean showAccount )
    {
        super();

        setModel( new TableModel( showAccount ) );

        setAutoCreateColumnsFromModel( false );
        setDefaultRenderer( Account.class, new AccountTableRenderer() );
        setDefaultEditor( Account.class, new AccountTableEditor() );
        
        setDefaultRenderer( Double.class, new AmountTableRenderer() );
        setDefaultEditor( Double.class, new AmountTableEditor() );

        m_upAction = new AbstractAction( "Upp" ) { public void actionPerformed( ActionEvent ev ) { up(); } };
        m_downAction = new AbstractAction( "Ner" ) { public void actionPerformed( ActionEvent ev ) { down(); } };

        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, InputEvent.ALT_MASK ), m_upAction.getValue( Action.NAME ) );
        getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, InputEvent.ALT_MASK ), m_downAction.getValue( Action.NAME ) );

        getActionMap().put( m_upAction.getValue( Action.NAME ), m_upAction );
        getActionMap().put( m_downAction.getValue( Action.NAME ), m_downAction );

        new EnablePredicateAdapter( null,
                                    null,
                                    new Action [] { m_upAction, m_downAction },
                                    null,
                                    new ListSelectionPredicate( getSelectionModel(), ListSelectionPredicate.TEST_SOME ) );
    }

    public final Action getUpAction() { return m_upAction; }
    public final Action getDownAction() { return m_downAction; }
    
    public final void setTransactionSet( TransactionSet ts )
    {
        ( (TableModel) getModel() ).setData( ts );
    }

    public final TransactionSet getTransactionSet()
    {
        return (TransactionSet) ( (TableModel) getModel() ).getData();
    }


    public void setAccounts( AccountPopulation accounts )
    {
        ( (AccountTableEditor) getDefaultEditor( Account.class ) ).setAccounts( accounts );
    }



    private void up()
    {
        TransactionSet ts = getTransactionSet();
        if ( ts == null ) return;

        int [] rows = getSelectedRows();
        if ( rows.length == 0 ) return;
        if ( rows[ 0 ] == 0 ) return;

        ts.beginTransaction();
        for
            ( int i = 0; i < rows.length; i++ )
        {
            ts.move( rows[ i ], rows[ i ] - 1 );
        }
        ts.endTransaction( null, null );

        getSelectionModel().clearSelection();
        for
            ( int i = 0; i < rows.length; i++ )
        {
            getSelectionModel().addSelectionInterval( rows[ i ] - 1, rows[ i ] - 1 );
        }
    }

    private void down()
    {
        TransactionSet ts = getTransactionSet();
        if ( ts == null ) return;

        int [] rows = getSelectedRows();
        if ( rows.length == 0 ) return;
        if ( rows[ rows.length - 1 ] == ts.size() - 1 ) return;

        ts.beginTransaction();
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            ts.move( rows[ i ], rows[ i ] + 1 );
        }
        ts.endTransaction( null, null );

        getSelectionModel().clearSelection();
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            getSelectionModel().addSelectionInterval( rows[ i ] + 1, rows[ i ] + 1 );
        }
    }


    public boolean isEditable() { return true; }
    
    
    private class TableModel extends ObservableListTableModel
    {
        TableModel( boolean showAccount )
        {
            super( null );

            if
                ( showAccount )
            {
                m_columns =
                    new Column []
                    {
                        new Column( "Konto", Account.class, true )
                        {
                            public Object getValue( Object target ) { return getTransaction( target ).getAccount(); }
                            public void setValue( Object target, Object value ) { getTransaction( target ).setAccount( (Account) value ); }
                            public boolean isEditable() { return TransactionSetTable.this.isEditable(); }
                        },
                        
                        new Column( "Text", String.class, true )
                        {
                            public Object getValue( Object target ) { return getTransaction( target ).getDescription(); }
                            public void setValue( Object target, Object value ) { getTransaction( target ).setDescription( (String) value ); }
                            public boolean isEditable() { return TransactionSetTable.this.isEditable(); }
                        },
                        
                        new Column( "Summa", Double.class, true )
                        {
                            public Object getValue( Object target ) { return (Double) getTransaction( target ).getAmount(); }
                            public void setValue( Object target, Object value )
                            {
                                getTransaction( target ).setAmount( ( value == null ) ? 0f : ( (Number) value ).doubleValue() );
                            }
                            public boolean isEditable() { return TransactionSetTable.this.isEditable(); }
                        },
                    };
            } else {
                m_columns =
                    new Column []
                    {
                        new Column( "Verifikation", Number.class, false )
                        {
                            public Object getValue( Object target )
                            {
                                Verification v = getTransaction( target ).getVerification();
                                if ( v == null ) getTransaction( target ).setDescription( "KNAS" );
                                if ( v == null ) return "KNAS";
                                return v.getNumber();
                            }
                        },
                        
                        new Column( "Datum", String.class, false )
                        {
                            public Object getValue( Object target )
                            {
                                Verification v = getTransaction( target ).getVerification();
                                if ( v == null ) return "KNAS";
                                return Constants.DATE_FORMAT.format( v.getDate() );
                            }
                        },
                        
                        new Column( "Text", String.class, false )
                        {
                            public Object getValue( Object target )
                            {
                                return getTransaction( target ).getDescription();
                            }
                        },
                        
                        new Column( "Summa", Double.class, false )
                        {
                            public Object getValue( Object target )
                            {
                                return (Double) getTransaction( target ).getAmount();
                            }
                        },
                    };
            }
        }
        
        protected Column [] getColumns() { return m_columns; }

        private Transaction getTransaction( Object o ) { return (Transaction) o; }
        
        private Column [] m_columns;
    }


}

