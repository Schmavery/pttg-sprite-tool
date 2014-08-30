package main;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;


public class SaveAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;

	public SaveAction(){
		super("Save");
	}
	 
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("Probably saving...");
		
	}

}
