package main.tools;

import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.ImageData;
import main.MainWindow;

public class BoxTool extends Tool
{

	public BoxTool()
	{
		super("Bounding Box Tool", "res/lasso.png");
		
		JPanel oPanel = new JPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));
		oPanel.add(new JLabel("Box Tool"));
		
		setOptionInnerPanel(oPanel);
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		MainWindow mw = MainWindow.MAIN_WINDOW;
		ImageData iData = mw.getCanvas().getImageData();
	}

}
