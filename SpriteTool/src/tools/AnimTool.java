package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

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

	ActionListener renameListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent event){
			AnimTool.this.updateName();
		}
	};
	
	ActionListener editListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent event){
			AnimTool.this.updateName();
		}
	};
	
	
	
	public AnimTool()
	{
		super("Animation Tool", "res/board.png", ImageType.SHEET);
		
		JPanel oPanel = new JPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));

		setOptionInnerPanel(oPanel);
	}
	
	@Override
	public void selected(){
		super.selected();
		List<Animation> anims = MainWindow.MAIN_WINDOW.getSheetData().getAnimations();
		for (Animation anim : anims){
			JPanel animPanel = new JPanel();
			animPanel.setName(anim.getName());
			JTextField tf = new JTextField(anim.getName());
			animPanel.add(tf);
			JButton editBtn = new JButton("Edit");
			editBtn.addActionListener(editListener);
			animPanel.add(editBtn);
		}
		
	}
	
	public void updateName(){
		
	}

	@Override
	public void onClick(MouseEvent event, int x, int y)
	{

	}

}
