package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import main.ImageData.ImageType;
import panels.ImagePanel;
import panels.OptionsPanel;
import panels.StatusPanel;
import tools.Tool;
import tools.Tools;


public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_HEIGHT = 700;
	public static final int WINDOW_WIDTH = 1000;
	public static MainWindow MAIN_WINDOW;
	
	public ImagePanel imagePanel;
	public StatusPanel statusPanel;
	public OptionsPanel optionsPanel;
	
	public Tool currentTool = Tools.getMagnifier();
	
	private boolean isDirty = false;
	private String savePath = "";
	
	public static void main(String[] args)
	{
		new Preferences();
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				MainWindow w = new MainWindow();
				w.setVisible(true);
			}
		});
	}
	
	
	public MainWindow(){
		if (MAIN_WINDOW == null){
			MainWindow.MAIN_WINDOW = this;
		}
		
		setTitle("PTTG Sprite Tool");
		
		try{
			Image img = ImageIO.read(new File("res/board.png"));
			setIconImage(img);
		}catch (IOException e){
			e.printStackTrace();
		}
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(true);
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		setLayout(new BorderLayout(5, 5));
		
		statusPanel = new StatusPanel();
		
		imagePanel = new ImagePanel();
		
		
		JMenuBar menuBar = new JMenuBar();
		setupMenu(menuBar);
		
		JPanel toolbarShell = new JPanel();
		setupToolbar(toolbarShell);
		
		add(statusPanel, BorderLayout.SOUTH);
		add(toolbarShell, BorderLayout.EAST);
		add(menuBar, BorderLayout.NORTH);
		add(imagePanel, BorderLayout.CENTER);

		
		
		setupListeners();
		currentTool.selected();
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
		
		JMenuItem closeMI = new JMenuItem();
		closeMI.setAction(new AbstractAction("Close") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainWindow.this.exit();
			}
			
		});
		menu.add(closeMI);
		
		openMI.setAccelerator(KeyStroke.getKeyStroke("control O"));
		saveMI.setAccelerator(KeyStroke.getKeyStroke("control S"));
	}
	
	private void setupToolbar(JPanel toolbarShell){
		toolbarShell.setLayout(new BorderLayout());
		
		JPanel toolbar = new JPanel(new GridLayout(5, 1, 3, 3));
		toolbar.setBorder(new TitledBorder("Toolbar"));
		
		createToolbarButton(Tools.getMagnifier(), toolbar);
		createToolbarButton(Tools.getSnipTool(), toolbar);
		createToolbarButton(Tools.getAnchor(), toolbar);
		createToolbarButton(Tools.getBoxTool(), toolbar);
		createToolbarButton(Tools.getHookTool(), toolbar);

//		toolbar.add(new JButton("A"));
		
		optionsPanel = new OptionsPanel();
		Dimension d = new Dimension(150, 150);
		optionsPanel.setPreferredSize(d);
		optionsPanel.setBorder(new TitledBorder("Options"));
		toolbarShell.add(optionsPanel, BorderLayout.CENTER);
		toolbarShell.add(toolbar, BorderLayout.NORTH);		
	}
	
	private void createToolbarButton(final Tool tool, JPanel toolbar){
		JButton button = new JButton();
		button.setAction(new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event)
			{
				currentTool.deselected();
				currentTool = tool;
				currentTool.selected();
			}
		});
		button.setIcon(new ImageIcon(tool.getImage()));
		button.setBackground(Color.LIGHT_GRAY);
		tool.setButton(button);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(40, 40));
		button.setMargin(new Insets(0,0,0,0));
		toolbar.add(button);
	}
	
	private void setupListeners(){
		// Save preferences on exit.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
	            MainWindow.this.exit();
	        }
	    });
		
	}
	
	public Canvas getCanvas(){
		return imagePanel.getCanvas();
	}
	
	public SheetData getSheetData(){
		return imagePanel.getSheetData();
	}
	
	public ImagePanel getImagePanel(){
		return imagePanel;
	}

	public void openImage(String path){
		imagePanel.setSheetPath(path);
		load(path + ".dat");
		savePath = path;
		Tools.setButtonEnabledState(ImageType.SHEET);
		this.isDirty = false;
	}	
	
	public Tool getCurrentTool(){
		return currentTool;
	}
	
	public void setIsDirty(boolean b){
		this.isDirty = b;
	}
	
	public void save(boolean useDefault){
		// if save is successful
		if (useDefault && savePath != null && !savePath.isEmpty()){
			save(savePath);
		} else {
			// Display save dialog.
			JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(MainWindow.MAIN_WINDOW);
			File file = fc.getSelectedFile();
			if (file != null){
//				System.out.println(file.getAbsolutePath());
				save(file.getAbsolutePath());
			}
		}
	}
	
	private void save(String path){
		this.isDirty = false;
	}
	
	public void load(String path){
		// Attempt to load data file for image.
	}

	public void exit(){
		if (isDirty){
			// Prompt for save first
			int result = JOptionPane.showConfirmDialog(this, 
					"Save before exiting?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
//			JOptionPane.show
			switch (result){
			case JOptionPane.YES_OPTION:
				save(true);
				break;
			case JOptionPane.NO_OPTION:
				Preferences.PREFS.savePrefChanges();
				System.exit(0);
				break;
			case JOptionPane.CANCEL_OPTION:
				System.out.println("Cancel!");
				return;
			}
		} else {
			Preferences.PREFS.savePrefChanges();
			System.exit(0);
		}
	}
}
