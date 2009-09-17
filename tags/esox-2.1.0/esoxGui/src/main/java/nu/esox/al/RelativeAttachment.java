package nu.esox.al;

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

import java.lang.ref.*;
import java.util.*;

  /**
   * Attach an edge to the point: position * container width + offset.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */
public class RelativeAttachment extends Attachment
{
    private double m_position;
    private double m_offset;
	
    private static SoftReference<Map<HashKey,Attachment>> m_instances;
    private static HashKey m_hashKey = new HashKey( null, 0, 0 );

    
      /**
       * Creates an instance.
       * 
       * @param edge The attached edge.
       * @param position Edge position relative containers width/height.
       */
    public static RelativeAttachment create( Edge edge, double position )
    {
        return create( edge, position, 0 );
    }

    
      /**
       * Creates an instance.
       * 
       * @param edge The attached edge.
       * @param position Edge position relative containers width/height.
       * @param offset Offset from relative containers width/height.
       */
    public static RelativeAttachment create( Edge edge, double position, double offset )
    {
        m_hashKey.m_edge = edge;
        m_hashKey.m_offset = offset;
        m_hashKey.m_position = position;

        Map instances = getInstances();

        RelativeAttachment ca = (RelativeAttachment) instances.get( m_hashKey );

        if
            ( ca == null )
        {
            ca = new RelativeAttachment( edge, position, offset );
        }
	
        return ca;
    }

    
    private RelativeAttachment( Edge e, double position, double offset )
    {
        super( e );

        m_position = position;
        m_offset = offset;
	
        getInstances().put( new HashKey( e, position, offset ), this );
    }

    
    double calculatePosition( Constraint constraint )
    {
        return m_edge.getContainerSpan( constraint ) * m_position + m_offset;
    }

    double getDelta()
    {
        return - m_offset;
    }

    private static Map<HashKey,Attachment> getInstances()
    {
        if
            ( ( m_instances == null ) || ( m_instances.get() == null ) )
        {
            m_instances = new SoftReference<Map<HashKey,Attachment>>( new HashMap<HashKey,Attachment>() );
        }

        return m_instances.get();
    }

    public double getOffset()
    {
        return m_offset;
    }

    public double getPosition()
    {
        return m_position;
    }

    double getWeight()
    {
        return m_position;
    }

    boolean setOffset( double offset )
    {
        if
            ( m_offset != offset )
        {
            m_offset = offset;
            return true;
        } else {
            return false;
        }
    }

    boolean setPosition( double position )
    {
        if
            ( m_position != position )
        {
            m_position = position;
            return true;
        } else {
            return false;
        }
    }

    public String toString()
    {
        return m_edge + "_RELATIVE " + m_position + "% " + m_offset;
    }

    
    private static class HashKey
    {
        Edge m_edge;
        double m_position;
        double m_offset;

        public HashKey( Edge e, double position, double offset )
        {
            m_edge = e;
            m_position = position;
            m_offset = offset;
        }
	 	
        public boolean equals( Object o )
        {
            return ( o == this ) || ( ( o instanceof HashKey ) && ( (HashKey) o ).equals( this ) );
        }

        private boolean equals( HashKey hk )
        {
            return ( m_edge == hk.m_edge ) && ( m_offset == hk.m_offset ) && ( m_position == hk.m_position );
        }

        public int hashCode()
        {
            return (int) ( m_edge.hashCode() + m_offset + m_position );
        }
    };

}
