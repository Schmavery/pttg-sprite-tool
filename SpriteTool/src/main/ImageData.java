package main;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JButton;

public class ImageData
{
	public static enum ImageType {SHEET, IMAGE, EITHER};
	private static final int MAX_MAG = 50;
	private static final int DEFAULT_SCALE = 1;
	
	private LinkedList<Hook> hooks;
	private Rectangle rect;
	private Polygon collisionPoly;
	private SheetData owner;
	private ImageType type;
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
		this(null, img, owner);
	}
	
	public ImageData(Rectangle rect, BufferedImage img, SheetData owner){
		this.img = img;
		this.owner = owner;
		this.rect = rect;
		this.hooks = new LinkedList<>();
		this.type = ImageType.IMAGE;
		if (img != null){
			System.out.println("Img height: "+this.img.getHeight());
		}
		resetScale();
	}
	
	public void setImageType(ImageType type){
		this.type = type;
		resetScale();
	}
	
	public float getScale(){
		return scale;
	}
	
	
	public float setScale(float scale){
		scale = Math.max(1, Math.min(MAX_MAG,scale));
		this.scale = scale;
		MainWindow.MAIN_WINDOW.getCanvas().refresh();
		System.out.println("Set scale to "+scale);
		return scale;
	}
	
	public void resetScale(){
		try{
			if (type.equals(ImageType.IMAGE)){
				scale = Integer.parseInt(Preferences.PREFS.get("image_mag"));
			} else {
				scale = Integer.parseInt(Preferences.PREFS.get("sheet_mag"));
			}
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
	
	public JButton getButton(){
		return button;
	}
	
	public ImageType getType(){
		return type;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public void setPoly(Polygon p){
		collisionPoly = p;
	}
	
	public Polygon getPoly(){
		return collisionPoly;
	}
	
	public LinkedList<Hook> getHooks(){
		return hooks;
	}
	
	public void addHook(Hook h){
		hooks.add(h);
	}
	
	public void clearHooks(){
		hooks.clear();
	}
}
