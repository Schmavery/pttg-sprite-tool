package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.ImageData.ImageType;
import main.MainWindow;

public class NameTool extends Tool {
	
	JTextField nameField;

	public NameTool() {
		super("Name Tool", "res/letter.png", ImageType.IMAGE);
		
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.setLayout(new BorderLayout());
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		JLabel lbl = new JLabel("Image Name:");
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(200, 25));
		nameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String newName = ((JTextField) event.getSource()).getText();
				MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().setName(newName);
			}
		});
		
		inner.add(lbl);
		inner.add(nameField);
		oPanel.add(inner, BorderLayout.NORTH);
	}
	
	@Override
	public void resetOptionsInnerPanel(){
		super.resetOptionsInnerPanel();
		nameField.setText(MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().getName());
	}

	@Override
	public void onClick(MouseEvent event, int x, int y) {
		// Do nothing
	}

}
