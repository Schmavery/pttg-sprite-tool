package parsers;

public class JSONParser implements Parser {

	@Override
	public ParserSupport getSupportType() {
		// TODO Auto-generated method stub
		return ParserSupport.SAVE;
	}

	@Override
	public String getSuffix() {
		// TODO Auto-generated method stub
		return ".json";
	}

	@Override
	public String getImagePath(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(String imgPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(String data) {
		// TODO Auto-generated method stub

	}

}
