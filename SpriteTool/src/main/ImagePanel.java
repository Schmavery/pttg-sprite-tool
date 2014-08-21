package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.tools.Tools;

public class ImagePanel extends JPanel implements EditorPanel
{
	private static final long serialVersionUID = 1L;
	private static final int SCROLL_SPEED = 10;
	private Canvas canvas;
	private JPanel lowerPanel;
	private JScrollPane imgScrollPane;
	
	public ImagePanel(){
		setLayout(new BorderLayout());
		canvas = new Canvas();
		JScrollPane canvasScrollPane = new JScrollPane();
		imgScrollPane = new JScrollPane();
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		lowerPanel.add(new JLabel(new ImageIcon(Tools.getAnchor().getImage())));
		lowerPanel.add(new JLabel(new ImageIcon(Tools.getMagnifier().getImage())));
		lowerPanel.add(new JLabel(new ImageIcon(Tools.getBoxTool().getImage())));
		lowerPanel.add(new JLabel(new ImageIcon(Tools.getSnipTool().getImage())));
		lowerPanel.setPreferredSize(new Dimension(50, 50));
		imgScrollPane.setViewportView(lowerPanel);
		
		canvasScrollPane.setViewportView(canvas);
		canvasScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
		add(canvasScrollPane, BorderLayout.CENTER);
		add(imgScrollPane, BorderLayout.SOUTH);
		imgScrollPane.setVisible(true);
		canvasScrollPane.setVisible(true);
		
	}
	
	public Canvas getCanvas(){
		return canvas;
	}

	public void setImagePath(String path){
		System.out.println("setting in imgframe: "+path);
		canvas.setImagePath(path);
	}
}
