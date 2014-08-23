package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import main.Canvas.CanvasType;
import main.tools.Tools;

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
	
	private SheetData sheetData;
	
	public ImagePanel(){
		sheetData = new SheetData();
		setLayout(new BorderLayout());
		canvas = new Canvas(CanvasType.IMAGE);
		JScrollPane canvasScrollPane = new JScrollPane();
		
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.setPreferredSize(new Dimension(0, THUMB_WIDTH+20));
		
		JScrollPane lowerRightScroll = new JScrollPane();
		lowerRightScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		lowerRightScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		lowerRight = new JPanel();
		lowerRight.setLayout(new BoxLayout(lowerRight, BoxLayout.X_AXIS));
		lowerRightScroll.setViewportView(lowerRight);
		
		sheetButton = new JButton();
		sheetButton.setFocusPainted(false);
		sheetButton.setBackground(Color.WHITE);
		sheetButton.setText("Full Canvas");
		sheetButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		sheetButton.setHorizontalTextPosition(SwingConstants.CENTER);
		sheetButton.setBorder(new LineBorder(Color.BLACK));
		sheetButton.setPreferredSize(new Dimension(THUMB_WIDTH+20, THUMB_HEIGHT));
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
	
	private Image getMaxScaledInstance(BufferedImage img, int maxDimension){
		float max = maxDimension;
		float ratio = Math.min(max/img.getWidth(), max/img.getHeight());
		int width = (int) (img.getWidth()*ratio);
		int height = (int) (img.getHeight()*ratio);
		return img.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING);
	}

	public void setSheetPath(String path){
		System.out.println("setting in imgPanel: "+path);
		sheetData.setSheetPath(path);
		canvas.setImagePath(path);
		sheetButton.setIcon(new ImageIcon(getMaxScaledInstance(sheetData.getImage(), THUMB_WIDTH)));
		sheetButton.revalidate();
		sheetButton.repaint();
	}
	
	public SheetData getSheetData(){
		return sheetData;
	}
	
	public void addSnippedImage(Rectangle rect){
		System.out.println("Cut Image Added");
		if (sheetData.hasImage() && rect != null){
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
			
			sheetData.newImageData(rect, img, button);
		}
	}
}
