package tools;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import main.ImageData.ImageType;

public class PaletteTool extends Tool {

	public PaletteTool()
	{
		super("Palette Tool", "res/bucket.png", ImageType.IMAGE);
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y) {
		// Placeholder
	}
	
	@Override
	public void drawTool(Graphics g, int mouseX, int mouseY){
		// Placeholder
	}

}
