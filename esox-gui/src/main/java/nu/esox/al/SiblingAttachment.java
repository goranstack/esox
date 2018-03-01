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
import java.util.List;

  /**
   * Attach an edge to an edge of another attachable
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */
public class SiblingAttachment extends Attachment
{
      /**
       * @see nu.esox.al.SiblingAttachment
       * 
   * @author  Dennis Malmström
       * @version 2.0
       */
    public static abstract class Position
    {
        protected Position() {}
        protected abstract double calculatePosition( SiblingAttachment attachment, Constraint constraint );
        protected abstract double followAttachment( SiblingAttachment attachment, Edge start, Constraint constraint, double weight, double delta, boolean dontCrossAttachable, Collection<Equation> terminatedEquations, boolean trace );
    };

    public static final Position MAX = new MaxPosition();
    public static final Position MIN = new MinPosition();
    
    private double m_offset;
    private Edge m_attachedEdge;
    private Attachable [] m_attachedAttachables;
    private Constraint [] m_attachedConstraints;
    private Position m_position;


      /**
       * Creates an instance.
       * 
       * @param edge The attached edge.
       * @param attachable Attachable to which the edge is attached.
       * @param attachableEdge The edge of attachable to which the edge is attached.
       */
    public static SiblingAttachment create( Edge edge, Attachable attachable, Edge attachableEdge )
    {
        return create( edge, new Attachable [] { attachable }, MIN, attachableEdge, 0 );
    }

    
      /**
       * Creates an instance.
       * 
       * @param edge The attached edge.
       * @param attachables Attachables to which the edge is attached.
       * @param pos Max or min position among attachables to which the edge is attached.
       * @param attachableEdge The edge of attachable to which the edge is attached.
       */
    public static SiblingAttachment create( Edge edge, Attachable [] attachables, Position pos, Edge attachableEdge )
    {
        return create( edge, attachables, pos, attachableEdge, 0 );
    }

    
      /**
       * Creates an instance.
       * 
       * @param edge The attached edge.
       * @param attachable Attachable to which the edge is attached.
       * @param attachableEdge The edge of attachable to which the edge is attached.
       * @param offset Offset from attachableEdge.
       */
    public static SiblingAttachment create( Edge edge, Attachable attachable, Edge attachableEdge, double offset )
    {
        return new SiblingAttachment( edge, new Attachable [] { attachable }, MIN, attachableEdge, offset );
    }

    
      /**
       * Creates an instance.
       * 
       * @param edge The attached edge.
       * @param attachables Attachables to which the edge is attached.
       * @param pos Max or min position among attachables to which the edge is attached.
       * @param offset Offset from attachableEdge.
       */
    public static SiblingAttachment create( Edge edge, Attachable [] attachables, Position pos, Edge attachableEdge, double offset )
    {
        return new SiblingAttachment( edge, attachables, pos, attachableEdge, offset );
    }

    
    private SiblingAttachment( Edge e, Attachable [] attachables, Position pos, Edge oe, double offset )
    {
        super( e );
        
        m_offset = offset;
        
        m_attachedAttachables = attachables;
        m_position = ( pos == null ) ? MIN : pos;
        
        m_attachedEdge = oe;
    }

    double calculatePosition( Constraint constraint )
    {
        return m_position.calculatePosition( this, constraint );
    }

    
    double followAttachment( Edge start, Constraint constraint, double weight, double delta, boolean dontCrossAttachable, Collection<Equation> terminatedEquations, boolean trace )
    {
        delta += m_offset;

        log( "CROSS ATT  ", weight, delta, this, false, trace );

        return m_position.followAttachment( this, start, constraint, weight, delta, dontCrossAttachable, terminatedEquations, trace );
    }
    
    public Constraint [] getAttachedConstraints()
    {
        return m_attachedConstraints;
    }
    
    public Edge getAttachedEdge()
    {
        return m_attachedEdge;
    }
    
    double getDelta()
    {
        return 0;
    }
    
    public double getOffset()
    {
        return m_offset;
    }
    
    final double getRequestedSpan( Edge start, Constraint constraint, double weight, double delta, boolean trace )
    {
        Attachment oppAtt = m_edge.getOppositeEdge().getAttachmentFor( constraint );

        List<Equation> equations = null;
	
        if
            ( oppAtt instanceof SiblingAttachment )
        {
            if ( m_edge == Edge.RIGHT ) return 0; // only do it once
            if ( m_edge == Edge.BOTTOM ) return 0; // only do it once
		
            equations = new ArrayList<Equation>();
        }

        double r = followAttachment( start, constraint, weight, delta, false, equations, trace );

        if
            ( oppAtt instanceof SiblingAttachment )
        {
            return handleOpposingAttachableAttachments( constraint, weight, delta, equations, trace );
        } else {
            return r;
        }
    }
    
    double getWeight()
    {
        return 0;
    }
    
    private double handleOpposingAttachableAttachments( Constraint constraint, double weight, double delta, Collection<Equation> equations, boolean trace )
    {
        log( "OPP COMPAT ", weight, delta, this, false, trace );

        Attachment oppAtt = m_edge.getOppositeEdge().getAttachmentFor( constraint );

        List<Equation> oppositeEquations = new ArrayList<Equation>();
        oppAtt.followAttachment( oppAtt.getEdge(), constraint, weight, delta, false, oppositeEquations, trace );

	
        double r = 0;
        double w = m_edge.getPreferredSpanFor( constraint );
	
        Iterator i = equations.iterator();
        while
            ( i.hasNext() )
        {
            Equation leftEq = (Equation) i.next();
		
            Iterator j = oppositeEquations.iterator();
            while
                ( j.hasNext() )
            {
                Equation rightEq = (Equation) j.next();

                double rL = 0;
                double rR = 0;
                double rB = 0;
                if
                    ( leftEq.m_weight != rightEq.m_weight )
                {
                    rB = ( leftEq.m_delta - rightEq.m_delta + w ) / ( leftEq.m_weight - rightEq.m_weight );
                }

                if
                    ( leftEq.m_weight == 0 )
                {
                    rL = leftEq.m_delta;
                } else {
                    rL = leftEq.m_delta / leftEq.m_weight;
                }

                if
                    ( rightEq.m_weight == 0 )
                {
                    rR = rightEq.m_delta;
                } else {
                    rR = rightEq.m_delta / rightEq.m_weight;
                }
			
                r = Math.max( r, rL );
                r = Math.max( r, rR );
                r = Math.max( r, rB );
            }
        }

        log( "OPP COMPAT ", 1, r, this, true, trace );

        return r;
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
    
    public String toString()
    {
        return m_edge + "_SIBLING_" + m_attachedEdge + " " + m_offset;
    }
    
    void validate( AttachableContainer e, Constraint constraint )
    {
        m_attachedConstraints = new Constraint[ m_attachedAttachables.length ];
        for
            ( int i = 0; i < m_attachedAttachables.length; i++ )
        {
            m_attachedConstraints[ i ] = e.getConstraintFor( m_attachedAttachables[ i ] );
            if
                ( m_attachedConstraints[ i ] == null )
            {
                throw new AttachmentError( "Unresolved sibling attachment involving " + m_edge + " edge of sibling " + constraint.getAttachable().getId() );
            }
        }
    }




    private static class MaxPosition extends Position
    {
        public double calculatePosition( SiblingAttachment attachment, Constraint constraint )
        {
            double pos = Double.MIN_VALUE;
            for
                ( int i = 0; i < attachment.m_attachedConstraints.length; i++ )
            {
                pos = Math.max( pos, attachment.m_attachedEdge.getEdgePosition( attachment.m_attachedConstraints[ i ] ) + attachment.m_offset );
            }
            return pos;
        }

        public double followAttachment( SiblingAttachment attachment, Edge start, Constraint constraint, double weight, double delta, boolean dontCrossAttachable, Collection<Equation> terminatedEquations, boolean trace )
        {
            double r = Double.MIN_VALUE;
            for
                ( int i = 0; i < attachment.m_attachedConstraints.length; i++ )
            {
                r = Math.max( r, attachment.m_attachedEdge.getAttachmentFor( attachment.m_attachedConstraints[ i ] ).getRequestedSpan( start, attachment.m_attachedConstraints[ i ], weight, delta, false, terminatedEquations, trace ) );
            }
            return r;
        }
    }

    
    private static class MinPosition extends Position
    {
        public double calculatePosition( SiblingAttachment attachment, Constraint constraint )
        {
            double pos = Double.MAX_VALUE;
            for
                ( int i = 0; i < attachment.m_attachedConstraints.length; i++ )
            {
                pos = Math.min( pos, attachment.m_attachedEdge.getEdgePosition( attachment.m_attachedConstraints[ i ] ) + attachment.m_offset );
            }
            return pos;
        }

        public double followAttachment( SiblingAttachment attachment, Edge start, Constraint constraint, double weight, double delta, boolean dontCrossAttachable, Collection<Equation> terminatedEquations, boolean trace )
        {
            double r = Double.MAX_VALUE;
            for
                ( int i = 0; i < attachment.m_attachedConstraints.length; i++ )
            {
                r = Math.min( r, attachment.m_attachedEdge.getAttachmentFor( attachment.m_attachedConstraints[ i ] ).getRequestedSpan( start, attachment.m_attachedConstraints[ i ], weight, delta, false, terminatedEquations, trace ) );
            }
            return r;
        }
    }


    
}
