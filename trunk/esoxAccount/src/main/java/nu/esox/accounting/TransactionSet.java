package nu.esox.accounting;

import org.xml.sax.*;
import java.util.*;
import nu.esox.util.*;
import nu.esox.xml.*;


@SuppressWarnings( "serial" )
public class TransactionSet extends ObservableList<Transaction> implements XmlWriter.UnsharedWriteable
{
    public static final String PROPERTY_AMOUNT = "PROPERTY_AMOUNT";
    public static final String PROPERTY_SUM = "PROPERTY_SUM";
    public static final String PROPERTY_SIZE = "PROPERTY_SIZE";


    
    private Double m_amount = null;
    private Double m_sum = null;
    private int m_size = -1;
    

    public TransactionSet()
    {
    }

    public final double getAmount()
    {
        assure();
        return m_amount;
    }

    public final double getSum()
    {
        assure();
        return m_sum;
    }

    public final int getSize()
    {
        assure();
        return m_size;
    }

    private void assure()
    {
        if
            ( m_amount == null )//Transaction.addAmounts( tmp, m_ib );
        {
            double amount = 0;
            double sum = 0;
            int size = 0;
            
            for
                ( Transaction t : this )
            {
                if
                    ( ( t.getAccount() != null ) && t.hasAmount() )
                {
                    size++;
                    amount = Transaction.addAmounts( amount, t.getAmount() );
                    if ( t.getAmount() > 0 ) sum = Transaction.addAmounts( sum, t.getAmount() );
                }
            }

            m_size = size;

            if
                ( m_size == 0 )
            {
                m_amount = Transaction.AMOUNT_UNDEFINED;
                m_sum = Transaction.AMOUNT_UNDEFINED;
            } else {
                m_amount = amount;
                if
                    ( amount == 0 )
                {
                    m_sum = sum;
                } else {
                    m_sum = Transaction.AMOUNT_UNDEFINED;
                }
            }
            
        }
    }

    
    public void normalize()
    {
        List<Transaction> tmp = new ArrayList<Transaction>();

        for
            ( Transaction t : this )
        {
            if
                ( ( t.getAccount() == null ) &&
                  ( t.getDescription().trim().equals( "" ) ) &&
                  ( ! t.hasAmount() ) )
            {
                tmp.add( t );
                t.removeObservableListener( m_transactionListener );
            }
        }

        for
            ( Transaction t : tmp )
        {
            t.removeObservableListener( m_transactionListener );
            remove( t );
        }
    }

    public void move( int i1, int i2 )
    {
        super.add( i2, super.remove( i1 ) );
    }

    public boolean add( Transaction transaction )
    {
        assert ! contains( transaction );

        boolean x = super.add( transaction );
        transaction.addObservableListener( m_transactionListener );
        
        invalidate();
        fireValueChanged( PROPERTY_AMOUNT, null );
        fireValueChanged( PROPERTY_SIZE, null );
        return x;
    }

    public void remove( Transaction transaction )
    {
        assert contains( transaction );

        transaction.removeObservableListener( m_transactionListener );
        super.remove( transaction );
        
        invalidate();
        fireValueChanged( PROPERTY_AMOUNT, null );
        fireValueChanged( PROPERTY_SIZE, null );
    }

    public void clear()
    {
        if ( isEmpty() ) return;

        for ( Transaction t : this ) t.removeObservableListener( m_transactionListener );
        super.clear();
        
        invalidate();
        fireValueChanged( PROPERTY_AMOUNT, null );
        fireValueChanged( PROPERTY_SIZE, null );
    }

    protected final void invalidate()
    {
        m_amount = null;
        m_size = -1;
    }
    
    private ObservableListener m_transactionListener =
        new ObservableListener()
        {
            public void  valueChanged( ObservableEvent ev )
            {
                if
                    ( ( ev.getInfo() == Transaction.PROPERTY_AMOUNT ) || ( ev.getInfo() == Transaction.PROPERTY_ACCOUNT ) )
                {
                    invalidate();
                    fireValueChanged( PROPERTY_SUM, null );
                    fireValueChanged( PROPERTY_AMOUNT, null );
                    fireValueChanged( PROPERTY_SIZE, null );
                }
            }
        };

    
    public String xmlGetTag()
    {
        return "transactions";
    }
    
    public void xmlWriteAttributes( XmlWriter w ) {}
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( this, 0 );
    }

    public Transaction xmlCreate_transaction( Object superModel, Attributes as ) throws java.text.ParseException
    {
        return new Transaction( as );
    }


    public void xmlAddSubmodel( Object subModel )
    {
        add( (Transaction) subModel );
    }
}
