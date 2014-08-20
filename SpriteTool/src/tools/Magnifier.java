package tools;

import java.awt.event.MouseEvent;

import main.MainWindow;

public class Magnifier extends Tool
{
	public Magnifier(){
		super("Magnifier", "res/mag.png");
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
