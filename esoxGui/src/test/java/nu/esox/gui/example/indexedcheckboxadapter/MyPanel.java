package nu.esox.gui.example.indexedcheckboxadapter;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import nu.esox.gui.ModelPanel;
import nu.esox.gui.aspect.IndexedCheckBoxAdapter;
import nu.esox.gui.layout.ColumnLayout;

public class MyPanel extends ModelPanel {

	public MyPanel() {
		setLayout(new ColumnLayout());
		JCheckBox checkbox1 = new JCheckBox("Indexed Checkbox 1");
		JCheckBox checkbox2 = new JCheckBox("Indexed Checkbox 2");
		JCheckBox checkbox3 = new JCheckBox("Indexed Checkbox 3");
		add(checkbox1);
		add(checkbox2);
		add(checkbox3);
		new IndexedCheckBoxAdapter(0, checkbox1, this, MyModel.class, "isMyBoolean", "setMyBoolean", Boolean.class, "myboolean", true, false, null, false);
		new IndexedCheckBoxAdapter(1, checkbox2, this, MyModel.class, "isMyBoolean", "setMyBoolean", Boolean.class, "myboolean", true, false, null, false);
		new IndexedCheckBoxAdapter(2, checkbox3, this, MyModel.class, "isMyBoolean", "setMyBoolean", Boolean.class, "myboolean", Boolean.TRUE, Boolean.FALSE, null, Boolean.FALSE);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Indexed Checkbox Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ModelPanel leftPanel = new MyPanel();
		ModelPanel rightPanel = new MyPanel();
		MyModel model = new MyModel();
		leftPanel.setModel(model);
		rightPanel.setModel(model);
		frame.getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel));
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
