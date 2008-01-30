package nu.esox.gui.examples.fish.domain;

import nu.esox.util.*;


public class Catches extends ObservableList
{
    public Catches()
    {
    }

    public Catch add()
    {
        Catch c = new Catch();
        add( c );
        return c;
    }

    public Catch getCatch( int i )
    {
        return (Catch) get( i );
    }
}
