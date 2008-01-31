package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;



public class AmountTableRenderer extends DefaultTableCellRenderer
{
    {
        setHorizontalAlignment( JTextField.RIGHT );
    }
    
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
    {
        return super.getTableCellRendererComponent( table, AmountTextField.format( value ), isSelected, hasFocus, row, column );
    }
}

