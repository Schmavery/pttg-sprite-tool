package main;

import javax.swing.JScrollPane;

public class ImagePanel extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	private static final int SCROLL_SPEED = 10;
	private Canvas canvas;
	
	public ImagePanel(){
		canvas = new Canvas();
		setViewportView(canvas);
	    setVisible(true);
	    getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
	}
	
	public Canvas getCanvas(){
		return canvas;
	}

	public void setImagePath(String path){
		System.out.println("setting in imgframe: "+path);
		canvas.setImagePath(path);
	}
}
