package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;
import nu.esox.util.*;


public abstract class ImportPanel extends JPanel
{
    private final Action m_okAction = new AbstractAction( "Ok" ) { public void actionPerformed( ActionEvent ev ) { ok(); } };
    private final Action m_discardAction = new AbstractAction( "Släng" ) { public void actionPerformed( ActionEvent ev ) { discard(); } };
    private final JTextArea m_textArea = new JTextArea( 10, 40 );
    private final VerificationPanel m_verificationPanel = new VerificationPanel( "import-verification-transaction-table" );
    private final Predicate m_hasTrash = new Predicate();
    
    
    public ImportPanel()
    {
        super( new BorderLayout() );

        JScrollPane top =
            new JScrollPane( m_textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER )
            {
                protected JViewport createViewport()
                {
                    return
                        new JViewport()
                        {
                            public void setViewPosition( Point p ) { p.x = 0; super.setViewPosition( p ); }
                        };
                }
            };

        JPanel bottom = new JPanel( new ColumnLayout( 5, true, true ) );
        JPanel buttons = new JPanel( new RowLayout( 5, false, true ) );
        buttons.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        bottom.add( buttons );
        buttons.add( new JButton( m_okAction ) );
        buttons.add( new JButton( m_discardAction ) );

        bottom.add( m_verificationPanel );

        add( new JSplitPane( JSplitPane.VERTICAL_SPLIT, top, bottom ) );

        new EnablePredicateAdapter( null,
                                    null,
                                    m_okAction,
                                    null,
                                    m_verificationPanel.getHasModel() );


        new EnablePredicateAdapter( null,
                                    null,
                                    m_discardAction,
                                    null,
                                    new OrPredicate( m_verificationPanel.getHasModel(), m_hasTrash ) );

        m_textArea.getDocument().addDocumentListener(
                                                     new DocumentListener()
                                                     {
                                                         public void changedUpdate( DocumentEvent ev ) {}

                                                         public void removeUpdate( DocumentEvent ev )
                                                         {
                                                             if ( m_textArea.getText().length() == 0 ) m_textArea.setEditable( true );
                                                         }

                                                         public void insertUpdate( DocumentEvent ev )
                                                         {
                                                             m_textArea.setEditable( false );
                                                             SwingUtilities.invokeLater(
                                                                                        new Runnable()
                                                                                        {
                                                                                            public void run() { parse(); }
                                                                                        } );
                                                         }
                                                     } );
        
        m_hasTrash.set( false );
    }


    private void parse()
    {
        List<Line> lines = new ArrayList<Line>();
        StringTokenizer tr = new StringTokenizer( m_textArea.getText(), "\n", true );
        while
            ( tr.hasMoreTokens() )
        {
            String token = tr.nextToken();
            if ( ! token.equals( "\n" ) ) if ( tr.hasMoreTokens() ) tr.nextToken();
                 
            Line l = parse( token );
            
            if
                ( ! lines.isEmpty() )
            {
                Line head = lines.get( 0 );
                if
                    ( ( ( head.m_date == null ) &&  ( l.m_date != null ) ) ||
                      ( ( head.m_date != null ) &&  ( l.m_date == null ) ) ||
                      ( l.m_sum != null ) )
                {
                    lines.clear();
                }
            }
            lines.add( l );
        }


        if
            ( lines.isEmpty() )
        {
            m_verificationPanel.setVerification( null );
            return;
        }

        
        int i = lines.size();
        String tmp = m_textArea.getText();
        int n = tmp.length() - 1;
        if ( n < 0 ) n = 0;
        
        while
            ( n > 0 )
        {
            if ( tmp.charAt( n ) == '\n' ) i--;
            n--;
            if ( n == 0 ) break;
            if ( i == 0 ) break;
        }
        
        Line head = lines.get( 0 );
        if
            ( head.m_date == null )
        {
            m_textArea.setSelectionStart( n );
            m_textArea.setSelectionEnd( tmp.length() );
            m_textArea.repaint();
            m_hasTrash.set( true );
            discard();
        } else {
            if ( n > 0 ) n++;
            m_textArea.setSelectionStart( n );
            m_textArea.setSelectionEnd( tmp.length() );
            m_textArea.repaint();
            m_textArea.requestFocus();
            m_hasTrash.set( false );
            
            Verification v =
                new Verification()
                {
                    public VerificationSet getOwner() { return getVerificationSet(); }
                };
            
            v.setDate( head.m_date );

            {
                String name = "PG0";
                for
                    ( Verification ver : new TypedCollection<Verification>( getVerificationSet() ) )
                {
                    if ( ver.getName().toUpperCase().startsWith( "PG" ) ) name = ver.getName();
                }

                try
                {
                    v.setName( "PG" + ( Integer.parseInt( name.replace( "PG", "" ) ) + 1 ) );
                }
                catch ( NumberFormatException ex ) {}
            }
            
            double a = 0;

            
            class TemporaryTransaction extends Transaction
            {
                TemporaryTransaction( Verification v ) { super( v ); }
                protected void preSetAccount() {}
                protected void postSetAccount()
                {
                      // todo: save getDescription() -> getAccount() in root
                }
            }

            
            for
                ( Line l : lines )
            {
                Transaction t = new TemporaryTransaction( v );
                t.setDescription( l.m_accountNumber );
                  // todo: use getDescription() to lookup account and set it
                try
                {
                    t.setAmount( - Double.parseDouble( l.m_amount.replaceAll( " ", "" ).replaceAll( ",", "." ) ) );
                    a += t.getAmount();
                }
                catch ( NumberFormatException ex ) {}
                
                v.getTransactions().add( t );
            }
            
            {
                Transaction t = new TemporaryTransaction( v );
                t.setAccount( getVerificationSet().getOwner().getAccounts().getAccount( 1020 ) );
                t.setAmount( - a );
                v.getTransactions().add( t );
            }
            
            m_verificationPanel.setVerification( v );
        }
    }

    protected abstract VerificationSet getVerificationSet();
    
    private void ok()
    {
        Verification v = getVerificationSet().create();

        v.setDate( m_verificationPanel.getVerification().getDate() );
        v.setName( m_verificationPanel.getVerification().getName() );

        for
            ( Transaction t0 : new TypedCollection<Transaction>( m_verificationPanel.getVerification().getTransactions() ) )
        {
            Transaction t = v.createTransaction();
            t.setAccount( t0.getAccount() );
            t.setDescription( t0.getDescription() );
            t.setAmount( t0.getAmount() );
            v.getTransactions().add( t );
        }
        
        m_verificationPanel.setVerification( null );

        discard();
    }
    
    private void discard()
    {
        m_textArea.replaceSelection( "" );
        m_hasTrash.set( false );
        parse();
    }

    private Line parse( String line )
    {
        String date = null;
        String text = "";
        String accountNumber = "";
        String amount = "";
        String sum = "";

        StringTokenizer t = new StringTokenizer( line, "\t" );
        if ( t.hasMoreTokens() ) date = t.nextToken().trim();
        if ( t.hasMoreTokens() ) text = t.nextToken().trim();
        if ( t.hasMoreTokens() ) accountNumber = t.nextToken().trim();
        if ( t.hasMoreTokens() ) amount = t.nextToken().trim();
        if ( t.hasMoreTokens() ) sum = t.nextToken().trim();

        return new Line( date, text, accountNumber, amount, sum );
    }
    
    private class Line
    {
        private final Date m_date;
        private final String m_text;
        private final String m_accountNumber;
        private final String m_amount;
        private final String m_sum;
        
        Line( String date, String text, String accountNumber, String amount, String sum )
        {
            Date tmp = null;
            try
            {
                tmp = Constants.DATE_FORMAT.parse( date );
            }
            catch ( ParseException ex ) {}

            m_date = tmp;
            if
                ( m_date != null )
            {
                m_text = text.length() == 0 ? null : text;
                m_accountNumber = accountNumber.length() == 0 ? null : accountNumber;
                m_amount = amount.length() == 0 ? null : amount;
                m_sum = sum.length() == 0 ? null : sum;
            } else {
                m_text = null;
                m_accountNumber = null;
                m_amount = null;
                m_sum = null;
            }
        }
    }
}
