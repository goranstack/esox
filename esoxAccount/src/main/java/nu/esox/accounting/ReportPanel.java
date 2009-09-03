package nu.esox.accounting;

import java.beans.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;


@SuppressWarnings( "serial" )
public abstract class ReportPanel extends ModelPanel
{
    interface Section
    {
        void prepare( JPanel panel, Account.Type [] types, List<Accounts> accounts, List<JTable> tables );
    }


    
    public class TypeSection implements Section
    {
        private final int m_i;
        
        public TypeSection( int i )
        {
            m_i = i;
        }
        
        public void prepare( JPanel panel, Account.Type [] types, List<Accounts> accounts, List<JTable> tables )
        {
            panel.add( addSection( types[ m_i ], tables, accounts ) );
        }
    }


    
    public class SumSection implements Section
    {
        private final String m_label;
        private final int [] m_is;
        
        public SumSection( String label, int [] is )
        {
            assert is.length > 0;
            m_label = label;
            m_is = is;
        }
        
        public void prepare( JPanel panel, Account.Type [] types, List<Accounts> accounts, List<JTable> tables )
        {
            Account a = accounts.get( m_is[ 0 ] ).getTotal();
            for ( int i = 1; i < m_is.length; i++ ) a = new AccumulatedAccount( m_label, a, accounts.get( m_is[ i ] ).getTotal() );
            addTable( createTable( a ), panel, tables );
        }
    }

    
    protected abstract JTable createTable( Account a );
    protected abstract JTable createTable( ObservableList<Account> accounts );


    
    private final EmptyAccountFilter m_emptyAccountFilter = new EmptyAccountFilter();
    private final Action m_exportAction = new AbstractAction( "Exportera" ) { public void actionPerformed( ActionEvent ev ) { export(); } };

    

    public void init( String tableName, Account.Type [] types, Section [] sections )
    {
        setLayout( new BorderLayout( 5, 5 ) );
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

        {
            JPanel buttons = new JPanel( new RowLayout( 5, true, true ) );
            add( buttons, BorderLayout.NORTH );
              //buttons.add( new JButton( m_exportAction ) );
            JCheckBox cb = new JCheckBox( "Visa tomma konton" );
            buttons.add( cb );
            new CheckBoxAdapter( cb, new SimpleModelOwner( m_emptyAccountFilter ), EmptyAccountFilter.class, "getShowEmptyAccounts", "setShowEmptyAccounts", null );
        }

        List<Accounts> accounts = new ArrayList<Accounts>();
        List<JTable> tables = new ArrayList<JTable>();
        
        JPanel center = new JPanel( new ColumnLayout( 5, true, true ) );

        for ( Section s : sections ) s.prepare( center, types, accounts, tables );

        {
            TableColumnModel tcm = tables.get( tables.size() - 1 ).getColumnModel();
              //SwingPrefs.add( tableName, tables.get( tables.size() - 1 ) );    todo: fixed size on all but leftmost column ???
            for ( JTable t : tables ) t.setColumnModel( tcm );
        }

        JScrollPane sp = new JScrollPane( center,
                                          ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        sp.setColumnHeaderView( tables.get( 0 ).getTableHeader() );
        add( sp, BorderLayout.CENTER );
    }

    
    private JPanel addSection( Account.Type type, List<JTable> ts, List<Accounts> as )
    {
        Accounts a = new Accounts( type );
        as.add( a );
        JTable t = createTable( a );
        JPanel p = new JPanel( new ColumnLayout( 0, true, true ) );
        p.add( new JLabel( a.getTotal().getType().toString() ) );
        addTable( t, p, ts );
        addTable( createTable( a.getTotal() ), p, ts );
          //new SubModelAdapter( t, this, "setAccountPopulation", AccountPopulation.class, Year.class, "getAccounts" );
        new SubModelAdapter( t,
                             "setAccountPopulation",
                             AccountPopulation.class,
                             this,
                             Year.class,
                             "getAccounts",
                             null );
        return p;
    }

    private void addTable( JTable t, JPanel p, List<JTable> ts )
    {
        ts.add( t );
        p.add( t );
    }
    
    public Year getYear() { return (Year) getModel(); }
    public void setYear( Year y ) { setModel( y ); }


    private void export()
    {
        DefaultTableModel tm = new DefaultTableModel();
    }

    
    public final class Accounts extends FilteredObservableList<Account>
    {
        private final Account m_total;
        private double m_ib = 0;
        private double m_amount = 0;
        private double m_budget = 0;
        
        Accounts( Account.Type t )
        {
            this( "Summa " + t, new AccountTypeFilter( t ), t );
        }
        
        Accounts( String name, FilteredObservableList.Filter<Account> filter, Account.Type t )
        {
            super( null, filter );

            m_total =
                new Account( 0, name )
                {
                    public double getAmount() { return m_amount; }
                    public double getBudget() { return m_budget; }
                    public double getIb() { return m_ib; }
                };
            m_total.setType( t );
        }

        Account getTotal() { return m_total; }
        
        public void valueChanged( ObservableEvent ev )
        {
            super.valueChanged( ev );
            refresh();
        }

        public void setSource( ObservableList<Account> source )
        {
            super.setSource( source );
            refresh();
        }

        private void refresh()
        {
            m_ib = 0;
            m_amount = 0;
            m_budget = 0;

            for
                ( Account a : this )
            {
                m_ib += a.getIb();
                m_amount += a.getAmount();
                m_budget += a.getBudget();
            }
        }
    }

 
    private class EmptyAccountFilter extends nu.esox.util.Observable implements FilteredObservableList.Filter<Account>
    {
        private boolean m_showEmptyAccounts = true;

        public boolean pass( Account a )
        {
            return m_showEmptyAccounts || ( a.getAmount() != 0 ) || ( a.getBudget() != 0 );
        }
        
        public boolean getShowEmptyAccounts() { return m_showEmptyAccounts; }
        public void setShowEmptyAccounts( boolean b )
        {
            if ( b == m_showEmptyAccounts ) return;
            m_showEmptyAccounts = b;
            fireValueChanged( null, null );
        }
    }
    
 
    private class AccountTypeFilter extends nu.esox.util.Observable implements FilteredObservableList.Filter<Account>
    {
        private final Account.Type m_type;

        AccountTypeFilter( Account.Type type )
        {
            m_type = type;
            listenTo( m_emptyAccountFilter );
        }
        
        public boolean pass( Account a )
        {
            return ( m_type == a.getType() ) && m_emptyAccountFilter.pass( a );
        }
    }


    private class AccumulatedAccount extends Account
    {
        private final Account m_account1;
        private final Account m_account2;

        AccumulatedAccount( String name, Account a1, Account a2 )
        {
            super( 0, name );
            m_account1 = a1;
            m_account2 = a2;
            listenTo( m_account1 );
            listenTo( m_account2 );
        }

        public double getAmount() { return m_account1.getAmount() + m_account2.getAmount(); }
        public double getIb() { return m_account1.getIb() + m_account2.getIb(); }
        public double getBudget() { return m_account1.getBudget() + m_account2.getBudget(); }
    }
}

