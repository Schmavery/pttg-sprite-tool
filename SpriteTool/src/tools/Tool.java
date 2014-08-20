package tools;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Tool
{
	private String toolName;
	private BufferedImage img;
	
	public Tool(String name, String path){
		toolName = name;
		try
		{
			img = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Image getImage(){
		return img;
	}
	
	
	
	public abstract void selected();
	public abstract void deselected();
	public abstract void onClick(MouseEvent event);
}
