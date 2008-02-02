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
class LeftEdge extends VerticalEdge
{
    final double addPreferredSpan( Constraint constraint, double origin )
    {
        return origin - getPreferredSpanFor( constraint );
    }

    void attach( Constraint constraint, Attachment a)
    {
        constraint.setLeft( a );
    }

    double crossComponent( Edge start, Constraint constraint, double weight, double delta, Collection<Equation> terminatedEquations, boolean trace )
    {
        double span = getPreferredSpanFor( constraint );

        delta -= span;

        if ( trace ) System.err.println( "CROSS COMP " + span );

        double r = getOppositeEdge().getAttachmentFor( constraint ).getRequestedSpan( start, constraint, weight, delta, true, terminatedEquations, trace );
	
        return r;
    }

    public final Attachment getAttachmentFor( Constraint constraint )
    {
        return constraint.getLeft();
    }

    final double getEdgePosition( Constraint constraint )
    {
        return constraint.getX0();
    }

    final Edge getOppositeEdge()
    {
        return RIGHT;
    }

    final double getContainerOrigin( Constraint constraint )
    {
        return 0;
    }

    final double getPreferredEdgePosition( Constraint constraint )
    {
        return constraint.getAttachable().getX();
    }

    double getWeight()
    {
        return 0;
    }

    public String toString()
    {
        return "LEFT";
    }
}
