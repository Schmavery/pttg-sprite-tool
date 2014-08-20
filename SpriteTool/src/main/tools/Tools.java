package main.tools;

public class Tools
{
	private Tools(){}
	private static Magnifier mag = new Magnifier();
	private static Anchor anchor = new Anchor();
	
	public static Magnifier getMagnifier(){
		return mag;
	}
	
	public static Anchor getAnchor(){
		return anchor;
	}

}
