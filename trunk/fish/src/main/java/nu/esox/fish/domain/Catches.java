package nu.esox.fish.domain;

import nu.esox.util.*;


@SuppressWarnings( "serial" )
public class Catches extends ObservableList<Catch>
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
}
