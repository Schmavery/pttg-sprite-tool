package main;

import java.util.LinkedList;
import java.util.List;

/**
 * By default, Animations are created with unique names. 
 */
public class Animation
{
	private static final int DEFAULT_PAUSE = 10;
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
		try{
			pause = Integer.parseInt(Preferences.PREFS.get("pause"));
		} catch (NumberFormatException e){
			Preferences.PREFS.set("pause", String.valueOf(DEFAULT_PAUSE));
			pause = DEFAULT_PAUSE;
		}
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
	
	public void shiftFrame(ImageData frame, boolean moveUp){
		int index = frames.indexOf(frame);
		int newIndex = Math.max(Math.min(frames.size() - 1, index + (moveUp ? 1 : -1)), 0);
		frames.add(newIndex, frames.remove(index));
	}
	
	public String getName(){ return name; }
	
	public List<ImageData> getFrames(){ return frames; }
	
	public int getPause(){ return pause; }
	
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
	
	public void loadData(String data){
		for (String l : data.split("\n")){
			if (l.startsWith("anim ")){
				name = l.substring(l.indexOf("[[")+2, l.indexOf("]]"));
			} else if (l.startsWith("pause ")){
				pause = Integer.valueOf(l.replaceAll("[^\\d]", ""));
			} else if (l.startsWith("frame ")){
				int frameId = Integer.valueOf(l.replaceAll("[^\\d]", ""));
				addFrame(MainWindow.MAIN_WINDOW.getSheetData().getImageDataById(frameId));
			}
		}
	}
}
