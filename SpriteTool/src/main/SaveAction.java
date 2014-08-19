package main;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;


public class SaveAction extends AbstractAction
{
	JFileChooser fc = new JFileChooser();

	public SaveAction(){
		super("Save");
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("Probably saving...");
		fc.showSaveDialog(MainWindow.MAIN_WINDOW);
		File file = fc.getSelectedFile();
		if (file != null){
			System.out.println(file.getAbsolutePath());
//			MainWindow.MAIN_WINDOW.setImagePath(file.getAbsolutePath());
		}
	}

}
