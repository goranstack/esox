package nu.esox.accounting;

import java.text.*;
import java.util.*;
import org.xml.sax.*;
import nu.esox.util.*;
import nu.esox.xml.*;


public class Year extends nu.esox.util.Observable implements XmlWriter.UnsharedWriteable, Comparable
{
    private final int m_number;
    private final AccountPopulation m_allAccounts = new AccountPopulation();
    private final VerificationSet m_verifications = new VerificationSet() { public Year getOwner() { return Year.this; } };
    
    public Year( int number )
    {
        m_number = number;
    }


    public int getNumber() { return m_number; }
    public AccountPopulation getAccounts() { return m_allAccounts; }
    public VerificationSet getVerifications() { return m_verifications; }

    public String toString() { return Integer.toString( getNumber() ); }

    public int compareTo( Object o )
    {
        return m_number - ( (Year) o ).m_number;
    }

    public void closeYear()
    {
        Date d = null;

        try
        {
            d = Constants.DATE_FORMAT.parse( getNumber() + "-12-31" );
        }
        catch ( ParseException ex ) {}
        
        {
            Verification v = getVerifications().create();
            v.setName( "Interimsfodringar" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 1300 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
//                 v.setBalancingTransaction( t );
            }
        }

        {
            Verification v = getVerifications().create();
            v.setName( "Lager" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 1510 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
//                v.setBalancingTransaction( t );
            }
        }

        {
            Verification v = getVerifications().create();
            v.setName( "Upplupna kostnader" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2300 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
//                 v.setBalancingTransaction( t );
            }
        }

        {
            Verification v = getVerifications().create();
            v.setName( "Avskrivningar" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 7910 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
//                v.setBalancingTransaction( t );
            }

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 1829 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
//                v.setBalancingTransaction( t );
            }
        }


        {
            Verification v = getVerifications().create();
            v.setName( "Omföring av årets resultat" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 8999 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
            }

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2999 ) );
                t.setAmount( 0 );
                v.getTransactions().add( t );
            }
        }

          // todo: ...
    }
    

    private boolean toNewYear( Verification v, String verificationName )
    {
        verificationName = verificationName.toUpperCase();
        
        Verification v0 = null;
        
        for
            ( Verification tmp : new TypedCollection<Verification>( getVerifications() ) )
        {
            if
                ( tmp.getName().toUpperCase().equals( verificationName ) )
            {
                v0 = tmp;
                break;
            }
        }
        
        if ( v0 == null ) return false;

        for
            ( Transaction t0 : new TypedCollection<Transaction>( v0.getTransactions() ) )
        {
            Transaction t = v.createTransaction();
            t.setAccount( v.getOwner().getOwner().getAccounts().getAccount( t0.getAccount().getNumber() ) );
            t.setAmount( - t0.getAmount() );
            v.getTransactions().add( t );
        }

        return true;
    }

    
    public Year newYear()
    {
        Year y = new Year( m_number + 1 );
        m_allAccounts.copy( y.getAccounts() );
        y.startYear();
        return y;
    }

    public void startYear()
    {
        Date d = null;

        try
        {
            d = Constants.DATE_FORMAT.parse( getNumber() + "-01-01" );
        }
        catch ( ParseException ex ) {}
        
        {
            Verification v = getVerifications().create();
            v.setName( "Återföring interimsfodringar" );
            v.setDate( d );

            if
                ( ! toNewYear( v, "Interimsfodringar" ) )
            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 1300 ) );
                t.setAmount( - getAccounts().getAccount( 1300 ).getAmount() );
                v.getTransactions().add( t );
            }
        }

        {
            Verification v = getVerifications().create();
            v.setName( "Omföring inventarier" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 1829 ) );
                t.setAmount( - getAccounts().getAccount( 1829 ).getIb() );
                v.getTransactions().add( t );
            }

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 1820 ) );
                t.setAmount( getAccounts().getAccount( 1829 ).getIb() );
                v.getTransactions().add( t );
            }
        }
       
        {
            Verification v = getVerifications().create();
            v.setName( "Återföring upplupna kostnader" );
            v.setDate( d );

            if
                ( ! toNewYear( v, "Upplupna kostnader" ) )
            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2300 ) );
                t.setAmount( - getAccounts().getAccount( 2300 ).getAmount() );
                v.getTransactions().add( t );
            }
        }

        {
            Verification v = getVerifications().create();
            v.setName( "Återföring förbet. medlemsavg." );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2310 ) );
                t.setAmount( - getAccounts().getAccount( 2310 ).getIb() );
                v.getTransactions().add( t );
            }

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 3810 ) );
                t.setAmount( getAccounts().getAccount( 2310 ).getIb() );
                v.getTransactions().add( t );
            }
        }
 
        {
            Verification v = getVerifications().create();
            v.setName( "Omföring av eget kapital" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2998 ) );
                t.setAmount( - getAccounts().getAccount( 2998 ).getIb() );
                v.getTransactions().add( t );
            }

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2990 ) );
                t.setAmount( getAccounts().getAccount( 2998 ).getIb() );
                v.getTransactions().add( t );
            }
        }
   
        {
            Verification v = getVerifications().create();
            v.setName( "Omföring av föregående års resultat" );
            v.setDate( d );

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2998 ) );
                t.setAmount( getAccounts().getAccount( 2999 ).getIb() );
                v.getTransactions().add( t );
            }

            {
                Transaction t = v.createTransaction();
                t.setAccount( getAccounts().getAccount( 2999 ) );
                t.setAmount( - getAccounts().getAccount( 2999 ).getIb() );
                v.getTransactions().add( t );
            }
        }
    }
    
    public String xmlGetTag()
    {
        return "year";
    }
    
    public void xmlWriteAttributes( XmlWriter w )
    {
        w.write( "number", m_number );
    }
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( m_allAccounts );
        w.write( m_verifications );
    }

    
    Year( Attributes a ) throws java.text.ParseException
    {
        this( XmlReader.xml2Integer( a, "number" ) );
    }

    public AccountPopulation xmlCreate_accounts( Object superModel, Attributes a )
    {
        return m_allAccounts;
    }

    public VerificationSet xmlCreate_verifications( Object superModel, Attributes a )
    {
        return m_verifications;
    }
}
