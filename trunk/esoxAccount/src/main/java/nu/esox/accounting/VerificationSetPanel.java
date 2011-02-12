package nu.esox.accounting;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import nu.esox.util.*;
import nu.esox.util.Observable;
import nu.esox.gui.*;
import nu.esox.gui.layout.*;
import nu.esox.gui.aspect.*;
import nu.esox.gui.list.*;


@SuppressWarnings( "serial" )
public class VerificationSetPanel extends ModelPanel
{
    private final Action m_importAction = new AbstractAction( "Importera" ) { public void actionPerformed( ActionEvent ev ) { openImportDialog(); } };
    private final Action m_titlePagesAction = new AbstractAction( "Försättsblad" ) { public void actionPerformed( ActionEvent ev ) { createTitlePages(); } };
    private final VerificationSetTable m_table = new VerificationSetTable( "Ny", "Bort" );
    private ImportPanel m_importPanel = null;
    
    
    public VerificationSetPanel()
    {
        super( new BorderLayout( 5, 5 ) );
//        SwingPrefs.add( "verification-table", m_table );
        
        setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );

        JComponent left = new JPanel( new BorderLayout( 5, 5 ) );
        JPanel right = new JPanel( new BorderLayout( 5, 5 ) );

        left.add( new JScrollPane( m_table ), BorderLayout.CENTER );

        
        VerificationPanel verificationPanel = new VerificationPanel( "verification-transaction-table" );
        right.add( verificationPanel, BorderLayout.CENTER );

        JPanel buttons = new JPanel( new RowLayout( 5, false, true ) );
        left.add( buttons, BorderLayout.SOUTH );
        buttons.add( new JButton( m_table.getUpAction() ) );
        buttons.add( new JButton( m_table.getDownAction() ) );
        buttons.add( new JButton( m_table.getAddAction() ) );
        buttons.add( new JButton( m_table.getDeleteAction() ) );
        buttons.add( new JButton( m_importAction ) );
        buttons.add( new JButton( m_titlePagesAction ) );


        {
            JSplitPane sp = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, left, right );
//            SwingPrefs.add( "verification-split", sp );
            add( sp, BorderLayout.CENTER );
        }


        new EnablePredicateAdapter( this, null, m_importAction, null, m_table.getHasModel() );
        
        new SubModelAdapter( m_table, "setVerificationSet", VerificationSet.class, this, Year.class, "getVerifications", null );

        new TableSelectionAdapter( m_table, verificationPanel )//, Verification.class, "setVerification" )
        {
//             protected Object getModelFrom( int [] selected )
//             {
//                 return ( selected.length == 1 ) ? getYear().getVerifications().get( selected[ 0 ] ) : null;
//             }
            
            protected ObservableIF getItemOfRow( int row ) { return getYear().getVerifications().get( row ); }
            protected int getRowOfItem( ObservableIF item ) { return getYear().getVerifications().indexOf( item ); }
        };

        new EnablePredicateAdapter( null, null, m_titlePagesAction, null, new ListSelectionPredicate( m_table.getSelectionModel(), ListSelectionPredicate.TEST_SOME ) );
    }

    
    public final void setYear( Year y )
    {
        setModel( y );
    }

    public final Year getYear()
    {
        return (Year) getModel();
    }


    public void openImportDialog()
    {
        if
            ( m_importPanel == null )
        {
            m_importPanel =
                new ImportPanel()
                {
                    protected VerificationSet getVerificationSet()
                    {
                        return getYear().getVerifications();
                    }
                };

            JDialog d = new JDialog( (JFrame) getTopLevelAncestor(), "Importera", false );
            d.getContentPane().add( m_importPanel );
            d.pack();
        }

        ( (JDialog) m_importPanel.getTopLevelAncestor() ).setVisible( true );
    }



    private final JFileChooser m_fileChooser = new JFileChooser();

    
    public void createTitlePages()
    {
        if
            ( m_fileChooser.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION )
        {
            try
            {
                ArrayList<Verification> vs = new ArrayList<Verification>();
                
                int [] rows = m_table.getSelectedRows();
                for ( int i : m_table.getSelectedRows() ) vs.add( getYear().getVerifications().get( i ) );
                
                TitlePager.doit( vs, m_fileChooser.getSelectedFile() );
            }
            catch ( Exception ex )
            {
                System.err.println( ex );
                ex.printStackTrace();
            }
        }
    }

    
//     private class IsBalanced extends Predicate implements ObservableListener, PropertyChangeListener
//     {
//         {
//             m_verificationPanel.addPropertyChangeListener( this );
//         }
        
//         public void propertyChange( PropertyChangeEvent ev ) // implements PropertyChangeListener
//         {
//             if
//                 ( ev.getPropertyName() == nu.esox.swing.ComponentAdapter.MODEL_PROPERTY )
//             {
//                 if
//                     ( ev.getOldValue() != null )
//                 {
//                     ( (Verification) ev.getOldValue() ).removeObservableListener( this );
//                 }
                
//                 if
//                     ( ev.getNewValue() != null )
//                 {
//                     ( (Verification) ev.getNewValue() ).addObservableListener( this );
//                 }

//                 update();
//             }
//         }
        
//         public final void valueChanged( ObservableEvent ev ) // implements ObservableListener
//         {
//             update();
//         }
        
//         private void update()
//         {
//             if
//                 ( m_verificationPanel.getVerification() == null )
//             {
//                 set( false );
//             } else if
//                 ( m_verificationPanel.getVerification().getAmount() != 0 )
//             {
//                 set( false );
//             } else if
//                 ( m_verificationPanel.getVerification().getTransactions().size() == 0 )
//             {
//                 set( false );
//             } else {
//                 set( true );
//             }
//         }
//     }


    
}

