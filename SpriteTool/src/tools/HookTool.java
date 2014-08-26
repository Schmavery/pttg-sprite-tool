package tools;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import main.Hook;
import main.ImageData.ImageType;

public class HookTool extends Tool
{
	private Hook newHook;
	
	public HookTool(){
		super("Hook Tool", "res/pencil.png", ImageType.IMAGE);
		setOptionInnerPanel(new JPanel());
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		// TODO Auto-generated method stub

	}

}
