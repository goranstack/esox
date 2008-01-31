package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;


public class AmountTableEditor extends AbstractCellEditor implements TableCellEditor
{
    private final JFormattedTextField m_textField = new AmountTextField();

    
    AmountTableEditor()
    {
    }
    
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row, int column )
    {
        m_textField.setValue( value );
        m_textField.selectAll();
        return m_textField;
    }
    
    public Object getCellEditorValue()
    {
        try
        {
            m_textField.commitEdit();
        }
        catch ( java.text.ParseException ex ) {}
        return m_textField.getValue();
    }


}

    
