package panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import javax.swing.SwingConstants;
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
		tabPane.setTabPlacement(SwingConstants.LEFT);
		
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
		panel.setLayout(new BorderLayout());
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		
		addPrefBool("Show Tooltips", "show_tips", panel);
		addPrefItem("Sheet Magnification", "sheet_mag", inner);
		addPrefItem("Image Magnification", "image_mag", inner);
		addPrefItem("AutoSnip Size (px)", "autosnip_size", inner);
		
		panel.add(inner, BorderLayout.NORTH);
	}
	
	private void setupCollisionPanel(JPanel panel){
		panel.setLayout(new BorderLayout());
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		
		addPrefBool("Auto Collision Box", "hull_auto", inner);
		final JCheckBox collChk = addPrefBool("Auto Collision Box", "coll_auto", inner);
		final JPanel collContainer = new JPanel();
		collContainer.setLayout(new BoxLayout(collContainer, BoxLayout.Y_AXIS));
		addPrefItem("Collision X", "coll_x", collContainer);
		addPrefItem("Collision Y", "coll_y", collContainer);
		addPrefItem("Collision Height", "coll_h", collContainer);
		addPrefItem("Collision Width", "coll_w", collContainer);
		setContainerEnabledState(collContainer, collChk.isSelected());
		inner.add(collContainer);
		
		collChk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event)
			{
				System.out.println("Stuffff");
				setContainerEnabledState(collContainer, collChk.isSelected());
			}
		});

		panel.add(inner, BorderLayout.NORTH);
	}
	
	private void setContainerEnabledState(Component container, boolean enabled) {
        if (container instanceof JPanel){
        	for (Component comp : ((Container) container).getComponents()){
        		setContainerEnabledState(comp, enabled);
        	}
        } else {
        	((Component) container).setEnabled(enabled);
        }
	}

	private void setupAnimationPanel(JPanel panel){
		panel.setLayout(new BorderLayout());
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		
		addPrefItem("Default Pause", "pause", inner);
		
		panel.add(inner, BorderLayout.NORTH);
	}
	
	private void setupAnchorPanel(JPanel panel){
		panel.setLayout(new BorderLayout());
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		
		addPrefBool("Auto Anchor", "anchor_auto", inner);
		addPrefItem("Anchor X", "anchor_x", inner);
		addPrefItem("Anchor Y", "anchor_y", inner);
		
		panel.add(inner, BorderLayout.NORTH);;
	}

	private void addPrefItem(String title, String key, JPanel container){
		Dimension d = new Dimension(150, 30);
		JLabel l = new JLabel(title + ":");
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setPreferredSize(d);
		JPanel wrapper = new JPanel();
		
		JTextField txt = new JTextField(tmpPrefs.get(key));
		txt.setPreferredSize(d);
		txt.setName(key);
		txt.getDocument().putProperty(PROP_KEY, key);
		txt.getDocument().addDocumentListener(docListener);

		wrapper.add(l);
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
