package tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import main.ImageData;
import main.MainWindow;

public class Anchor extends Tool
{

	public Anchor()
	{
		super("Anchor", "res/anchor.png");
	}

	@Override
	public void onClick(MouseEvent event)
	{
		ImageData imgData = MainWindow.MAIN_WINDOW.getCanvas().getImageData();
		int x = (int) (event.getX()/imgData.getScale());
		int y = (int) (event.getY()/imgData.getScale());
		imgData.setAnchor(x, y);
	}
}
