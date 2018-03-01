package nu.esox.accounting;

import java.text.*;
import javax.swing.*;
import nu.esox.gui.*;
import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class AccountNumberTextField extends JTextField implements ObservableListener
{
    public static final DecimalFormat FORMAT = new DecimalFormat( "0000" );
    
    private JPopupMenu m_popupMenu = null;
    private AccountPopulation m_accounts;

    
    public AccountNumberTextField()
    {
        setColumns( 4 );
        setHorizontalAlignment( JTextField.RIGHT );
        TextFieldFocusHandler.add( this );
    }

    public void setAccounts( AccountPopulation accounts )
    {
        if ( m_accounts != null ) m_accounts.removeObservableListener( this );
        m_accounts = accounts;
        m_popupMenu = null;
        if ( m_accounts != null ) m_accounts.addObservableListener( this );
    }

    public void valueChanged( ObservableEvent ev )
    {
        m_popupMenu = null;
    }

    private void postPopupMenu()
    {
        if
            ( m_popupMenu == null )
        {
            if
                ( m_accounts != null )
            {
                m_popupMenu = new JPopupMenu(); // todo: type = submenu
                for ( Account a : m_accounts )  m_popupMenu.add( new Xxx( a ) );
            }
        }
        
        if
            ( m_popupMenu != null )
        {
            m_popupMenu.show( AccountNumberTextField.this, 0, 0 );
        }
    }
    

    boolean assure()
    {
        if
            ( m_accounts != null )
        {
            if
                ( getText().trim().length() < 4 )
            {
                postPopupMenu();
                return true;
            }
        }

        return false;
    }
    
    
    class Xxx extends JMenuItem implements java.awt.event.ActionListener
    {
        private final Account m_account;
        
        Xxx( Account account )
        {
            super( "" + account );
            m_account = account;
            addActionListener( this );
        }
        
        public void actionPerformed( java.awt.event.ActionEvent ev )
        {
            AccountNumberTextField.this.setText( "" + m_account );
            AccountNumberTextField.this.postActionEvent();   // todo: also if nothing is selected
        }
    }
}
