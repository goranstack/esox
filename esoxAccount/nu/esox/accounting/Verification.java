package nu.esox.accounting;

import org.xml.sax.*;
import java.util.*;
import nu.esox.util.*;
import nu.esox.xml.*;


@SuppressWarnings( "serial" )
public class Verification extends NamedAndNumbered implements XmlWriter.UnsharedWriteable
{
    public static final String PROPERTY_DATE = "PROPERTY_DATE";
    public static final String PROPERTY_ALERT = "PROPERTY_ALERT";
    public static final String PROPERTY_AMOUNT = "PROPERTY_AMOUNT";
    public static final String PROPERTY_SUM = "PROPERTY_SUM";
    public static final String PROPERTY_EDITABLE = "PROPERTY_EDITABLE";

    private boolean m_editable = true;
    private Date m_date = null;
    private String m_alert = "";
    private final TransactionSet m_transactions =
        new TransactionSet()
        {
            public void xmlAddSubmodel( Object subModel )
            {
                super.xmlAddSubmodel( subModel );
                ( (Transaction) subModel ).setVerification( Verification.this );
            }
        };


    public Verification()
    {
        ObservableListener l =
            new ObservableListener()
            {
                public void  valueChanged( ObservableEvent ev )
                {
                    if       ( ev.getInfo() == TransactionSet.PROPERTY_AMOUNT ) fireValueChanged( PROPERTY_AMOUNT, ev.getData() );
                    else if ( ev.getInfo() == TransactionSet.PROPERTY_SUM )    fireValueChanged( PROPERTY_SUM, ev.getData() );
                }
            };

        m_transactions.addObservableListener( l );
    }

    public final Date getDate() { return m_date; }
    public final String getAlert() { return m_alert; }
    public final TransactionSet getTransactions() { return m_transactions; }
    public final double getAmount() { return m_transactions.getAmount(); }
    public final double getSum() { return m_transactions.getSum(); }
    public VerificationSet getOwner() { return null; }
    public final AccountPopulation getAccounts() { return getOwner().getOwner().getAccounts(); }
    
        
    public final void setAlert( String alert )
    {
        if ( alert == null ) alert = "";
        if ( m_alert.equals( alert ) ) return;

        m_alert = alert;
        fireValueChanged( PROPERTY_ALERT, m_date );
    }
    
    public final void setDate( Date date )
    {
        if ( m_date == date ) return;

        m_date = date;
        fireValueChanged( PROPERTY_DATE, m_date );
    }

    public void setEditable( boolean editable )
    {
        if ( m_editable == editable ) return;

        m_editable = editable;
        fireValueChanged( PROPERTY_EDITABLE, m_editable );
    }

    public boolean isEditable() { return m_editable; }

    public Transaction createTransaction()
    {
        return new Transaction( this );
    }

    public void dispose()
    {
        for ( Transaction t : getTransactions() ) t.setAccount( null );
    }
    
    
    public String xmlGetTag()
    {
        return "verification";
    }
    
    public void xmlWriteAttributes( XmlWriter w )
    {
        w.write( "editable", m_editable );
        w.write( "name", getName() );
        if ( m_date != null ) w.write( "date", Constants.DATE_FORMAT.format( m_date ) );
        if ( m_alert.length() > 0 ) w.write( "alert", m_alert );
    }
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        if ( ! m_transactions.isEmpty() ) w.write( m_transactions );
    }


    Verification( Attributes as ) throws java.text.ParseException
    {
        this();
        setName( XmlReader.xml2String( as, "name" ) );
        setEditable( XmlReader.xml2Boolean( as, "editable" ) );
        String tmp = XmlReader.xml2String( as, "date" );
        if ( tmp != null ) m_date = Constants.DATE_FORMAT.parse( tmp );
        m_alert = XmlReader.xml2String( as, "alert", "" );
    }
    
    public TransactionSet xmlCreate_transactions( Object superModel, Attributes a )
    {
        return m_transactions;
    }
}
