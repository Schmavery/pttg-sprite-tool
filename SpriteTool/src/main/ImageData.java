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
//	private SheetData owner;
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
//		this.owner = owner;
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
	
	@Override
	public String toString(){
		String str = "";
		str += "img\n";
		str += "pt (" + rect.x + "," + rect.y + ")\n";
		str += "dim (" + rect.width + "," + rect.height + ")\n";
		
		if (anchorPt != null){
			str += "anchor\n";
			str += "pt (" + anchorPt.x + "," + anchorPt.y + ")\n";
		}
		
		str += "hooks\n";
		str += getHooks().size() + " hooks\n";
		for (Hook h : getHooks()){
			str += "pt (" + h.getPt().x + "," + h.getPt().y + ")\n";
			str += "name [[" + h.getName() + "]]\n";
		}
		str += "endhooks\n";	
		
		if (collisionPoly != null){
			str += "collision\n";
			for (int i = 0; i < collisionPoly.npoints; i++){
				str += "pt (" + collisionPoly.xpoints[i] + "," + collisionPoly.ypoints[i] + ")\n";
			}
			str += "endcollision\n";
		}
		str += "endimg\n";
		
		return str;
	}
	
	private enum ParserState {DEFAULT, ANCHOR, HOOKS, BOUNDS, COLLISION};
	
	/**
	 * Loads save data into this ImageData object.  This functions endeavours to
	 * be as flexible as possible when parsing data, but is guaranteed to work for
	 * the output of this program.
	 * @param data String describing save data for one ImageData object.
	 */
	public void loadData(String data){
		ParserState state = ParserState.DEFAULT;
		Hook tmpHook = null;
		Point pt;
		
		for (String l : data.split("\n")){
			switch (state){
			case ANCHOR:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					anchorPt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					state = ParserState.DEFAULT;
				}
				break;
			case HOOKS:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					tmpHook = new Hook("", pt);
				} else if (l.matches("name \\[\\[.+\\]\\]") && tmpHook != null){
					String name = l.substring(l.indexOf("[[")+2, l.indexOf("]]"));
					tmpHook.setName(name);
					hooks.add(tmpHook);
				} else if (l.startsWith("endhooks")){
					state = ParserState.DEFAULT;
				}
				break;
			case BOUNDS:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					rect.setLocation(pt);
				} else if (l.matches("dim +\\([0-9]+, ?[0-9]+\\)")){
					// Parse width and height as a point for code reuse ;)
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					rect.setSize(pt.x, pt.y);
					state = ParserState.DEFAULT;
				}
				break;
			case COLLISION:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					collisionPoly.addPoint(pt.x, pt.y);
				}
				break;
			case DEFAULT:
				if (l.startsWith("anchor")){
					state = ParserState.ANCHOR;
				} else if (l.startsWith("hooks")){
					state = ParserState.HOOKS;
					hooks = new LinkedList<>();
				} else if (l.startsWith("img")){
					state = ParserState.BOUNDS;
					rect = new Rectangle();
				} else if (l.startsWith("collision")){
					state = ParserState.COLLISION;
					collisionPoly = new Polygon();
				}
				break;
			}
			
		}
	}
	
	/**
	 * Partially parse the save data to obtain the rect
	 * defining the subimage.
	 * @param data String loaded data
	 * @return Rectangle defining bounds of subimage.
	 */
	public static Rectangle parseLoadRect(String data){
		Rectangle rect = new Rectangle();
		Point pt;
		for (String l : data.split("\n")){
			if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
				pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
				rect.setLocation(pt);
			} else if (l.matches("dim +\\([0-9]+, ?[0-9]+\\)")){
				pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
				rect.setSize(pt.x, pt.y);
				break;
			}
		}
		return rect;
	}
	
	/**
	 * Accepts a String of form "(x,y)" and translates it
	 * a point with corresponding x and y values.  There is
	 * a reasonable amount of flexibility in the string format.
	 * The only requirement is that the coords be comma-separated.
	 * @param str String describing a point.
	 * @return Point corresponding to the input String.
	 */
	private static Point parsePoint(String str){
 		String xStr = str.substring(0, str.indexOf(","));
		String yStr = str.substring(str.indexOf(","));
		// Strip non-numeric chars
		xStr = xStr.replaceAll("[^\\d]", "");
		yStr = yStr.replaceAll("[^\\d]", "");
		Point pt = null;
		try {
			pt = new Point(Integer.parseInt(xStr), Integer.parseInt(yStr));
		} catch (NumberFormatException e){
			System.out.println("Invalid point parsed: "+ str);
		}
		return pt;
	}
}
