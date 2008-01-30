package nu.esox.xml;

  /*
    Copyright (C) 2002  Dennis Malmström (dennis.malmstrom@telia.com)

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.
    
    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
  */


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
