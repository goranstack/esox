package nu.esox.gui.list;

import javax.swing.*;


public class ListCellRendererProxy implements ListCellRenderer
{
    private final ListCellRenderer m_delegate;

    public ListCellRendererProxy( ListCellRenderer delegate )
    {
        m_delegate = delegate;
    }

    public java.awt.Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
        return m_delegate.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
    }
}
