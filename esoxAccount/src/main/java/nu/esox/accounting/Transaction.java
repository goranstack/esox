package nu.esox.accounting;

import org.xml.sax.*;
import nu.esox.util.*;
import nu.esox.xml.*;


@SuppressWarnings( "serial" )
public class Transaction extends Observable implements XmlWriter.Writeable
{
    public static final String PROPERTY_DESCRIPTION = "PROPERTY_DESCRIPTION";
    public static final String PROPERTY_ACCOUNT = "PROPERTY_ACCOUNT";
    public static final String PROPERTY_AMOUNT = "PROPERTY_AMOUNT";
    public static final String PROPERTY_EDITABLE = "PROPERTY_EDITABLE";

    public static Double AMOUNT_UNDEFINED = new Double( Double.NaN );

    private Verification m_verification;
    private String m_description = "";
    private Account m_account = null;
    private double m_amount = AMOUNT_UNDEFINED;


    public Transaction( Verification verification )
    {
        m_verification = verification;
    }

    public Verification getVerification() { return m_verification; }
    public final String getDescription() { return m_description; }
    public final Account getAccount() { return m_account; }
    public final double getAmount() { return m_amount; }
    public final boolean hasAmount() { return ! isAmountUndefined( m_amount ); }
    public boolean isEditable() { return false; }

    public final void setDescription( String description )
    {
        if ( description == null ) description = "";
        if ( m_description.equals( description ) ) return;

        m_description = description;
        fireValueChanged( PROPERTY_DESCRIPTION, m_description );
    }

    public final void clear()
    {
        beginTransaction();
        setAccount( null );
        setDescription( "" );
        setAmount( Double.NaN );
        endTransaction( "clear", null );
    }
    
    public final void setAccount( Account account )
    {
        if ( m_account == account ) return;

        preSetAccount();
        m_account = account;
        postSetAccount();

        fireValueChanged( PROPERTY_ACCOUNT, m_account );
    }

    protected void preSetAccount()
    {
        if ( m_account != null ) m_account.getTransactions().remove( this );
    }

    protected void postSetAccount()
    {
        if ( m_account != null ) m_account.getTransactions().add( this );
    }
    
    public final void setAmount( double amount )
    {
        if ( m_amount == amount ) return;

        m_amount = amount;
        fireValueChanged( PROPERTY_AMOUNT, new Double( m_amount ) );
    }

    public static boolean isAmountUndefined( double amount )
    {
        return Double.isNaN( amount );
    }

    
    public String xmlGetTag()
    {
        return "transaction";
    }
    
    public void xmlWriteAttributes( XmlWriter w )
    {
        w.write( "description", m_description );
        if ( hasAmount() ) w.write( "amount", m_amount );
    }
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( m_account );
    }

    Transaction( Attributes as ) throws java.text.ParseException
    {
        setDescription( XmlReader.xml2String( as, "description" ) );
        setAmount( XmlReader.xml2Double( as, "amount", AMOUNT_UNDEFINED ) );
    }

    void setVerification( Verification v )
    {
        assert m_verification == null;
        assert v != null;
        m_verification = v;
    }
    
    public void xmlAddSubmodel( Object subModel )
    {
        m_account = (Account) subModel;
    }

}
