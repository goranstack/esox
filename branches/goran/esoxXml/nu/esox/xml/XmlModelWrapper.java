package nu.esox.xml;

public class XmlModelWrapper implements XmlWriter.UnsharedWriteable
{
    private String m_tag;
    private XmlWriter.Writeable m_model;

    public XmlModelWrapper( String tag )
    {
        m_tag = tag;
    }
    public XmlModelWrapper( String tag, XmlWriter.Writeable model )
    {
        m_tag = tag;
        m_model = model;
    }

    public String xmlGetTag()
    {
        return m_tag;
    }

    public void xmlWriteAttributes( XmlWriter w )
    {
    }
        
    public void xmlWriteSubmodels( XmlWriter w )
    {
        w.write( m_model );
    }

    public void xmlAddSubmodel( Object o )
    {
    }
}
