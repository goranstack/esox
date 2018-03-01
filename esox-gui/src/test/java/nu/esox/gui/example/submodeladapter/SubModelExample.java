package nu.esox.gui.example.submodeladapter;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import nu.esox.gui.ModelPanel;
import nu.esox.gui.TextFieldFocusHandler;
import nu.esox.gui.aspect.CheckBoxAdapter;
import nu.esox.gui.aspect.SubModelAdapter;
import nu.esox.gui.aspect.TextFieldAdapter;
import nu.esox.gui.layout.ColumnLayout;
import nu.esox.util.Observable;
import nu.esox.util.ObservableEvent;
import nu.esox.util.ObservableListener;

import org.apache.commons.lang.ObjectUtils;

/**
 * Example of how to use a SubModelAdapater
 * 
 * @author G Stack
 *
 */
public class SubModelExample
{

	/**
	 * Abstract super class to Car end Engine since they have 
	 * some common aspects.
	 */
	static abstract class CommonModel extends Observable
	{
		boolean aspect1 = false;
		String aspect2;
		
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
	 * The addObservableListener call to the engine is not necessary to get
	 * this example to work, but it shows how to get notified
	 * when the child model is changed.
	 *
	 */
	static class Car extends CommonModel
	{
		Engine engine;

		public Car()
		{
			engine = new Engine();
			engine.addObservableListener(new ObservableListener() {
				
				@Override
				public void valueChanged(ObservableEvent e) {
					System.out.println(DateFormat.getTimeInstance().format(new Date()) + " Engine has changed");
					
				}
			});
		}
		

		public Engine getEngine()
		{
			return engine;
		}
		
				
	}
	
	static class Engine extends CommonModel
	{
				
	}

	
	/**
	 * Abstract super class to CarPanel end EnginePanel since they have 
	 * some common aspects.
	 */
	static abstract class CommonPanel extends ModelPanel
	{
		JCheckBox checkBox;
		JTextField textField;
		
		/**
		 * Both the car and the engine has a a boolean and a string aspect in this
		 * example. Create UI components and binding for these aspects in this constructor
		 * that car and engine inherit.
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
		EnginePanel enginePanel;
		
		public CarPanel() 
		{
			super();
			enginePanel = new EnginePanel();
			add(enginePanel);
			new SubModelAdapter(enginePanel, "setEngine", Engine.class, this, Car.class, "getEngine", null);
		}
	}
	
	/**
	 * The EnginePanel has check box and a text field both inherited from the 
	 * super class.
	 *
	 */
	static class EnginePanel extends CommonPanel
	{
		
		public EnginePanel() 
		{
			super();
			setBorder(BorderFactory.createTitledBorder("Engine"));
		}
		
		/**
		 * This method is called by the SubModelAdapter using reflection
		 */
		public void setEngine(Engine engine)
		{
			setModel(engine);
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
