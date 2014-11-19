package panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import main.Preferences;

public class PrefPanel extends JPanel
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
				System.out.println("prefname: "+ (String) e.getDocument().getProperty(PROP_KEY) + ": "
						+ e.getDocument().getText(0, e.getDocument().getLength()));
				
				PrefPanel.this.tmpPrefs.set((String) e.getDocument().getProperty(PROP_KEY), 
						e.getDocument().getText(0, e.getDocument().getLength()));
			}
			catch (BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
	};
	
	private ChangeListener changeListener = new ChangeListener()
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			if (e.getSource() instanceof JCheckBox){
				JCheckBox src = (JCheckBox) e.getSource();
				PrefPanel.this.tmpPrefs.set(src.getName(), Boolean.toString(src.isSelected()));
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
		JPanel collPanel = new JPanel();
		JPanel animPanel = new JPanel();
		JPanel anchorPanel = new JPanel();
		
		tabPane.addTab("General", genPanel);
		tabPane.addTab("Anchor", anchorPanel);
		tabPane.addTab("Collision", collPanel);
		tabPane.addTab("Animation", animPanel);
		
		setupGeneralPanel(genPanel);
		setupAnchorPanel(anchorPanel);
		setupCollisionPanel(collPanel);
		setupAnimationPanel(animPanel);
		
		
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
			public void actionPerformed(ActionEvent e)
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
	
	private void setupGeneralPanel(JPanel panel){
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		addPrefBool("Show Tooltips", "show_tips", panel);
		
		JPanel firstPanel = new JPanel(new GridLayout(4, 2));
		
		addPrefItem("Sheet Magnification", "sheet_mag", firstPanel);
		addPrefItem("Image Magnification", "image_mag", firstPanel);
		addPrefItem("AutoSnip Size (px)", "autosnip_size", firstPanel);
		
		
		
		panel.add(firstPanel);
		panel.add(Box.createVerticalGlue());
	}
	
	private void setupCollisionPanel(JPanel panel){
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		addPrefBool("Auto Collision Box", "coll_auto", panel);
		
		JPanel secondPanel = new JPanel(new GridLayout(5, 2));
		addPrefItem("Collision X", "coll_x", secondPanel);
		addPrefItem("Collision Y", "coll_y", secondPanel);
		addPrefItem("Collision Height", "coll_h", secondPanel);
		addPrefItem("Collision Width", "coll_w", secondPanel);
		
		
		panel.add(secondPanel);
		panel.add(Box.createVerticalGlue());
	}
	
	private void setupAnimationPanel(JPanel panel){
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JPanel firstPanel = new JPanel(new GridLayout(1, 2));
		addPrefItem("Default Pause", "pause", firstPanel);
		
		panel.add(firstPanel);
		panel.add(Box.createVerticalGlue());
	}
	
	private void setupAnchorPanel(JPanel panel){
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		addPrefBool("Auto Anchor", "anchor_auto", panel);
		
		JPanel firstPanel = new JPanel(new GridLayout(2, 2, 0, 0));
		addPrefItem("Anchor X", "anchor_x", firstPanel);
		addPrefItem("Anchor Y", "anchor_y", firstPanel);
		
		panel.add(firstPanel);
		panel.add(Box.createVerticalGlue());
	}
	
	private void addPrefItem(String title, String key, JPanel container){
		Dimension d = new Dimension(150, 30);
		JLabel l = new JLabel(title + ":");
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setPreferredSize(d);
		JPanel wrapper = new JPanel();
		wrapper.add(l);
		container.add(wrapper);
		
		JTextField txt = new JTextField(tmpPrefs.get(key));
		txt.setPreferredSize(d);
		txt.setName(key);
		txt.getDocument().putProperty(PROP_KEY, key);
		txt.getDocument().addDocumentListener(docListener);
		wrapper = new JPanel();
		wrapper.add(txt);
		container.add(wrapper);
	}
	
	private JCheckBox addPrefBool(String title, String key, JPanel container){
		Dimension d = new Dimension(200, 15);
		JCheckBox chk = new JCheckBox(title, Boolean.parseBoolean(tmpPrefs.get(key)));
		chk.setPreferredSize(d);
		chk.setName(key);
		chk.setAlignmentX(Component.CENTER_ALIGNMENT);

		chk.addChangeListener(changeListener);
		container.add(Box.createVerticalStrut(10));
		container.add(chk);
		container.add(Box.createVerticalStrut(5));
		return chk;
	}

}
