package nu.esox.fish.gui;

import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import nu.esox.fish.domain.Catch;
import nu.esox.fish.domain.Catches;
import nu.esox.gui.aspect.EnablePredicateAdapter;
import nu.esox.gui.aspect.TableSelectionAdapter;
import nu.esox.gui.layout.ColumnLayout;
import nu.esox.gui.layout.RowLayout;
import nu.esox.gui.list.ListSelectionPredicate;
import nu.esox.util.ObservableIF;


@SuppressWarnings( "serial" )
public class Main extends JFrame
{
    private final Catches m_catches;
    private final JTable m_table;

    
    public Main( Catches c )
    {
    	setIconImage(new ImageIcon(getClass().getResource("bluefish.png")).getImage());
        m_catches = c;
        
        getContentPane().setLayout( new ColumnLayout( 5, true, true ) );
        ( (JComponent) getContentPane() ).setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

//         JList list = new JList( new ObservableCollectionListModel( m_catches ) );
//         getContentPane().add( new JScrollPane( list ) );

        m_table = new JTable( new CatchTableModel( m_catches ) );
        getContentPane().add( new JScrollPane( m_table ) );

        {
            JPanel buttons = new JPanel( new RowLayout( 5 ) );
            getContentPane().add( buttons );
            buttons.add( new JButton( m_addAction ) );
            buttons.add( new JButton( m_deleteAction ) );
        }

        {
            CatchPanel p = new CatchPanel();
            getContentPane().add( p );

            new TableSelectionAdapter( m_table, p )
            {
                protected ObservableIF getItemOfRow( int row ) { return m_catches.get( row ); }
                protected int getRowOfItem( ObservableIF item ) { return m_catches.indexOf( item ); }
            };

//             new ListSelectionAdapter( list, p );
            new EnablePredicateAdapter( p, null, m_deleteAction, null, new ListSelectionPredicate( m_table.getSelectionModel(), ListSelectionPredicate.TEST_SOME ) );
        }

    }


    private final Action m_addAction =
        new AbstractAction( "Add" )
        {
            public void actionPerformed( ActionEvent ev )
            {
                int i = m_catches.indexOf( m_catches.add() );
                m_table.getSelectionModel().setSelectionInterval( i, i );
            }
        };

    private final Action m_deleteAction =
        new AbstractAction( "Delete" )
        {
            public void actionPerformed( ActionEvent ev )
            {
                int [] n = m_table.getSelectedRows();
                m_table.clearSelection();
                
                m_catches.beginTransaction( true );
                for ( int i = n.length - 1; i >= 0; i-- ) m_catches.remove( n[ i ] );
                m_catches.endTransaction( null, null );
            }
        };



    


    public static void main( String [] args )
    {
        Catches cs = new Catches();
        {
            Catch c = cs.add();
            c.getFish().setSpecies( "eel" );
            c.getFish().setWeight( 2.412 );
            c.getFish().setLength( 105 );
            c.getFish().setGirth( Float.NaN );
            c.getCoordinates().setVenue( "Järnlunden" );
            c.getCoordinates().setSwim( "Secret" );
            c.getCoordinates().setWhen( new Date() );
            c.setMethod( "ledgering" );
            c.setBait( "dead bleak" );
        }

        {
            Catch c = cs.add();
            c.getFish().setSpecies( "zander" );
            c.getFish().setWeight( 5.934 );
            c.getFish().setLength( Float.NaN );
            c.getFish().setGirth( Float.NaN );
            c.getCoordinates().setVenue( "Lilla rengen" );
            c.getCoordinates().setSwim( "Secret" );
            c.getCoordinates().setWhen( new Date() );
            c.setMethod( "ledgering" );
            c.setBait( "dead bleak" );
        }

        {
            Catch c = cs.add();
            c.getFish().setSpecies( "perch" );
            c.getFish().setWeight( 1.060 );
            c.getFish().setLength( Float.NaN );
            c.getFish().setGirth( Float.NaN );
            c.getCoordinates().setVenue( "Fullbosjön" );
            c.getCoordinates().setSwim( "Secret" );
            c.getCoordinates().setWhen( new Date() );
            c.setMethod( "ice fishing" );
            c.setBait( "lure" );
        }

        {
            Catch c = cs.add();
            c.getFish().setSpecies( "pike" );
            c.getFish().setWeight( 6.4f );
            c.getFish().setLength( Float.NaN );
            c.getFish().setGirth( Float.NaN );
            c.getCoordinates().setVenue( "Bjursund" );
            c.getCoordinates().setSwim( "Secret" );
            c.getCoordinates().setWhen( new Date() );
            c.setMethod( "spinning" );
            c.setBait( "6\" shad jigg" );
        }

        Main f = new Main( cs );

        f.addWindowListener(
                            new java.awt.event.WindowAdapter()
                            {
                                public void windowClosing( java.awt.event.WindowEvent e )
                                {
                                    System.exit( 0 );
                                }
                            } );

        f.pack();
        f.setVisible( true );
    }
}
