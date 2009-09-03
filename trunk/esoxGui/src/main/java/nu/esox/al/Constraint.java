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


  /**
   * Used for relating attachments to attachables.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */

public final class Constraint
{
    private final static double INVALID = Double.MAX_VALUE;
    private final static double PENDING = - Double.MAX_VALUE;

    private Attachment m_top = NullAttachment.TOP;
    private Attachment m_bottom = NullAttachment.BOTTOM;
    private Attachment m_left = NullAttachment.LEFT;
    private Attachment m_right = NullAttachment.RIGHT;
	
    private Attachable m_attachable;

    double m_x0 = INVALID;
    double m_x1 = INVALID;
    double m_y0 = INVALID;
    double m_y1 = INVALID;

      /**
       * Constructor specifying no attachments.
       */
    public Constraint()
    {
        this( null, null, null, null, null );
    }

    
      /**
       * Constructor specifying one attachment.
       * 
       * @param a0 An attachment.
       */
    public Constraint( Attachment a0 )
    {
        this( null, a0, null, null, null );
    }


      /**
       * Constructor specifying two attachments.
       * 
       * @param a0 An attachment.
       * @param a1 An attachment.
       */
    public Constraint( Attachment a0, Attachment a1 )
    {
        this( null, a0, a1, null, null );
    }

    
      /**
       * Constructor specifying three attachments.
       * 
       * @param a0 An attachment.
       * @param a1 An attachment.
       * @param a2 An attachment.
       */
    public Constraint( Attachment a0, Attachment a1, Attachment a2 )
    {
        this( null, a0, a1, a2, null );
    }

    
      /**
       * Constructor specifying four attachments.
       * 
       * @param a0 An attachment.
       * @param a1 An attachment.
       * @param a2 An attachment.
       * @param a3 An attachment.
       */
    public Constraint( Attachment a0, Attachment a1, Attachment a2, Attachment a3 )
    {
        this( null, a0, a1, a2, a3 );
    }


    Constraint( Attachable attachable )
    {
        this( attachable, null, null, null, null );
    }
    
    Constraint( Attachable attachable, Attachment a0, Attachment a1, Attachment a2, Attachment a3 )
    {
        m_attachable = attachable;

        if ( a0 != null ) a0.attach( this, a0 );
        if ( a1 != null ) a1.attach( this, a1 );
        if ( a2 != null ) a2.attach( this, a2 );
        if ( a3 != null ) a3.attach( this, a3 );
    }

    Constraint( Attachable attachable, Constraint a )
    {
        this( attachable, a.m_left, a.m_right, a.m_top, a.m_bottom );
    }

    void calculateRequests( java.awt.geom.Dimension2D size )
    {
        double w = Math.max( m_left.getRequestedSpan( this, false ), size.getWidth() );
        w = Math.max( m_right.getRequestedSpan( this, false ), w );
	
        double h = Math.max( m_top.getRequestedSpan( this, false ), size.getHeight() );
        h = Math.max( m_bottom.getRequestedSpan( this, false ), h );

        size.setSize( w, h );
    }

    public final Attachable getAttachable()
    {
        return m_attachable;
    }

    public Attachment getBottom()
    {
        return m_bottom;
    }

    public Attachment getLeft()
    {
        return m_left;
    }

    public Attachment getRight()
    {
        return m_right;
    }

    public Attachment getTop()
    {
        return m_top;
    }

    double getX0()
    {
        if ( m_x0 == PENDING ) return handleRecursiveAttachment( "left" );

        if
            ( m_x0 == INVALID )
        {
            m_x0 = PENDING;
            m_x0 = m_left.calculatePosition( this );
        }
	
        return m_x0;
    }

    double getX1()
    {
        if ( m_x1 == PENDING ) return handleRecursiveAttachment( "right" );

        if
            ( m_x1 == INVALID )
        {
            m_x1 = PENDING;
            m_x1 = m_right.calculatePosition( this );
        }
	
        return m_x1;
    }

    double getY0()
    {
        if ( m_y0 == PENDING ) return handleRecursiveAttachment( "top" );

        if
            ( m_y0 == INVALID )
        {
            m_y0 = PENDING;
            m_y0 = m_top.calculatePosition( this );
        }
	
        return m_y0;
    }

    double getY1()
    {
        if ( m_y1 == PENDING ) return handleRecursiveAttachment( "bottom" );

        if
            ( m_y1 == INVALID )
        {
            m_y1 = PENDING;
            m_y1 = m_bottom.calculatePosition( this );
        }
	
        return m_y1;
    }

    private double handleRecursiveAttachment( String edge )
    {
        throw new AttachmentError( "Recursive attachment involving " + edge + " edge of attachable " + m_attachable.getId() );
    }

    void invalidateLayout()
    {
        m_x0 = m_x1 = m_y0 = m_y1 = INVALID;
    }

    void setAttachment( Attachment a )
    {
        if ( a != null ) a.attach( this, a );
        m_attachable.invalidate();
    }

    void setBottom( Attachment as )
    {
        m_bottom = as;
    }

    void applyBounds()
    {
        m_attachable.setBounds( m_x0, m_y0, m_x1 - m_x0, m_y1 - m_y0 );
    }

    void setLeft( Attachment as )
    {
        m_left = as;
    }

    void setRight( Attachment as )
    {
        m_right = as;
    }

    void setTop( Attachment as )
    {
        m_top = as;
    }

    void validate( AttachableContainer e )
    {
        m_top.validate( e, this );
        m_bottom.validate( e, this );
        m_left.validate( e, this );
        m_right.validate( e, this );
    }

    void validateLayout()
    {
        getX0();
        getX1();
        getY0();
        getY1();
    }


    static class Comparator
    {
        private Attachable m_attachable;
		
        public Comparator( Attachable a )
        {
            m_attachable = a;
        }
		
        public boolean equals( Object o )
        {
            return m_attachable == ( (Constraint) o ).m_attachable;
        }
    };
	
}
