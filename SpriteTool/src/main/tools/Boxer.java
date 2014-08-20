package main.tools;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.ImageData;
import main.MainWindow;

public class Boxer extends Tool
{

	public Boxer()
	{
		super("Bounding Box Tool", "res/lasso.png");
		
		JPanel oPanel = new JPanel();
		oPanel.add(new JLabel("Boxer"));
		
		setOptionInnerPanel(oPanel);
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		MainWindow mw = MainWindow.MAIN_WINDOW;
		ImageData iData = mw.getCanvas().getImageData();
	}

}
