package nu.esox.gui.layout;


import java.io.Serializable;


public class SizeRequirements implements Serializable
{
    public int minimum;
    public int preferred;
    public int maximum;

    
    public SizeRequirements()
    {
        minimum = 0;
        preferred = 0;
        maximum = 0;
    }

    public SizeRequirements( int min, int pref, int max, float a )
    {
        minimum = min;
        preferred = pref;
        maximum = max;
    }

    public String toString()
    {
        return "[" + minimum + "," + preferred + "," + maximum + "]";
    }

    static final long serialVersionUID = 42;
}
