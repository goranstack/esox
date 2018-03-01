package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;



@SuppressWarnings( "serial" )
public class AmountTableRenderer extends DefaultTableCellRenderer
{
    private final String m_format;

    AmountTableRenderer( String format )
    {
        m_format = format;
        setHorizontalAlignment( JTextField.RIGHT );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        return super.getTableCellRendererComponent( table, String.format( m_format, AmountTextField.format( value ) ), isSelected, hasFocus, row, column );
    }
}

