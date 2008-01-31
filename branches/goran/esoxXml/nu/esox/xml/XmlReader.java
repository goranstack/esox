package nu.esox.xml;

import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;


public class XmlReader extends DefaultHandler
{
    public interface DeferredCreator
    {
        Object create();
    }
    
    public static boolean TRACE = !true;
    public static boolean TRACE_2 = !true;
    public static boolean TRACE_3 = !true; // text
    private String m_traceIndent = "";
    
    private final InputStream m_stream;
    private final Map m_tag2class;

    private Map m_ids = new HashMap();
    private List m_modelStack = new ArrayList();
    private Object m_root = null;
    private Object m_superModel = null;
    
    
    

    public XmlReader( InputStream s, Map tag2class ) throws SAXException, ParserConfigurationException, IOException
    {
        this( s, null, tag2class );
    }

    public XmlReader( InputStream s, Object superModel, Map tag2class ) throws SAXException, ParserConfigurationException, IOException
    {
        m_stream = s;
        m_tag2class = tag2class;
        m_superModel = superModel;

        SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
        sp.parse( m_stream, this );
    }


    public Object getRoot()
    {
        return m_root;
    }


      // DocumentHandler

    public void startDocument() throws SAXException
    {
        if ( TRACE ) System.err.println( "start" );
    }

    public void endDocument() throws SAXException
    {
        if ( TRACE ) System.err.println( "end" );
    }
    
    public void startElement( String namespaceURI, String localName, String qualifiedName, Attributes attrs ) throws SAXException
    {
        if
            ( TRACE )
        {
            System.err.print( m_traceIndent + "<" + qualifiedName );
            m_traceIndent += "  ";
            int I = attrs.getLength();
            for ( int i = 0; i <I; i++ )
            {
                System.err.print( " " + attrs.getQName( i ) + "=" + attrs.getValue( i ) );
            }
            System.err.println( ">" );
        }

        {  // is it a ref ?
            String idref = attrs.getValue( "idref" );
            if
                ( idref != null )
            {
                Object model = m_ids.get( idref );

                if
                    ( model instanceof DeferredCreator )
                {
                    Object realModel = ( (DeferredCreator) model ).create();
                    if ( realModel == null ) throw new SAXException( "Deferred creator failed: " + model );
                    model = realModel;
                    m_ids.put( idref, model );
                }
                
                if
                    ( model != null )
                {
                    addSubmodel( model );
                    m_modelStack.add( model );
                    m_superModel = model;
                    return;
                } else {
                    throw new SAXException( "Can't resolve reference: " + idref );
                }
            }
        }

        Object model = createBySupermodel( qualifiedName, attrs );
        if ( model == null ) model = createByClass( qualifiedName, attrs );

        if
            ( model == null )
        {
              // exception ?
            System.err.println( "Can't create model" );
            System.err.println( "     tag: " + qualifiedName );
            System.err.println( "   super: " + m_superModel + " " + m_superModel.getClass() );
            m_modelStack.add( null );
            throw new SAXException( "Can't create model for " + qualifiedName );
        } else {
            if ( m_root == null ) m_root = model;
            
            addSubmodel( model );
            m_modelStack.add( model );
            m_superModel = model;

            String id = attrs.getValue( "id" );
            if ( id != null ) m_ids.put( id, model );

//            done( model );
        }
    }

    public void endElement( String namespaceURI, String simpleName, String qualifiedName ) throws SAXException
    {
        if
            ( TRACE )
        {
            m_traceIndent = m_traceIndent.substring( 2 );
            System.err.println( m_traceIndent + "</" + qualifiedName + ">" );
        }

        done( m_superModel );
        
        if
            ( ! m_modelStack.isEmpty() )
        {
            m_modelStack.remove( m_modelStack.size() - 1 );
        }

        if
            ( m_modelStack.isEmpty() )
        {
            m_superModel = null;
        } else {
            if
                ( m_modelStack.get( m_modelStack.size() - 1 ) != null )
            {
                m_superModel = m_modelStack.get( m_modelStack.size() - 1 );
            }
        }
    }
    
    
    public void characters( char buf[], int offset, int len ) throws SAXException
    {
        addText( new String( buf, offset, len ) );
    }







    private Method getAddTextMethod( Class c )
    {
        Method m = null;
        
        if
            ( m_addTextMethods.containsKey( c ) )
        {
            m = (Method) m_addTextMethods.get( c );
        } else {
            while
                ( m == null && c != null )
            {
                try
                {
                    m = c.getDeclaredMethod( "xmlAddText", m_addTextArgTypes );
                }
                catch ( NoSuchMethodException ex )
                {
                }

                c = c.getSuperclass();
            }
            
            m_addTextMethods.put( m_superModel.getClass(), m );
        }

        return m;
    }
    
   

    private static Class [] m_addTextArgTypes = new Class [] { String.class };
    private static Object [] m_addTextArgs = new Object [ 1 ];

    private Map m_addTextMethods = new HashMap(); // [ Class -> Method ]

    
    private void addText( String text ) throws SAXException
    {
        if ( TRACE_3 ) System.err.println( m_traceIndent + "## addText " + text + " to " + m_superModel );
        if ( m_superModel == null ) return;
        
        Method m = getAddTextMethod( m_superModel.getClass() );
            
        if ( TRACE_3 ) System.err.println(  m_traceIndent + "## " + m );
        if
            ( m != null )
        {
            boolean tmp = false;
            try
            {
                m_addTextArgs[ 0 ] = text;
                Boolean accepted = null;

                tmp = m.isAccessible();
                m.setAccessible( true );

                if
                    ( m.getReturnType().equals( boolean.class ) || m.getReturnType().equals( Boolean.class ) )
                {
                    accepted = (Boolean) m.invoke( m_superModel, m_addTextArgs );
                } else {
                    m.invoke( m_superModel, m_addTextArgs );
                }
                
                if
                    ( ( accepted != null ) && ! ( (Boolean) accepted ).booleanValue() )
                {
                    throw new SAXException( "Text [" + text + "] not accepted by [" + m_superModel + "]" );
                }
            }
            catch ( IllegalAccessException ex )
            {
                throw new SAXException( ex );
            }
            catch ( InvocationTargetException ex )
            {
                if ( TRACE_3 ) System.err.println(  m_traceIndent + "## InvocationTargetException" );
                if ( TRACE_3 ) System.err.println(  m_traceIndent + "## " + m_superModel.getClass() );
                if ( TRACE_3 ) System.err.println(  m_traceIndent + "## " + m_superModel );
                if ( TRACE_3 ) System.err.println(  m_traceIndent + "## " + m );
                if ( TRACE_3 ) System.err.println(  m_traceIndent + "## " + text );
                if ( TRACE_3 ) System.err.println(  m_traceIndent + "## " + ex.getTargetException() );
                throw new SAXException( m_superModel.getClass() + "." + m, ex );
            }
            finally
            {
                m.setAccessible( tmp );
            }
        }
    }







   
    private Method getAddSubModelMethod( Class c )
    {
        Method m = null;
        
        if
            ( m_addSubmodelMethods.containsKey( c ) )
        {
            m = (Method) m_addSubmodelMethods.get( c );
        } else {
            while
                ( m == null && c != null )
            {
                try
                {
                    m = c.getDeclaredMethod( "xmlAddSubmodel", m_addSubmodelArgTypes );
                }
                catch ( NoSuchMethodException ex )
                {
                }

                c = c.getSuperclass();
            }
            
            m_addSubmodelMethods.put( m_superModel.getClass(), m );
        }

        return m;
    }
    

    private static Class [] m_addSubmodelArgTypes = new Class [] { Object.class };
    private static Object [] m_addSubmodelArgs = new Object [ 1 ];

    private Map m_addSubmodelMethods = new HashMap(); // [ Class -> Method ]

    
    private void addSubmodel( Object model ) throws SAXException
    {
        if ( TRACE_2 ) System.err.println( m_traceIndent + "## addSubmodel " + model + " to " + m_superModel );
        if ( m_superModel == null ) return;
        
        Method m = getAddSubModelMethod( m_superModel.getClass() );
            
        if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + m );
        if
            ( m != null )
        {
            boolean tmp = false;
            try
            {
                m_addSubmodelArgs[ 0 ] = model;
                Boolean accepted = null;

                tmp = m.isAccessible();
                m.setAccessible( true );

                if
                    ( m.getReturnType().equals( boolean.class ) || m.getReturnType().equals( Boolean.class ) )
                {
                    accepted = (Boolean) m.invoke( m_superModel, m_addSubmodelArgs );
                } else {
                    m.invoke( m_superModel, m_addSubmodelArgs );
                }
                
                if
                    ( ( accepted != null ) && ! ( (Boolean) accepted ).booleanValue() )
                {
                    throw new SAXException( "Submodel [" + model + "] not accepted by [" + m_superModel + "]" );
                }
            }
            catch ( IllegalAccessException ex )
            {
                throw new SAXException( ex );
            }
            catch ( InvocationTargetException ex )
            {
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## InvocationTargetException" );
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + m_superModel.getClass() );
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + m_superModel );
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + m );
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + model.getClass() );
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + model );
                if ( TRACE_2 ) System.err.println(  m_traceIndent + "## " + ex.getTargetException() );
                throw new SAXException( m_superModel.getClass() + "." + m, ex );
            }
            finally
            {
                m.setAccessible( tmp );
            }
        }
    }









    private static class Key
    {
        private Class m_class;
        private String m_tag;
        
        Key()
        {
            this( null, null );
        }
        
        Key( Class c, String tag )
        {
            set( c, tag );
        }
        
        public void set( Class c, String tag )
        {
            m_class = c;
            m_tag = tag;
        }
        
        public boolean equals( Object o )
        {
            return ( o == this ) || ( ( o instanceof Key ) && m_class.equals( ( (Key) o ).m_class ) && m_tag.equals( ( (Key) o ).m_tag ) );
        }
        
        public int hashCode()
        {
            return m_class.hashCode() + m_tag.hashCode();
        }
    }

    private static final Key m_key = new Key();
    


    
    private Method getCreateMethod( Class c, String tag )
    {
        m_key.set( c, tag );
        
        Method m = null;
        if
            ( m_createMethods.containsKey( m_key ) )
        {
            m = (Method) m_createMethods.get( m_key );
        } else {

            Class C = c;
            while
                ( m == null && C != null )
            {
                try
                {
                    m = C.getDeclaredMethod( "xmlCreate_" + tag.replace( '-', '_' ), m_createArgTypes );
                }
                catch ( NoSuchMethodException ex )
                {
                }
                
                C = C.getSuperclass();
            }

            m_createMethods.put( new Key( c, tag ), m );
        }

        return m;
    }


    private static Class [] m_createArgTypes = new Class [] { Object.class, Attributes.class };
    private static Object [] m_createArgs = new Object [ 2 ];


    private Map m_createMethods = new HashMap(); // [ Key -> Method ] ]

    
    private Object createBySupermodel( String tag, Attributes attrs ) throws SAXException
    {
        if ( m_superModel == null ) return null;
        
        Method m = getCreateMethod( m_superModel.getClass(), tag );

        if ( TRACE_2 ) System.err.println( m_traceIndent + "## createBySupermodel( " + tag + ") method = " + m );
            
        if
            ( m != null )
        {
            boolean tmp = false;
            try
            {
                m_createArgs[ 0 ] = m_superModel;
                m_createArgs[ 1 ] = attrs;

                tmp = m.isAccessible();
                m.setAccessible( true );
                Object model = m.invoke( m_superModel, m_createArgs );

                if ( TRACE_2 ) System.err.println( m_traceIndent + "## createBySupermodel -> " + model );
                return model;
            }
            catch ( IllegalAccessException ex )
            {
                throw new SAXException( ex );
            }
            catch ( InvocationTargetException ex )
            {
                if ( ex.getCause() instanceof Exception ) throw new SAXException( (Exception) ex.getCause() );
                if ( ex.getCause() instanceof Error ) throw (Error) ex.getCause();
                if ( ex.getCause() instanceof RuntimeException ) throw (RuntimeException) ex.getCause();
                throw new SAXException( ex );
            }
            finally
            {
                m.setAccessible( tmp );
            }
        }

        return null;
    }



    private Object createByClass( String tag, Attributes attrs ) throws SAXException
    {
        Class c = (Class) m_tag2class.get( tag );

        if ( TRACE_2 ) System.err.println( m_traceIndent + "## createByClass( " + tag + ") class = " + c );

        if ( c == null ) return null;

        Method m = getCreateMethod( c, tag );

        if ( TRACE_2 ) System.err.println( m_traceIndent + "## createByClass( " + tag + ") method = " + m );

        if
            ( m != null )
        {
            boolean tmp = false;
            try
            {
                m_createArgs[ 0 ] = m_superModel;
                m_createArgs[ 1 ] = attrs;

                tmp = m.isAccessible();
                m.setAccessible( true );
                Object model = m.invoke( null, m_createArgs );

                if ( TRACE_2 ) System.err.println( m_traceIndent + "##  -> createByClass" + model );
                return model;
            }
            catch ( IllegalAccessException ex )
            {
                throw new SAXException( ex );
            }
            catch ( InvocationTargetException ex )
            {
                if ( ex.getCause() instanceof Exception ) throw new SAXException( (Exception) ex.getCause() );
                if ( ex.getCause() instanceof Error ) throw (Error) ex.getCause();
                if ( ex.getCause() instanceof RuntimeException ) throw (RuntimeException) ex.getCause();
                throw new SAXException( ex );
            }
            finally
            {
                m.setAccessible( tmp );
            }
        }

        return null;
    }
    








    private Method getDoneMethod( Class c )
    {
        Method m = null;
        
        if
            ( m_doneMethods.containsKey( c ) )
        {
            m = (Method) m_doneMethods.get( c );
        } else {
            while
                ( m == null && c != null )
            {
                try
                {
                    m = c.getDeclaredMethod( "xmlDone", m_doneArgTypes );
                }
                catch ( NoSuchMethodException ex )
                {
                }

                c = c.getSuperclass();
            }
            
            m_doneMethods.put( m_superModel.getClass(), m );
        }

        return m;
    }
    

    private static Class [] m_doneArgTypes = new Class [] {};
    private static Object [] m_doneArgs = new Object [ 0 ];

    private Map m_doneMethods = new HashMap(); // [ Class -> Method ]


    
    private void done( Object model ) throws SAXException
    {
        if ( model == null ) return;
        
        Method m = getDoneMethod( model.getClass() );
            
        if
            ( m != null )
        {
            boolean tmp = false;
            try
            {
                tmp = m.isAccessible();
                m.setAccessible( true );
                m.invoke( model, m_doneArgs );
            }
            catch ( IllegalAccessException ex )
            {
                throw new SAXException( ex );
            }
            catch ( InvocationTargetException ex )
            {
                if ( ex.getCause() instanceof Exception ) throw new SAXException( (Exception) ex.getCause() );
                if ( ex.getCause() instanceof Error ) throw (Error) ex.getCause();
                if ( ex.getCause() instanceof RuntimeException ) throw (RuntimeException) ex.getCause();
                throw new SAXException( ex );
            }
            finally
            {
                m.setAccessible( tmp );
            }
        }
    }





















    public static String xml2String( Attributes as, String tag )
    {
        return xml2String( as, tag, null );
    }

    public static String xml2String( Attributes as, String tag, String nullValue )
    {
        String tmp = as.getValue( tag );
        return ( tmp == null ) ? nullValue : tmp;
    }

    public static Date xml2Date( Attributes as, String tag, Date nullValue, Date errorValue, DateFormat df )
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return df.parse( tmp );
        }
        catch ( ParseException ex )
        {
            return errorValue;
        }

    }

    public static Date xml2Date( Attributes as, String tag, Date nullValue, DateFormat df ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        return df.parse( tmp );
    }


    public static short xml2Short( Attributes as, String tag, short nullValue, short errorValue )
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Short.parseShort( tmp );
        }
        catch ( NumberFormatException ex )
        {
            return errorValue;
        }

    }
    
    public static short xml2Short( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        try
        {
            return Short.parseShort( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    public static short xml2Short( Attributes as, String tag, short nullValue ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Short.parseShort( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }


    public static int xml2Integer( Attributes as, String tag, int nullValue, int errorValue )
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Integer.parseInt( tmp );
        }
        catch ( NumberFormatException ex )
        {
            return errorValue;
        }

    }
    
    public static int xml2Integer( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        try
        {
            return Integer.parseInt( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    public static int xml2Integer( Attributes as, String tag, int nullValue ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Integer.parseInt( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    
    public static byte xml2Byte( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        try
        {
            return Byte.parseByte( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    public static byte xml2Byte( Attributes as, String tag, byte nullValue ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Byte.parseByte( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    
    public static long xml2Long( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        try
        {
            return Long.parseLong( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    public static long xml2Long( Attributes as, String tag, long nullValue ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Long.parseLong( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }


    
    public static float xml2Float( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        try
        {
            return Float.parseFloat( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    public static float xml2Float( Attributes as, String tag, float nullValue ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Float.parseFloat( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }


    
    public static double xml2Double( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        try
        {
            return Double.parseDouble( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }

    public static double xml2Double( Attributes as, String tag, double nullValue ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        try
        {
            return Double.parseDouble( tmp );
        }
        catch ( NumberFormatException ex )
        {
            throw new ParseException( tmp, 0 );
        }
    }


    
    public static boolean xml2Boolean( Attributes as, String tag, boolean nullValue )
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) return nullValue;
        return Boolean.valueOf( tmp ).booleanValue();
    }

    public static boolean xml2Boolean( Attributes as, String tag ) throws ParseException
    {
        String tmp = as.getValue( tag );
        if ( tmp == null ) throw new ParseException( tmp, 0 );
        return Boolean.valueOf( tmp ).booleanValue();
    }
     
}
