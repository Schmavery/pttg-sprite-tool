package main;

import java.util.LinkedList;
import java.util.List;

/**
 * By default, Animations are created with unique names. 
 */
public class Animation
{
	private static int Id;
	private String name;
	private List<ImageData> frames;
	private int pause;  // Delay between frames
	
	public Animation(){
		this(String.valueOf(Id++));
	}
	
	public Animation(String name){
		frames = new LinkedList<>();
		setName(name);
	}
	
	public static void resetId(List<Animation> anims){
		int max = 0;
		int tmp;
		for (Animation anim : anims){
			if (anim.getName().matches("\\d+")){
				tmp = Integer.parseInt(anim.getName());
				if (tmp > max){
					max = tmp;
				}
			}
		}
		Id = max + 1;
	}
	
	public void addFrame(ImageData iData){
		frames.add(iData);
	}
	
	public void setPause(int length){
		pause = length;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){ return name; }
	
	public String toString(){
		String ret = "";
		ret += "anim [["+ getName() +"]]\n";
		ret += "pause "+ pause +"\n";
		for (ImageData frame : frames){
			ret += "frame " + frame.getId() + "\n";
		}
		ret += "endanim\n";
		
		return ret;
	}
}
