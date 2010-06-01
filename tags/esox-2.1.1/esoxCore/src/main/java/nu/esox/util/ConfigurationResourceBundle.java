package nu.esox.util;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;


public class ConfigurationResourceBundle extends ResourceBundle
{
    private final Class m_class;
    private final String m_fileName;
    private final String m_propertyNameSuffix;

    private ResourceBundle m_propertyResourceBundle;

    
    public ConfigurationResourceBundle( Class c )
    {
        this( c, "configuration.properties" );
    }
    
    public ConfigurationResourceBundle( Class c, String fileName )
    {
        this( c, fileName, ".config.file" );
    }
    
    public ConfigurationResourceBundle( Class c, String fileName, String propertyNameSuffix )
    {
        m_class = c;
        m_fileName = fileName;
        m_propertyNameSuffix = propertyNameSuffix;
        
        reread();
    }

    public void setParent( ConfigurationResourceBundle config )
    {
        super.setParent( config );
    }
    
    public void reread()
    {
        String systemPropertyName = null;

        if
            ( m_class.getPackage() != null )
        {
            systemPropertyName = m_class.getPackage().getName() + m_propertyNameSuffix;
        } else {
            String tmp = m_class.getName();
            systemPropertyName = tmp.substring( 0, tmp.lastIndexOf( "." ) ) + m_propertyNameSuffix;
        }
        
        String fn = System.getProperty( systemPropertyName, m_fileName );

        try
        {
            m_propertyResourceBundle = new PropertyResourceBundle( m_class.getResourceAsStream( fn ) );
        }
        catch ( Exception ex )
        {
            try
            {
                m_propertyResourceBundle = new PropertyResourceBundle( new FileInputStream( fn ) );
            }
            catch ( Exception ex2 )
            {
                Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Can't find configuration file for " + m_class + ": " + fn );
                throw new RuntimeException( "Can't find configuration file for " + m_class + ": " + fn );
            }
        }
    }

    
    public Enumeration<String> getKeys()
    {
        return m_propertyResourceBundle.getKeys();
    }

    protected Object handleGetObject( String key )
    {
        return m_propertyResourceBundle.getObject( key );
    }





    public Font getFontProperty( String propertyName, Font base )
    {
        String name = getStringProperty( propertyName + "-name", base.getName() );
        int style = getIntProperty( propertyName + "-style", base.getStyle() );
        int size = getIntProperty( propertyName + "-size", base.getSize() );

        return new Font( name, style, size );
    }

    public Font getFontProperty( String propertyName )
    {
        String name = getStringProperty( propertyName + "-name" );
        int style = getIntProperty( propertyName + "-style" );
        int size = getIntProperty( propertyName + "-size" );

        return new Font( name, style, size );
    }

    public Color getColorProperty( String propertyName, Color fallback )
    {
        try
        {
            String tmp = getString( propertyName );
        }
        catch ( MissingResourceException ex )
        {
            return fallback;
        }
        
        return parseColor( propertyName, getStringProperty( propertyName ) );
    }

    public Color getColorProperty( String propertyName )
    {
        return parseColor( propertyName, getStringProperty( propertyName ) );
    }

    private Color parseColor( String propertyName, String tmp )
    {
        if ( tmp.length() == 0 ) return null;
        
        int r = 0, g = 0, b = 0;
        
        StringTokenizer t = new StringTokenizer( tmp, ",", false );
        if
            ( t.hasMoreTokens() )
        {
            try
            {
                r = Integer.parseInt( t.nextToken() );
            }
            catch ( NumberFormatException ex )
            {
                Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
                throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            }
        }
        
        if
            ( t.hasMoreTokens() )
        {
            try
            {
                g = Integer.parseInt( t.nextToken() );
            }
            catch ( NumberFormatException ex )
            {
                Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
                throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            }
        }
        
        if
            ( t.hasMoreTokens() )
        {
            try
            {
                b = Integer.parseInt( t.nextToken() );
            }
            catch ( NumberFormatException ex )
            {
                Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
                throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            }
        }

        return new Color( r, g, b );
    }




    
    public int getIntProperty( String propertyName, int fallback )
    {
        try
        {
             String tmp = getString( propertyName );
             try
             {
                 if
                     ( tmp.startsWith( "0x" ) )
                 {
                     return Integer.parseInt( tmp.substring( 2 ), 16 );
                 } else {
                     return Integer.parseInt( tmp );
                 }
             }
             catch ( NumberFormatException ex )
             {
                 Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
                 throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
             }
        }
        catch ( MissingResourceException ex )
        {
            return fallback;
        }
    }

    public int getIntProperty( String propertyName )
    {
        String tmp = getStringProperty( propertyName );
        
        try
        {
            if
                ( tmp.startsWith( "0x" ) )
            {
                return Integer.parseInt( tmp.substring( 2 ), 16 );
            } else {
                return Integer.parseInt( tmp );
            }
        }
        catch ( NumberFormatException ex )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
        }
    }

    public short getShortProperty( String propertyName )
    {
        String tmp = getStringProperty( propertyName );
        
        try
        {
            if
                ( tmp.startsWith( "0x" ) )
            {
                return Short.parseShort( tmp.substring( 2 ), 16 );
            } else {
                return Short.parseShort( tmp );
            }
        }
        catch ( NumberFormatException ex )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
        }
    }

    public long getLongProperty( String propertyName )
    {
        String tmp = getStringProperty( propertyName );
        
        try
        {
            if
                ( tmp.startsWith( "0x" ) )
            {
                return Long.parseLong( tmp.substring( 2 ), 16 );
            } else {
                return Long.parseLong( tmp );
            }
        }
        catch ( NumberFormatException ex )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
        }
    }






    
    public boolean getBooleanProperty( String propertyName, boolean fallback )
    {
        try
        {
            return Boolean.valueOf( getStringProperty( propertyName ) ).booleanValue();
        }
        catch ( MissingResourceException ex )
        {
            return fallback;
        }
    }

    public boolean getBooleanProperty( String propertyName )
    {
        return Boolean.valueOf( getStringProperty( propertyName ) ).booleanValue();
    }

    public float getFloatProperty( String propertyName )
    {
        String tmp = getStringProperty( propertyName );
        
        try
        {
            return Float.parseFloat( tmp );
        }
        catch ( NumberFormatException ex )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
        }
    }

    public double getDoubleProperty( String propertyName )
    {
        String tmp = getStringProperty( propertyName );
        
        try
        {
            return Double.parseDouble( tmp );
        }
        catch ( NumberFormatException ex )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
        }
    }

    public double getDoubleProperty( String propertyName, double fallback )
    {
        try
        {
            return getDoubleProperty( propertyName );
        }
        catch ( MissingResourceException ex )
        {
            return fallback;
        }
    }

   public String getStringProperty( String propertyName )
    {
        String tmp = getString( propertyName );
        if
            ( tmp == null )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, " + propertyName + " not defined" );
            throw new RuntimeException( "Configuration error, " + propertyName + " not defined" );
        } else {
            return tmp;
        }
    }

    public String getStringProperty( String propertyName, String fallback )
    {
        try
        {
            return getString( propertyName );
        }
        catch ( MissingResourceException ex )
        {
            return fallback;
        }
    }
    
    public long getTimeProperty( String propertyName, long fallback ) // milleseconds
    {
        try
        {
            return getTimeProperty( propertyName );
        }
        catch ( MissingResourceException ex )
        {
            return fallback;
        }
    }
    
    public long getTimeProperty( String propertyName ) // milleseconds
    {
        String tmp = getString( propertyName );

        int n = tmp.indexOf( "ms" );
        if ( n == -1 ) n = tmp.indexOf( "s" );
        if ( n == -1 ) n = tmp.indexOf( "m" );
        if ( n == -1 ) n = tmp.indexOf( "h" );

        long t = 0;
        String unit = null;
            
        try
        {
            if
                ( n == -1 )
            {
                t = Long.parseLong( tmp );
            } else {
                t = Long.parseLong( tmp.substring( 0, n ) );
                unit = tmp.substring( n );
            }
        }
        catch ( NumberFormatException ex )
        {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse value for " + propertyName + ":" + tmp );
        }
        
        if      ( unit == null )        ;
        else if ( unit.equals( "ms" ) ) ;
        else if ( unit.equals( "s" ) )  t = t * 1000;
        else if ( unit.equals( "m" ) )  t = t * 1000 * 60;
        else if ( unit.equals( "h" ) )  t = t * 1000 * 60 * 60;
        else {
            Logger.getLogger( Logger.GLOBAL_LOGGER_NAME ).severe( "Configuration error, can't parse unit for " + propertyName + ":" + tmp );
            throw new RuntimeException( "Configuration error, can't parse unit for " + propertyName + ":" + tmp );
        }
        
        return t;
    }

/*
    public static void main( String [] args )
    {
        String tmp = "12m";
        
        try
        {
            int n = tmp.indexOf( "ms" );
            if ( n == -1 ) n = tmp.indexOf( "s" );
            if ( n == -1 ) n = tmp.indexOf( "m" );
            if ( n == -1 ) n = tmp.indexOf( "h" );
            int i = Integer.parseInt( tmp.substring( 0, n ) );

            System.err.println( i );
            System.err.println( tmp.substring( n ) );
        }
        catch ( NumberFormatException ex )
        {
            System.err.println( "Configuration error, can't parse " + tmp );
        }
 
    }

*/
}
