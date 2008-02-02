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
public abstract class Edge
{
    public static final Edge TOP = new TopEdge();
    public static final Edge BOTTOM = new BottomEdge();
    public static final Edge LEFT = new LeftEdge();
    public static final Edge RIGHT = new RightEdge();
    
    abstract double addPreferredSpan( Constraint constraint, double origin );
    abstract void attach( Constraint constraint, Attachment a );
    abstract double crossComponent( Edge start, Constraint constraint, double weight, double delta, Collection<Equation> terminatedEquations, boolean trace );
    public abstract Attachment getAttachmentFor( Constraint constraint );
    abstract double getEdgePosition( Constraint constraint );
    abstract Edge getOppositeEdge();
    abstract double getContainerOrigin( Constraint constraint );
    abstract double getContainerSpan( Constraint constraint );
    abstract double getPreferredEdgePosition( Constraint constraint );
    abstract double getPreferredSpanFor( Constraint constraint );
    abstract double getWeight();
}
