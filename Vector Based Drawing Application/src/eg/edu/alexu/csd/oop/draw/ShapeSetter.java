package eg.edu.alexu.csd.oop.draw;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class ShapeSetter extends JFrame {
	private Color stroke = Color.black;
	private Color fill = Color.black;
	private Object obj = MyDrawer.shape;
	private Map<String, Double> draft = ((Shape) obj).getProperties();
	private JTextField textField1,textField2,textField3,textField4;
	
	public static void main(String[] args) {
		new ShapeSetter();
	}
	
	

	// private labelName="";
	// private fieldName;
	public ShapeSetter() {
		this.setSize(350, 330);
		setResizable(false);
		this.requestFocusInWindow(); 
		this.setTitle("Properties");
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		getContentPane().setLayout(null);
		((Shape) obj).setColor(stroke);
		((Shape) obj).setFillColor(fill);
		//final Circle obj = new Circle();

		final JButton strokeSample = new JButton("");
		strokeSample.setBounds(61, 267, 27, 23);
		getContentPane().add(strokeSample);
		strokeSample.setEnabled(false);
		strokeSample.setBackground(stroke);
		
		final JButton fillSample = new JButton("");
		fillSample.setBounds(164, 267, 27, 23);
		getContentPane().add(fillSample);
		fillSample.setEnabled(false);
		fillSample.setBackground(stroke);
		
		JButton btnNewButton = new JButton("Stroke");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stroke = JColorChooser.showDialog(null,
						"Pick a Stroke", Color.BLACK);
				((Shape) obj).setColor(stroke);
				strokeSample.setBackground(stroke);
				
			}
		});
		btnNewButton.setBounds(30, 233, 89, 23);
		getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Fill");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fill = JColorChooser.showDialog(null,
						"Pick a Fill", Color.BLACK);
				((Shape) obj).setFillColor(fill);
				fillSample.setBackground(fill);
			}
		});
		btnNewButton_1.setBounds(135, 233, 89, 23);
		getContentPane().add(btnNewButton_1);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int j = 0;
				for (Entry<String, Double> entry : draft.entrySet()) {
					if (j == 0) {
						entry.setValue(Double.parseDouble(textField1.getText()));
					}
					if (j == 1) {
						entry.setValue(Double.parseDouble(textField2.getText()));
					}
					if (j == 2) {
						entry.setValue(Double.parseDouble(textField3.getText()));
					}
					if (j == 3) {
						entry.setValue(Double.parseDouble(textField4.getText()));
					}
					j++;
				}
				((Shape) obj).setProperties(draft);
				dispose();
			}
		});
	
		btnOk.setBounds(234, 233, 89, 23);
		getContentPane().add(btnOk);
		
		int i = 0;
		for (Entry<String, Double> entry : draft.entrySet()) {
			if (i == 0) {
				JLabel parameter1 = new JLabel(entry.getKey());
				parameter1.setBounds(49, 49, 58, 30);
				getContentPane().add(parameter1);

				textField1 = new JTextField();
				textField1.setBounds(134, 54, 86, 20);
				getContentPane().add(textField1);
				textField1.setColumns(10);
			}
			if (i == 1) {
				JLabel parameter2 = new JLabel(entry.getKey());
				parameter2.setBounds(49, 90, 58, 30);
				getContentPane().add(parameter2);

				textField2 = new JTextField();
				textField2.setColumns(10);
				textField2.setBounds(134, 95, 86, 20);
				getContentPane().add(textField2);
			}
			if (i == 2) {
				JLabel parameter3 = new JLabel(entry.getKey());
				parameter3.setBounds(49, 131, 58, 30);
				getContentPane().add(parameter3);

				textField3 = new JTextField();
				textField3.setColumns(10);
				textField3.setBounds(134, 136, 86, 20);
				getContentPane().add(textField3);
				this.setVisible(true);
			}
			if (i == 3) {
				JLabel parameter4 = new JLabel(entry.getKey());
				parameter4.setBounds(49, 172, 58, 30);
				getContentPane().add(parameter4);

				textField4 = new JTextField();
				textField4.setColumns(10);
				textField4.setBounds(134, 177, 86, 20);
				getContentPane().add(textField4);
				this.setVisible(true);
			}
			i++;
		}
	}
	

}