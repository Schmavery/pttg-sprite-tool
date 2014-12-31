package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import main.Animation;
import main.ImageData;

public class AnimPreviewPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int PREVIEW_HEIGHT = 150;
	public static final int PREVIEW_WIDTH = 150;
	ArrayList<ImageData> frames;
	Animation anim;
	int currIndex;
	Thread redrawThread;
	volatile boolean animate;
	float scale;
	int centerX, centerY;
	
	public AnimPreviewPanel() {
		currIndex = 0;
		setPreferredSize(new Dimension(PREVIEW_HEIGHT,PREVIEW_WIDTH));
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.DARK_GRAY));
		animate = false;
//		calcScale();
	}
	
	private void calcScale(){
		int n = 0, s = 0, e = 0, w = 0;
		for (ImageData iData : anim.getFrames()){
			if (n < iData.getAnchor().y) 
				n = iData.getAnchor().y;
			if (w < iData.getAnchor().x) 
				w = iData.getAnchor().x;
			if (s < iData.getHeight() - iData.getAnchor().y)
				s = iData.getHeight() - iData.getAnchor().y;
			if (e < iData.getWidth() - iData.getAnchor().x)
				e = iData.getWidth() - iData.getAnchor().x;
		}
		centerY = n;
		centerX = w;
		int newHeight = n + s;
		int newWidth = w + e;
		System.out.println("("+newHeight+","+newWidth+")"+centerX+","+centerY+" - "+scale);
		scale = (float) ((1.0*PREVIEW_HEIGHT)/newHeight);
		if ((1.0*PREVIEW_WIDTH/newWidth) < scale){
			scale = (float) ((1.0*PREVIEW_WIDTH)/newWidth);
		}
	}
	
	public void startRedrawThread(){
		animate = true;
		redrawThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (animate){
					currIndex = (currIndex + 1) % frames.size();
					repaint();
					try {
						Thread.sleep(anim.getPause());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		redrawThread.start();
	}
	
	public void lazyKillRedrawThread(){
		// This will eventually kill the thread.
		animate = false;
		repaint();
	}
	
	public void forceKillRedrawThread(){
		// This will eventually kill the thread.
		animate = false;
		if (redrawThread != null){
			try {
				redrawThread.join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted while joining redraw thread");
			}
		}
		repaint();
	}
	
	public void updateAnimation(Animation anim){
		forceKillRedrawThread();
		if (anim == null || anim.getFrames().size() == 0) return;
		
		this.anim = anim;
		this.frames = new ArrayList<ImageData>(anim.getFrames());
		currIndex = 0;
		calcScale();
		startRedrawThread();
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if (animate){
			Graphics2D g2 = (Graphics2D) g;
			Image img = frames.get(currIndex).getImage();
			ImageData iData = anim.getFrames().get(currIndex);
//			System.out.println("anim"+scale);
			g2.drawImage(img, (int) (scale*(centerX - iData.getAnchor().x)), 
					(int) (scale*(centerY - iData.getAnchor().y))
					, (int) (scale*iData.getWidth()), (int) (scale*iData.getHeight()), null);
		}
	}
}
