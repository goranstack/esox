package nu.esox.accounting;

import org.xml.sax.*;
import java.util.*;
import nu.esox.util.*;
import nu.esox.xml.*;


public class VerificationSet extends ObservableList implements XmlWriter.UnsharedWriteable
{
    public VerificationSet()
    {
    }


    public final Verification create()
    {
        Verification v = new NumberedVerification();
        add( v );
        return v;
    }

    public Year getOwner() { return null; }


    
    private class NumberedVerification extends Verification
    {
        NumberedVerification() {}
        NumberedVerification( Attributes as ) throws java.text.ParseException { super( as ); }
        public VerificationSet getOwner() { return VerificationSet.this; }
        public int getNumber() { return 1 + indexOf( this ); }
    }

    
    public String xmlGetTag()
    {
        return "verifications";
    }
    
    public void xmlWriteAttributes( XmlWriter w ) {}
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( this, 0 );
    }

    
    public Verification xmlCreate_verification( Object superModel, Attributes as ) throws java.text.ParseException
    {
        Verification v = new NumberedVerification( as );
        add( v );
        return v;
    }
}
