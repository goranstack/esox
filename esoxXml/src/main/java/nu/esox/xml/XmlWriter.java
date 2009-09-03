package nu.esox.xml;


import java.io.*;
import java.util.*;


public class XmlWriter
{
    private final PrintStream m_stream;
    private Map<Writeable,Integer> m_ids = new IdentityHashMap<Writeable,Integer>();
    private String m_indent = "";
    private String m_pending = null;
    
    public static interface Writeable 
    {
        String xmlGetTag();
        void xmlWriteAttributes( XmlWriter w );
        void xmlWriteSubmodels( XmlWriter w );
    }

    public static interface UnsharedWriteable extends Writeable 
    {
    }



    public XmlWriter( PrintStream s, String version, String encoding, boolean standAlone )
    {
        m_stream = s;

        if ( version == null ) version = "1.0";
        if ( encoding == null ) encoding = "ISO-8859-1";

        m_stream.print( "<?xml version=\"" );
        m_stream.print( version );
        m_stream.print( "\" encoding=\"" );
        m_stream.print( encoding );
        m_stream.print( "\"" );
        if ( standAlone ) m_stream.print( " standalone=\"yes\"" );
        m_stream.println( "?>" );
    }

    
    public PrintStream getPrintStream()
    {
        return m_stream;
    }
    
    public void write( Collection c, int skipLines )
    {
        boolean first = true;
        
        Iterator i = c.iterator();
        while
            ( i.hasNext() )
        {
            if ( ! first ) skip( skipLines );  
            write( (Writeable) i.next() );
            first = false;         
        }
    }

    public void write( String str )
    {
        flush( true );
        m_stream.println( m_indent + str );
    }

    public void comment( String comment )
    {
        flush( true );
        m_stream.println( m_indent + "<!-- " + comment + " -->" );
    }

    public void skip( int lines )
    {
        flush( true );
        while ( lines-- > 0 ) m_stream.println();
    }

    private void writeString( String value )
    {
        flush( false );
        
        int I = value.length();

        for
            ( int i = 0; i < I; i++ )
        {
            char c = value.charAt( i );

            if      ( c == '"' )  m_stream.print( "&quot;" );
            else if ( c == '&' )  m_stream.print( "&amp;" );
            else if ( c == '<' )  m_stream.print( "&lt;" );
            else if ( c == '>' )  m_stream.print( "&gt;" );
            else if ( c == '\'' ) m_stream.print( "&apos;" );
            else if ( c == '\n' ) m_stream.print( "&#10;" );
            else if ( c == '\r' ) m_stream.print( "&#13;" );
            else if ( c == '\t' ) m_stream.print( "&#9;" );
            else                  m_stream.print( c );
        }
    }

    public void write( String propertyName, String value )
    {
        if ( value == null ) return;
        m_stream.print( " " + propertyName + "=\"" );
        writeString( value );
        m_stream.print( "\"" );
    }

    public void write( String propertyName, boolean value )
    {
        write( propertyName, Boolean.toString( value ) );
    }

    public void write( String propertyName, long value )
    {
        write( propertyName, Long.toString( value ) );
    }

    public void write( String propertyName, int value )
    {
        write( propertyName, Integer.toString( value ) );
    }

    public void write( String propertyName, float value )
    {
        write( propertyName, Float.toString( value ) );
    }

    public void write( String propertyName, double value )
    {
        write( propertyName, Double.toString( value ) );
    }






    public void write( Writeable w )
    {
        if ( w == null ) return;

        flush( true );
        
        String tag = w.xmlGetTag();

        Integer id = m_ids.get( w );

        if
            ( id != null )
        {
            m_stream.print( m_indent + "<" + tag );
            write( "idref", id.intValue() );
            m_stream.println( "/>" );
        } else {
            m_stream.print( m_indent + "<" + tag );

            if
                ( ! ( w instanceof UnsharedWriteable ) )
            {
                id = m_ids.size();
                m_ids.put( w, id );
                write( "id", id.intValue() );
            }

            w.xmlWriteAttributes( this );
            m_pending = ">";

            String tmp = m_indent;
            m_indent += "  ";

            w.xmlWriteSubmodels( this );

            m_indent = tmp;

            if
                ( m_pending == null )
            {
                m_stream.println( m_indent + "</" + tag + ">" );
            } else {
                m_stream.println( "/>" );
                m_pending = null;
            }
        }
    }


    private void flush( boolean newline )
    {
        if
            ( m_pending != null )
        {
            m_stream.print( m_pending );
            if ( newline ) m_stream.println();
            m_pending = null;
        }
    }
}
