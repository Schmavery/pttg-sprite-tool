package main.tools;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.MainWindow;

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
	
	
	
	public void selected(){
		MainWindow.MAIN_WINDOW.statusPanel.setLeftLabel(toolName);
		System.out.println("HI");
	}
	
	public void deselected(){
		
	}
	public abstract void onClick(MouseEvent event);
	public void onClick(int x, int y){}
	
	public String getName(){
		return toolName;
	}
}
