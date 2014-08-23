package main;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;


public class OpenAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	JFileChooser fc = new JFileChooser();
	public OpenAction(){
		super("Open");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		fc.showOpenDialog(MainWindow.MAIN_WINDOW);
		File file = fc.getSelectedFile();
		if (file != null){
			System.out.println(file.getAbsolutePath());
			MainWindow.MAIN_WINDOW.setSheetPath(file.getAbsolutePath());
		}
	}

}
