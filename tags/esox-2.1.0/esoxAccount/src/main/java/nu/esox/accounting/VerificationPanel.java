package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.list.*;


@SuppressWarnings( "serial" )
public class VerificationPanel extends ModelPanel
{
    private final Predicate m_isEditable = new Predicate();
    private final Action m_balanceAction;
    private final Action m_deleteAction;
    private final TransactionSetTable m_table;

    
    public VerificationPanel( String tableId )
    {
        super( new BorderLayout( 5, 5 ) );

        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

        
        NamedAndNumberedEditor e = new NamedAndNumberedEditor( false );
        JFormattedTextField dtf = new JFormattedTextField( Constants.DATE_FORMAT );
        JTextArea ata = new JTextArea( 5, 20 );
        ata.setBorder( dtf.getBorder() );
        
        {
            e.setLayout( new RowLayout( 10, false, false, RowLayout.LEFT, 0.5 ) );
            add( e, BorderLayout.NORTH );
            new SubModelAdapter( e, "setNamedAndNumbered", NamedAndNumbered.class, this, null );
            
            e.add( new LabelPanel( dtf, "Datum" ) );
            dtf.setColumns( 10 );
            new FormattedTextFieldAdapter( dtf, this, Verification.class, "getDate", "setDate", java.util.Date.class, null, null, null );

            JCheckBox cb = new JCheckBox();
            e.add( new LabelPanel( cb, "Öppen" ) );
            new CheckBoxAdapter( cb, this, Verification.class, "isEditable", "setEditable", null );
        }
        
        m_table =
            new TransactionSetTable( true )
            {
                public boolean isEditable() { return m_isEditable.isTrue(); }
            };
        
        {
            JPanel p = new JPanel( new ColumnLayout( 5, true, true ) );
            add( p, BorderLayout.CENTER );
      
            if ( tableId != null ) SwingPrefs.add( tableId, m_table );
            p.add( new JScrollPane( m_table ), ColumnLayout.FILL );
            new SubModelAdapter( m_table, "setTransactionSet", TransactionSet.class, this, Verification.class, "getTransactions", null );
            new SubModelAdapter( m_table, "setAccounts", AccountPopulation.class, this, Verification.class, "getAccounts", null );


            {
                m_balanceAction = new AbstractAction( "Balansera" ) { public void actionPerformed( ActionEvent ev ) { if ( isEnabled() ) balance(); } };
                m_deleteAction = new AbstractAction( "Töm" ) { public void actionPerformed( ActionEvent ev ) { if ( isEnabled() ) delete(); } };
                
                m_table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_B, InputEvent.ALT_MASK ), m_balanceAction.getValue( Action.NAME ) );
                m_table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, InputEvent.ALT_MASK ), m_deleteAction.getValue( Action.NAME ) );
                m_table.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK ), m_deleteAction.getValue( Action.NAME ) );

                m_table.getActionMap().put( m_balanceAction.getValue( Action.NAME ), m_balanceAction );
                m_table.getActionMap().put( m_deleteAction.getValue( Action.NAME ), m_deleteAction );
                m_table.getActionMap().put( m_deleteAction.getValue( Action.NAME ), m_deleteAction );

                p.add( new LabelPanel( ata, "Kom ihåg!" ) );
                new TextAreaAdapter( ata, 500, this, Verification.class, "getAlert", "setAlert", null );
                
                JPanel buttons = new JPanel( new RowLayout( 5 ) );
                p.add( buttons );
                buttons.add( new JButton( m_table.getUpAction() ) );
                buttons.add( new JButton( m_table.getDownAction() ) );
                buttons.add( new JButton( m_balanceAction ) );
                buttons.add( new JButton( m_deleteAction ) );
        
                new EnablePredicateAdapter( null,
                                            null,
                                            new Action [] { m_deleteAction },
                                            null,
                                            new AndPredicate( new ListSelectionPredicate( m_table.getSelectionModel(), ListSelectionPredicate.TEST_SOME ), m_isEditable ) );
                
                new EnablePredicateAdapter( null,
                                            null,
                                            new Action [] { m_balanceAction },
                                            null,
                                            new AndPredicate( new ListSelectionPredicate( m_table.getSelectionModel(), ListSelectionPredicate.TEST_ONE ), m_isEditable ) );
            }
        }
        
        {
            JPanel p = new JPanel( new RowLayout( 5, false, true ) );
            add( p, BorderLayout.SOUTH );

            JFormattedTextField tf = new AmountTextField();
            p.add( new JLabel( "" ),  RowLayout.FILL );
            p.add( new LabelPanel( tf, "Saldo" ) );
            new FormattedTextFieldAdapter( tf, this, Verification.class, "getAmount", null, double.class, null, Double.NaN, Double.NaN );
        }

        new EnablePredicateAdapter( new JComponent [] { m_table },
                                    null,
                                    null,
                                    null,
                                    getHasModel() );

        new EnablePredicateAdapter( new JComponent [] { dtf, e, ata },
                                    null,
                                    null,
                                    null,
                                    new AndPredicate( getHasModel(), m_isEditable ) );
        
        setVerification( null );
    }

    
    protected void preSetModel( ObservableIF oldModel, ObservableIF newModel )
    {
        super.preSetModel( oldModel, newModel );
        if
            ( oldModel != null )
        {
            Verification v = (Verification) oldModel;
            v.getTransactions().removeObservableListener( m_rowMonitor );
            v.removeObservableListener( m_verifiationListener );
            v.getTransactions().normalize();
        }
    }
    
    protected void postSetModel( ObservableIF oldModel, ObservableIF newModel )
    {
        super.postSetModel( oldModel, newModel );
        if
            ( newModel != null )
        {
            Verification v = (Verification) newModel;
            if
                ( v.isEditable() )
            {
                 assureEmptyRow( 5 );
            }

            v.getTransactions().addObservableListener( m_rowMonitor );
            v.addObservableListener( m_verifiationListener );
        }

        verificationChanged();
    }

    public final void setVerification( Verification v )
    {
        setModel( v );
    }

    public final Verification getVerification()
    {
        return (Verification) getModel();
    }


    

    private void verificationChanged()
    {
        m_isEditable.set( ( getVerification() != null ) && getVerification().isEditable() );
    }

    private void assureEmptyRow( int count )
    {
        if
            ( getVerification().isEditable() )
        {
            if
                ( getVerification().getTransactions().getSize() == getVerification().getTransactions().size() )
            {
                addNewTransactions( getVerification(), count );
            }
        }
    }
    
    private void addNewTransactions( Verification v, int count )
    {
        for
            ( int i = 0; i < count; i++ )
        {
            v.getTransactions().add( getVerification().createTransaction() );
        }
    }

    

    private void balance()
    {
        TransactionSet ts = getVerification().getTransactions();
        if ( ts == null ) return;

        int [] rows = m_table.getSelectedRows();
        if ( rows.length != 1 ) return;

        Transaction t = ts.get( rows[ 0 ] );
        double a = t.hasAmount() ? t.getAmount() : 0;
        t.setAmount( a - ts.getAmount() );
    }


    private void delete()
    {
        TransactionSet ts = getVerification().getTransactions();
        if ( ts == null ) return;

        int [] rows = m_table.getSelectedRows();
        if ( rows.length == 0 ) return;

        ts.beginTransaction();
        for
            ( int i = rows.length - 1; i >= 0; i-- )
        {
            ts.get( rows[ i ] ).clear();
        }
        ts.endTransaction( null, null );

        m_table.getSelectionModel().clearSelection();
    }

    private final ObservableListener m_verifiationListener =
        new ObservableListener()
        {
            public void valueChanged( ObservableEvent ev )
            {
                verificationChanged();
            }
        };

    private final ObservableListener m_rowMonitor =
        new ObservableListener()
        {
            public void valueChanged( ObservableEvent ev )
            {
                if ( ev.getInfo() == TransactionSet.PROPERTY_SIZE )
                {
                    assureEmptyRow( 1 );
                }
            }
        };

}

