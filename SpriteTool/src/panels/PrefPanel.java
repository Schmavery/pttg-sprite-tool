package panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import main.Preferences;

public class PrefPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private static final String PROP_KEY = "prefname";
	
	private DocumentListener docListener = new DocumentListener(){
		@Override
		public void changedUpdate(DocumentEvent e){
			handleDocumentEvent(e);
		}
		@Override
		public void insertUpdate(DocumentEvent e){
			handleDocumentEvent(e);
		}
		@Override
		public void removeUpdate(DocumentEvent e){
			handleDocumentEvent(e);
		}
		private void handleDocumentEvent(DocumentEvent e){
			try
			{
				System.out.println("prefname: "+ (String) e.getDocument().getProperty(PROP_KEY));
				System.out.println("text: " + e.getDocument().getText(0, e.getDocument().getLength()));
				PrefPanel.this.tmpPrefs.set((String) e.getDocument().getProperty(PROP_KEY), 
						e.getDocument().getText(0, e.getDocument().getLength()));
			}
			catch (BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
	};
	
	private JDialog parent;
	private Preferences tmpPrefs;
	
	public PrefPanel(JDialog parent){
		this.parent = parent;
		setLayout(new BorderLayout());
		setSize(400, 150);
		
		tmpPrefs = new Preferences();
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setTabPlacement(JTabbedPane.LEFT);
		
		JPanel genPanel = new JPanel();
		tabPane.addTab("General", genPanel);
		setupGeneralPanel(genPanel);
		
		JPanel acceptPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		setupAcceptPanel(acceptPanel);
		
		add(tabPane, BorderLayout.CENTER);
		add(acceptPanel, BorderLayout.SOUTH);
	    setVisible(true);
	}
	
	private void setupAcceptPanel(JPanel acceptPanel){
		JButton okButton = new JButton();
		okButton.setAction(new AbstractAction("OK")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Save things first
				Preferences.PREFS = tmpPrefs;
				Preferences.PREFS.savePrefChanges();
				PrefPanel.this.parent.dispose();
			}
		});
		acceptPanel.add(okButton);
		
		JButton cancelButton = new JButton();
		cancelButton.setAction(new AbstractAction("Cancel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				PrefPanel.this.parent.dispose();
			}
		});
		acceptPanel.add(cancelButton);
	}
	
	private void setupGeneralPanel(JPanel genPanel){
		
		genPanel.setLayout(new BoxLayout(genPanel, BoxLayout.Y_AXIS));
		
		JPanel firstPanel = new JPanel(new GridLayout(4, 2));
		
//		JLabel l;
//		JTextField txt;
		
		addPrefItem("Sheet Magnification:", "sheet_mag", firstPanel);
		addPrefItem("Image Magnification:", "image_mag", firstPanel);
		addPrefItem("AutoSnip Size (px):", "autosnip_size", firstPanel);
		
//		l = new JLabel("Sheet Magnification:");
//		l.setHorizontalAlignment(JLabel.CENTER);
//		firstPanel.add(l);
//		txt = new JTextField(tmpPrefs.get("defaultmag"));
//		txt.setPreferredSize(d);
//		txt.addActionListener(this);
//		txt.getDocument().putProperty(PROP_KEY, "defaultmag");
//		txt.getDocument().addDocumentListener(docListener);
//		firstPanel.add(txt);
//		
//		l = new JLabel("Image Magnification:");
//		l.setHorizontalAlignment(JLabel.CENTER);
//		firstPanel.add(l);
//		firstPanel.add(new JTextField("Default..."));
////		txt = new JTextField(tmpPrefs.get("defaultmag"));
//		txt.setPreferredSize(d);
//		txt.addActionListener(this);
//		txt.getDocument().putProperty(PROP_KEY, "defaultmag");
//		txt.getDocument().addDocumentListener(docListener);
//		firstPanel.add(txt);
//		
//		l = new JLabel("AutoSnip Size (px):");
//		l.setHorizontalAlignment(JLabel.CENTER);
//		firstPanel.add(l);
////		firstPanel.add(new JTextField("Default..."));
//		txt = new JTextField(tmpPrefs.get("defaultmag"));
//		txt.setPreferredSize(d);
//		txt.addActionListener(this);
//		txt.getDocument().putProperty(PROP_KEY, "defaultmag");
//		txt.getDocument().addDocumentListener(docListener);
//		firstPanel.add(txt);
		
		
		genPanel.add(firstPanel);
		genPanel.add(Box.createVerticalGlue());
	}
	
	private void addPrefItem(String label, String key, JPanel container){
		Dimension d = new Dimension(200, 20);
		JLabel l = new JLabel(label);
		l.setHorizontalAlignment(JLabel.CENTER);
		container.add(l);
		
		JTextField txt = new JTextField(tmpPrefs.get(key));
		txt.setPreferredSize(d);
		txt.addActionListener(this);
		txt.getDocument().putProperty(PROP_KEY, key);
		txt.getDocument().addDocumentListener(docListener);
		container.add(txt);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		System.out.println(event.paramString());
	}

}
