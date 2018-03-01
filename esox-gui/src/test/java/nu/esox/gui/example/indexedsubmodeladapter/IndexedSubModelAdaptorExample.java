package nu.esox.gui.example.indexedsubmodeladapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import nu.esox.gui.ModelPanel;
import nu.esox.gui.TextFieldFocusHandler;
import nu.esox.gui.aspect.CheckBoxAdapter;
import nu.esox.gui.aspect.IndexedAspect;
import nu.esox.gui.aspect.ModelOwnerIF;
import nu.esox.gui.aspect.SubModelAdapter;
import nu.esox.gui.aspect.TextFieldAdapter;
import nu.esox.gui.layout.ColumnLayout;
import nu.esox.util.Observable;
import nu.esox.util.ObservableEvent;
import nu.esox.util.ObservableListener;

import org.apache.commons.lang.ObjectUtils;

/**
 * This example shows how to customize an adapter to handle an aspect method with arguments.
 * Since the aspect is a separate object in the adaptor it's easy to plug in a 
 * special aspect. This technique is already used in the framework by the
 * IndexedCheckBoxAdapter and in this example we customize a SubModelAdapter
 * to bind to an indexed get-sub-model-method.
 * 
 * @author G Stack
 *
 */
public class IndexedSubModelAdaptorExample
{
	
	static class IndexedSubModelAdapter extends SubModelAdapter
	{
		public IndexedSubModelAdapter( int index,
				Object subModelTarget,
	            String setSubModelMethodName,
	            Class subModelClass,
	            ModelOwnerIF modelOwner,
	            Class modelClass,
	            String getAspectMethodName,
	            String aspectName
				) {
			super(subModelTarget, setSubModelMethodName, subModelClass, modelOwner, new IndexedAspect(index, modelClass, getAspectMethodName, null, subModelClass), aspectName);
		}
	}

	/**
	 * Abstract super class to Car end Passenger since they have 
	 * some common aspects.
	 */
	static abstract class CommonModel extends Observable
	{
		boolean aspect1 = false;
		String aspect2;
		
		public CommonModel() {
		}
		
		
		CommonModel(String aspect2) {
			super();
			this.aspect2 = aspect2;
		}

		public boolean isAspect1()
		{
			return aspect1;
		}
		
		public void setAspect1(boolean aspect1)
		{
			if (this.aspect1 != aspect1)
			{
				this.aspect1 = aspect1;
				fireValueChanged("aspect1", this.aspect1);
			}
		}
		
		public String getAspect2()
		{
			return aspect2;
		}
		
		public void setAspect2(String aspect2)
		{
			if (!ObjectUtils.equals(this.aspect2, aspect2))
			{
				this.aspect2 = aspect2;
				fireValueChanged("aspect2", this.aspect2);
			}
		}
		
		
	}
	
	/**
	 * The addObservableListener call to the passenger is not necessary to get
	 * this example to work, but it shows how to get notified
	 * when one of the passenger is changed.
	 *
	 */
	static class Car extends CommonModel
	{
		static String[] names = new String[]{"Göran", "Urban", "Johan", "Martha", "Josef", "Elisabeth", "Wille", "Dennis"};
		List<Passenger> passengers = new ArrayList<Passenger>();

		public Car()
		{			
			for (int i = 0; i < 8; i++) {						
				Passenger passenger = new Passenger(names[i]);
				passengers.add(passenger);
				passenger.addObservableListener(new ObservableListener() {
					@Override
					public void valueChanged(ObservableEvent e) {
						System.out.println(DateFormat.getTimeInstance().format(new Date()) + " A passenger has changed");
						
					}
				});
			}

		}
		

		public Passenger getPassenger(int index)
		{
			return passengers.get(index);
		}		
				
	}
	
	static class Passenger extends CommonModel
	{

		Passenger(String aspect2) {
			super(aspect2);
		}
				
	}

	
	/**
	 * Abstract super class to CarPanel end PassengerPanel since they have 
	 * some common aspects.
	 */
	static abstract class CommonPanel extends ModelPanel
	{
		JCheckBox checkBox;
		JTextField textField;
		
		/**
		 * Both the car and the passenger has a a boolean and a string aspect in this
		 * example. Create UI components and binding for these aspects in this constructor
		 * that car and passenger inherit.
		 */
		public CommonPanel()
		{
			setLayout(new ColumnLayout());
			checkBox = new JCheckBox("Aspect 1");
			textField = new JTextField(20);
			add(checkBox);
			add(textField);
			new CheckBoxAdapter(checkBox, this, Car.class, "isAspect1", "setAspect1", null);			
			new TextFieldAdapter(textField, this, Car.class, "getAspect2", "setAspect2", null);
			TextFieldFocusHandler.add(textField);
		}
		
	}
	
	static class CarPanel extends CommonPanel
	{
		public CarPanel() 
		{
			super();
			setBorder(BorderFactory.createTitledBorder("Car"));

			for (int i = 0; i < 8; i++) {
				PassengerPanel passengerPanel = new PassengerPanel();
				add(passengerPanel);
				new IndexedSubModelAdapter(i, passengerPanel, "setPassenger", Passenger.class, this, Car.class, "getPassenger", "passenger");				
			}
			
		}
	}
	
	/**
	 * The PassengerPanel has check box and a text field both inherited from the 
	 * super class.
	 *
	 */
	static class PassengerPanel extends CommonPanel
	{
		
		public PassengerPanel() 
		{
			super();
			setBorder(BorderFactory.createTitledBorder("Passenger"));
		}
		
		/**
		 * This method is called by the SubModelAdapter using reflection
		 */
		public void setPassenger(Passenger passenger)
		{
			setModel(passenger);
		}
	}

		
	public static void main(String[] args)
	{
		JFrame window = new JFrame("Car");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CarPanel carPanel = new CarPanel();
		carPanel.setModel(new Car());
		window.getContentPane().add(carPanel);
		window.setLocation(100, 100);
		window.pack();	
		window.setVisible(true);	
	}

}
