package main.tools;

public class Tools
{
	private Tools(){}
	private static Magnifier mag = new Magnifier();
	private static Anchor anchor = new Anchor();
	private static Boxer boxer = new Boxer();
	
	public static Magnifier getMagnifier(){
		return mag;
	}
	
	public static Anchor getAnchor(){
		return anchor;
	}
	
	public static Boxer getBoxer(){
		return boxer;
	}

}
