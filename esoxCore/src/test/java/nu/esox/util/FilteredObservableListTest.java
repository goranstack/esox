package nu.esox.util;

import static org.junit.Assert.*;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.junit.Test;

/**
 * Testing to let a Observable model act as a filter for a FilteredObservableList
 * 
 * @author Goran Stack
 *
 */
public class FilteredObservableListTest {

	@Test
	public void test()
	{
		final MutableBoolean valueChangedCalled = new MutableBoolean(false);
		ObservableList<String> source = new ObservableList<String>();
		source.add("aaaaaaa");
		source.add("bbbbbbb");
		source.add("ccccccc");
		FilterBarModel<String> filter = new FilterBarModel<String>();
		FilteredObservableList<String> filteredList = new FilteredObservableList<String>(source, filter)
		{
			@Override
			public void valueChanged(ObservableEvent ev) {
				super.valueChanged(ev);
				valueChangedCalled.setValue(true);
			}
		};
		
		assertTrue("3 elements in filtered list", filteredList.size() == 3);
		filter.setSelectedFilter(new FilteredObservableList.Filter<String>(){

			@Override
			public boolean pass(String o) {
				return o.startsWith("a");
			}});
		assertTrue("valueChanged called", valueChangedCalled.booleanValue());
		assertTrue("1 elements in filtered list", filteredList.size() == 1);
	}
	
	/**
	 * FilterBarModel<T> is used as model for a panel where a filter can be selected from a set of filters
	 * 
	 * @param <T> The type of the elements that is filtered by the filter
	 */
	private static class FilterBarModel<T> extends Observable implements FilteredObservableList.Filter<T>
	{
		private FilteredObservableList.Filter<T> selectedFilter;
		
		public FilterBarModel() 
		{
			selectedFilter = new FilteredObservableList.Filter<T>(){

				@Override
				public boolean pass(T o) {
					return true;
				}};
		}

		public void setSelectedFilter(FilteredObservableList.Filter<T> selectedFilter) 
		{
			if (!ObjectUtils.equals(this.selectedFilter, selectedFilter))
			{
				this.selectedFilter = selectedFilter;
				fireValueChanged("selectedFilter", selectedFilter);
			}
		}

		@Override
		public boolean pass(T o) {
			return selectedFilter.pass(o);
		}

		
		
	}

}
