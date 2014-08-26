package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import tools.Tool;
import main.ImageData.ImageType;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	
	private SheetData sheetData;
	
	int mouseX = 0;
	int mouseY = 0;
	
	public Canvas(SheetData sheetData){
		addMouseListener(this);
		addMouseMotionListener(this);
//		imgData = new ImageData();
		this.sheetData = sheetData;
		resetScale();
	}

	public void setImagePath(String path){
		try
		{
			BufferedImage img = ImageIO.read(new URL("file:///" + path));
//			imgData = new ImageData(img);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		refresh();
	}
	
	public void incScale(){
//		setScale(imgData.getScale() + 1);
		setScale(sheetData.getCurrentImageData().getScale() + 1);
	}
	
	public void decScale(){
//		setScale(imgData.getScale() - 1);
		setScale(sheetData.getCurrentImageData().getScale() - 1);
	}
	
	public ImageData getImageData(){
		return sheetData.getCurrentImageData();
	}
	
	public void setScale(float scale){
		sheetData.getCurrentImageData().setScale(scale);
		refresh();
	}
	
	public void resetScale(){
		sheetData.getCurrentImageData().resetScale();
		refresh();
	}
	
	public void refresh(){
		revalidate();
		repaint();
	}
	
	public int getScaledWidth(){
		return getScaledCoord(sheetData.getCurrentImageData().getWidth());
	}
	
	public int getScaledHeight(){
		return getScaledCoord(sheetData.getCurrentImageData().getHeight());
	}
	
	public int getScaledCoord(double coord){
		return (int) (coord*sheetData.getCurrentImageData().getScale());
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		ImageData imgData = sheetData.getCurrentImageData();
		
		if (imgData.hasImage()){
			g.clearRect(0, 0, getScaledWidth(), getScaledHeight());
//			g.drawImage(imgData.getImage().getScaledInstance(getScaledWidth(), 
//					getScaledHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, null);
//			g2.drawImage(imgData.getImage(), 0, 0, getScaledWidth(), getScaledHeight(), null);
			g2.drawImage(imgData.getImage(), 0, 0, getScaledWidth(), getScaledHeight(), null);
			
			
			if (imgData.hasAnchor()){
				g.setXORMode(Color.WHITE);
				g.fillRect(getScaledCoord(imgData.getAnchor().x), getScaledCoord(imgData.getAnchor().y), 
						(int) imgData.getScale(), (int) imgData.getScale());
				g2.setStroke(new BasicStroke(2));
				g.setPaintMode();
				g.setColor(Color.PINK);
				g.drawRect(getScaledCoord(imgData.getAnchor().x), getScaledCoord(imgData.getAnchor().y), 
						(int) imgData.getScale(), (int) imgData.getScale());
			}
			
			if (imgData.getType().equals(ImageType.SHEET)){
				Rectangle r;
				g.setColor(Color.DARK_GRAY);
				for (ImageData iData: sheetData.getAllImageData()){
					if (!iData.equals(imgData)){
						r = iData.getRect();
						g.drawRect(getScaledCoord(r.x), getScaledCoord(r.y), 
								getScaledCoord(r.width), getScaledCoord(r.height));
					}
				}
			}
			
			if (imgData.getType().equals(ImageType.IMAGE)){
				int[] yCoords;
				int[] xCoords;
				
				g.setColor(Color.ORANGE);
				if (imgData.getPoly() != null){
					xCoords = imgData.getPoly().xpoints;
					yCoords = imgData.getPoly().ypoints;
					for (int i = 0; i < imgData.getPoly().npoints; i++){
						g.drawLine(getScaledCoord(xCoords[i]), getScaledCoord(yCoords[i]), 
								getScaledCoord(xCoords[(i + 1) % imgData.getPoly().npoints]), 
								getScaledCoord(yCoords[(i + 1) % imgData.getPoly().npoints]));
					}
				}
			}
			
			MainWindow.MAIN_WINDOW.currentTool.drawTool(g, mouseX, mouseY);
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (sheetData.getCurrentImageData().hasImage()){
			return new Dimension(getScaledWidth(), getScaledHeight());			
		} else {
			return new Dimension(0,0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
			Tool tool = MainWindow.MAIN_WINDOW.getCurrentTool();
			if (tool != null){
				tool.onClick(event);
				refresh();
			}
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		mouseX = (int) (event.getX()/sheetData.getCurrentImageData().getScale());
		mouseY = (int) (event.getY()/sheetData.getCurrentImageData().getScale());
		repaint();
	}
}
