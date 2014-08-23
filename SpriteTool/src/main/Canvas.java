package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.tools.Tool;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	public static enum CanvasType {SHEET, IMAGE};
	
	private ImageData imgData;
	private CanvasType type;
	
	int mouseX = 0;
	int mouseY = 0;
	
	public Canvas(CanvasType ct){
		addMouseListener(this);
		addMouseMotionListener(this);
		imgData = new ImageData();
		type = ct;
		resetScale();
	}

	public void setImagePath(String path){
		try
		{
			BufferedImage img = ImageIO.read(new URL("file:///" + path));
			imgData = new ImageData(img);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		refresh();
	}
	
	public void incScale(){
		setScale(imgData.getScale() + 1);
	}
	
	public void decScale(){
		setScale(imgData.getScale() - 1);
	}
	
	public ImageData getImageData(){
		return imgData;
	}
	
	public void setScale(float scale){
		imgData.setScale(scale);
		refresh();
	}
	
	public void resetScale(){
		imgData.resetScale();
		refresh();
	}
	
	public void refresh(){
		revalidate();
		repaint();
	}
	
	public int getScaledWidth(){
		return getScaledCoord(imgData.getWidth());
	}
	
	public int getScaledHeight(){
		return getScaledCoord(imgData.getHeight());
	}
	
	public int getScaledCoord(int coord){
		return (int) (coord*imgData.getScale());
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		if (imgData.hasImage()){
			g.clearRect(0, 0, getScaledWidth(), getScaledHeight());
//			g.drawImage(imgData.getImage().getScaledInstance(getScaledWidth(), 
//					getScaledHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, null);
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
			
			MainWindow.MAIN_WINDOW.currentTool.drawTool(g, mouseX, mouseY);
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (imgData.hasImage()){
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
		mouseX = (int) (event.getX()/imgData.getScale());
		mouseY = (int) (event.getY()/imgData.getScale());
		repaint();
	}
}
