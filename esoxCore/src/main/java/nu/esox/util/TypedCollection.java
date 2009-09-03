package nu.esox.util;

import java.io.*;
import java.util.*;


@SuppressWarnings("unchecked")
public class TypedCollection<T> implements Collection<T>
{
    protected Collection m_source;

    public TypedCollection( Collection source )
    {
        m_source = source;
    }
    

    public boolean	add( T o ) { throw new UnsupportedOperationException(); }
    public boolean	addAll( Collection<? extends T> c ) { throw new UnsupportedOperationException(); }
    public void	clear() { throw new UnsupportedOperationException(); }
    public boolean	contains( Object o ) { return m_source.contains( o ); }
    public boolean	containsAll( Collection<?> c ) { return m_source.containsAll( c ); }
    public boolean	equals( Object o ) { return m_source.equals( o ); }
    public int	hashCode() { return m_source.hashCode(); }
    public boolean	isEmpty() { return m_source.isEmpty(); }
    public Iterator<T>	iterator() { return (Iterator<T>) m_source.iterator(); }
    public boolean	remove( Object o ) { throw new UnsupportedOperationException(); }
    public boolean	removeAll( Collection<?> c ) { throw new UnsupportedOperationException(); }
    public boolean	retainAll( Collection<?> c ) { throw new UnsupportedOperationException(); }
    public int	size() { return m_source.size(); }
    public Object[]	toArray() { return m_source.toArray(); }
    public <T> T[] toArray( T[] a ) { return (T[]) m_source.toArray( a ); }
}
