package main;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.WindowConstants;


public class PrefAction extends AbstractAction
{

	public PrefAction(){
		super("Preferences");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		JDialog dialog = new JDialog(MainWindow.MAIN_WINDOW, "Properties", true);
		PrefPanel pref = new PrefPanel(dialog);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.add(pref);
		dialog.setSize(400, 150);
		dialog.setLocationRelativeTo(MainWindow.MAIN_WINDOW);
		dialog.setVisible(true);
	}

}
