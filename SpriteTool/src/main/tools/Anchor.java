package main.tools;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MainWindow;

public class Anchor extends Tool
{

	public Anchor()
	{
		super("Anchor", "res/anchor.png");
		JPanel oPanel = new JPanel();
		oPanel.add(new JLabel("Anchor"));
		setOptionInnerPanel(oPanel);
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
//		ImageData imgData = MainWindow.MAIN_WINDOW.getCanvas().getImageData();
//		int x = (int) (event.getX()/imgData.getScale());
//		int y = (int) (event.getY()/imgData.getScale());
//		imgData.setAnchor(x, y);
		MainWindow.MAIN_WINDOW.getCanvas().getImageData().setAnchor(x, y);
	}
}
