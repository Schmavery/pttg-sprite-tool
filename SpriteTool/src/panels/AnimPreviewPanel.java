package panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	public AnimPreviewPanel() {
		currIndex = 0;
		setPreferredSize(new Dimension(PREVIEW_HEIGHT,PREVIEW_WIDTH));
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.DARK_GRAY));
		animate = false;
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
		startRedrawThread();
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if (animate){
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(frames.get(currIndex).getImage(), 0, 0, PREVIEW_HEIGHT,PREVIEW_WIDTH, null);
		}
	}
}
