package src.lexical;

public class Token {
	
	private Tag Tag;
	private String Lexema;
	
	public Tag getTag() {
		return Tag;
	}
	public void setTag(Tag tag) {
		Tag = tag;
	}
	public String getLexema() {
		return Lexema;
	}
	public void setLexema(String lexema) {
		Lexema = lexema;
	}
	
}
