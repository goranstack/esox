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

import java.util.*;

  /**
   * @author  Dennis Malmström
   * @version 2.0
   */
class NullAttachment extends Attachment
{
    final static NullAttachment TOP = new NullAttachment( Edge.TOP );
    final static NullAttachment BOTTOM = new NullAttachment( Edge.BOTTOM );
    final static NullAttachment LEFT = new NullAttachment( Edge.LEFT );
    final static NullAttachment RIGHT = new NullAttachment( Edge.RIGHT );


    public static NullAttachment create( Edge e )
    {
        if      ( e == Edge.LEFT )   return LEFT;
        else if ( e == Edge.RIGHT )  return RIGHT;
        else if ( e == Edge.TOP )    return TOP;
        else if ( e == Edge.BOTTOM ) return BOTTOM;
        else                         return null;
    }

    
    private NullAttachment( Edge e )
    {
        super( e );
    }

    final double calculatePosition( Constraint constraint )
    {
        if
            ( m_edge.getOppositeEdge().getAttachmentFor( constraint ) instanceof NullAttachment )
        {
            return m_edge.getPreferredEdgePosition( constraint );
        } else {
            return m_edge.addPreferredSpan( constraint, m_edge.getOppositeEdge().getEdgePosition( constraint ) );
        }
    }

    double getDelta()
    {
        return 0;
    }

    final double getRequestedSpan( Edge start, Constraint constraint, double weight, double delta, boolean dontCrossComponent, Collection<Equation> terminatedEquations, boolean trace )
    {
        if
            ( dontCrossComponent )
        {
            if
                ( m_edge.getOppositeEdge().getAttachmentFor( constraint ) instanceof NullAttachment )
            {
                return followAttachment( start, constraint, weight, delta, false, terminatedEquations, trace );
            } else {
                log( "DEAD  END  ", weight, delta, this, false, trace );
                return 0;
            }
        } else {
            return m_edge.crossComponent( start, constraint, weight, delta, terminatedEquations, trace );
        }

    }

    double getWeight()
    {
        return m_edge.getWeight();
    }

    double solve( Constraint constraint, double weight, double delta, boolean trace )
    {
        if
            ( m_edge.getOppositeEdge().getAttachmentFor( constraint ) instanceof NullAttachment )
        {
            return m_edge.getPreferredEdgePosition( constraint ) + delta;
        } else {
            return super.solve( constraint, weight, delta, trace );
        }
    }

    public String toString()
    {
        return m_edge + "_NO";
    }
}
