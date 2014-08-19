package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener
{
	
	BufferedImage img;
	
	public Canvas(){
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		addMouseListener(this);
	}

	public void setImagePath(String path){
		try
		{
			img = ImageIO.read(new URL("file:///" + path));
			System.out.println("Height: " + img.getHeight());
//			setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		revalidate();
	}
	
	@Override
	public void paintComponent(Graphics g){
		if (img != null){
//			g.clearRect(0, 0, getWidth(), getHeight());
//			img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_AREA_AVERAGING);
//			g.drawImage(img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, null);
			g.drawImage(img, 0, 0, null);
			
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		if (img != null){
			return new Dimension(img.getWidth(), img.getHeight());			
		} else {
			return new Dimension(0,0);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		int x = event.getX();
		int y = event.getY();
		System.out.println("Clicked (" + x + "," + y + ")");
		if (img != null){
			MainWindow.MAIN_WINDOW.centerPanel.setBackground(new Color(img.getRGB(x, y)));
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
