package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class SheetData
{
	private LinkedList<ImageData> imgs;
	private BufferedImage spriteSheet;
	private ImageData currImgData = null;
	
	public SheetData(){
		this(null);
	}
	
	public SheetData(String path){
		setSheetPath(path);
	}
	
	public void setSheetPath(String path){
		if (path != null){
			try
			{
				spriteSheet = ImageIO.read(new URL("file:///" + path));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void newImageData(Rectangle rect, BufferedImage img, JButton button){
		ImageData iData = new ImageData(img, this);
	}
	
	public BufferedImage getImage(){
		return spriteSheet;
	}
	
	public boolean hasImage(){
		return (spriteSheet != null);
	}
}
