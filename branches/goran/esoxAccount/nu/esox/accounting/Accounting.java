package nu.esox.accounting;

import org.xml.sax.*;
import nu.esox.util.*;
import nu.esox.xml.*;


public class Accounting implements XmlWriter.UnsharedWriteable
{
    private final YearSet m_years = new YearSet();

    public Accounting()
    {
    }


    public YearSet getYears() { return m_years; }

    
    public String xmlGetTag()
    {
        return "accounting";
    }
    
    public void xmlWriteAttributes( XmlWriter w ) {}
    
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( m_years, 0 );
    }

    public Accounting xmlCreate_accounting( Object superModel, Attributes a )
    {
        return this;
    }
    
    public Year xmlCreate_year( Object superModel, Attributes a ) throws java.text.ParseException
    {
        Year y = new Year( a );
        m_years.add( y );
        return y;
    }
}
