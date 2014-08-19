package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ImageFrame extends JScrollPane
{
	private JLabel imgLabel;
	private Canvas canvas;
	
	public ImageFrame(){
		canvas = new Canvas();
		setViewportView(canvas);
	    setVisible(true);
	}

	public void setImagePath(String path){
//		if (imgLabel == null){
//			imgLabel = new JLabel(new ImageIcon(path));
//		} else {
//			imgLabel.setIcon(new ImageIcon(path));
//		}
//		setViewportView(imgLabel);
		System.out.println("setting in imgframe: "+path);
		canvas.setImagePath(path);
	}
}
