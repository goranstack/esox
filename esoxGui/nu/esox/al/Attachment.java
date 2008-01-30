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
   * Abstract superclass for all types of attachments.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */
public abstract class Attachment
{
    Edge m_edge;


    Attachment( Edge e )
    {
        m_edge = e;
    }

    abstract double calculatePosition( Constraint constraint );
    abstract double getDelta();
    abstract double getWeight();

    final void attach( Constraint constraint, Attachment a )
    {
        m_edge.attach( constraint, a );
    }

    double followAttachment( Edge start, Constraint constraint, double weight, double delta, boolean dontCrossComponent, Collection terminatedEquations, boolean trace )
    {
        return terminate( start, constraint, weight, delta, terminatedEquations, trace );
    }

    final Edge getEdge()
    {
        return m_edge;
    }

    final double getRequestedSpan( Constraint constraint, boolean trace )
    {
        double weight = getWeight();
        double delta = getDelta();

        log( "\nSTART      ", weight, delta, this, false, trace );

        double r = getRequestedSpan( m_edge, constraint, weight, delta, trace );

        log( "DONE       ", weight, delta, this, false, trace );

        return r;
    }

    double getRequestedSpan( Edge start, Constraint constraint, double weight, double delta, boolean trace )
    {
        return getRequestedSpan( start, constraint, weight, delta, false, null, trace );
    }

    double getRequestedSpan( Edge start, Constraint constraint, double weight, double delta, boolean dontCrossComponent, Collection terminatedEquations, boolean trace )
    {
        double r = followAttachment( start, constraint, weight, delta, dontCrossComponent, terminatedEquations, trace );

        if
            ( ! dontCrossComponent )
        {
            r = Math.max( r, m_edge.crossComponent( start, constraint, weight, delta, terminatedEquations, trace ) );
        }

        return r;
    }

    final void log( String action, double weight, double delta, Object where, boolean eval, boolean trace )
    {
        if
            ( trace )
        {
            System.err.print( action + weight + " * W = z + " + delta );
            if ( eval ) System.err.print( " -> " + ( delta / weight ) );
            System.err.println( "           " + where );
        }
    }

    double solve( Constraint constraint, double weight, double delta, boolean trace )
    {
        return delta / weight;
    }

    final double terminate( Edge start, Constraint constraint, double weight, double delta, Collection terminatedEquations, boolean trace )
    {
        weight += - getWeight();
        delta += - getDelta();

        if ( terminatedEquations != null ) terminatedEquations.add( new Equation( weight, delta ) );

        double r = 0;

        if
            ( start == m_edge )
        {
            log( "SAME EDGE  ", weight, delta, this, false, trace );
        } else if
            ( weight == 0 )
        {
            log( "NO SOL     ", weight, delta, this, false, trace );
        } else {
            log( "TERMINATE  ", weight, delta, this, true, trace );

            r = solve( constraint, weight, delta, trace );
        }

        return r;
    }

    void validate( AttachableContainer e, Constraint constraint )
    {
    }
}
