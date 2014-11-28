package parsers;

public interface Parser {
	public String getSuffix();
	public String getImagePath(String data);
	public String save();
	public void load(String data);
}