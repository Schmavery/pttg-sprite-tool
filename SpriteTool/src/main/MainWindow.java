package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

import main.tools.Tool;
import main.tools.Tools;


public class MainWindow extends JFrame
{

//	public static enum Tools {MAG, BRUSH};
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_HEIGHT = 700;
	public static final int WINDOW_WIDTH = 1000;
	public static MainWindow MAIN_WINDOW;
	
	public ImagePanel centerPanel;
	public StatusPanel statusPanel;
	public Tool currentTool = Tools.getMagnifier();
	
	public static void main(String[] args)
	{
		new Preferences();
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
		
		
		
		statusPanel = new StatusPanel();
		
		
		centerPanel = new ImagePanel();
		centerPanel.setBorder(new TitledBorder("Image"));
		
		JMenuBar menuBar = new JMenuBar();
		setupMenu(menuBar);
		
		JPanel toolbarShell = new JPanel();
		setupToolbar(toolbarShell);
		
		JPanel options = new JPanel();
		options.setBorder(new TitledBorder("Options"));
		toolbarShell.add(options);
		
		add(statusPanel, BorderLayout.SOUTH);
		add(toolbarShell, BorderLayout.EAST);
		add(menuBar, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		
		
		setupListeners();
//		currentTool.selected();
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
	
	private void setupToolbar(JPanel toolbarShell){
		toolbarShell.setLayout(new BoxLayout(toolbarShell, BoxLayout.PAGE_AXIS));
//		toolbarShell.setPreferredSize(new Dimension(120, 100));
		JPanel toolbar = new JPanel(new GridLayout(5, 3, 3, 3));
		toolbar.setBorder(new TitledBorder("Toolbar"));
		
		JButton magButton = new JButton();
		magButton.setAction(new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event)
			{
				currentTool.deselected();
				currentTool = Tools.getMagnifier();
				currentTool.selected();
			}
		});
//		magButton.setBorderPainted(false);
//		magButton.setContentAreaFilled(false);
		magButton.setIcon(new ImageIcon(Tools.getMagnifier().getImage()));
		magButton.setBackground(Color.LIGHT_GRAY);
		magButton.setFocusPainted(false);
		magButton.setPreferredSize(new Dimension(30, 30));
		magButton.setMargin(new Insets(0,0,0,0));
		toolbar.add(magButton);
		
		
		JButton anchorButton = new JButton();
		anchorButton.setAction(new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event)
			{
				currentTool.deselected();
				currentTool = Tools.getAnchor();
				currentTool.selected();
			}
		});
		anchorButton.setIcon(new ImageIcon(Tools.getAnchor().getImage()));
		anchorButton.setBackground(Color.LIGHT_GRAY);
		anchorButton.setFocusPainted(false);
		anchorButton.setPreferredSize(new Dimension(30, 30));
		anchorButton.setMargin(new Insets(0,0,0,0));
		toolbar.add(anchorButton);
		

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
	}
	
	private void setupListeners(){
		// Save preferences on exit.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
	            Preferences.PREFS.savePrefChanges();
	            System.exit(0);
	        }
	    });
		
	}
	
	public Canvas getCanvas(){
		return centerPanel.getCanvas();
	}

	public void setImagePath(String path){
		System.out.println("Setting image");
		centerPanel.setImagePath(path);
	}	
	
	public Tool getCurrentTool(){
		return currentTool;
	}

}
