package main.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import main.Canvas;
import main.MainWindow;

public class SnipTool extends Tool
{
	public boolean autoSnip = false;
	private enum SnipState {START, FIRST, SECOND};
	private static final Color BOXCOLOR = new Color(84, 232, 255);
	private SnipState state = SnipState.START;
	private Rectangle rect;
	
	public SnipTool(){
		super("Snip Tool", "res/scissors.png");
		
		JPanel oPanel = new JPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));
		JCheckBox chk = new JCheckBox("Autosnip");
		chk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				autoSnip = !autoSnip;
			}
		});
		oPanel.add(chk);
		setOptionInnerPanel(oPanel);
	}
	
	@Override
	public void selected(){
		super.selected();
		state = SnipState.START;
		rect = null;
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		if (autoSnip){
			doAutoSnip();
		} else {
			switch (state){
			case START:
				rect = new Rectangle();
				rect.setLocation(x, y);
				state = SnipState.FIRST;
				break;
			case FIRST:
				rect.setSize(x - rect.x, y - rect.y);
				state = SnipState.SECOND;
				break;
			case SECOND:
				if (rect.contains(x, y)){
					// Click was inside rect, confirmed
					// TODO: Add rect to list of Boxes.
				} else {
				}
				rect = null;
				state = SnipState.START;
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void drawTool(Graphics g, int mouseX, int mouseY)
	{
		((Graphics2D) g).setStroke(new BasicStroke(2));
		Canvas c = MainWindow.MAIN_WINDOW.getCanvas();
		super.drawTool(g, mouseX, mouseY);
		if (rect != null){
			g.setColor(BOXCOLOR);
			switch (state){
			case FIRST:
				g.drawRect(c.getScaledCoord(rect.x), c.getScaledCoord(rect.y), 
						c.getScaledCoord(mouseX - rect.x + 1), c.getScaledCoord(mouseY - rect.y + 1));
				break;
			case SECOND:
				g.drawRect(c.getScaledCoord(rect.x), c.getScaledCoord(rect.y), 
						c.getScaledCoord(rect.width + 1), c.getScaledCoord(rect.height + 1));
				break;
			case START:
				break;
			}
			g.setColor(Color.WHITE);
		}
	}
	
	public void doAutoSnip(){
		
	}

}
