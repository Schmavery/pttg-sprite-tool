package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class Preferences implements Cloneable
{
	public static Preferences PREFS;
	private static final String PREF_FILENAME = "spritetool.properties";
	private Properties properties;
	
	public Preferences(){
		if (PREFS == null){
			PREFS = this;
		}
		loadPrefs();
	}
	
	public void loadPrefs() {
	    properties = new Properties();
	    InputStream in = null;
	 
	    // First try loading from the current directory
	    try {
	        File f = new File(PREF_FILENAME);
	        in = new FileInputStream(f);
	    }
	    catch ( Exception e ) { in = null; }
	 
	    try {
	        if ( in == null ) {
	            // Try loading from classpath
	            in = Preferences.class.getClass().getResourceAsStream(PREF_FILENAME);
	        }
	 
	        // Try loading properties from the file (if found)
	        properties.load(in);
	    }
	    catch ( Exception e ) { 
	    	System.out.println("Could not load preferences.");
	    	properties = new Properties();
	    	loadDefaults();
	    }
	 
	}
	
	private void loadDefaults(){
		set("sheet_mag", "3");
		set("image_mag", "30");
		set("autosnip_size", "16");
		set("coll_auto", "true");
		set("coll_x", "4");
		set("coll_y", "6");
		set("coll_w", "8");
		set("coll_h", "8");
	}
	
	public void savePrefChanges() {
	    try {
//	        props.setProperty("ServerAddress", serverAddr);
//	        props.setProperty("ServerPort", ""+serverPort);
	        File f = new File(PREF_FILENAME);
	        OutputStream out = new FileOutputStream( f );
	        properties.store(out, "Preferences for the PTTG Sprite Tool");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}
	
	public String get(String key){
		if (properties == null){
			System.out.println("Preferences did not load.");
		} else {
			if (!properties.containsKey(key)){
				System.out.println("Preferences have no value for '" + key + "'");
			} else {
				return properties.getProperty(key);
			}
		}
		return "";
	}
	
	public void set(String key, String value){
		properties.setProperty(key, value);
	}

}
