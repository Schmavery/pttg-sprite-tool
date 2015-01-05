package tools;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.Canvas;
import main.ImageData;
import main.ImageData.ImageType;
import main.MainWindow;
import main.Preferences;

public class SnipTool extends Tool
{
	private enum SnipState {START, FIRST, SECOND, SELECTED};
	private SnipState state = SnipState.START;
	
	private Rectangle rect;
	private JButton deleteButton;
	
	public SnipTool(){
		super("Snip Tool", "res/scissors.png", ImageType.SHEET);
		
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.setLayout(new BorderLayout());
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		
		JButton autoSnip;
		autoSnip = new JButton("Autosnip");
		autoSnip.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				doAutoSnip();
				MainWindow.MAIN_WINDOW.getCanvas().refresh();
			}
		});
		
		deleteButton = new JButton("Delete?");
		deleteButton.setBackground(Color.RED);
		deleteButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				MainWindow.MAIN_WINDOW.getSheetData().removeImageData(rect, true);
				SnipTool.this.selected();
			}
		});

		JButton deleteAllButton;
		deleteAllButton = new JButton("Delete All?");
		deleteAllButton.setBackground(Color.RED);
		deleteAllButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				MainWindow.MAIN_WINDOW.getSheetData().removeAllSnipped();
				SnipTool.this.selected();
			}
		});
		
		innerPanel.add(autoSnip);
		innerPanel.add(Box.createVerticalGlue());
		innerPanel.add(Box.createVerticalStrut(8));
		innerPanel.add(Box.createVerticalGlue());
		innerPanel.add(deleteAllButton);
		innerPanel.add(Box.createVerticalGlue());
		innerPanel.add(Box.createVerticalStrut(8));
		innerPanel.add(Box.createVerticalGlue());
		innerPanel.add(deleteButton);
		oPanel.add(innerPanel, BorderLayout.NORTH);
	}
	
	@Override
	public void selected(){
		super.selected();
		state = SnipState.START;
		rect = null;
		setDeleteVisibility(false);
	}
	
	private void setDeleteVisibility(boolean b){
		deleteButton.setVisible(b);
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		switch (state){
		case START:
			// Check if you clicked in an existing rect
			boolean exists = false;
			for (ImageData iData : MainWindow.MAIN_WINDOW.getSheetData().getAllImageData()){
				if (iData.getType() != ImageType.SHEET && iData.getRect().contains(x, y)){
					state = SnipState.SELECTED;
					rect = (Rectangle) iData.getRect();
					setDeleteVisibility(true);
					exists = true;
					break;
				}
			}
			if (!exists){
				rect = new Rectangle();
				rect.setLocation(x, y);
				state = SnipState.FIRST;
			}
			break;
		case FIRST:
			if (x <= rect.x || y <= rect.y){
				rect.setLocation(x, y);
				break;
			}
			rect.setSize(x - rect.x + 1, y - rect.y + 1);
			state = SnipState.SECOND;
			break;
		case SECOND:
			if (rect.contains(x, y)){
				MainWindow.MAIN_WINDOW.getImagePanel().addSnippedImage(rect);
			}
			rect = null;
			state = SnipState.START;
			break;
		case SELECTED:
			rect = null;
			state = SnipState.START;
			setDeleteVisibility(false);
			break;
		default:
			break;
		}
	}

	public static void doAutoSnip(){
		int imgSize = Integer.parseInt(Preferences.PREFS.get("autosnip_size"));
		BufferedImage img = MainWindow.MAIN_WINDOW.getSheetData().getImage();
		Rectangle rect;
		for (int i = 0; i < img.getHeight()/imgSize; i++){
			for (int j = 0; j < img.getWidth()/imgSize; j++){
				rect = new Rectangle(j*imgSize, i*imgSize, imgSize, imgSize);
				if (!isEmptyImageRegion(img, rect)){
					MainWindow.MAIN_WINDOW.getImagePanel().addSnippedImage(rect);
				}
			}
		}
	}
	
	private static boolean isEmptyImageRegion(BufferedImage img, Rectangle rect){
		int value = 0;
		for (int i = rect.x; i < rect.x + rect.width; i++){
			for (int j = rect.y; j < rect.y + rect.height; j++){
				if (img.getRGB(i, j) != value){
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void drawTool(Graphics g, int mouseX, int mouseY)
	{
		((Graphics2D) g).setStroke(new BasicStroke(2));
		Canvas c = MainWindow.MAIN_WINDOW.getCanvas();
		super.drawTool(g, mouseX, mouseY);
		if (rect != null){
			g.setColor(Color.CYAN);
			switch (state){
			case FIRST:
				g.drawRect(c.getScaledCoord(rect.x), c.getScaledCoord(rect.y), 
						c.getScaledCoord(mouseX - rect.x + 1), c.getScaledCoord(mouseY - rect.y + 1));
				break;
			case SECOND:
				g.drawRect(c.getScaledCoord(rect.x), c.getScaledCoord(rect.y), 
						c.getScaledCoord(rect.width), c.getScaledCoord(rect.height));
				break;
			case START:
				break;
			case SELECTED:
				
				g.setColor(Color.RED);
				g.drawRect(c.getScaledCoord(rect.x), c.getScaledCoord(rect.y), 
						c.getScaledCoord(rect.width), c.getScaledCoord(rect.height));
			}
		}
	}
}
