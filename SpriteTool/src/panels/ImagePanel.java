package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import main.Canvas;
import main.ImageData;
import main.ImageData.ImageType;
import main.Preferences;
import main.SheetData;
import tools.Tools;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private static final int SCROLL_SPEED = 10;
	private static final int THUMB_WIDTH = 75;
	private static final int THUMB_HEIGHT = 75;
	private Canvas canvas;
	private JPanel lowerPanel;
	private JPanel lowerRight;
	private JButton sheetButton;
	
	// Owner of sheetdata
	private SheetData sheetData;
	
	public ImagePanel(){
		sheetData = new SheetData();
		setLayout(new BorderLayout());
		canvas = new Canvas(sheetData);
		JScrollPane canvasScrollPane = new JScrollPane();
		
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.setPreferredSize(new Dimension(0, THUMB_WIDTH+20));
		
		JScrollPane lowerRightScroll = new JScrollPane();
		lowerRightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		lowerRightScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		lowerRight = new JPanel();
		lowerRight.setLayout(new BoxLayout(lowerRight, BoxLayout.X_AXIS));
		lowerRightScroll.setViewportView(lowerRight);
		
		sheetButton = new JButton();
		sheetButton.setFocusPainted(false);
		sheetButton.setBackground(Color.WHITE);
		sheetButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		sheetButton.setHorizontalTextPosition(SwingConstants.CENTER);
		sheetButton.setBorder(new LineBorder(Color.BLACK));
		sheetButton.setPreferredSize(new Dimension(THUMB_WIDTH+20, THUMB_HEIGHT));
		
		sheetButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				Tools.setButtonEnabledState(ImageType.SHEET);
				ImagePanel.this.switchImage((JButton) event.getSource(), ImageType.SHEET);
			}
		});
		
		lowerPanel.add(sheetButton, BorderLayout.WEST);
		lowerPanel.add(lowerRightScroll, BorderLayout.CENTER);
		
		canvasScrollPane.setViewportView(canvas);
		canvasScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
		add(canvasScrollPane, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.SOUTH);
		canvasScrollPane.setVisible(true);
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
	
	private static Image getMaxScaledInstance(BufferedImage img, int maxDimension){
		float max = maxDimension;
		float ratio = Math.min(max/img.getWidth(), max/img.getHeight());
		int width = (int) (img.getWidth()*ratio);
		int height = (int) (img.getHeight()*ratio);
		return img.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
	}

	public void setSheetPath(String path){
		System.out.println("setting in imgPanel: "+path);
		sheetData.reset();
		sheetData.setSheetPath(path, sheetButton);
		sheetButton.setIcon(new ImageIcon(getMaxScaledInstance(sheetData.getImage(), THUMB_WIDTH)));
		sheetButton.setText("View Canvas");
		canvas.refresh();
		sheetButton.revalidate();
		sheetButton.repaint();
	}
	
	public SheetData getSheetData(){
		return sheetData;
	}
	
	public void switchImage(JButton src, ImageType type){
		sheetData.setCurrentImage(src);
		getCanvas().refresh();
	}
	
	public ImageData addSnippedImage(Rectangle rect){
		ImageData.resetId(getSheetData().getAllImageData());
		boolean autoColl = Boolean.parseBoolean(Preferences.PREFS.get("coll_auto"));
		boolean autoHull = Boolean.parseBoolean(Preferences.PREFS.get("hull_auto"));
		boolean autoAnchor = Boolean.parseBoolean(Preferences.PREFS.get("anchor_auto"));
		
		if (sheetData.hasImage() && rect != null){
			// Check if this rect overlaps another.
			for (ImageData iData : sheetData.getAllImageData()){
				if (iData.getType().equals(ImageType.IMAGE) && iData.getRect().intersects(rect)){
					System.out.println("Image intersects another");
					return null;
				}
			}
			
			BufferedImage img = sheetData.getImage().getSubimage(rect.x, rect.y, rect.width, rect.height);
			ImageIcon ii = new ImageIcon(getMaxScaledInstance(img, THUMB_WIDTH));
			JButton button = new JButton(ii);
			button.setFocusPainted(false);
			button.setBackground(Color.WHITE);
			button.setBorder(new LineBorder(Color.BLACK));
			Dimension size = new Dimension(THUMB_WIDTH, THUMB_HEIGHT);
			button.setPreferredSize(size);
			button.setMinimumSize(size);
			button.setMaximumSize(size);
			lowerRight.add(button);
			lowerRight.revalidate();
			lowerRight.repaint();
			
			button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					Tools.setButtonEnabledState(ImageType.IMAGE);
					ImagePanel.this.switchImage((JButton) event.getSource(), ImageType.IMAGE);
				}
			});
			
			ImageData iData = sheetData.newImageData(rect, img, button);
			if (autoHull){
				iData.generateConvexHullPoly();
			} else if (autoColl){
				// Autogenerate and add a collision box
				int collX = Integer.valueOf(Preferences.PREFS.get("coll_x"));
				int collY = Integer.valueOf(Preferences.PREFS.get("coll_y"));
				int collW = Integer.valueOf(Preferences.PREFS.get("coll_w"));
				int collH = Integer.valueOf(Preferences.PREFS.get("coll_h"));
				
				Polygon p = new Polygon();
				// Wrapping the box clockwise
				p.addPoint(collX, collY);
				p.addPoint(collX + collW, collY);
				p.addPoint(collX + collW, collY + collH);
				p.addPoint(collX, collY + collH);
				
				iData.setPoly(p);
			}
			if (autoAnchor){
				int aX = Integer.valueOf(Preferences.PREFS.get("anchor_x"));
				int aY = Integer.valueOf(Preferences.PREFS.get("anchor_y"));
				iData.setAnchor(aX, aY);
			}
			return iData;
		}
		return null;
	}
}
