package panels;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class StatusPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	JLabel leftLabel;
	
	public StatusPanel(){
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setPreferredSize(new Dimension(this.getWidth(), 20));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		leftLabel = new JLabel("Magnifier");
		add(leftLabel);
	}
	
	public void setLeftLabel(String str){
		leftLabel.setText(str);
	}
	
}
