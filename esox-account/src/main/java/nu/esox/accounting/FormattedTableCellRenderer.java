package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;



@SuppressWarnings( "serial" )
public class FormattedTableCellRenderer extends DefaultTableCellRenderer
{
    private final String m_format;

    FormattedTableCellRenderer( String format )
    {
        m_format = format;
//        setHorizontalAlignment( JTextField.RIGHT );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        return super.getTableCellRendererComponent( table, String.format( m_format, value.toString() ), isSelected, hasFocus, row, column );
    }
}

