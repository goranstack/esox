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
   * @author  Dennis Malmström
   * @version 2.0
   */
class Equation
{
	public final double m_weight;
	public final double m_delta;

    public Equation( double weight, double delta )
    {
        m_weight = weight;
        m_delta = delta;
    }
    
    public String toString()
    {
        String str = m_weight + " * W = z + " + m_delta;
        return str;
    }
}
