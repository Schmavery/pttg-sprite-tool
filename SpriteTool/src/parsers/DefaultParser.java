package parsers;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import main.Animation;
import main.Hook;
import main.ImageData;
import main.MainWindow;
import panels.ImagePanel;

public class DefaultParser implements Parser {

	private enum ParserState {DEFAULT, ANCHOR, HOOKS, INITIAL, COLLISION};
	
	@Override
	public String getImagePath(String data){
		String str = data.split("\n", 2)[0];
		if (str.startsWith("##")){
			return str.substring(2);
		}
		return "";
	}
	
	@Override
	public String getSuffix(){
		return ".dat";
	}
	
	@Override
	public String save() {
		String out = "";
		for (ImageData img : MainWindow.MAIN_WINDOW.getSheetData().getAllImageData()){
			out += imgToString(img);
		}
		for (Animation anim : MainWindow.MAIN_WINDOW.getSheetData().getAnimations()){
			out += animToString(anim);
		}
		return out;
	}
	
	@Override
	public void load(String input) {
		StringBuilder sb = new StringBuilder();
		ImagePanel imgPanel = MainWindow.MAIN_WINDOW.getImagePanel();
		
		for(String str : input.split("\n")){
			if (str.startsWith("img")){
				sb.setLength(0);
				sb.append(str+"\n");
			} else if (str.startsWith("anim"))
			{
				sb.setLength(0);
				sb.append(str+"\n");
			} else if (str.startsWith("endimg")){
				String data = sb.toString();
				ImageData imgData = imgPanel.addSnippedImage(parseLoadRect(data));
				loadImageData(imgData, data);
			} else if (str.startsWith("endanim")){
				String data = sb.toString();
				Animation tmpAnim = new Animation();
				MainWindow.MAIN_WINDOW.getSheetData().getAnimations().add(tmpAnim);
				loadAnimData(tmpAnim, data);
			} else {
				sb.append(str+"\n");
			}
		}

	}
	
	public static Rectangle parseLoadRect(String data){
		Rectangle rect = new Rectangle();
		Point pt;
		for (String l : data.split("\n")){
			if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
				pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
				rect.setLocation(pt);
			} else if (l.matches("dim +\\([0-9]+, ?[0-9]+\\)")){
				pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
				rect.setSize(pt.x, pt.y);
				break;
			}
		}
		return rect;
	}
	
	/**
	 * Accepts a String of form "(x,y)" and translates it
	 * a point with corresponding x and y values.  There is
	 * a reasonable amount of flexibility in the string format.
	 * The only requirement is that the coords be comma-separated.
	 * @param str String describing a point.
	 * @return Point corresponding to the input String.
	 */
	private static Point parsePoint(String str){
 		String xStr = str.substring(0, str.indexOf(","));
		String yStr = str.substring(str.indexOf(","));
		// Strip non-numeric chars
		xStr = xStr.replaceAll("[^\\d]", "");
		yStr = yStr.replaceAll("[^\\d]", "");
		Point pt = null;
		try {
			pt = new Point(Integer.parseInt(xStr), Integer.parseInt(yStr));
		} catch (NumberFormatException e){
			System.out.println("Invalid point parsed: "+ str);
		}
		return pt;
	}
	
	public static void loadImageData(ImageData iData, String data){
		ParserState state = ParserState.DEFAULT;
		Hook tmpHook = null;
		Point pt;
		
		for (String l : data.split("\n")){
			switch (state){
			case ANCHOR:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					iData.setAnchor(pt.x, pt.y);
					state = ParserState.DEFAULT;
				}
				break;
			case HOOKS:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					tmpHook = new Hook("", pt);
				} else if (l.matches("name \\[\\[.+\\]\\]") && tmpHook != null){
					String name = l.substring(l.indexOf("[[")+2, l.indexOf("]]"));
					tmpHook.setName(name);
					iData.addHook(tmpHook);
				} else if (l.startsWith("endhooks")){
					state = ParserState.DEFAULT;
				}
				break;
			case INITIAL:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					iData.getRect().setLocation(pt);
				} else if (l.matches("dim +\\([0-9]+, ?[0-9]+\\)")){
					// Parse width and height as a point for code reuse ;)
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					iData.getRect().setSize(pt.x, pt.y);
				} else if (l.matches("name \\[\\[.+\\]\\]")){
					String name = l.substring(l.indexOf("[[")+2, l.indexOf("]]"));
					iData.setName(name);
					state = ParserState.DEFAULT;
				}
				break;
			case COLLISION:
				if (l.matches("pt +\\([0-9]+, ?[0-9]+\\)")){
					pt = parsePoint(l.substring(l.indexOf("("), l.indexOf(")")+1));
					iData.getPoly().addPoint(pt.x, pt.y);
				} else if (l.startsWith("endcollision")){
					state = ParserState.DEFAULT;
				}
				break;
			case DEFAULT:
				if (l.startsWith("anchor")){
					state = ParserState.ANCHOR;
				} else if (l.startsWith("hooks")){
					state = ParserState.HOOKS;
					iData.clearHooks();
				} else if (l.startsWith("img")){
					String idStr = l.substring(4);
					if (idStr.matches("\\d+")){
						iData.setId(Integer.valueOf(idStr));
					}
					state = ParserState.INITIAL;
					iData.clearRect();
				} else if (l.startsWith("collision")){
					state = ParserState.COLLISION;
					iData.setPoly(new Polygon());
				}
				break;
			}
			
		}
	}
	
	public static void loadAnimData(Animation anim, String data){
		for (String l : data.split("\n")){
			if (l.startsWith("anim ")){
				anim.setName(l.substring(l.indexOf("[[")+2, l.indexOf("]]")));
			} else if (l.startsWith("pause ")){
				anim.setPause(Integer.valueOf(l.replaceAll("[^\\d]", "")));
			} else if (l.startsWith("frame ")){
				int frameId = Integer.valueOf(l.replaceAll("[^\\d]", ""));
				anim.addFrame(MainWindow.MAIN_WINDOW.getSheetData().getImageDataById(frameId));
			}
		}
	}
	
	public static String imgToString(ImageData img){
		String str = "";
		str += "img " + img.getId() + "\n";
		str += "pt (" + img.getRect().x + "," + img.getRect().y + ")\n";
		str += "dim (" + img.getRect().width + "," + img.getRect().height + ")\n";
		str += "name [["+ img.getName() +"]]\n";
		
		if (img.getAnchor() != null){
			str += "anchor\n";
			str += "pt (" + img.getAnchor().x + "," + img.getAnchor().y + ")\n";
		}
		
		str += "hooks\n";
		str += img.getHooks().size() + " hooks\n";
		for (Hook h : img.getHooks()){
			str += "pt (" + h.getPt().x + "," + h.getPt().y + ")\n";
			str += "name [[" + h.getName() + "]]\n";
		}
		str += "endhooks\n";	
		
		if (img.getPoly() != null){
			str += "collision\n";
			for (int i = 0; i < img.getPoly().npoints; i++){
				str += "pt (" + img.getPoly().xpoints[i] + "," + img.getPoly().ypoints[i] + ")\n";
			}
			str += "endcollision\n";
		}
		str += "endimg\n";
		
		return str;
	}
	
	public static String animToString(Animation anim){
		String ret = "";
		ret += "anim [["+ anim.getName() +"]]\n";
		ret += "pause "+ anim.getPause() +"\n";
		for (ImageData frame : anim.getFrames()){
			ret += "frame " + frame.getId() + "\n";
		}
		ret += "endanim\n";
		
		return ret;
	}

}