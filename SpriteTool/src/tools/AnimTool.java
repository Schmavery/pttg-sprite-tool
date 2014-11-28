package tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import main.Animation;
import main.Canvas;
import main.ImageData;
import main.ImageData.ImageType;
import main.MainWindow;
import panels.AnimPreviewPanel;

/**
 * Option panel displays a list of animations with names.
 * Clicking on each one will highlight included frames on
 * the spritesheet.
 */
public class AnimTool extends Tool
{
	enum AnimActionType {RENAME, PAUSE, NEW, DELETE};
	private static JPanel colorPanel = new JPanel();
	public static final Color DEFAULT_COLOR = colorPanel.getBackground(); 
	Animation selectedAnim;
	ImageData selectedFrame;
	JPanel animListPanel;
	JPanel editPanel;
	Container selectedAnimPanel;
	AnimPreviewPanel preview;
	
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
			editPanel.setVisible(false);
			selectedAnim = null;
			selectedFrame = null;
			switch (this.type){
			case DELETE:
				AnimTool.this.animListPanel.remove((Component) ((JButton) event.getSource()).getParent());
				MainWindow.MAIN_WINDOW.getSheetData().getAnimations().remove(anim);
				MainWindow.MAIN_WINDOW.setIsDirty(true);
				Animation.resetId(MainWindow.MAIN_WINDOW.getSheetData().getAnimations());
				preview.updateAnimation(selectedAnim);
				break;
			case NEW:
				Animation anim = new Animation();
				MainWindow.MAIN_WINDOW.getSheetData().getAnimations().add(anim);
				JPanel added = addAnimToPanel(anim);
				selectAnimation(added, anim);
				MainWindow.MAIN_WINDOW.setIsDirty(true);
				break;
			case RENAME:
				this.anim.setName(((JTextField) event.getSource()).getText());
				selectAnimation(((Component)event.getSource()).getParent(), this.anim);
				MainWindow.MAIN_WINDOW.setIsDirty(true);
				break;
			case PAUSE:
				this.anim.setPause(Integer.parseInt(((JTextField) event.getSource()).getText()));
				selectAnimation(((Component)event.getSource()).getParent(), this.anim);
				MainWindow.MAIN_WINDOW.setIsDirty(true);
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
			doFrameAction(((Component) event.getSource()).getName());
		}
	};

	
	public AnimTool()
	{
		super("Animation Tool", "res/board.png", ImageType.SHEET);
		
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.setLayout(new BorderLayout());
		
		JScrollPane animListScrollPane = new JScrollPane();
		animListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		animListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		animListScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5,10000000));
//		animListScrollPane.getVerticalScrollBar().getUI().
		JPanel animListWrapper = new JPanel(new BorderLayout());
		animListPanel = new JPanel();
		animListPanel.setBackground(Color.RED);
		animListPanel.setLayout(new BoxLayout(animListPanel, BoxLayout.Y_AXIS));
		animListWrapper.add(animListPanel, BorderLayout.NORTH);
		animListScrollPane.getViewport().add(animListWrapper);
		
		JButton newAnimBtn = new JButton(new AnimAction("New Anim", AnimActionType.NEW, null));
		newAnimBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		editPanel = new JPanel();
		editPanel.setBorder(new TitledBorder("Frame:"));
		
		addFrameEditButton(editPanel, "ADD", "\u271A");
		addFrameEditButton(editPanel, "REMOVE", "\u2718");
		addFrameEditButton(editPanel, "UP", "\u2191");
		addFrameEditButton(editPanel, "DOWN", "\u2193");

		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		
		JPanel title = new JPanel();
		JLabel nameLbl = new JLabel("Name");
		nameLbl.setPreferredSize(new Dimension(70, 25));
		JLabel pauseLbl = new JLabel("Pause");
		title.add(nameLbl);
		title.add(pauseLbl);
		
		upperPanel.add(newAnimBtn);
		upperPanel.add(editPanel);
		upperPanel.add(Box.createVerticalStrut(10));
		upperPanel.add(title);
		preview = new AnimPreviewPanel();
		
		
		oPanel.add(upperPanel, BorderLayout.NORTH);
		oPanel.add(animListScrollPane, BorderLayout.CENTER);
		oPanel.add(preview, BorderLayout.SOUTH);
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
		resetOptionsInnerPanel();
	}
	
	@Override
	public void deselected(){
		super.deselected();
		preview.lazyKillRedrawThread();
	}
	
	private JPanel addAnimToPanel(final Animation anim){
		final JPanel animPanel = new JPanel();
		animPanel.setName(anim.getName());
		animPanel.setBackground(Color.BLUE);
		animPanel.setBackground(DEFAULT_COLOR);
		JTextField tf = new JTextField(anim.getName());
		tf.setAction(new AnimAction("", AnimActionType.RENAME, anim));
		tf.setPreferredSize(new Dimension(80, 25));
		
		JTextField pauseField = new JTextField(String.valueOf(anim.getPause()));
		pauseField.setAction(new AnimAction("", AnimActionType.PAUSE, anim));
		pauseField.setPreferredSize(new Dimension(30, 25));
		
		MouseListener selector = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event){
				selectAnimation(animPanel, anim);
			}
		};
		
		animPanel.addMouseListener(selector);
		tf.addMouseListener(selector);
		
		JButton delBtn = new JButton(new AnimAction("X", AnimActionType.DELETE, anim));
		delBtn.setBackground(Color.RED);
		delBtn.setMargin(new Insets(0,0,0,0));
		
		animPanel.add(tf);
		animPanel.add(pauseField);
		animPanel.add(delBtn);
		animListPanel.add(animPanel);
		resetOptionsInnerPanel();
		return animPanel;
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
		if (selectedFrame != null && event.isControlDown()){
			if (selectedAnim.getFrames().contains(selectedFrame)){
				doFrameAction("REMOVE");
			} else {
				doFrameAction("ADD");
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
	
	private void selectAnimation(Container parent, Animation anim){
		if (selectedAnimPanel != null){
			selectedAnimPanel.setBackground(DEFAULT_COLOR);
		}
		selectedAnimPanel = parent;
		selectedAnimPanel.setBackground(Color.DARK_GRAY);
		selectedAnim = anim;
		preview.updateAnimation(selectedAnim);
		MainWindow.MAIN_WINDOW.getImagePanel().getCanvas().refresh();
	}
	
	private void doFrameAction(String action){
		switch (action){
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
			System.out.println("Invalid frame action: " + action);
		}
		preview.updateAnimation(selectedAnim);
		MainWindow.MAIN_WINDOW.setIsDirty(true);
		MainWindow.MAIN_WINDOW.getImagePanel().getCanvas().refresh();
		animListPanel.getParent().getParent().validate();
	}

}
