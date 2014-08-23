package main;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class OptionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	JPanel innerPanel;
	
	public OptionsPanel()
	{
//		setLayout(new BorderLayout());
		innerPanel = new JPanel();
		add(innerPanel, BorderLayout.CENTER);
	}
	
	public void setInner(JPanel inner){
		remove(innerPanel);
		add(inner, BorderLayout.CENTER);
		innerPanel = inner;
		revalidate();
		repaint();
	}

}
