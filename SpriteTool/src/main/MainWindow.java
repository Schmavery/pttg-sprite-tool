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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

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
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.ImageData.ImageType;
import panels.ImagePanel;
import panels.OptionsPanel;
import panels.StatusPanel;
import parsers.DefaultParser;
import parsers.JSONParser;
import parsers.Parser;
import tools.Tool;
import tools.Tools;


public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static final int WINDOW_HEIGHT = 700;
	public static final int WINDOW_WIDTH = 1000;
	public static final int TIP_DELAY = 500;
	public static final String DEFAULT_SUFFIX = ".dat";
	public static final String TITLE = "PTTG Sprite Tool";
	public static MainWindow MAIN_WINDOW;
	
	
	
	public ImagePanel imagePanel;
	public StatusPanel statusPanel;
	public OptionsPanel optionsPanel;
	
	public Tool currentTool = Tools.getMagnifier();
	
	private JFileChooser fileChooser = new JFileChooser();
	private boolean isDirty = false;
	private HashMap<FileNameExtensionFilter, Parser> filters;
	private Parser currParser;
	
	private String imgPath = "";
	private String dataPath = "";
	
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
		
		setTitle(TITLE);
		
		try{
			Image img = ImageIO.read(new File("res/board.png"));
			setIconImage(img);
		}catch (IOException e){
			e.printStackTrace();
		}
		
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(true);
		setLocationRelativeTo(null);
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
		
		ToolTipManager.sharedInstance().setEnabled(Boolean.valueOf(Preferences.PREFS.get("show_tips")));
		ToolTipManager.sharedInstance().setInitialDelay(TIP_DELAY);
		ToolTipManager.sharedInstance().setReshowDelay(0);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
	            MainWindow.this.exit();
	        }
	    });
		
		filters = new HashMap<>();
		// Define these in reverse order
		filters.put(new FileNameExtensionFilter("JSON file (*.json)", "json"), new JSONParser());
		filters.put(new FileNameExtensionFilter("Data file (*.dat)", "dat"), new DefaultParser());
		
		currentTool.selected();
	}
	
	private void setupMenu(JMenuBar menuBar){
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		menu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem newMI = new JMenuItem();
		
		// Note: If you select a data file, it will be as if you "Open"ed it.
		newMI.setAction(new AbstractAction("New") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e){
				fileChooser.showOpenDialog(MainWindow.MAIN_WINDOW);
				File file = fileChooser.getSelectedFile();
				if (file != null){
					System.out.println("-->"+file.getAbsolutePath());
					MainWindow.MAIN_WINDOW.openImage(file.getAbsolutePath(), false);
				}
			}
		});
		newMI.setMnemonic(KeyEvent.VK_N);
		menu.add(newMI);
		
		JMenuItem openMI = new JMenuItem();
		openMI.setAction(new AbstractAction("Open") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e){
				fileChooser.showOpenDialog(MainWindow.MAIN_WINDOW);
				File file = fileChooser.getSelectedFile();
				if (file != null){
					System.out.println(">>>"+file.getAbsolutePath());
					MainWindow.MAIN_WINDOW.openImage(file.getAbsolutePath(), true);
				}
			}
		});
		openMI.setMnemonic(KeyEvent.VK_O);
		menu.add(openMI);
		
		JMenuItem saveMI = new JMenuItem();
		saveMI.setAction(new AbstractAction("Save") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e){
				MainWindow.this.save(true);
			}
		});
		saveMI.setMnemonic(KeyEvent.VK_S);
		menu.add(saveMI);
		
		JMenuItem saveAsMI = new JMenuItem();
		saveAsMI.setAction(new AbstractAction("Save As") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e){
				MainWindow.this.save(false);
			}
		});
		menu.add(saveAsMI);
		
		JMenuItem exportAsMI = new JMenuItem();
		exportAsMI.setAction(new AbstractAction("Export As") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e){
//				MainWindow.this.export(false);
			}
		});
		menu.add(exportAsMI);
		
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
		
		JPanel toolbar = new JPanel(new GridLayout(3, 3, 3, 3));
		toolbar.setBorder(new TitledBorder("Toolbar"));
		
		for (Tool t : Tools.getTools()){
			createToolbarButton(t, toolbar);
		}

//		toolbar.add(new JButton("A"));
		
		optionsPanel = new OptionsPanel();
		
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
				setCurrentTool(tool);
			}
		});
		button.setIcon(new ImageIcon(tool.getImage()));
		button.setBackground(Color.LIGHT_GRAY);
		tool.setButton(button);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(40, 40));
		button.setMargin(new Insets(0,0,0,0));
		button.setToolTipText(tool.getName());
		toolbar.add(button);
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

	public Tool getCurrentTool(){
		return currentTool;
	}
	
	public void setCurrentTool(Tool tool){
		currentTool.deselected();
		currentTool = tool;
		currentTool.selected();
	}
	
	public void openImage(String path, boolean tryLoadData){
		// This may be a data file rather than an image.
		dataPath = "";
		boolean loadData = tryLoadData;
		for (Parser p : filters.values()){
			if (path.endsWith(p.getSuffix())){
				dataPath = path;
				loadData = true;
				
				
				// Get image path from data file
				try (BufferedReader br = new BufferedReader(new FileReader(path))){
					StringBuffer sb = new StringBuffer();
					String read;
					sb.append((read = br.readLine()) + "\n");
					while ((read = br.readLine()) != null) sb.append(read + "\n");
					
					imgPath = p.getImagePath(sb.toString());
					System.out.println("Read imgPath"+imgPath);
					File f = new File(imgPath);
					if (!f.isAbsolute()){
						imgPath = (new File(dataPath)).getParent()+File.separator+imgPath;						
					}
					currParser = p;
				}
				catch (IOException e)
				{
					System.out.println("Could not read.");
				}
				break;
			}
		}
		if (loadData && dataPath.length() == 0){
			dataPath = path + DEFAULT_SUFFIX;
			currParser = new DefaultParser();
			//imagePanel.setSheetPath(path);
			imgPath = path;
		}
		
		
		imagePanel.setSheetPath(imgPath);
		Tools.setButtonEnabledState(ImageType.SHEET);
		if (loadData){
			load(dataPath);
		}
		setIsDirty(false);
	}	
	
	public void setIsDirty(boolean b){
		this.isDirty = b && imgPath != null && imgPath.length() > 0;
		if (this.isDirty){
			setTitle(TITLE + ": *"+ imgPath);
		} else {
			setTitle(TITLE + ": "+ imgPath);
		}
	}
	
	public void save(boolean useDefault){
		System.out.println("Attempting save");
		// if save is successful
		if (useDefault && dataPath != null && !dataPath.isEmpty() && currParser != null){
			save(dataPath, currParser);
		} else {
			// Display save dialog.
			JFileChooser fc = new JFileChooser();
			for (FileNameExtensionFilter f : filters.keySet()){
				fc.setFileFilter(f);
			}

			fc.showSaveDialog(MainWindow.MAIN_WINDOW);
			System.out.println(fc.getFileFilter().getDescription());
			currParser = filters.get(fc.getFileFilter());
			File file = fc.getSelectedFile();
			if (file != null){
				save(file.getAbsolutePath(), currParser);
			}
		}
	}
	
	private void save(String path, Parser parser){
		dataPath = path + (path.endsWith(parser.getSuffix()) ? "" : parser.getSuffix());
		
		try (PrintWriter out = new PrintWriter(dataPath)){
			Path pData = Paths.get(dataPath);
			Path pImg = Paths.get(imgPath);
			out.write(parser.save((pData.getParent()).relativize(pImg).toString()));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Could not write to file.");
			e.printStackTrace();
		}
		setIsDirty(false);
	}
	
	/**
	 * Attempts to load data from specified path, creates 
	 * ImageData objects corresponding to the loaded data
	 * and adds them to the ImagePanel.
	 * @param path : String - Absolute path to data file.
	 * @return Path of the image for this data.
	 */
	public void load(String path){
		Parser parser = new DefaultParser();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path))))
		{
			String str = br.readLine();
			StringBuilder sb = new StringBuilder();
			while(str != null){
				str = br.readLine();
				sb.append(str);
				sb.append("\n");
			}
			parser.load(sb.toString());
			ImageData.resetId(getSheetData().getAllImageData());
			Animation.resetId(getSheetData().getAnimations());
			currentTool.selected();
		} catch (IOException e) {
			System.out.println("Load failed: "+path);
		}
	}

	public void exit(){
		System.out.println("Exiting");
		if (isDirty){
			// Prompt for save first
			int result = JOptionPane.showConfirmDialog(this, 
					"Save before exiting?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (result){
			case JOptionPane.YES_OPTION:
				save(true);
				break;
			case JOptionPane.NO_OPTION:
				Preferences.PREFS.savePrefChanges();
				System.exit(0);
				break;
			case JOptionPane.CANCEL_OPTION:
				return;
			}
		} else {
			Preferences.PREFS.savePrefChanges();
			System.exit(0);
		}
	}
}
