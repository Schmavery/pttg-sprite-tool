package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import main.Canvas;
import main.ImageData.ImageType;
import main.MainWindow;

public class BoxTool extends Tool
{
	private enum BoxState {START, BUILDING, CONFIRM};
	private BoxState state;
	private LinkedList<Point> pts;
	private Rectangle rect;
//	private boolean boxMode;
	private JCheckBox boxMode;

	public BoxTool()
	{
		super("Collision Box Tool", "res/lasso.png", ImageType.IMAGE);
		pts = new LinkedList<>();
		JPanel oPanel = new JPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));
//		oPanel.add(new JLabel(getName()));
		boxMode = new JCheckBox("Box Mode");
		boxMode.setSelected(true);
		boxMode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				setBoxMode(boxMode.isSelected());
			}
		});
		oPanel.add(boxMode);
		
		setOptionInnerPanel(oPanel);
	}
	
	@Override
	public void selected(){
		super.selected();
		rect = null;
		state = BoxState.START;
		pts.clear();
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		switch(state){
		case START:
			if (boxMode.isSelected()){
				rect = new Rectangle();
				rect.setLocation(x, y);
				state = BoxState.BUILDING;
			} else {
				state = BoxState.BUILDING;
				pts.add(new Point(x, y));
			}
			break;
		case BUILDING:
			if (boxMode.isSelected()){
				if (x > rect.x && y > rect.y){
					rect.setSize(x - rect.x, y - rect.y);
					state = BoxState.CONFIRM;
				} else {
					rect.setLocation(x, y);
				}
			} else {
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
			}
			break;
		case CONFIRM:
			if (boxMode.isSelected()){
				if (rect.contains(x, y)){
					Polygon p = new Polygon();
					p.addPoint(rect.x, rect.y);
					p.addPoint(rect.x + rect.width + 1, rect.y);
					p.addPoint(rect.x + rect.width + 1, rect.y + rect.height + 1);
					p.addPoint(rect.x                 , rect.y + rect.height + 1);
					MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().setPoly(p);
				}
				state = BoxState.START;
			} else {
				// Check if you clicked in the poly.
				Polygon p = new Polygon();
				for (Point pt : pts){
					p.addPoint(pt.x, pt.y);
				}
				
				if (p.contains(x, y)){
					MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().setPoly(p);
				}
				state = BoxState.START;
				pts.clear();
			}
			break;
		}
	}
	
	public void setBoxMode(boolean b){
		System.out.println("Setting boxmode: "+b);
		boxMode.setSelected(b);
		selected();
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
			
			if (boxMode.isSelected()){
				g.drawRect(c.getScaledCoord(rect.x + 0.5), c.getScaledCoord(rect.y + 0.5), 
						c.getScaledCoord(rect.width), c.getScaledCoord(rect.height));
				g.fillOval(c.getScaledCoord(rect.x), 
						c.getScaledCoord(rect.y), c.getScaledCoord(1), c.getScaledCoord(1));
				g.fillOval(c.getScaledCoord(rect.x + rect.width), 
						c.getScaledCoord(rect.y), c.getScaledCoord(1), c.getScaledCoord(1));
				g.fillOval(c.getScaledCoord(rect.x), 
						c.getScaledCoord(rect.y + rect.height), c.getScaledCoord(1), c.getScaledCoord(1));
				g.fillOval(c.getScaledCoord(rect.x + rect.width), 
						c.getScaledCoord(rect.y + rect.height), c.getScaledCoord(1), c.getScaledCoord(1));
			} else {
				prev = pts.getLast();
				for (Point pt : pts){
					g.drawLine(c.getScaledCoord(pt.x + 0.5), c.getScaledCoord(pt.y + 0.5), 
							c.getScaledCoord(prev.x + 0.5), c.getScaledCoord(prev.y + 0.5));
					g.fillOval(c.getScaledCoord(pt.x), c.getScaledCoord(pt.y), c.getScaledCoord(1), c.getScaledCoord(1));
					prev = pt;
				}
			}
			break;
		case BUILDING:
			g.setColor(Color.ORANGE);
			
			if (boxMode.isSelected()){
				g.fillOval(c.getScaledCoord(rect.x), c.getScaledCoord(rect.y), c.getScaledCoord(1), c.getScaledCoord(1));
				g.drawRect(c.getScaledCoord(rect.x + 0.5), c.getScaledCoord(rect.y + 0.5), 
						c.getScaledCoord(mouseX - rect.x), c.getScaledCoord(mouseY - rect.y));
			} else {
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
			}
			break;
		}
	}
}
