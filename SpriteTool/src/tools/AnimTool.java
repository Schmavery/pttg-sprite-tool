package tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.Animation;
import main.Canvas;
import main.ImageData;
import main.ImageData.ImageType;
import main.MainWindow;

/**
 * Option panel displays a list of animations with names.
 * Clicking on each one will highlight included frames on
 * the spritesheet.
 */
public class AnimTool extends Tool
{
	enum AnimActionType {SELECT, RENAME, NEW, DELETE};
	Animation selectedAnim;
	ImageData selectedFrame;
	JPanel animListPanel;
	JPanel editPanel;
	
	class AnimAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		Animation anim;
		AnimActionType type;
		
		public AnimAction(AnimActionType type, Animation anim) {
			this("", type, anim);
		}
		
		public AnimAction(String label, AnimActionType type, Animation anim) {
			super(label);
			this.anim = anim;
			this.type = type;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			System.out.println("Action performed " + type.toString());
			editPanel.setVisible(false);
			selectedAnim = null;
			selectedFrame = null;
			switch (this.type){
			case DELETE:
				//TODO: Removey stuff? Reset?
				AnimTool.this.animListPanel.remove((Component) ((JButton) event.getSource()).getParent());
				MainWindow.MAIN_WINDOW.getSheetData().getAnimations().remove(anim);
				break;
			case SELECT:
				selectedAnim = anim;
				break;
			case NEW:
				Animation anim = new Animation();
				MainWindow.MAIN_WINDOW.getSheetData().getAnimations().add(anim);
				addAnimToPanel(anim);
				break;
			case RENAME:
				this.anim.setName(((JTextField) event.getSource()).getText());
				break;
			}
			animListPanel.getParent().getParent().validate();
			MainWindow.MAIN_WINDOW.getImagePanel().getCanvas().refresh();
		}
		
	}
	
	ActionListener listener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			switch (((Component) event.getSource()).getName()){
			case "ADD":
				selectedAnim.addFrame(selectedFrame);
				break;
			case "REMOVE":
				selectedAnim.getFrames().remove(selectedFrame);
				break;
			case "UP":
				selectedAnim.shiftFrame(selectedFrame, true);
				break;
			case "DOWN":
				selectedAnim.shiftFrame(selectedFrame, false);
				break;
			default:
				System.out.println("Invalid frame action: " + ((Component) event.getSource()).getName());
			}
			MainWindow.MAIN_WINDOW.getImagePanel().getCanvas().refresh();
			animListPanel.getParent().getParent().validate();
		}
	};

	
	public AnimTool()
	{
		super("Animation Tool", "res/board.png", ImageType.SHEET);
		
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));
		animListPanel = new JPanel();
		animListPanel.setLayout(new BoxLayout(animListPanel, BoxLayout.Y_AXIS));
		JButton newAnimBtn = new JButton(
				new AnimAction("New Anim", AnimActionType.NEW, new Animation()));
		newAnimBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		editPanel = new JPanel();
		editPanel.setBorder(new TitledBorder("Frame:"));
		editPanel.setVisible(false);
		
		addFrameEditButton(editPanel, "ADD", "\u271A");
		addFrameEditButton(editPanel, "REMOVE", "\u2718");
		addFrameEditButton(editPanel, "UP", "\u2191");
		addFrameEditButton(editPanel, "DOWN", "\u2193");

		
		oPanel.add(newAnimBtn);
		oPanel.add(Box.createVerticalStrut(5));
		oPanel.add(editPanel);
		oPanel.add(animListPanel);
	}
	
	public void addFrameEditButton(JPanel container, String name, String title){
		JButton btn = new JButton(title);
		btn.setMargin(new Insets(0,2,0,2));
		btn.setName(name);
		container.add(btn);
		btn.addActionListener(listener);
	}
	
	@Override
	public void selected(){
		super.selected();
		selectedAnim = null;
		selectedFrame = null;
		editPanel.setVisible(false);
		animListPanel.removeAll();
		for (Animation anim : MainWindow.MAIN_WINDOW.getSheetData().getAnimations()){
			addAnimToPanel(anim);
		}
		
	}
	
	private void addAnimToPanel(Animation anim){
		JPanel animPanel = new JPanel();
		animPanel.setName(anim.getName());
		
		JTextField tf = new JTextField(anim.getName());
		tf.setAction(new AnimAction("", AnimActionType.RENAME, anim));
		tf.setPreferredSize(new Dimension(80, 25));
		
		JButton editBtn = new JButton(new AnimAction("Edit", AnimActionType.SELECT, anim));
		editBtn.setMargin(new Insets(0,0,0,0));

		
		JButton delBtn = new JButton(new AnimAction("X", AnimActionType.DELETE, anim));
		delBtn.setBackground(Color.RED);
		delBtn.setMargin(new Insets(0,0,0,0));
		
		animPanel.add(tf);
		animPanel.add(editBtn);
		animPanel.add(delBtn);
		animListPanel.add(animPanel);
		
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y){
		editPanel.setVisible(false);
		if (selectedAnim != null){
			for (ImageData iData : MainWindow.MAIN_WINDOW.getSheetData().getAllImageData()){
				if (iData.getRect() != null && iData.getRect().contains(x, y)){
					selectedFrame = iData;
					editPanel.setVisible(true);
					break;
				}
			}
		}
	}
	
	@Override
	public void drawTool(Graphics g, int mouseX, int mouseY){
		Canvas c = MainWindow.MAIN_WINDOW.getCanvas();
		Rectangle r;
		if (selectedAnim != null) {
			int count = 0;
			g.setFont(new Font("Monospaced", Font.BOLD, 14));
			for (ImageData iData : selectedAnim.getFrames()){
				r = iData.getRect();
				g.setColor(Color.BLACK);
				g.drawString(String.valueOf(++count), c.getScaledCoord(r.x + 1), c.getScaledCoord(r.y + 4));
				g.setColor(Color.GREEN);
				g.drawRect(c.getScaledCoord(r.x), c.getScaledCoord(r.y), 
						c.getScaledCoord(r.width), c.getScaledCoord(r.height));
			}
			if (selectedFrame != null){
				g.setColor(Color.RED);
				r = selectedFrame.getRect();
				g.drawRect(c.getScaledCoord(r.x), c.getScaledCoord(r.y), 
						c.getScaledCoord(r.width), c.getScaledCoord(r.height));
			}
		}
	}

}
