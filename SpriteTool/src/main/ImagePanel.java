package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ImagePanel extends JScrollPane
{
	private static final int SCROLL_SPEED = 10;
	private JLabel imgLabel;
	private Canvas canvas;
	
	public ImagePanel(){
		canvas = new Canvas();
		setViewportView(canvas);
	    setVisible(true);
	    getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
	}
	
	public Canvas getCanvas(){
		return canvas;
	}

	public void setImagePath(String path){
		System.out.println("setting in imgframe: "+path);
		canvas.setImagePath(path);
	}
}
