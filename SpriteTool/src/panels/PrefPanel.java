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
import javax.swing.JFormattedTextField;
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
		Dimension d = new Dimension(300, 20);
		
		genPanel.setLayout(new BoxLayout(genPanel, BoxLayout.Y_AXIS));
		
		JPanel firstPanel = new JPanel(new GridLayout(4, 2));
		firstPanel.add(new JLabel("Default Magnification:"));
		JTextField defaultMag = new JTextField(tmpPrefs.get("defaultmag"));
		defaultMag.setPreferredSize(d);
		defaultMag.addActionListener(this);
		defaultMag.getDocument().putProperty(PROP_KEY, "defaultmag");
		defaultMag.getDocument().addDocumentListener(docListener);
		firstPanel.add(defaultMag);
		firstPanel.add(new JLabel("Space Filler:"));
		firstPanel.add(new JTextField("Default..."));
		firstPanel.add(new JLabel("Space Filler:"));
		firstPanel.add(new JTextField("Default..."));
		firstPanel.add(new JLabel("Space Filler:"));
		firstPanel.add(new JTextField("Default..."));
		
		
		genPanel.add(firstPanel);
		genPanel.add(Box.createVerticalGlue());
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		// TODO Auto-generated method stub
		System.out.println(event.paramString());
	}

}
