package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import tools.Tool;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private static final int MAX_MAG = 100;
	private ImageData imgData;
	
	public Canvas(){
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		addMouseListener(this);
		imgData = new ImageData();
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
		scale = Math.max(1, Math.min(MAX_MAG,scale));
		System.out.println("Set scale to "+scale);
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
		return getScaledCoord(imgData.getImage().getWidth());
	}
	
	public int getScaledHeight(){
		return getScaledCoord(imgData.getImage().getHeight());
	}
	
	public int getScaledCoord(int coord){
		return (int) (coord*imgData.getScale());
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (imgData.hasImage()){
			g.clearRect(0, 0, getScaledWidth(), getScaledHeight());
			g.drawImage(imgData.getImage().getScaledInstance(getScaledWidth(), 
					getScaledHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, null);

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
		int x = (int) (event.getX()/imgData.getScale());
		int y = (int) (event.getY()/imgData.getScale());
		System.out.println("Clicked (" + x + "," + y + ")");
		if (imgData.hasImage()){
//			MainWindow.MAIN_WINDOW.centerPanel.setBackground(new Color(img.getRGB(x, y)));
			Tool tool = MainWindow.MAIN_WINDOW.getCurrentTool();
			if (tool != null && imgData.inBounds(x, y)){
				tool.onClick(event);
				refresh();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
//		System.out.println("Entered");
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
//		System.out.println("Exited");
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		System.out.println("Pressed");
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		System.out.println("Released");
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		
	}
}
