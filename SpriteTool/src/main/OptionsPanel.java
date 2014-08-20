package main;

import javax.swing.JPanel;

public class OptionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	JPanel innerPanel;
	
	public OptionsPanel()
	{
		innerPanel = new JPanel();
		add(innerPanel);
	}
	
	public void setInner(JPanel inner){
		remove(innerPanel);
		add(inner);
		innerPanel = inner;
		revalidate();
		repaint();
	}

}
