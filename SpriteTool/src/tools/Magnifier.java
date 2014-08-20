package tools;

import java.awt.event.MouseEvent;

import main.MainWindow;

public class Magnifier extends Tool
{
	public Magnifier(){
		super("Magnifier", "res/mag.png");
	}

	@Override
	public void selected()
	{
		System.out.println("Tool switched to magnifier");
		MainWindow.MAIN_WINDOW.statusPanel.setLeftLabel("Magnifier");
	}

	@Override
	public void deselected()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(MouseEvent event)
	{
		MainWindow mw = MainWindow.MAIN_WINDOW;
		if (event.isControlDown()){
			mw.getCanvas().decScale();
		} else {
			mw.getCanvas().incScale();
		}
		mw.getCanvas().refresh();
	}
}
