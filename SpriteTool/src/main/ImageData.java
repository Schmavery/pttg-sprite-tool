package main;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class ImageData
{
	private static final int DEFAULT_SCALE = 1;
	private BufferedImage img;
	private float scale;
	private Point anchorPt;

	public ImageData(BufferedImage img){
		this();
		this.img = img;
	}
	
	public ImageData(){
		resetScale();
	}
	
	public float getScale(){
		return scale;
	}
	
	public void setScale(float scale){
		this.scale = scale;
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
	
}
