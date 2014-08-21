package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class SheetData
{
	private LinkedList<ImageData> imgs;
	private BufferedImage spriteSheet;
	private ImageData currImgData = null;
	
	public SheetData(String path){
		try
		{
			BufferedImage spriteSheet = ImageIO.read(new URL("file:///" + path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
}
