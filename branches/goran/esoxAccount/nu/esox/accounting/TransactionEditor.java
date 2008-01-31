package nu.esox.accounting;

import java.text.*;
import java.awt.event.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.model.*;
import nu.esox.util.*;


public class TransactionEditor extends ModelPanel
{
    public TransactionEditor()
    {
        super( new RowLayout( 5 ) );

        {
            AccountComboBox cb = new AccountComboBox();
            add( new LabelPanel( cb, "Konto" ) );
            new ComboBoxAdapter( cb, this, Transaction.class, "getAccount", "setAccount", Account.class, null, null, null );
            new SubModelAdapter( cb, "setTransaction", Transaction.class, this, null );
        }

        {
            JTextField t = new JTextField( 30 );
            add( new LabelPanel( t, "Text" ), RowLayout.FILL );
            new TextFieldAdapter( t, this, Transaction.class, null, "getDescription", "setDescription" );
        }

        {
            JFormattedTextField t = new AmountTextField();
            add( new LabelPanel( t, "Belopp" ) );
            new FormattedTextFieldAdapter( t, this, Transaction.class, "getAmount", "setAmount", double.class, null, null, null );
            t.addActionListener( new ActionListener() { public void actionPerformed( ActionEvent ev ) { apply(); } } );
        }

    }

    public Transaction getTransaction() { return (Transaction) getModel(); }

    public void setTransaction( Transaction transaction )
    {
        setModel( transaction );
    }

    protected void apply()
    {
        requestFocus();
    }
    

    public static class AccountComboBox extends JComboBox
    {
        AccountComboBox()
        {
            super( new ObservableCollectionComboBoxModel( null ) );
        }

        public void setTransaction( Transaction t )
        {
            AccountPopulation ap = null;
            if
                ( ( t != null ) && ( t.getVerification() != null ) )
            {
//             System.err.println( t );
//             System.err.println( t.getVerification() );
//             System.err.println( t.getVerification().getOwner() );
//             System.err.println( t.getVerification().getOwner().getOwner() );
//             System.err.println( t.getVerification().getOwner().getOwner().getAccounts() );
                ap = t.getVerification().getOwner().getOwner().getAccounts();
            }
            ( (ObservableCollectionComboBoxModel) getModel() ).setData( ap );
        }
    }
}
