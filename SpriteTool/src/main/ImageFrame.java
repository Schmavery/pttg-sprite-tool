package main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageFrame extends JScrollPane
{
	private JLabel imgLabel;
	
	public ImageFrame(){
//		JScrollPane scrollPane = new JScrollPane();
//		add(scrollPane);
//	    ImageIcon img = new ImageIcon("D:\\Avery\\Desktop\\SAVE ITa.jpg");
//	    imgLabel = new JLabel(img);
//	    scrollPane.setViewportView(imgLabel);
//	    setSize(300, 250);
	    setVisible(true);
	}

	public void setImagePath(String path){
		System.out.println("setting in imgframe: "+path);
		if (imgLabel == null){
			imgLabel = new JLabel(new ImageIcon(path));
		} else {
			imgLabel.setIcon(new ImageIcon(path));
		}
		setViewportView(imgLabel);
	}
}
