package nu.esox.accounting;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import nu.esox.gui.*;
import nu.esox.util.*;


public class Main extends JFrame
{
    private static String m_databasePath;


    

    private final Accounting m_accounting;

    private final JDesktopPane m_desktop = new JDesktopPane();
    private final VerificationSetPanel m_verificationSetPanel = new VerificationSetPanel();
    private final AccountPopulationEditor m_accountPopulationEditor = new AccountPopulationEditor();
    private final BalanceReportPanel m_balanceReportPanel = new BalanceReportPanel();
    private final ResultReportPanel m_resultReportPanel = new ResultReportPanel();
    private final JMenu m_yearMenu = new JMenu( "År" );
    
    
    public Main( Accounting accounting )
    {
        SwingPrefs.add( "main", this );
        m_accounting = accounting;
       
        JPanel p = new JPanel( new BorderLayout() );
        getContentPane().add( p );
        
        p.add( m_desktop, BorderLayout.CENTER );
        m_desktop.setOpaque( true );
        m_desktop.setBackground( Color.gray );

        addInternalFrame( "verification-frame", "Verifikationer", m_verificationSetPanel );
        addInternalFrame( "accounts-frame", "Konton", m_accountPopulationEditor );
        addInternalFrame( "balance-frame", "Balans", m_balanceReportPanel );
        addInternalFrame( "result-frame", "Resultat", m_resultReportPanel );

        p.add( createMenuBar(), BorderLayout.NORTH );

        {
            p.add( new JLabel( "Status" ), BorderLayout.SOUTH );
        }

        pack();

        SwingUtilities.invokeLater( new Runnable() { public void run() { load(); } } );
    }

    private void load()
    {
        Preferences prefs = Preferences.userRoot().node( "nu/esox/accounting" );
        try
        {
            prefs.sync();
        }
        catch ( BackingStoreException ex )
        {
            System.err.println( ex );
        }

        SwingPrefs.apply( prefs );
    }

    private void addInternalFrame( String name, String title, JComponent c )
    {
        JInternalFrame f = new JInternalFrame( title, true, false, true, true );
        SwingPrefs.add( name, f );
        m_desktop.add( f );
        f.setVisible( true );
        f.getContentPane().add( c );
        f.pack();
    }

    private JMenuBar createMenuBar()
    {
        JMenuBar mb = new JMenuBar();

        {
            JMenu m = mb.add( new JMenu( "Arkiv" ) );
            m.add( m_newAction );
            m.add( m_closeAction );
            m.add( m_exitAction );
        }

        {
            mb.add( m_yearMenu );
            buildYearMenu();
            m_accounting.getYears().addObservableListener(
                                                          new ObservableListener()
                                                          {
                                                              public void valueChanged( ObservableEvent ev )
                                                              {
                                                                  buildYearMenu();
                                                              }
                                                          } );
        }

        {
            JMenu m = mb.add( new JMenu( "Verktyg" ) );
            m.add( m_newYearAction );
            m.add( m_closeYearAction );
        }
        
        {
            JMenu m = mb.add( new JMenu( "Fönster" ) );

            for
                ( JInternalFrame f : m_desktop.getAllFrames() )
            {
                m.add( new InternalFrameCheckBoxMenuItem( f ) );
            }
        }
        
        return mb;
    }

    private void buildYearMenu()
    {
        m_yearMenu.removeAll();

        for ( Year y : new TypedCollection<Year>( m_accounting.getYears() ) ) m_yearMenu.add( new SelectYearAction( y ) );
    }
    
    private class SelectYearAction extends AbstractAction
    {
        private final Year m_year;
        
        SelectYearAction( Year y )
        {
            super( Integer.toString( y.getNumber() ) );
            m_year = y;
        }

        public void actionPerformed( ActionEvent ev )
        {
            setYear( m_year );
        }
    }


    private static final String m_title = "LSDK bokföring 1.1";
    
    private void setYear( Year y )
    {
        if
            ( y == null )
        {
            setTitle( m_title );
            m_balanceReportPanel.setYear( null );
            m_resultReportPanel.setYear( null );
            m_verificationSetPanel.setYear( null );
            m_accountPopulationEditor.setAccountPopulation( null );
            m_closeYearAction.setEnabled( false );
        } else {
            setTitle( m_title + " - " + Integer.toString( y.getNumber() ) );
            m_balanceReportPanel.setYear( y );
            m_resultReportPanel.setYear( y );
            m_verificationSetPanel.setYear( y );
            m_accountPopulationEditor.setAccountPopulation( y.getAccounts() );
            m_closeYearAction.setEnabled( true );
        }
    }
    
//     private JComponent createYearList()
//     {
//         JList l = new JList( new ObservableCollectionListModel( m_accounting.getYears() ) );
//         SwingPrefs.add( "years", l );
//           //todo: single selection
//         new ListSelectionAdapter( l, m_balanceReportPanel, Year.class, "setYear" );
//         new ListSelectionAdapter( l, m_resultReportPanel, Year.class, "setYear" );
//         new ListSelectionAdapter( l, m_verificationSetPanel, Year.class, "setYear" );
//         new ListSelectionAdapter( l, m_accountPopulationEditor, AccountPopulation.class, "setAccountPopulation" )
//         {
//             protected Object getModelFrom( Object model )
//             {
//                 return ( (Year) model ).getAccounts();
//             }
//         };
//         new ListSelectionMonitor( l.getSelectionModel(), new Object [] { m_closeYearAction } );
    
//         return new JScrollPane( l );
//     }

    
    private void save()
    {
        Preferences prefs = Preferences.userRoot().node( "nu/esox/accounting" );
//        prefs.clear();
        
        SwingPrefs.collect( prefs );

        try
        {
            prefs.flush();
        }
        catch ( BackingStoreException ex )
        {
            System.err.println( ex );
        }
    }


    private final Action m_newAction = new AbstractAction( "Nytt fönster" ) { public void actionPerformed( ActionEvent ev ) { newWindow( m_accounting, null ); } };
    private final Action m_closeAction = new AbstractAction( "Stäng" ) { public void actionPerformed( ActionEvent ev ) { closeWindow(); } };
    private final Action m_exitAction = new AbstractAction( "Avsluta" ) { public void actionPerformed( ActionEvent ev ) { exit(); } };
    private final Action m_newYearAction = new AbstractAction( "Nytt år" ) { public void actionPerformed( ActionEvent ev ) { newYear(); } };
    private final Action m_closeYearAction = new AbstractAction( "Bokslut" ) { public void actionPerformed( ActionEvent ev ) { closeYear(); } };


    private static LinkedList<Main> m_instances = new LinkedList<Main>();

    
    private static void newWindow( Accounting a, Year y )
    {
        final Main f = new Main( a );
        m_instances.add( f );
        
        f.setYear( y );
        f.show();
        f.addWindowListener( new WindowAdapter() { public void windowClosing( WindowEvent ev ) { f.closeWindow(); } } );
    }
    
    private void closeWindow()
    {
        m_instances.remove( this );
        dispose();
        if ( m_instances.isEmpty() ) exit();
    }
    
    private void exit()
    {
        while
            ( true )
        {
            Year y = (Year) m_accounting.getYears().get( m_accounting.getYears().size() - 1 );
            if
                ( y.getVerifications().isEmpty() )
            {
                m_accounting.getYears().remove( y );
            } else {
                break;
            }
        }
        
        try
        {
            PrintStream s = new PrintStream( new FileOutputStream( m_databasePath ) );
            nu.esox.xml.XmlWriter w = new nu.esox.xml.XmlWriter( s, "1.0", "ISO-8859-1", true );
            w.write( m_accounting );
            s.close();
        }
        catch ( IOException ex ) {}
        
        save();
        System.exit( 0 );
    }

    
    private void newYear()
    {
        Year y = (Year) m_accounting.getYears().get( m_accounting.getYears().size() - 1 );
        if
            ( y.getVerifications().isEmpty() )
        {
            y.startYear();
        } else {
            m_accounting.getYears().add( y.newYear() );
        }
    }
    
    private void closeYear()
    {
        ( (Year) m_accounting.getYears().get( m_accounting.getYears().size() - 1 ) ).closeYear();
    }
    
 
    private class InternalFrameCheckBoxMenuItem extends JCheckBoxMenuItem
    {
        private final JInternalFrame m_frame;
        
        InternalFrameCheckBoxMenuItem( JInternalFrame f )
        {
            super( f.getTitle() );
            m_frame = f;

            addItemListener(
                            new ItemListener()
                            {
                                public void itemStateChanged( ItemEvent ev )
                                {
                                    try
                                    {
                                        m_frame.setIcon( ! isSelected() );
                                    }
                                    catch ( java.beans.PropertyVetoException ex ) {}
                                }
                            } );
            
            m_frame.addInternalFrameListener( 
                                             new InternalFrameAdapter()
                                             {
                                                 public void internalFrameDeiconified( InternalFrameEvent ev ) { setSelected( true ); }
                                                 public void internalFrameIconified( InternalFrameEvent ev ) { setSelected( false ); }
                                                 
                                             } );

            setSelected( ! m_frame.isIcon() );
        }
    }


    
    
    public static void main( String [] args ) throws Exception
    {
        if
            ( args.length == 0 )
        {
            System.err.println( "Usage: java -jar lsdk.jar <database-path> [ <import files> ... ]" );
            System.err.println( "Properties: " );
            System.err.println( "  swing.aatext            true/false [true]   Turn on/off font antialiasing" );
            System.err.println( "  nu.esox.font.name                           Font name" );
            System.err.println( "  nu.esox.font.size       [12.0]              Font size (only used if nu.esox.font.name is defined)" );
            System.err.println( "  nu.esox.font.size-delta [-2.0]              Font size delta, 0 if nu.esox.font.size is defined" );
            return;
        }

        m_databasePath = args[ 0 ];
        
        if
            ( args.length > 1 )
        {
            Import.main( args );
        } else {
            
            Thread.setDefaultUncaughtExceptionHandler(
                                                      new Thread.UncaughtExceptionHandler()
                                                      {
                                                          public void uncaughtException( Thread t, Throwable e )
                                                          {
                                                              System.err.println( "hepp" );
                                                              e.printStackTrace();
                                                              if ( e.getCause() != null ) uncaughtException( t, e.getCause() );
                                                          }
                                                      } );
            
            Accounting a = new Accounting();
                    
            try
            {
                new nu.esox.xml.XmlReader( new FileInputStream( m_databasePath ), a, new HashMap() );
                newWindow( a, (Year) a.getYears().get( a.getYears().size() - 1 ) );
            }
            catch ( Exception ex )
            {
                throw ex;
            }
//             for
//                 ( Object o : a.getYears().getYear( 2006 ).getAccounts() )
//             {
//                 Account a2 = (Account) o;
//                 ArrayList tmp = new ArrayList();
//                 for
//                     ( Object O : a2.getTransactions() )
//                 {
//                     Transaction t = (Transaction) O;
//                     if ( t.getVerification() == null ) tmp.add( t );
//                 }
//                 a2.getTransactions().removeAll( tmp );
//             }
                  
        }
    }

    private static Font m_font;
    private static float m_fontSizeDelta = -2;


    static
    {
        if ( System.getProperty( "swing.aatext" ) == null ) System.setProperty( "swing.aatext", "true" );
//        System.err.println( System.getProperties() );
//          for ( UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels() ) System.err.println( laf.getName() );

//         try
//         {
//             UIManager.setLookAndFeel( UIManager.getInstalledLookAndFeels()[ 2 ].getClassName() );
//         }
//         catch ( Exception ex )
//         {
//             ex.printStackTrace();
//         }
//         System.err.println( "### " + UIManager.getFont( "Table.font" ) );


        String fontName = System.getProperty( "nu.esox.font.name" );
        float fontSize = 12f;
        try
        {
            fontSize = Float.parseFloat( System.getProperty( "nu.esox.font.size" ) );
            m_fontSizeDelta = 0;
        }
        catch ( Exception ex ) {}
        
        for ( Font f : 	GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts() )
        {
            if ( f.getFontName().equals( fontName ) ) m_font = f.deriveFont( fontSize );
        }

        try
        {
            m_fontSizeDelta = Float.parseFloat( System.getProperty( "nu.esox.font.size-delta" ) );
        }
        catch ( Exception ex ) {}
       
        for
            ( String s : new String [] { "TextField.font", "PasswordField.font", "TextArea.font", "TextPane.font", "EditorPane.font", "FormattedTextField.font", "Button.font", "CheckBox.font", "RadioButton.font", "ToggleButton.font", "ToolTip.font", "ProgressBar.font", "ComboBox.font", "DesktopIcon.font", "TitledBorder.font", "Label.font", "List.font", "TabbedPane.font", "Table.font", "TableHeader.font", "MenuBar.font", "Menu.font", "MenuItem.font", "PopupMenu.font", "CheckBoxMenuItem.font", "RadioButtonMenuItem.font", "Spinner.font", "Tree.font", "ToolBar.font" } )
        {
            x( s );
        }
  
        UIManager.put( "ComboBox.disabledForeground", UIManager.get( "ComboBox.foreground" ) );
        UIManager.put( "TextField.inactiveForeground", UIManager.get( "TextField.foreground" ) );
        UIManager.put( "TextArea.inactiveForeground", UIManager.get( "TextArea.foreground" ) );
        UIManager.put( "FormattedTextField.inactiveForeground", UIManager.get( "FormattedTextField.foreground" ) );
        UIManager.put( "Button.margin", new Insets( 0, 3, 0, 3 ) );
        UIManager.put( "ToggleButton.margin", new Insets( 0, 3, 0, 3 ) );
        
// //         UIManager.put( "TitledBorder.border", BorderFactory.createEtchedBorder() );
        
// //         UIManager.put( "OptionPane.border",
// //                        BorderFactory.createCompoundBorder( BorderFactory.createEtchedBorder( javax.swing.border.EtchedBorder.RAISED ),
// //                                                            UIManager.getBorder( "OptionPane.border" ) ) );

    }

    private static void x( String fn )
    {
        Font f = UIManager.getFont( fn );
        if ( m_font != null ) f = m_font;
        
        if
            ( f != null )
        {
            UIManager.put( fn, f.deriveFont( Font.PLAIN, f.getSize2D() + m_fontSizeDelta ) );
        }
    }
}

