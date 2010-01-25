package nu.esox.gui.example.indexedcheckboxadapter;

import java.util.ArrayList;
import java.util.List;

import nu.esox.util.Observable;

/**
 * Example that shows how to bind a property with an index
 * 
 * @author G Stack
 * 
 */
public class MyModel extends Observable {

	private List<Boolean> booleans;

	public MyModel() {
		booleans = new ArrayList<Boolean>();
		booleans.add(new Boolean(false));
		booleans.add(new Boolean(true));
		booleans.add(new Boolean(false));		
	}
	
	public Boolean isMyBoolean(int index)
	{
		return booleans.get(index);
	}
	
	public void setMyBoolean(int index, Boolean newValue)
	{
		if (!equals(newValue, isMyBoolean(index)))
		{
			booleans.set(index, newValue);
			fireValueChanged("myboolean", newValue);
		}
	}
	
	private static boolean equals(Object o1, Object o2) {
		if (o1 == o2)
			return true;
		if (o1 == null)
			return false;
		return o1.equals(o2);
	}
}
