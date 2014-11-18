package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Canvas;
import main.Hook;
import main.ImageData.ImageType;
import main.MainWindow;

public class HookTool extends Tool
{
	private Hook currHook;
	
	private JTextField text;
	private JButton accept;
	
	public HookTool(){
		super("Hook Tool", "res/pencil.png", ImageType.IMAGE);
		
		JPanel oPanel = getOptionsInnerPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));

		ActionListener acceptListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event){
				HookTool.this.updateName();
			}
		};
		
		text = new JTextField();
		text.addActionListener(acceptListener);
		accept = new JButton("Update Hook Name");
		accept.addActionListener(acceptListener);

		oPanel.add(text);
		oPanel.add(accept);
	}
	
	private void updateName(){
		String newName = text.getText();
		if (newName.isEmpty()){
			deleteHook();
		} else if (currHook != null){
			if (currHook.getName() == null || currHook.getName().isEmpty()){
				// Add the hook to the imagedata
				currHook.setName(newName);
				MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().addHook(currHook);
			} else {
				// Update the imagedata hook
				currHook.setName(newName);
			}
		}
	}
	
	private void deleteHook(){
		if (currHook != null){
			LinkedList<Hook> hooks = MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().getHooks();
			if (hooks.contains(currHook)){
				hooks.remove(currHook);
			} else {
				System.out.println("Couldn't find hook to remove.");
			}
		}
	}
	
	private void setNamePanelVisible(boolean b){
		System.out.println("Setting visible: "+b);
		if (b){
			text.setText(currHook.getName());
			text.requestFocusInWindow();
		}
		text.setVisible(b);
		accept.setVisible(b);
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		// Check if you clicked on an existing hook
		Point p = new Point(x, y);
		currHook = null;
		for (Hook h : MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().getHooks()){
			if (h.getPt().equals(p)){
				currHook = h;
				break;
			}
		}
		// Create new hook
		if (currHook == null){
			System.out.println("Added new hook");
			currHook = new Hook("", p);
		}
		setNamePanelVisible(true);
	}
	
	@Override
	public void selected(){
		super.selected();
		currHook = null;
		setNamePanelVisible(false);
	}
	
	@Override
	public void drawTool(Graphics g, int mouseX, int mouseY){
		if (currHook != null && currHook.getPt() != null){
			Canvas c = MainWindow.MAIN_WINDOW.getCanvas();
			g.setColor(Color.GREEN);
			g.fillRect(c.getScaledCoord(currHook.getPt().x), c.getScaledCoord(currHook.getPt().y), 
					c.getScaledCoord(1), c.getScaledCoord(1));
		}
	}

}
