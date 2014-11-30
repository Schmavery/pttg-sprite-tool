package parsers;

public interface Parser {
	public enum ParserSupport {SAVE, LOAD, BOTH};
	public ParserSupport getSupportType();
	public String getSuffix();
	public String getImagePath(String data);
	public String save(String imgPath);
	public void load(String data);
}