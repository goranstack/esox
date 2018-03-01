package nu.esox.gui.example.formattedtextfieldadapter;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;

import net.miginfocom.swing.MigLayout;
import nu.esox.gui.ModelPanel;
import nu.esox.gui.aspect.FormattedTextFieldAdapter;
import nu.esox.util.Observable;

/**
 * 
 * @author G Stack
 *
 */
public class FormattedTextFieldAdapterTest {

	public static void main(String[] args) {
		JFrame window = new JFrame("Numbers");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NumberModel model = new NumberModel();
		NumberPanel panel1 = new NumberPanel();
		panel1.setModel(model);
		NumberPanel panel2 = new NumberPanel();
		panel2.setModel(model);
		window.getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, panel2));
		window.setLocation(100, 100);
		window.pack();
		window.setVisible(true);
	}
	
	@Test
	public void test()
	{
		NumberPanel panel = new NumberPanel();
		NumberModel model = new NumberModel();
		panel.setModel(model);
		model.setaBigDecimal(new BigDecimal(123456));
		model.setaBigDecimal(null);
		panel.setModel(null);
	}

	private static class NumberPanel extends ModelPanel {
		public NumberPanel() {
			setLayout(new MigLayout("wrap 2"));
			
			add("primitiveInt", int.class);
			add("primitiveShort", short.class);
			add("primitiveLong", long.class);
			add("primitiveFloat", float.class);
			add("primitiveDouble", double.class);
			add("primitiveByte", byte.class);

			add("anInteger", Integer.class);
			add("aShort", Short.class);
			add("aLong", Long.class);
			add("aFloat", Float.class);
			add("aDouble", Double.class);
			add("aByte", Byte.class);
			add("aBigDecimal", BigDecimal.class);
			add("aBigInteger", BigInteger.class);
		}
		
		private void add(String aspectName, Class aspectClass)
		{
			add(aspectName, aspectClass, null, null);		
		}

		
		private void add(String aspectName, Class aspectClass, Object nullValue, Object undefinedValue)
		{
			add(new JLabel(aspectName));
			JFormattedTextField field = new JFormattedTextField();
			field.setColumns(20);
			new FormattedTextFieldAdapter(field, this, NumberModel.class, "get" + capitalizeFirstLetter(aspectName), 
					"set"+ capitalizeFirstLetter(aspectName), aspectClass, aspectName, nullValue, undefinedValue);
			add(field);			
		}
		
	  /**
	   * Capitalize the first letter but leave the rest as they are.<br> 
	   * For example <code>fooBar</code> becomes <code>FooBar</code>. 
	   */
		private static String capitalizeFirstLetter(String data) {
			if (data.startsWith("primitive") || data.startsWith("an")) {
				String firstLetter = data.substring(0, 1).toUpperCase();
				String restLetters = data.substring(1);
				return firstLetter + restLetters;
			} else
				return data;
		}
	}

	public static class NumberModel extends Observable {
		private int primitiveInt;
		private short primitiveShort;
		private long primitiveLong;
		private float primitiveFloat;
		private double primitiveDouble;
		private byte primitiveByte;
		private Integer anInteger;
		private Short aShort;
		private Long aLong;
		private Float aFloat;
		private Double aDouble;
		private Byte aByte;
		private BigDecimal aBigDecimal;
		private BigInteger aBigInteger;

		public int getPrimitiveInt() {
			return primitiveInt;
		}

		public void setPrimitiveInt(int primitiveInt) {
			if (this.primitiveInt != primitiveInt) {
				this.primitiveInt = primitiveInt;
				fireValueChanged("primitiveInt", primitiveInt);
			}
		}

		public short getPrimitiveShort() {
			return primitiveShort;
		}

		public void setPrimitiveShort(short primitiveShort) {
			if (this.primitiveShort != primitiveShort) {
				this.primitiveShort = primitiveShort;
				fireValueChanged("primitiveShort", primitiveShort);
			}
		}

		public long getPrimitiveLong() {
			return primitiveLong;
		}

		public void setPrimitiveLong(long primitiveLong) {
			if (this.primitiveLong != primitiveLong) {
				this.primitiveLong = primitiveLong;
				fireValueChanged("primitiveLong", primitiveLong);
			}
		}

		public float getPrimitiveFloat() {
			return primitiveFloat;
		}

		public void setPrimitiveFloat(float primitiveFloat) {
			if (this.primitiveFloat != primitiveFloat) {
				this.primitiveFloat = primitiveFloat;
				fireValueChanged("primitiveFloat", primitiveFloat);
			}
		}

		public double getPrimitiveDouble() {
			return primitiveDouble;
		}

		public void setPrimitiveDouble(double primitiveDouble) {
			if (this.primitiveDouble != primitiveDouble) {
				this.primitiveDouble = primitiveDouble;
				fireValueChanged("primitiveDouble", primitiveDouble);
			}
		}

		public byte getPrimitiveByte() {
			return primitiveByte;
		}

		public void setPrimitiveByte(byte primitiveByte) {
			if (this.primitiveByte != primitiveByte) {
				this.primitiveByte = primitiveByte;
				fireValueChanged("primitiveByte", primitiveByte);
			}
		}

		public Integer getAnInteger() {
			return anInteger;
		}

		public void setAnInteger(Integer anInteger) {
			System.out.println("setAnInteger " + anInteger);
			if (!ObjectUtils.equals(this.anInteger, anInteger))
			{
				this.anInteger = anInteger;
				fireValueChanged("anInteger", this.anInteger);
			}
		}

		public Short getaShort() {
			return aShort;
		}

		public void setaShort(Short aShort) {
			if (!ObjectUtils.equals(this.aShort, aShort))
			{
				this.aShort = aShort;
				fireValueChanged("aShort", this.aShort);
			}
		}

		public Long getaLong() {
			return aLong;
		}

		public void setaLong(Long aLong) {
			if (!ObjectUtils.equals(this.aLong, aLong))
			{
				this.aLong = aLong;
				fireValueChanged("aLong", this.aLong);
			}
		}

		public Float getaFloat() {
			return aFloat;
		}

		public void setaFloat(Float aFloat) {
			if (!ObjectUtils.equals(this.aFloat, aFloat))
			{
				this.aFloat = aFloat;
				fireValueChanged("aFloat", this.aFloat);
			}
		}

		public Double getaDouble() {
			return aDouble;
		}

		public void setaDouble(Double aDouble) {
			if (!ObjectUtils.equals(this.aDouble, aDouble))
			{
				this.aDouble = aDouble;
				fireValueChanged("aDouble", this.aDouble);
			}
		}

		public Byte getaByte() {
			return aByte;
		}

		public void setaByte(Byte aByte) {
			if (!ObjectUtils.equals(this.aByte, aByte))
			{
				this.aByte = aByte;
				fireValueChanged("aByte", this.aByte);
			}
		}

		public BigDecimal getaBigDecimal() {
			return aBigDecimal;
		}

		public void setaBigDecimal(BigDecimal aBigDecimal) {
			if (!ObjectUtils.equals(this.aBigDecimal, aBigDecimal))
			{
				this.aBigDecimal = aBigDecimal;
				fireValueChanged("aBigDecimal", this.aBigDecimal);
			}
		}

		public BigInteger getaBigInteger() {
			return aBigInteger;
		}

		public void setaBigInteger(BigInteger aBigInteger) {
			if (!ObjectUtils.equals(this.aBigInteger, aBigInteger))
			{
				this.aBigInteger = aBigInteger;
				fireValueChanged("aBigInteger", this.aBigInteger);
			}
		}

	}

}
