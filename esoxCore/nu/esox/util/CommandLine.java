package nu.esox.util;

import java.util.*;


public class CommandLine extends HashMap<String,Object>
{
    public CommandLine( String [] args )
    {
        add( args );
    }

    public void add( String [] args )
    {
        String name = null;

        int i = 0;
        while ( i < args.length && ! args[ i ].startsWith( "-" ) ) i++;
        
        while
            ( i < args.length )
        {
            name = args[ i ];
            i++;

            int n = i;
            while ( i < args.length && ! args[ i ].startsWith( "-" ) ) i++;
            
            if
                ( n == i )
            {
                put( name, "" );
            } else if
                ( n + 1 == i )
            {
                put( name, args[ n ] );
            } else {
                List l = new ArrayList();
                for ( int j = n; j < i; j++ ) l.add( args[ j ] );
                put( name, l );
            }
        }
    }



    public static void main( String [] args )
    {
        System.err.println( new CommandLine( args ) );
    }
}
