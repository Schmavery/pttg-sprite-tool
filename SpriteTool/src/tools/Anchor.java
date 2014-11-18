package tools;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MainWindow;
import main.ImageData.ImageType;

public class Anchor extends Tool
{

	public Anchor()
	{
		super("Anchor", "res/anchor.png", ImageType.IMAGE);
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.add(new JLabel("Anchor"));
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().setAnchor(x, y);
	}
}
