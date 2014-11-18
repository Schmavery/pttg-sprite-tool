package tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Animation;
import main.ImageData.ImageType;
import main.MainWindow;

/**
 * Option panel displays a list of animations with names.
 * Clicking on each one will highlight included frames on
 * the spritesheet.
 */
public class AnimTool extends Tool
{
	enum AnimActionType {EDIT, RENAME, NEW, DELETE};
	Animation selectedAnim;
	JPanel animListPanel;
	JPanel editPanel;
	
	class AnimAction extends AbstractAction {
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
			switch (this.type){
			case DELETE:
				//TODO: Removey stuff? Reset?
				AnimTool.this.animListPanel.remove((Component) ((JButton) event.getSource()).getParent());
				MainWindow.MAIN_WINDOW.getSheetData().getAnimations().remove(anim);
				break;
			case EDIT:
				selectedAnim = anim;
				//TODO: Highlight stuff
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
		}
	}

	
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
		JButton moveUpBtn;
		JButton moveDownBtn;
		JButton addBtn;
		JButton removeBtn;
		
		oPanel.add(newAnimBtn);
		oPanel.add(editPanel);
		oPanel.add(animListPanel);
	}
	
	@Override
	public void selected(){
		super.selected();
		selectedAnim = null;
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
		
		JButton editBtn = new JButton(new AnimAction("Edit", AnimActionType.EDIT, anim));
		editBtn.setMargin(new Insets(0,0,0,0));

		
		JButton delBtn = new JButton(new AnimAction("X", AnimActionType.DELETE, anim));
		delBtn.setBackground(Color.RED);
		delBtn.setMargin(new Insets(0,0,0,0));
		
		animPanel.add(tf);
		animPanel.add(editBtn);
		animPanel.add(delBtn);
		animListPanel.add(animPanel);
		animListPanel.getParent().getParent().validate();
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		if (selectedAnim != null){
			
		}
	}

}
