package main;

import java.awt.Dimension;
import java.awt.Graphics;
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
	private static final int MAX_MAG = 100;
	BufferedImage img;
	ImageData imgData;
	private float scale = 2;
	
	public Canvas(){
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		addMouseListener(this);
		resetScale();
	}

	public void setImagePath(String path){
		try
		{
			img = ImageIO.read(new URL("file:///" + path));
			imgData = new ImageData(img);
			resetScale();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		refresh();
	}
	
	public void incScale(){
		setScale(scale + 1);
	}
	
	public void decScale(){
		setScale(scale - 1);
	}
	
	public void setScale(float scale){
		scale = Math.max(1, Math.min(MAX_MAG,scale));
		System.out.println("Set scale to "+scale);
		this.scale = scale;
		refresh();
	}
	
	public void resetScale(){
		try{
			scale = Integer.parseInt(Preferences.PREFS.get("defaultmag"));
		} catch (NumberFormatException e){
			scale = 1;
			Preferences.PREFS.set("defaultmag", "1");
		}
		refresh();
	}
	
	public void refresh(){
//		if (MainWindow.MAIN_WINDOW != null && MainWindow.MAIN_WINDOW.centerPanel != null){
//			MainWindow.MAIN_WINDOW.centerPanel.revalidate();
//		}
		revalidate();
		repaint();
	}
	
	public int getScaledWidth(){
		return (int) (img.getWidth()*scale);
	}
	
	public int getScaledHeight(){
		return (int) (img.getHeight()*scale);
	}
	
	@Override
	public void paintComponent(Graphics g){
		if (img != null){
			g.clearRect(0, 0, getWidth(), getHeight());
//			img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_AREA_AVERAGING);
			g.drawImage(img.getScaledInstance(getScaledWidth(), 
					getScaledHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, null);
//			g.drawImage(img, 0, 0, null);
			
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (img != null){
			return new Dimension(getScaledWidth(), getScaledHeight());			
		} else {
			return new Dimension(0,0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		int x = (int) (event.getX()/scale);
		int y = (int) (event.getY()/scale);
		System.out.println("Clicked (" + x + "," + y + ")");
		if (img != null){
//			MainWindow.MAIN_WINDOW.centerPanel.setBackground(new Color(img.getRGB(x, y)));
			Tool tool = MainWindow.MAIN_WINDOW.getCurrentTool();
			if (tool != null){
				tool.onClick(event);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}
