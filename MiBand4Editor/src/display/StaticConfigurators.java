package display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MiBand4Editor;

public class StaticConfigurators {

	public static boolean askForBoolean(String title, String message, String buttonText) {
		GridBagLayout layout = new GridBagLayout();
		JDialog frame = new JDialog();
		frame.setTitle(title);
		JPanel panel = new JPanel(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.anchor=GridBagConstraints.CENTER;
		JLabel label = new JLabel(message);
		panel.add(label);
		JCheckBox box = new JCheckBox();
		box.setSelected(true);
		panel.add(box);
		constraints.gridx=0;
		constraints.gridy=1;
		layout.setConstraints(box, constraints);
		JButton ok = new JButton(buttonText);
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		panel.add(ok);
		constraints.gridy=2;
		constraints.gridx=0;
		layout.setConstraints(ok, constraints);
		frame.add(panel);
		frame.setModal(true);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		return box.isSelected();
	}
	
	public static String askForAlignment(MiBand4Editor parent) {
		GridBagLayout layout = new GridBagLayout();
		JDialog frame = new JDialog();
		frame.setTitle(parent.getInLang("edit_alignment_title"));
		JPanel panel = new JPanel(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.fill=GridBagConstraints.BOTH;
		JLabel label = new JLabel(parent.getInLang("edit_alignment"));
		panel.add(label);
		String[] options = new String[] {parent.getInLang("edit_alignment_left"),
				parent.getInLang("edit_alignment_right"),
				parent.getInLang("edit_alignment_hCenter"),
				parent.getInLang("edit_alignment_top"),
				parent.getInLang("edit_alignment_bottom"),
				parent.getInLang("edit_alignment_vCenter"),
				parent.getInLang("edit_alignment_topCenter"),
				parent.getInLang("edit_alignment_topLeft"),
				parent.getInLang("edit_alignment_topRight"),
				parent.getInLang("edit_alignment_center"),
				parent.getInLang("edit_alignment_centerLeft"),
				parent.getInLang("edit_alignment_centerRight"),
				parent.getInLang("edit_alignment_bottomCenter"),
				parent.getInLang("edit_alignment_bottomLeft"),
				parent.getInLang("edit_alignment_bottomRight")};
		JComboBox<String> box = new JComboBox<String>(options);
		box.setSelectedIndex(7);
		panel.add(box);
		constraints.gridx=0;
		constraints.gridy=1;
		layout.setConstraints(box, constraints);
		JButton ok = new JButton(parent.getInLang("edit_accept"));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
			}
		});
		panel.add(ok);
		constraints.gridy=2;
		constraints.gridx=0;
		layout.setConstraints(ok, constraints);
		frame.add(panel);
		frame.setModal(true);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		switch(box.getSelectedIndex()) {
		case 0:
			return "Left";
		case 1:
			return "Right";
		case 2:
			return "HCenter";
		case 3:
			return "Top";
		case 4:
			return "Bottom";
		case 5:
			return "VCenter";
		case 6:
			return "TopCenter";
		case 7:
			return "TopLeft";
		case 8:
			return "TopRight";
		case 9:
			return "Center";
		case 10:
			return "CenterLeft";
		case 11:
			return "CenterRight";
		case 12:
			return "BottomCenter";
		case 13:
			return "BottomLeft";
		case 14:
			return "BottomRight";
		}
		//Should not be reached
		return (String) box.getSelectedItem();
	}
	
}
