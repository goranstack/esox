package nu.esox.accounting;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import nu.esox.util.*;

//fixit: keyboard navigation
//fixit: action

public class AccountMenu extends JPopupMenu implements ObservableListener
{
    private AccountPopulation m_accounts;
    private boolean m_dirty = true;
    private final MenuKeyListener m_keyListener =
        new MenuKeyListener()
        {
            public void menuKeyReleased( MenuKeyEvent ev ) {}
            public void menuKeyTyped( MenuKeyEvent ev ) {}
            public void menuKeyPressed( MenuKeyEvent ev )
            {
                if
                    ( ev.getKeyCode() == MenuKeyEvent.VK_BACK_SPACE )
                {
                    if ( m_typed.length() > 0 ) m_typed = m_typed.substring( 0, m_typed.length() - 1 );
                } else if ( Character.isLetterOrDigit( ev.getKeyChar() ) )
                {
                    m_typed += ev.getKeyChar();
                } else {
                    return;
                }
                
                if ( m_typed.length() == 0 ) return;
                
                for
                    ( JMenuItem mi : m_menuItems )//( Account a : new TypedCollection<Account>( m_accounts ) )
                {
                    if 
                        ( mi.getText().startsWith( m_typed ) )
//                        ( Integer.toString( a.getNumber() ).startsWith( m_typed ) )
                    {
//                        System.err.println( a );

                        try
                        {
                            Robot r = new Robot();
                            r.mouseMove( mi.getLocationOnScreen().x + 1, mi.getLocationOnScreen().y + 1 );
                            r.mousePress( InputEvent.BUTTON1_MASK );
                            r.mouseRelease( InputEvent.BUTTON1_MASK );
                        }
                        catch ( java.awt.AWTException ex )
                        {
                            System.err.println( ex );
                        }
                        break;
                    }
                }
            }
        };

    private String m_typed = "";

    private List<JMenuItem> m_menuItems = new ArrayList<JMenuItem>();

    
    
    public AccountMenu()
    {
        addMenuKeyListener( m_keyListener );
    }

    
    public void setAccounts( AccountPopulation accounts )
    {
        if ( m_accounts == accounts ) return;
        
        if ( m_accounts != null ) m_accounts.removeObservableListener( this );
        m_accounts = accounts;
        if ( m_accounts != null ) m_accounts.addObservableListener( this );
        m_dirty = true;
    }

    public void valueChanged( ObservableEvent ev )
    {
        m_dirty = true;
    }

    public void show( Component invoker, int x, int y )
    {
        rebuild();
        m_typed = "";
        super.show( invoker, x, y );
    }

    private void rebuild()
    {
        removeAll();
        m_menuItems.clear();
        
        if
            ( m_accounts != null )
        {
//             Map<Account.Type,JMenu> types = new HashMap<Account.Type,JMenu>();
            
            for
                ( Account a : new TypedCollection<Account>( m_accounts ) )
            {
//                 JMenu m = types.get( a.getType() );
//                 if
//                     ( m == null )
//                 {
//                     m = new JMenu( a.getType().getName() );
//                     types.put( a.getType(), m );
//                     add( m );
// //                    m.addKeyListener( m_keyListener );
//                 }

                JMenuItem mi = new JMenuItem( a.getNumber() + " " + a.getName() );
                m_menuItems.add( mi );
                /*m.*/add( mi );
            }
        }

        m_dirty = false;
    }
}

