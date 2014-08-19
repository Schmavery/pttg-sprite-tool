package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;


public class MainWindow extends JFrame
{

	private static final long serialVersionUID = 1L;
	public static final int WINDOW_HEIGHT = 700;
	public static final int WINDOW_WIDTH = 1000;
	public static MainWindow MAIN_WINDOW;
	
	public ImageFrame centerPanel;
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				MainWindow w = new MainWindow();
				MainWindow.MAIN_WINDOW = w;
				w.setVisible(true);
			}
		});
	}
	
	
	public MainWindow(){
		setTitle("PTTG Sprite Tool");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout(5, 5));
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("statusbar");
		statusPanel.add(statusLabel);
		
		centerPanel = new ImageFrame();
		centerPanel.setBorder(new TitledBorder("Image"));
		
		JMenuBar menuBar = new JMenuBar();
		setupMenu(menuBar);
		
		JPanel toolbarShell = new JPanel();
		toolbarShell.setLayout(new BoxLayout(toolbarShell, BoxLayout.PAGE_AXIS));
		JPanel toolbar = new JPanel(new GridLayout(5, 3, 3, 3));
		toolbar.setBorder(new TitledBorder("Toolbar"));
		//		JButton b1 = new JButton(arg0, arg1)
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbar.add(new JButton("A"));
		toolbarShell.add(toolbar);
		
		JPanel options = new JPanel();
		options.setBorder(new TitledBorder("Options"));
		toolbarShell.add(options);
		
		add(statusPanel, BorderLayout.SOUTH);
		add(toolbarShell, BorderLayout.EAST);
		add(menuBar, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		
	}
	
	private void setupMenu(JMenuBar menuBar){
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		menu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem openMI = new JMenuItem();
		openMI.setAction(new OpenAction());
		openMI.setMnemonic(KeyEvent.VK_O);
		menu.add(openMI);
		
		JMenuItem saveMI = new JMenuItem();
		saveMI.setAction(new SaveAction());
		saveMI.setMnemonic(KeyEvent.VK_S);
		menu.add(saveMI);
		
		JMenuItem saveAsMI = new JMenuItem();
		saveAsMI.setAction(new AbstractAction("Save As") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Saving As?");
			}
			
		});
		menu.add(saveAsMI);
		
		JMenuItem prefMI = new JMenuItem();
		prefMI.setAction(new PrefAction());
		prefMI.setMnemonic(KeyEvent.VK_P);
		menu.add(prefMI);
		
		openMI.setAccelerator(KeyStroke.getKeyStroke("control O"));
		saveMI.setAccelerator(KeyStroke.getKeyStroke("control S"));
	}

	public void setImagePath(String path){
		System.out.println("Setting image");
//		ImageIcon img = new ImageIcon(path);
//		img.paintIcon(centerPanel, centerPanel.getGraphics(), 10, 10);
		centerPanel.setImagePath(path);
	}

}
