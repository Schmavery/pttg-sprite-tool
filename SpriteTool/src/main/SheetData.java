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
		this(path, null);
	}
	
	public SheetData(String path, JButton button){
		setSheetPath(path, button);
	}
	
	public void setSheetPath(String path, JButton button){
		imgs = new LinkedList<>();
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
		currImgData = new ImageData(spriteSheet, this);
		currImgData.setButton(button);
		imgs.add(currImgData);
	}
	
	public void newImageData(Rectangle rect, BufferedImage img, JButton button){
		ImageData iData = new ImageData(rect, img, this);
		iData.setButton(button);
		imgs.add(iData);
	}
	
	public void setCurrentImage(JButton button){
		for (ImageData iData : imgs){
			if (iData.getButton().equals(button)){
				currImgData = iData;
				return;
			}
		}
		System.out.println("Could not find button");
	}
	
	public BufferedImage getImage(){
		return spriteSheet;
	}
	
	public boolean hasImage(){
		return (spriteSheet != null);
	}
	
	public ImageData getCurrentImageData(){
		return currImgData;
	}
	
	public BufferedImage getCurrentImage(){
		return currImgData.getImage();
	}
}
