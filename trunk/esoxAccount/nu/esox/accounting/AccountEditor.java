package nu.esox.accounting;

import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.layout.*;
import nu.esox.util.*;


public class AccountEditor extends ModelPanel
{
    private final Predicate m_isEditable = new Predicate();

    private final NumberTextField m_numberTextField =
        new NumberTextField()
        {
            protected void verify( int value ) throws ParseException { verifyNumber( value ); }
        };



    public AccountEditor()
    {
        super( new ColumnLayout( 5, !false, true ) );

        JComboBox typeComboBox = new JComboBox( Account.TYPES );
        JCheckBox lockedCheckBox = new JCheckBox();
        JTextField nameTextField = new JTextField( 20 );
        JFormattedTextField initialBalanceTextField = new AmountTextField();
        JFormattedTextField budgetTextField = new AmountTextField();

        {
            JPanel p = new JPanel( new RowLayout( 10, false, false, RowLayout.LEFT, 0.5 ) );
            add( p );

            p.add( new LabelPanel( m_numberTextField, "Nummer" ) );
            new FormattedTextFieldAdapter( m_numberTextField, this, Account.class, "getNumber", "setNumber", int.class, null, null, null );

            typeComboBox.setPreferredSize( new java.awt.Dimension( typeComboBox.getPreferredSize().width, m_numberTextField.getPreferredSize().height ) );
            typeComboBox.setMaximumRowCount( Account.TYPES.length );
            p.add( new LabelPanel( typeComboBox, "Typ" ) );
            new ComboBoxAdapter( typeComboBox, this, Account.class, "getType", "setType", Account.Type.class, null, Account.TYPE_UNDEFINED.toString(), Account.TYPE_UNDEFINED.toString() );
            
            p.add( new LabelPanel( lockedCheckBox, "Låst" ) );
            new CheckBoxAdapter( lockedCheckBox, this, Account.class, null, "isLocked", "setLocked" );
        }

        {
            JPanel p = new JPanel( new RowLayout( 10, false, false, RowLayout.LEFT, 1.0 ) );
            add( p );

            p.add( new LabelPanel( nameTextField, "Namn" ) );
            new TextFieldAdapter( nameTextField, this, Account.class, "getName", "setName", String.class, null, "", "" );
            TextFieldFocusHandler.add( nameTextField );

            {
                LabelPanel l = new LabelPanel( initialBalanceTextField, "Ingånde balans" );
                p.add( l );
                new FormattedTextFieldAdapter( initialBalanceTextField, this, Account.class, "getIb", "setIb", double.class, null, null, null );
                new VisibleAdapter( l, this, Account.class, "hasIb", null, true, false, false, false );
            }

            {
                LabelPanel l = new LabelPanel( budgetTextField, "Budget" );
                p.add( l );
                new FormattedTextFieldAdapter( budgetTextField, this, Account.class, "getBudget", "setBudget", double.class, null, null, null );
                new VisibleAdapter( l, this, Account.class, "hasBudget", null, true, false, false, false );
            }
        }

        {
            JPanel p = new JPanel( new RowLayout( 10, false, false, RowLayout.LEFT, 1.0 ) );
            add( p );

            p.add( new JLabel( "" ), RowLayout.FILL );
            
            {
                JFormattedTextField t = new AmountTextField();
                p.add( new LabelPanel( t, "Saldo" ) );
                new FormattedTextFieldAdapter( t, this, Account.class, "getAmount", null, double.class, null, Double.NaN, Double.NaN );
            }
        }

        new EnablePredicateAdapter( new JComponent [] { lockedCheckBox },
                                    null,
                                    null,
                                    null,
                                    getHasModel() );

        new EnablePredicateAdapter( new JComponent [] { m_numberTextField, typeComboBox, nameTextField, initialBalanceTextField, budgetTextField },
                                    null,
                                    null,
                                    null,
                                    new AndPredicate( getHasModel(), m_isEditable ) );

    }

    public Account getAccount()
    {
        return (Account) getModel();
    }

    public void setAccount( Account account )
    {
        setModel( account );
    }

    protected void preSetModel( ObservableIF oldModel, ObservableIF newModel )
    {
        super.preSetModel( oldModel, newModel );
        
        if
            ( oldModel != null )
        {
            Account a = (Account) oldModel;
            a.removeObservableListener( m_accountListener );
        }
    }
    
    protected void postSetModel( ObservableIF oldModel, ObservableIF newModel )
    {
        super.postSetModel( oldModel, newModel );
        
        if
            ( newModel != null )
        {
            Account a = (Account) newModel;
            a.addObservableListener( m_accountListener );
            if ( a.getNumber() == 0 ) m_numberTextField.requestFocus();
        }

        accountChanged();
    }
    
    private void accountChanged()
    {
        m_isEditable.set( ( getAccount() != null ) && ! getAccount().isLocked() );
    }




    protected void verifyNumber( int value ) throws ParseException
    {
        if ( value == 0 ) throw new ParseException( "", 0 );
    }



    private final ObservableListener m_accountListener =
        new ObservableListener()
        {
            public void valueChanged( ObservableEvent ev )
            {
                accountChanged();
            }
        };



    
    public static void main( String [] args )
    {
        JFrame f = new JFrame();

        AccountEditor e1 = new AccountEditor();

        JPanel p = new JPanel( new ColumnLayout() );
        f.getContentPane().add( p );
        p.add( e1 );

        Account a = new Account( 42 );

        e1.setAccount( a );

        f.pack();
        f.show();
    }
}
