package tools;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.MainWindow;
import main.ImageData.ImageType;

public class NameTool extends Tool {
	
	JTextField nameField;

	public NameTool() {
		super("Name Tool", "res/letter.png", ImageType.IMAGE);
		
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.setLayout(new BorderLayout());
		JPanel inner = new JPanel();
		JLabel lbl = new JLabel("Image Name:");
		nameField = new JTextField();
		inner.add(lbl);
		inner.add(nameField);
		oPanel.add(inner, BorderLayout.NORTH);
	}
	
	@Override
	public void selected(){
		nameField.setText(formatFloat(MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().getName()));
	}

	@Override
	public void onClick(MouseEvent event, int x, int y) {
		// Do nothing
	}

}
