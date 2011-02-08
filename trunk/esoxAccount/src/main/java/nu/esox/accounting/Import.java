package nu.esox.accounting;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import nu.esox.util.*;


public class Import
{
    @SuppressWarnings( "serial" )
    private static final class EndOfFile extends IOException {};

    
    public static void importFile( String filename, Accounting a ) throws IOException, java.text.ParseException
    {
        BufferedReader r = new BufferedReader( new InputStreamReader( new FileInputStream( filename ), java.nio.charset.Charset.forName( "windows-1252" ) ) );
        
        
        String line = r.readLine();
        while
            ( line != null )
        {
            line = line.trim();
            if
                ( line.startsWith( "Verifikationslista" ) )

            {
                System.err.println( "Ver: " + filename );
                importVerifications( r, a );
                break;
            } else if
                  ( line.startsWith( "Balansrapport" ) )
            {
                System.err.println( "Bal: " + filename );
                importBalance( r, a );
                break;
            } else if
                  ( line.startsWith( "Resultatrapport" ) )
            {
                System.err.println( "Res: " + filename );
                importResult( r, a );
                break;
            }

            line = r.readLine();
        }

        r.close();
    }

    
    public static void importResult( BufferedReader r, Accounting a ) throws IOException, java.text.ParseException
    {
        Year year = null;
        Account.Type type = Account.TYPE_UNDEFINED;
        
        String line = r.readLine();
        while
            ( line != null )
        {
            line = line.trim();
            if
                ( line.startsWith( "Perioden" ) )
            {
                int yn = Integer.parseInt( line.substring( 9, 13 ) );
                year = a.getYears().getYear( yn );
                if
                    ( year == null )
                {
                    year = new Year( yn );
                    a.getYears().add( year );
                }
                line = r.readLine();
                break;
            }

            line = r.readLine();
        }
        
        while
            ( line != null )
        {
            try
            {
                int nr = Integer.parseInt( line.substring( 1, 5 ) );

                Account acc = year.getAccounts().getAccount( nr );
                if
                    ( acc == null )
                {
                    acc = new Account( nr );
                    year.getAccounts().add( acc );
                }
                acc.setName( line.substring( 7, 32 ).trim() );
                acc.setType( type );
            }
            catch ( NumberFormatException ex )
            {
                line = line.trim();
                for
                    ( Account.Type t : Account.TYPES )
                {
                    if ( t.toString().equals( line ) ) type = t;
                }
            }
            catch ( StringIndexOutOfBoundsException ex ) {}
            
            line = r.readLine();
        }
    }

    


    
    public static void importBalance( BufferedReader r, Accounting a ) throws IOException, java.text.ParseException
    {
        Year year = null;
        Account.Type type = Account.TYPE_UNDEFINED;
        
        String line = r.readLine();
        while
            ( line != null )
        {
            line = line.trim();
            if
                ( line.startsWith( "Perioden" ) )
            {
                int yn = Integer.parseInt( line.substring( 9, 13 ) );
                year = a.getYears().getYear( yn );
                if
                    ( year == null )
                {
                    year = new Year( yn );
                    a.getYears().add( year );
                }
                line = r.readLine();
                break;
            }

            line = r.readLine();
        }
        
        while
            ( line != null )
        {
            try
            {
                int nr = Integer.parseInt( line.substring( 1, 5 ) );

                Account acc = year.getAccounts().getAccount( nr );
                if
                    ( acc == null )
                {
                    acc = new Account( nr );
                    year.getAccounts().add( acc );
                }
                
                acc.setName( line.substring( 7, 31 ).trim() );
                acc.setType( type );

                try
                {
                    acc.setIb( Double.parseDouble( line.substring( 31, 45 ).replaceAll( "[^0-9-,]", "" ).replaceAll( ",", "." ) ) );
                }
                catch ( NumberFormatException ex )
                {
                }
            }
            catch ( NumberFormatException ex )
            {
                line = line.trim();
                for
                    ( Account.Type t : Account.TYPES )
                {
                    if ( t.toString().equals( line ) ) type = t;
                }
            }
            catch ( StringIndexOutOfBoundsException ex ) {}
            
            line = r.readLine();
        }
    }

    
    
    public static void importVerifications( BufferedReader r, Accounting ac ) throws IOException, java.text.ParseException
    {
        try
        {
            while
                ( true )
            {
                String line = nextLine( r );
                
                if
                    ( line.matches( "^ [0-9]+ +[0-9]{2}-[0-9]{2}-[0-9]{2}.*" ) )
                {
                    readVerification( r, line, ac );
                }
            }
        }
        catch ( EndOfFile endOfFile )
        {
        }
    }



    
    private static String nextLine( BufferedReader reader ) throws IOException
    {
        String line = reader.readLine();
        if ( line == null ) throw new EndOfFile();
        return line;
    }

    private static void readVerification( BufferedReader reader, String line, Accounting ac ) throws IOException, java.text.ParseException
    {
        Date date = Constants.DATE_FORMAT.parse( "20" + line.substring( 16, 24 ) );
        
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        Year y = ac.getYears().getYear( cal.get( Calendar.YEAR ) );
        if
            ( y == null )
        {
            y = new Year( cal.get( Calendar.YEAR ) );
            ac.getYears().add( y );
        }
        
        VerificationSet vs = y.getVerifications();
        Verification v = vs.create();
        v.setName( ( line.length() > 42 ) ? line.substring( 43 ).trim() : "" );
        v.setDate( date );
        
        
        line = reader.readLine();
        while
            ( line.length() > 0 )
        {
            if
                ( line.length() > 43 && Character.isDigit( line.charAt( 43 ) ) )
            {
                String text = line.substring( 0, 42 ).trim();
                String account = line.substring( 43, 47 ).trim();
                String value = line.substring( 47, 63 ).trim().replaceAll( "[^0-9,-]", "" );

                if
                    ( value.length() == 0 )
                {
                    value = line.substring( 63 ).trim().replaceAll( "[^0-9,-]", "" );
                    if
                        ( value.charAt( 0 ) == '-' )
                    {
                        value = value.replaceAll( "-", "" );
                    } else {
                        value = "-" + value;
                        System.err.println( value );
                    }
                    value = ( "-" + value ).replace( "--", "" );
                } else {
                }

                Transaction t = new Transaction( v );
                t.setAccount( v.getAccounts().getAccount( Integer.parseInt( account ) ) );
                t.setDescription( text );
                t.setAmount( Double.parseDouble( value.replaceAll( ",", "." ) ) );
                v.getTransactions().add( t );
            }
            
            line = reader.readLine();
        }
    }

    

    
    
    
    public static void main( String [] args ) throws Exception
    {
        String databasePath = args[ 0 ];
        Accounting a = new Accounting();
        new nu.esox.xml.XmlReader( new FileInputStream( databasePath ), a, new HashMap() );

        boolean skip = true;
        for
            ( String filename : args )
        {
            if
                ( skip )
            {
                skip = false;
                continue;
            }
            
            importFile( filename, a );
        }
//         Import.importResult( "xxx/res2005.txt", a );
//         Import.importBalance( "xxx/bal2005.txt", a );
//         Import.importVerifications( "xxx/ver2005.txt", a );
        
        try
        {
            PrintStream s = new PrintStream( new FileOutputStream( databasePath ) );
            nu.esox.xml.XmlWriter w = new nu.esox.xml.XmlWriter( s, "1.0", /*"ISO-8859-1"*/"", true );
            w.write( a );
            s.close();
        }
        catch ( IOException ex ) {}
    }
}
 




