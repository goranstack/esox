package nu.esox.accounting;

import org.xml.sax.*;
import java.util.*;
import nu.esox.util.*;
import nu.esox.xml.*;


@SuppressWarnings( "serial" )
public class AccountPopulation extends ObservableList<Account> implements XmlWriter.UnsharedWriteable
{
    private final Map<Integer, Account> m_map = new HashMap<Integer, Account>();

    private final ObservableListener m_numberListener =
        new ObservableListener()
        {
            public void valueChanged( ObservableEvent ev )
            {
                if
                    ( ev.getInfo() == Account.PROPERTY_NUMBER )
                {
                    Account a = (Account) ev.getSource();
                    for
                        ( Map.Entry<Integer, Account> e : m_map.entrySet() )
                    {
                        if
                            ( e.getValue() == a )
                        {
                            assert ! m_map.containsKey( a.getNumber() );
                            m_map.remove( e.getKey() );
                            m_map.put( a.getNumber(), a );
                            break;
                        }
                    }
                }
            }
        };
    
    
    public boolean add( Account account )
    {
        assert ! m_map.containsKey( account.getNumber() ) ;
        m_map.put( account.getNumber(), account );
        super.add( - ( Collections.binarySearch( this, account ) + 1 ), account );
        account.addObservableListener( m_numberListener );
        return true;
    }
    
    public void remove( Account account )
    {
        if
            ( contains( account ) )
        {
            m_map.remove( account.getNumber() );
            super.remove( account );
            account.removeObservableListener( m_numberListener );
        }
    }

    public Account getAccount( int number )
    {
        return m_map.get( number );
    }

    public void copy( AccountPopulation destination )
    {
        assert destination.isEmpty();

        for
            ( Account a : this )

        {
            destination.add( a.newYear() );
        }

    }
    
    public String xmlGetTag()
    {
        return "accounts";
    }
    
    public void xmlWriteAttributes( XmlWriter w ) {}
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( this, 0 );
    }
    
    public Account xmlCreate_account( Object superModel, Attributes as ) throws java.text.ParseException
    {
        Account a = new Account( as );
        add( a );
        return a;
    }
}
