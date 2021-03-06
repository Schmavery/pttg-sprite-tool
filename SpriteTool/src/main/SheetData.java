package main;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.ImageData.ImageType;
import tools.Tools;

public class SheetData
{
	private LinkedList<ImageData> imgs;
	private LinkedList<Animation> anims;
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
		anims = new LinkedList<>();
		if (path != null){
			try
			{
				System.out.println(">>>" + path);
				BufferedImage s = ImageIO.read(new URL("file:///" + path));
				System.out.println(s.getType() + BufferedImage.TYPE_INT_ARGB);
				spriteSheet = new BufferedImage(s.getWidth(), s.getHeight(), BufferedImage.TYPE_INT_ARGB);
				spriteSheet.getGraphics().drawImage(s, 0, 0, null);
			}
			catch (IOException e)
			{
				//e.printStackTrace();
				return;
			}
		}
		currImgData = new ImageData(spriteSheet, ImageType.SHEET);
		currImgData.setImageType(ImageType.SHEET);
		currImgData.setButton(button);
		imgs.add(currImgData);
	}
	
	public void reset(){
		for (ImageData iData : imgs){
			if (iData.getRect() != null)
				removeImageData(iData.getRect(), false);
		}
		imgs.clear();
	}
	
	public void removeAllSnipped(){
		Iterator<ImageData> iter = imgs.iterator();
		ImageData iData;
		while (iter.hasNext()){
			iData = iter.next();
			if (iData.getRect() != null){
				removeImageData(iData.getRect(), false);
				iter.remove();
			}
		}
	}
	
	public ImageData newImageData(Rectangle rect, BufferedImage img, JButton button){
		ImageData iData = new ImageData(rect, img, ImageType.IMAGE);
		iData.setButton(button);
		imgs.add(iData);
		return iData;
	}
	
	public void removeImageData(Rectangle rect, boolean autoRemove){
		ImageData match = null;
		for(Iterator<ImageData> it = imgs.iterator(); it.hasNext();){
			ImageData iData = it.next();
			if (rect.equals(iData.getRect())){
				match = iData;
				JPanel p = (JPanel) iData.getButton().getParent();
				p.remove(iData.getButton());
				p.invalidate();
				p.repaint();
				if (match != null && autoRemove){
					imgs.remove(match);
				}
				MainWindow.MAIN_WINDOW.getCanvas().repaint();
				break;
			}
		}
		
		if (match == null){
			System.out.println("Could not find image");
			return;
		}
		
		for (Iterator<Animation> it = anims.iterator(); it.hasNext();){
			Animation a = it.next();
			a.getFrames().remove(match);
			if (a.getFrames().isEmpty()){
				it.remove();
			}
		}
		
	}
	
	public void setCurrentImage(JButton button){
		for (ImageData iData : imgs){
			if (iData.getButton().equals(button)){
				currImgData = iData;
				ImageType type = currImgData.getType();
				if (MainWindow.MAIN_WINDOW.getCurrentTool().getType() != type){
					MainWindow.MAIN_WINDOW.setCurrentTool(Tools.getMagnifier());
				} else {
					MainWindow.MAIN_WINDOW.getCurrentTool().resetOptionsInnerPanel();
				}
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
	
	public LinkedList<ImageData> getAllImageData(){
		return imgs;
	}
	
	public LinkedList<Animation> getAnimations(){
		return anims;
	}
	
	public ImageData getImageDataById(int id){
		for (ImageData iData : imgs){
			if (iData.getId() == id){
				return iData;
			}
		}
		System.out.println("Could not find ImageData with id "+id);
		return null;
	}
}
