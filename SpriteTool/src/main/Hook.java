package main;

import java.awt.Point;

public class Hook
{
	private String name;
	private Point pt;
	
	public Hook(String name, Point pt){
		this.pt = pt;
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Point getPt()
	{
		return pt;
	}
}
