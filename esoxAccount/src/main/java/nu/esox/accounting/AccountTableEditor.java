package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.gui.list.*;
import nu.esox.gui.model.*;

    

@SuppressWarnings( "serial" )
public class AccountTableEditor extends DefaultCellEditor
{

//    private AccountPopulation m_accounts;

    
    AccountTableEditor()
    {
//         super( new AccountNumberTextField() );
        super( new JComboBox( new ObservableCollectionComboBoxModel<Account>( null ) ) ); //  todo: what about combobox + popupmenu tha triggers on any key?
                                                                                       
        ( (JComboBox) getComponent() ).setRenderer( new AccountListRenderer() );
    }
    
    @SuppressWarnings( "unchecked" )
    void setAccounts( AccountPopulation accounts )
    {
        ( (ObservableCollectionComboBoxModel<Account>) ( (JComboBox) getComponent() ).getModel() ).setData( accounts );
//        m_accounts = accounts;
//        ( (AccountNumberTextField) getComponent() ).setAccounts( m_accounts );
    }

//     public boolean stopCellEditing()
//     {
//         if ( ( (AccountNumberTextField) getComponent() ).assure() ) return false;
//         return super.stopCellEditing();
//     }
    
//     public Object getCellEditorValue()
//     {
//         try
//         {
//             String str = ( (AccountNumberTextField) getComponent() ).getText().replaceFirst( " .*$", "" );
//             return m_accounts.getAccount( Integer.parseInt( str ) );
//         }
//         catch ( NumberFormatException ex )
//         {
//             return null;
//         }
//     }
    
    class AccountListRenderer extends DefaultListCellRenderer
    {
        public Component	getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            return super.getListCellRendererComponent( list, AccountTableRenderer.renderAccount( value ), index, isSelected, cellHasFocus );
        }
    }
}

