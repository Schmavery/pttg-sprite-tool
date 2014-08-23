package main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class ImageData
{
	private static final int MAX_MAG = 50;
	private static final int DEFAULT_SCALE = 1;
	private Rectangle rect;
	private SheetData owner;
	private BufferedImage img;
	private float scale;
	private Point anchorPt;
	
	private JButton button;

	public ImageData(){
		this(null, null);
	}

	public ImageData(BufferedImage img){
		this(img, null);
	}
	
	public ImageData(BufferedImage img, SheetData owner){
		this.img = img;
		this.owner = owner;
		if (img != null){
			System.out.println("Img height: "+this.img.getHeight());
		}
		resetScale();
	}
	
	
	public float getScale(){
		return scale;
	}
	
	public float setScale(float scale){
		scale = Math.max(1, Math.min(MAX_MAG,scale));
		this.scale = scale;
		System.out.println("Set scale to "+scale);
		return scale;
	}
	
	public void resetScale(){
		try{
			scale = Integer.parseInt(Preferences.PREFS.get("defaultmag"));
		} catch (NumberFormatException e){
			scale = DEFAULT_SCALE;
			Preferences.PREFS.set("defaultmag", String.valueOf(DEFAULT_SCALE));
		}
	}
	
	public boolean hasImage(){
		return (img != null);
	}
	
	public BufferedImage getImage(){
		return img;
	}
	
	
	public boolean hasAnchor(){
		return (anchorPt != null);
	}
	
	public Point getAnchor(){
		return anchorPt;
	}
	
	public void setAnchor(int x, int y){
		if (anchorPt == null){
			anchorPt = new Point(x, y);
		} else {
			anchorPt.setLocation(x, y);
		}
		System.out.println("Anchor set at " + anchorPt.toString());
	}
	
	public void resetAnchor(){
		anchorPt = null;
	}
	
	public boolean inBounds(int x, int y){
		return (hasImage() && x < img.getWidth() && y < img.getHeight());
	}
	
	public int getWidth(){
		return img.getWidth();
	}
	
	public int getHeight(){
		return img.getHeight();
	}
	
	public void setButton(JButton button){
		this.button = button;
	}
}
