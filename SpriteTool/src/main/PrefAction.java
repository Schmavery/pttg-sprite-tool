package main;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

import panels.PrefPanel;


public class PrefAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;

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
		dialog.setSize(450, 450);
		dialog.setLocationRelativeTo(MainWindow.MAIN_WINDOW);
		dialog.setVisible(true);
	}

}
