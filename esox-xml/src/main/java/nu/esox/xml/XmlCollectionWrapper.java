package nu.esox.xml;


import java.util.*;


public class XmlCollectionWrapper<T> implements XmlWriter.UnsharedWriteable
{
    private String m_tag;
    private Collection<T> m_collection;

    public XmlCollectionWrapper( String tag, Collection<T> c )
    {
        m_tag = tag;
        m_collection = c;
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
        w.write( m_collection, 0 );
    }

    public void xmlAddSubmodel( T o )
    {
        m_collection.add( o );
    }

    public Collection<T> getCollection()
    {
        return m_collection;
    }
}
