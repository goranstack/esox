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


/*
  fixit:

  single table based reports with formatting -> export=dnd
  transaction table.double click -> popup transaction editor
  transaction table.xxx -> select account
  account table.xxx -> select transaction
  splash x 2
*/

@SuppressWarnings( "serial" )
public class Main extends JFrame
{
    private static String m_databasePath;


    private final Accounting m_accounting;

    private final VerificationSetPanel m_verificationSetPanel1 = new VerificationSetPanel();
    private final VerificationSetPanel m_verificationSetPanel2 = new VerificationSetPanel();
    private final AccountPopulationEditor m_accountPopulationEditor1 = new AccountPopulationEditor();
    private final AccountPopulationEditor m_accountPopulationEditor2 = new AccountPopulationEditor();
    private final BalanceReportPanel m_balanceReportPanel = new BalanceReportPanel();
    private final ResultReportPanel m_resultReportPanel = new ResultReportPanel();
    private final JMenu m_yearMenu = new JMenu( "År" );
    
    
    public Main( Accounting accounting )
    {
//        SwingPrefs.add( "main", this );
        m_accounting = accounting;
       
        JPanel p = new JPanel( new BorderLayout() );
        add( p );

        JSplitPane spl = new JSplitPane();
        spl.setOneTouchExpandable( true );
//        SwingPrefs.add( "main-split", spl );
        p.add( spl, BorderLayout.CENTER );

        {
            JSplitPane tmp = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
              //SwingPrefs.add( "main-left-split", tmp );
            tmp.setOneTouchExpandable( true );
            tmp.setBorder( BorderFactory.createTitledBorder( "Konton" ) );
            spl.setLeftComponent( tmp );
            tmp.setTopComponent( m_accountPopulationEditor1 );
            tmp.setBottomComponent( m_accountPopulationEditor2 );
        }

        {
            JSplitPane tmp = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
//            SwingPrefs.add( "main-right-split", tmp );
            tmp.setOneTouchExpandable( true );
            tmp.setBorder( BorderFactory.createTitledBorder( "Verifikationer" ) );
            spl.setRightComponent( tmp );
            tmp.setTopComponent( m_verificationSetPanel1 );
            tmp.setBottomComponent( m_verificationSetPanel2 );
        }

        {
            JDialog d = new JDialog( this, "Balans", false );
            d.add( m_balanceReportPanel );
            d.pack();
            m_balanceReportPanel.putClientProperty( "dialog", d );
        }
        
        {
            JDialog d = new JDialog( this, "Resultat", false );
            d.add( m_resultReportPanel );
            d.pack();
            m_resultReportPanel.putClientProperty( "dialog", d );
        }
        
        p.add( createMenuBar(), BorderLayout.NORTH );

        {
            p.add( new JLabel( "Status" ), BorderLayout.SOUTH );
        }

        pack();
    }

    private static boolean load( Accounting a )
    {
        Preferences prefs = Preferences.userRoot().node( "nu/esox/accounting" );
        try
        {
            prefs.sync();

            int i = 0;
            
            while
                ( prefs.nodeExists( "" + i ) )
            {
                Preferences n = prefs.node( "" + i );
                System.err.println( n.absolutePath() + " = " + n.getInt( "year", 0 ) );
                newWindow( a, a.getYears().getYear( n.getInt( "year", 0 ) ), n );
                i++;
            }

            return i > 0;
        }
        catch ( BackingStoreException ex )
        {
            System.err.println( ex );
            return false;
        }
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

            class Xxx extends AbstractAction
            {
                private final JDialog m_dialog;
                
                Xxx( JPanel p )
                {
                    super( ( (JDialog) p.getClientProperty( "dialog" ) ).getTitle() );
                    m_dialog = (JDialog) p.getClientProperty( "dialog" );
                }

                public void actionPerformed( ActionEvent ev ) { m_dialog.setVisible( true ); }
            }

            m.add( new Xxx( m_balanceReportPanel ) );
            m.add( new Xxx( m_resultReportPanel ) );
        }
        
        return mb;
    }

    private void buildYearMenu()
    {
        m_yearMenu.removeAll();

        for ( Year y : m_accounting.getYears() ) m_yearMenu.add( new SelectYearAction( y ) );
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


    private static final String m_title = "LSDK bokföring 1.2.RC1";
    
    private void setYear( Year y )
    {
        if
            ( y == null )
        {
            setTitle( m_title );
            m_balanceReportPanel.setYear( null );
            m_resultReportPanel.setYear( null );
            m_verificationSetPanel1.setYear( null );
            m_verificationSetPanel2.setYear( null );
            m_accountPopulationEditor1.setAccountPopulation( null );
            m_accountPopulationEditor2.setAccountPopulation( null );
            m_closeYearAction.setEnabled( false );
        } else {
            setTitle( m_title + " - " + Integer.toString( y.getNumber() ) );
            m_balanceReportPanel.setYear( y );
            m_resultReportPanel.setYear( y );
            m_verificationSetPanel1.setYear( y );
            m_verificationSetPanel2.setYear( y );
            m_accountPopulationEditor1.setAccountPopulation( y.getAccounts() );
            m_accountPopulationEditor2.setAccountPopulation( y.getAccounts() );
            m_closeYearAction.setEnabled( true );
        }
    }

    
    private Year getYear()
    {
        return m_verificationSetPanel1.getYear();
    }


    
    
    private static void save()
    {
        try
        {
            Preferences prefs = Preferences.userRoot().node( "nu/esox/accounting" );

            int i = 0;
            for
                ( Main f : m_instances )
            {
                Preferences n = prefs.node( "" + i );
                n.putInt( "year", f.getYear().getNumber() );

                System.err.println( n.absolutePath() + " = " + n.getInt( "year", -1 ) );
                save( f, n );
                save( (JDialog) f.m_balanceReportPanel.getClientProperty( "dialog" ), n );
                save( (JDialog) f.m_resultReportPanel.getClientProperty( "dialog" ), n );
                i++;
            }
            
            prefs.node( "" + i ).removeNode();
        
            prefs.flush();
        }
        catch ( BackingStoreException ex )
        {
            System.err.println( ex );
        }
    }
    
    
    private static void save( Component c, Preferences n )
    {
        if      ( c instanceof JDialog )     save( n, (JDialog) c );
        else if ( c instanceof Window )      save( n, (Window) c );
        else if ( c instanceof JSplitPane )  save( n, (JSplitPane) c );
        else if ( c instanceof JScrollBar )  save( n, (JScrollBar) c );
        else if ( c instanceof JTable )      save( n, (JTable) c );

        if
            ( c instanceof Container )
        {
            int i = 0;
            for ( Component ch : ( (Container) c ).getComponents() ) save( ch, n.node( "" + i++ ) );
        }
        
    }
    
    private static void save( Preferences n, JDialog d )
    {
        save( n.node( d.getTitle() ), (Window) d );
    }
    
    private static void save( Preferences n, Window w )
    {
        n.putInt( "x", w.getX() );
        n.putInt( "y", w.getY() );
        n.putInt( "w", w.getWidth() );
        n.putInt( "h", w.getHeight() );
    }
    
    private static void save( Preferences n, JSplitPane c )
    {
        n.putInt( "div", c.getDividerLocation() );
        n.putInt( "ldiv", c.getLastDividerLocation() );
    }
    
    private static void save( Preferences n, JScrollBar c )
    {
        n.putInt( "v", c.getValue() );
    }
    
    private static void save( Preferences n, JTable c )
    {
        for
            ( javax.swing.table.TableColumn tc : Collections.list( c.getColumnModel().getColumns() ) )
        {
            n.node( tc.getIdentifier().toString() ).putInt( "w", tc.getWidth() );
        }
    }




    private static void load( Main f, Preferences n )
    {
        if
            ( n != null )
        {
            load( (Component) f, n );
            load( (JDialog) f.m_balanceReportPanel.getClientProperty( "dialog" ), n );
            load( (JDialog) f.m_resultReportPanel.getClientProperty( "dialog" ), n );
        }

    }

    
    private static void load( Component c, Preferences n )
    {
        if      ( c instanceof JDialog )     load( n, (JDialog) c );
        else if ( c instanceof Window )      load( n, (Window) c );
        else if ( c instanceof JSplitPane )  load( n, (JSplitPane) c );
        else if ( c instanceof JScrollBar )  load( n, (JScrollBar) c );
        else if ( c instanceof JTable )      load( n, (JTable) c );

        if
            ( c instanceof Container )
        {
            int i = 0;
            for ( Component ch : ( (Container) c ).getComponents() ) load( ch, n.node( "" + i++ ) );
        }
    }
    
    private static void load( Preferences n, JDialog d )
    {
        load( n.node( d.getTitle() ), (Window) d );
    }
     
    private static void load( Preferences prefs, Window w )
    {
        System.err.println( prefs.absolutePath() + "   " + w.getClass() );
        w.setBounds( prefs.getInt( "x", w.getX() ),
                     prefs.getInt( "y", w.getY() ),
                     prefs.getInt( "w", w.getWidth() ),
                     prefs.getInt( "h", w.getHeight() ) );
    }
     
    private static void load( Preferences prefs, JSplitPane c )
    {
        int div = prefs.getInt( "div", -1 );
        if ( div != -1 ) c.setDividerLocation( div );
        div = prefs.getInt( "ldiv", -1 );
        if ( div != -1 ) c.setLastDividerLocation( div );
    }
     
    private static void load( Preferences prefs, JScrollBar c )
    {
        c.setValue( prefs.getInt( "v", 0 ) );
    }
    
    private static void load( Preferences n, JTable c )
    {
        for
            ( javax.swing.table.TableColumn tc : Collections.list( c.getColumnModel().getColumns() ) )
        {
            tc.setPreferredWidth( n.node( tc.getIdentifier().toString() ).getInt( "w", tc.getWidth() ) );
        }
    }



    private final Action m_newAction = new AbstractAction( "Nytt fönster" ) { public void actionPerformed( ActionEvent ev ) { newWindow( m_accounting, null, null ); } };
    private final Action m_closeAction = new AbstractAction( "Stäng" ) { public void actionPerformed( ActionEvent ev ) { closeWindow(); } };
    private final Action m_exitAction = new AbstractAction( "Avsluta" ) { public void actionPerformed( ActionEvent ev ) { exit(); } };
    private final Action m_newYearAction = new AbstractAction( "Nytt år" ) { public void actionPerformed( ActionEvent ev ) { newYear(); } };
    private final Action m_closeYearAction = new AbstractAction( "Bokslut" ) { public void actionPerformed( ActionEvent ev ) { closeYear(); } };


    private static LinkedList<Main> m_instances = new LinkedList<Main>();

    
    private static void newWindow( Accounting a, Year y, Preferences n )
    {
        final Main f = new Main( a );
        m_instances.add( f );
        
        f.setYear( y );
        load( f, n );
        f.setVisible( true );
        f.addWindowListener( new WindowAdapter() { public void windowClosing( WindowEvent ev ) { f.closeWindow(); } } );
    }
    
    private void closeWindow()
    {
        if ( m_instances.size() > 1 ) m_instances.remove( this );
        dispose();
        if ( m_instances.size() == 1 ) exit();
    }
    
    private void exit()
    {
        while
            ( ! m_accounting.getYears().isEmpty() )
        {
            Year y = m_accounting.getYears().get( m_accounting.getYears().size() - 1 );
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
            new File( m_databasePath ).renameTo( new File( m_databasePath + "." + new Date() ) );
        }
        catch ( Throwable ex )
        {
            ex.printStackTrace();
        }
        
        try
        {
            PrintStream s = new PrintStream( new FileOutputStream( m_databasePath ) );
            nu.esox.xml.XmlWriter w = new nu.esox.xml.XmlWriter( s, "1.0", /*"ISO-8859-1"*/"", true );
            w.write( m_accounting );
            s.close();
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
        catch ( Throwable ex )
        {
            ex.printStackTrace();
        }
        
        save();
        System.exit( 0 );
    }
    
    
    private void newYear()
    {
        Year y = m_accounting.getYears().get( m_accounting.getYears().size() - 1 );
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
        m_accounting.getYears().get( m_accounting.getYears().size() - 1 ).closeYear();
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
                try
                {
                    new nu.esox.xml.XmlReader( new FileInputStream( m_databasePath ), a, new HashMap() );
                }
                catch ( FileNotFoundException ignore ) {}
                
                if
                    ( a.getYears().isEmpty() )
                {
                    a.getYears().add( new Year( Calendar.getInstance().get( Calendar.YEAR ) ) );
                }

                if
                    ( ! load( a ) )
                {
                    newWindow( a, a.getYears().get( a.getYears().size() - 1 ), null );
                }
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

