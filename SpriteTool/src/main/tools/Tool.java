package main.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.MainWindow;

public abstract class Tool
{
	private String toolName;
	private BufferedImage img;
	private JPanel optionsInnerPanel;
	private JButton toolButton;
	
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
	
	public void setOptionInnerPanel(JPanel optionsPanel){
		this.optionsInnerPanel = optionsPanel;
	}
	
	public JPanel getOptionsInnerPanel(){
		resetOptionsInnerPanel();
		return optionsInnerPanel;
	}
	
	public void resetOptionsInnerPanel(){
	}
	
	public void selected(){
		MainWindow.MAIN_WINDOW.statusPanel.setLeftLabel(toolName);
		resetOptionsInnerPanel();
		MainWindow.MAIN_WINDOW.optionsPanel.setInner(optionsInnerPanel);
		if (toolButton != null){
			toolButton.setBackground(Color.WHITE);
		}
	}
	
	public void deselected(){
		if (toolButton != null){
			toolButton.setBackground(Color.LIGHT_GRAY);
		}
	}
	
	public void onClick(MouseEvent event){
		float scale = MainWindow.MAIN_WINDOW.getCanvas().getImageData().getScale();
		int x = (int) (event.getX()/scale);
		int y = (int) (event.getY()/scale);
		if (MainWindow.MAIN_WINDOW.getCanvas().getImageData().inBounds(x, y)){
			onClick(event, x, y);
		}
	}
	
	public void drawTool(Graphics g, int mouseX, int mouseY){
		
	}
	
	public abstract void onClick(MouseEvent event, int x, int y);
	
	public String getName(){
		return toolName;
	}
	
	public void setButton(JButton button){
		if (button != null){
//			button.setBackground(Color.WHITE);
			if (toolButton == null || !toolButton.equals(button)){
				toolButton = button;
			}
		}
	}
	
	public static String formatFloat(float f){
//		return String.valueOf(f);
		if(f == (int) f)
	        return String.format("%d",(int)f);
	    else
	        return String.format("%s",f);
	}
}
