package main.tools;

import main.ImageData.ImageType;

public class Tools
{
	private Tools(){}
	private static Magnifier mag = new Magnifier();
	private static Anchor anchor = new Anchor();
	private static BoxTool boxTool = new BoxTool();
	private static SnipTool snipTool = new SnipTool();
	private static Tool[] tools = {mag, anchor, boxTool, snipTool};
	
	public static Magnifier getMagnifier(){
		return mag;
	}
	
	public static Anchor getAnchor(){
		return anchor;
	}
	
	public static BoxTool getBoxTool(){
		return boxTool;
	}

	public static SnipTool getSnipTool(){
		return snipTool;
	}
	
	public static void setButtonEnabledState(ImageType type){
		for (Tool t : tools){
			t.setButtonEnabledState(type);
		}
	}
}
