package nu.esox.accounting;

import java.awt.*;
import javax.swing.*;
import nu.esox.gui.layout.*;


public class LabelPanel extends JPanel
{
    public LabelPanel( Component c, String label )
    {
        super( new ColumnLayout( true, true ) );

        JLabel l = new JLabel( label );
        l.setFont( c.getFont().deriveFont( Font.PLAIN, c.getFont().getSize2D() - 1 ) );

        add( l );
        add( c );
    }
}
