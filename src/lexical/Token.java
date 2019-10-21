package lexical;

public class Token {

  private Tag tag;
  private String value;

  public Token(Tag tag, String value) {
    this.tag = tag;
    this.value = value;
  }

  public void setToken(Tag tag, String value) {
    this.tag = tag;
    this.value = value;
  }

  public Tag getTag() {
    return tag;
  }

  public String getValue() {
    return value;
  }

  public String getTokenStr() {
    if (value == null) {
      return "<" + tag + ">";
    }
    return "<" + tag + ", '" + value + "'>";
  }

}
