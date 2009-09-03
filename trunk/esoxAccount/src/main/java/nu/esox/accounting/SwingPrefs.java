package nu.esox.accounting;

import java.util.prefs.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;


public class SwingPrefs
{
    private static final Map<String, List<Component>> m_components = new HashMap<String, List<Component>>();


    public static void add( String name, Component c )
    {
        List<Component> l = m_components.get( name );
        if
            ( l == null )
        {
            l = new ArrayList<Component>();
            m_components.put( name, l );
        }
        
        l.add( c );
    }


    public static void collect( Preferences prefs )
    {
        for
            ( Map.Entry<String, List<Component>> e : m_components.entrySet() )
        {
            String name = e.getKey();
            List<Component> l = e.getValue();

            Component c = l.get( 0 );

            if       ( c instanceof Window ) collect( name, (Window) c, prefs );
            else if ( c instanceof JInternalFrame ) collect( name, (JInternalFrame) c, prefs );
            else if ( c instanceof JList )          collect( name, (JList) c, prefs );
            else if ( c instanceof JTable )         collect( name, (JTable) c, prefs );
            else if ( c instanceof JSplitPane )     collect( name, (JSplitPane) c, prefs );
        }
    }


    public static void apply( Preferences prefs )
    {
        for
            ( Map.Entry<String, List<Component>> e : m_components.entrySet() )
        {
            String name = e.getKey();
            List<Component> l = e.getValue();

            for
                ( Component c : l )
            {
                if       ( c instanceof Window )         apply( name, (Window) c, prefs );
                else if ( c instanceof JInternalFrame ) apply( name, (JInternalFrame) c, prefs );
                else if ( c instanceof JList )          apply( name, (JList) c, prefs );
                else if ( c instanceof JTable )         apply( name, (JTable) c, prefs );
                else if ( c instanceof JSplitPane )     apply( name, (JSplitPane) c, prefs );
            }
        }
    }


    
//     public static void applyPrefs( Component C, Preferences prefs )
//     {
//         if
//             ( C.getName() != null )
//         {
//             System.err.println( C.getName() );
//             if       ( C instanceof JInternalFrame ) apply( C.getName(), (JInternalFrame) C, prefs );
//             else if ( C instanceof JList )          apply( C.getName(), (JList) C, prefs );
//             else if ( C instanceof JTable )         apply( C.getName(), (JTable) C, prefs );
//         }

//         if
//             ( C instanceof Container )
//         {
//             for ( Component c : ( (Container) C ).getComponents() ) applyPrefs( c, prefs );
//         }
//     }


//     public static void applyPrefs( Window W, Preferences prefs )
//     {
//         if
//             ( W.getName() != null )
//         {
//             System.err.println( W.getName() );
          
//             applyBounds( W.getName(), W, prefs );
//         }

//         for ( Component c : W.getComponents() ) applyPrefs( c, prefs );
//         for ( Window w : W.getOwnedWindows() ) applyPrefs( w, prefs );
//     }


    
//     public static void collectPrefs( Component C, Preferences prefs )
//     {
//         if
//             ( C.getName() != null )
//         {
//             if       ( C instanceof JInternalFrame ) collect( C.getName(), (JInternalFrame) C, prefs );
//             else if ( C instanceof JList )          collect( C.getName(), (JList) C, prefs );
//             else if ( C instanceof JTable )         collect( C.getName(), (JTable) C, prefs );
//         }
        
//         if
//             ( C instanceof Container )
//         {
//             for ( Component c : ( (Container) C ).getComponents() ) collectPrefs( c, prefs );
//         }
//     }


//     public static void collectPrefs( Window W, Preferences prefs )
//     {
//         if
//             ( W.getName() != null )
//         {
//             collectBounds( W.getName(), W, prefs );
//         }

//         for ( Component c : W.getComponents() ) collectPrefs( c, prefs );
//         for ( Window w : W.getOwnedWindows() ) collectPrefs( w, prefs );
//     }



    
    private static void collectBounds( String name, Component c, Preferences prefs )
    {
        prefs.put( name + ".x", Integer.toString( c.getX() ) );
        prefs.put( name + ".y", Integer.toString( c.getY() ) );
        prefs.put( name + ".w", Integer.toString( c.getWidth() ) );
        prefs.put( name + ".h", Integer.toString( c.getHeight() ) );
    }

    private static void applyBounds( String name, Component c, Preferences prefs )
    {
        c.setBounds( Integer.parseInt( prefs.get( name + ".x", Integer.toString( c.getX() ) ) ),
                     Integer.parseInt( prefs.get( name + ".y", Integer.toString( c.getY() ) ) ),
                     Integer.parseInt( prefs.get( name + ".w", Integer.toString( c.getWidth() ) ) ),
                     Integer.parseInt( prefs.get( name + ".h", Integer.toString( c.getHeight() ) ) ) );
    }



    private static void collect( String name, Window w, Preferences prefs )
    {
        collectBounds( name, w, prefs );
    }

    private static void apply( String name, Window w, Preferences prefs )
    {
        applyBounds( name, w, prefs );
    }



    private static void collect( String name, JInternalFrame i, Preferences prefs )
    {
        collectBounds( name, i, prefs );
        prefs.put( name + ".icon", Boolean.toString( i.isIcon() ) );
    }

    private static void apply( String name, JInternalFrame i, Preferences prefs )
    {
        applyBounds( name, i, prefs );
        try
        {
            i.setIcon( "true".equals( prefs.get( name + ".icon", "false" ) ) );
        }
        catch ( java.beans.PropertyVetoException ex ) {}
    }

    
    private static void collect( String name, JList l, Preferences prefs )
    {
//         prefs.put( name + ".selected", Integer.toString( l.getSelectedIndex() ) );
    }

    private static void apply( String name, JList l, Preferences prefs )
    {
//         int i = Integer.parseInt( prefs.get( name + ".selected", "-1" ) );
//         if ( i != -1 ) l.setSelectedIndex( i );
    }

    
    private static void collect( String name, JTable t, Preferences prefs )
    {
        for
            ( TableColumn tc : Collections.list( t.getColumnModel().getColumns() ) )
        {
            prefs.put( name + "." + t.getModel().getColumnName( t.getColumnModel().getColumnIndex( tc.getIdentifier() ) ) + ".width", Integer.toString( tc.getWidth() ) );
        }
    }

    private static void apply( String name, JTable t, Preferences prefs )
    {
        for
            ( TableColumn tc : Collections.list( t.getColumnModel().getColumns() ) )
        {
            int w = Integer.parseInt( prefs.get( name + "." + t.getModel().getColumnName( t.getColumnModel().getColumnIndex( tc.getIdentifier() ) ) + ".width", "-1" ) );
            if ( w != -1 ) tc.setPreferredWidth( w );
        }
    }
    
    private static void collect( String name, JSplitPane sp, Preferences prefs )
    {
        prefs.put( name + ".div", Integer.toString( sp.getDividerLocation() ) );
    }

    private static void apply( String name, JSplitPane sp, Preferences prefs )
    {
        int div = Integer.parseInt( prefs.get( name + ".div", "-1" ) );
        if ( div != -1 ) sp.setDividerLocation( div );
    }
}
