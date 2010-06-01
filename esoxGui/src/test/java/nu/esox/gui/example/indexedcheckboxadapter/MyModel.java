package nu.esox.gui.example.indexedcheckboxadapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

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
		if (!ObjectUtils.equals(newValue, isMyBoolean(index)))
		{
			booleans.set(index, newValue);
			fireValueChanged("myboolean", newValue);
		}
	}
	
}
