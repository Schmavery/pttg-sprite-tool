package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class OptionsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	JPanel innerPanel;
	
	public OptionsPanel()
	{
		setLayout(new BorderLayout());
		Dimension d = new Dimension(160, 150);
		setPreferredSize(d);
		setBorder(new TitledBorder("Options"));
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
