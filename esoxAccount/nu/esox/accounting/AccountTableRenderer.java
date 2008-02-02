package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


@SuppressWarnings( "serial" )
public class AccountTableRenderer extends DefaultTableCellRenderer
{
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        return super.getTableCellRendererComponent( table, renderAccount( value ), isSelected, hasFocus, row, column );
    }


    public static Object renderAccount( Object value )
    {
        if
            ( value != null )
        {
            Account a = (Account) value;
            value = NumberTextField.FORMAT.format( a.getNumber() ) + " " + a.getName();
        }
        return value;
    }
}
