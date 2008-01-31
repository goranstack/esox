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

import java.awt.geom.Dimension2D;
import java.util.*;
import java.util.List;

/**
 * 
 * 
   * @author  Dennis Malmström
 * @version 2.0
 */
public class AttachableContainer
{
    private boolean m_areConstraintsValid;
    private final List m_constraints = new ArrayList(); // [ Constraints ]
    private boolean m_isLayoutValid;
    private boolean m_isSizeValid;
    private final Dimension2D m_size = new Size();

	

      // Constraints set operations
    
    public void add( Attachable a, Constraint constraint )
    {
        remove( a );
        if
            ( constraint == null )
        {
            m_constraints.add( new Constraint( a ) );
        } else {
            m_constraints.add( new Constraint( a, constraint ) );
        }
	
        invalidateAttachments();
        invalidateLayoutAndSize();
    }

    public void remove( Attachable a )
    {
        remove( indexOf( a ) );
    }

    protected final void remove( int i )
    {
        if
            ( i != -1 )
        {
            m_constraints.remove( i );
            invalidateAttachments();
            invalidateLayoutAndSize();
        }
    }
    
    public final List getConstraints()
    {
        return Collections.unmodifiableList( m_constraints );
    }
    
    protected final int getConstraintCount()
    {
        return m_constraints.size();
    }
    
    protected final Constraint getConstraint( int i )
    {
        return (Constraint) m_constraints.get( i );
    }

    
      // layout operations
    
    public void layout()
    {
        validateLayout();

        Iterator i = m_constraints.iterator();
        while
            ( i.hasNext() )
        {
            Constraint c = (Constraint) i.next();
            c.applyBounds();
        }
    }
    
    public Dimension2D getPreferredLayoutSize()
    {
        validateSize();
        return m_size;
    }




    
    
    private int indexOf( Attachable a )
    {
        int I = m_constraints.size();
        for
            ( int i = 0; i < I; i++ )
        {
            if
                ( a.equals( ( (Constraint) m_constraints.get( i ) ).getAttachable() ) )
            {
                return i;
            }
        }

        return -1;
    }



    
    Constraint getConstraintFor( Attachable a )
    {
        int n = indexOf( a );
        if
            ( n == -1 )
        {
            return null;
        } else {
            return (Constraint) m_constraints.get( n );
        }
    }


    

    protected void invalidateLayoutAndSize()
    {
        m_isSizeValid = false;
        m_isLayoutValid = false;
    }

    protected void invalidateAttachments()
    {
        m_areConstraintsValid = false;
    }



    private void validateConstraints()
    {
        if
            ( ! m_areConstraintsValid )
        {	
            Iterator i = m_constraints.iterator();
            while
                ( i.hasNext() )
            {
                Constraint c = (Constraint) i.next();

                c.validate( this );
            }

            m_areConstraintsValid = true;
        }
    }

    private void validateLayout()
    {
        if
            ( ! m_isLayoutValid )
        {
            validateConstraints();
		
            Iterator i = m_constraints.iterator();
            while
                ( i.hasNext() )
            {
                Constraint c = (Constraint) i.next();
                c.invalidateLayout();
            }
		
            i = m_constraints.iterator();
            while
                ( i.hasNext() )
            {
                Constraint c = (Constraint) i.next();
                c.validateLayout();
            }
		
            m_isLayoutValid = true;
        }
    }

    private void validateSize()
    {
        if ( m_isSizeValid ) return;
	
        validateConstraints();
	
        Dimension2D d = new Size();
        Iterator i = m_constraints.iterator();
        while
            ( i.hasNext() )
        {
            Constraint c = (Constraint) i.next();
            c.calculateRequests( d );
        }

        m_size.setSize( d.getWidth(), d.getHeight() );
        
        m_isSizeValid = true;
    }






    
    
    private static class Size extends Dimension2D
    {
        private double m_width;
        private double m_height;
        
        public void setSize( double w, double h )
        {
            m_width = w;
            m_height = h;
        }
        
        public double getWidth()
        {
            return m_width;
        }
        
        public double getHeight()
        {
            return m_height;
        }
    };

}
