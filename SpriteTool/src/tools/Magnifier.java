package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.ImageData;
import main.ImageData.ImageType;
import main.MainWindow;

public class Magnifier extends Tool
{
	
	JTextField magAmt;
	
	public Magnifier(){
		super("Magnifier", "res/mag.png", ImageType.EITHER);
		
		JPanel oPanel = new JPanel();
		oPanel.setLayout(new BoxLayout(oPanel, BoxLayout.Y_AXIS));
		oPanel.add(new JLabel("Magnification:"));
		magAmt = new JTextField();
		magAmt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				ImageData iData = MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData();
				float scale = 1;
				try {
					scale = Float.parseFloat(magAmt.getText());
					magAmt.setText(formatFloat(iData.setScale(scale)));
				} catch (NumberFormatException e){
					scale = iData.getScale();
					magAmt.setText(formatFloat(scale));
				}
			}
		});
		
		oPanel.add(magAmt);
		setOptionInnerPanel(oPanel);
	}
	
	@Override
	public void resetOptionsInnerPanel(){
		magAmt.setText(formatFloat(MainWindow.MAIN_WINDOW.getSheetData().getCurrentImageData().getScale()));
	}
	
	@Override
	public void onClick(MouseEvent event, int x, int y)
	{
		MainWindow mw = MainWindow.MAIN_WINDOW;
		if (event.isControlDown()){
			mw.getCanvas().decScale();
		} else {
			mw.getCanvas().incScale();
		}
		resetOptionsInnerPanel();
		mw.getCanvas().refresh();
	}
	
}
