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
class BottomEdge extends HorizontalEdge
{
    final double addPreferredSpan( Constraint constraint, double origin )
    {
        return origin + getPreferredSpanFor( constraint );
    }

    void attach( Constraint constraint, Attachment a )
    {
        constraint.setBottom( a );
    }

    double crossComponent( Edge start, Constraint constraint, double weight, double delta, Collection terminatedEquations, boolean trace )
    {
        double span = getPreferredSpanFor( constraint );

        delta += span;

        if ( trace ) System.err.println( "CROSS COMP " + span );

        double r = getOppositeEdge().getAttachmentFor( constraint ).getRequestedSpan( start, constraint, weight, delta, true, terminatedEquations, trace );
	
        return r;
    }

    public final Attachment getAttachmentFor( Constraint constraint )
    {
        return constraint.getBottom();
    }

    final double getEdgePosition( Constraint constraint )
    {
        return constraint.getY1();
    }

    final Edge getOppositeEdge()
    {
        return TOP;
    }

    final double getContainerOrigin( Constraint constraint )
    {
        return getContainerSpan( constraint );
    }

    final double getPreferredEdgePosition( Constraint constraint )
    {
        return getOppositeEdge().getPreferredEdgePosition( constraint ) + getPreferredSpanFor( constraint );
    }

    double getWeight()
    {
        return 1;
    }

    public String toString()
    {
        return "BOTTOM";
    }
}
