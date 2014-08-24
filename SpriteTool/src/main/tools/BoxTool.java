package main.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Canvas;
import main.ImageData;
import main.ImageData.ImageType;
import main.MainWindow;

public class BoxTool extends Tool
{
	private enum BoxState {START, BUILDING, CONFIRM};
	private BoxState state;
	private LinkedList<Point> pts;
	private boolean boxMode;

	public BoxTool()
	{
		super("Collision Box Tool", "res/lasso.png", ImageType.IMAGE);
		pts = new LinkedList<>();
		JPanel oPanel = new JPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));
		oPanel.add(new JLabel(getName()));
		
		setOptionInnerPanel(oPanel);
	}
	
	@Override
	public void selected(){
		super.selected();
		state = BoxState.START;
		pts.clear();
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		MainWindow mw = MainWindow.MAIN_WINDOW;
		ImageData iData = mw.getCanvas().getImageData();
		
		switch(state){
		case START:
			state = BoxState.BUILDING;
			pts.add(new Point(x, y));
			break;
		case BUILDING:
//			state = BoxState.BUILDING;
			boolean overlap = false;
			for (Point pt : pts){
				if (pt.x == x && pt.y == y){
					overlap = true;
					break;
				}
			}
			if (overlap){
				state = BoxState.CONFIRM;
			} else {
				pts.add(new Point(x, y));
			}
			break;
		case CONFIRM:
			// check if you clicked in the poly.
			Polygon poly = new Polygon();
			for (Point pt : pts){
				poly.addPoint(pt.x, pt.y);
			}
			if (poly.contains(x, y)){
				System.out.println("You clicked in the poly!");
			}
			state = BoxState.START;
			//TODO: Save point list
			pts.clear();
			break;
		}
	}

	@Override
	public void drawTool(Graphics g, int mouseX, int mouseY){
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		Canvas c = MainWindow.MAIN_WINDOW.getCanvas();
		Point prev;
		switch(state){
		case START:
			break;
		case CONFIRM:
			g.setColor(Color.ORANGE);
			prev = pts.getLast();
			for (Point pt : pts){
				g.drawLine(c.getScaledCoord(pt.x + 0.5), c.getScaledCoord(pt.y + 0.5), 
						c.getScaledCoord(prev.x + 0.5), c.getScaledCoord(prev.y + 0.5));
				g.fillOval(c.getScaledCoord(pt.x), c.getScaledCoord(pt.y), c.getScaledCoord(1), c.getScaledCoord(1));
				prev = pt;
			}
			break;
		case BUILDING:
			g.setColor(Color.ORANGE);
			g.drawLine(c.getScaledCoord(pts.getLast().x + 0.5), c.getScaledCoord(pts.getLast().y + 0.5), 
					c.getScaledCoord(mouseX + 0.5), c.getScaledCoord(mouseY + 0.5));
			prev = null;
			for (Point pt : pts){
				if (prev != null){
					g.drawLine(c.getScaledCoord(pt.x + 0.5), c.getScaledCoord(pt.y + 0.5), 
							c.getScaledCoord(prev.x + 0.5), c.getScaledCoord(prev.y + 0.5));
				}
				g.fillOval(c.getScaledCoord(pt.x), c.getScaledCoord(pt.y), c.getScaledCoord(1), c.getScaledCoord(1));
				prev = pt;
			}
			g.fillOval(c.getScaledCoord(pts.getFirst().x), c.getScaledCoord(pts.getFirst().y), 
					c.getScaledCoord(1), c.getScaledCoord(1));
			break;
		}
	}
}
