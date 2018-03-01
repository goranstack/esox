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
   * Protocol for rectangular object that is subject to attachment layout.
   * 
   * @author  Dennis Malmström
   * @version 2.0
   */

public interface Attachable
{
    void setBounds( double x, double y, double w, double h );
    String getId();
    double getContainerWidth();
    double getContainerHeight();
    double getPreferredWidth();
    double getPreferredHeight();
    double getX();
    double getY();
    void invalidate();
}

