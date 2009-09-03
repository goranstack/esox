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
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */

public final class EditorOperations
{
    public static boolean setOffset( ContainerAttachment a, double offset )
    {
        return a.setOffset( offset );
    }

    public static boolean setOffset( SiblingAttachment a, double offset )
    {
        return a.setOffset( offset );
    }

    public static boolean setOffset( RelativeAttachment a, double offset )
    {
        return a.setOffset( offset );
    }

    public static boolean setPosition( RelativeAttachment a, double position )
    {
        return a.setPosition( position );
    }

    public static boolean isNullAttachment( Attachment a )
    {
        return a instanceof NullAttachment;
    }

    public static void setAttachment( Constraint c, Attachment a )
    {
        c.setAttachment( a );
    }

    public static void unsetAttachment( Constraint c, Edge e )
    {
        setAttachment( c, NullAttachment.create( e ) );
    }
}

