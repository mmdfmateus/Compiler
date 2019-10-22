package lexical;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LexicalAnalyser {

	private char c; // Current char
	private boolean eof; // End of File
	private BufferedReader file; // Source File
	private int line; // Line counter
	private ArrayList<String> tokensList = new ArrayList<>(); // List of tokens built during the read of source file
	// Tokens separated by type (ID, CONST, LITERAL) which will be in symbol table
	private ArrayList<Token> identifiersTokens = new ArrayList<>();
	private ArrayList<Token> constantTokens = new ArrayList<>();
	private ArrayList<Token> literalTokens = new ArrayList<>();

	// Constructor
	public LexicalAnalyser(String fileName) throws FileNotFoundException {
		eof = false;
		line = 1;
		file = new BufferedReader(new FileReader(fileName));
	}

	/** Runs the lexical analisys */
	public void run() throws IOException, Exception {
		getNextChar();
		while (!eof) {
			Token token = scanToken();
			if (eof) {
				return;
			}
			tokensList.add(token.getTokenStr());
			Tag tokenTag = token.getTag();
			// Only insert into symbol table if isn't added before
			if (tokenTag == Tag.ID) {
				if (!listHasToken(identifiersTokens, token)) {
					identifiersTokens.add(token);
				}
			} else if (tokenTag == Tag.CONST_INT || tokenTag == Tag.CONST_FLOAT) {
				if (!listHasToken(constantTokens, token)) {
					constantTokens.add(token);
				}
			} else if (tokenTag == Tag.LITERAL) {
				if (!listHasToken(literalTokens, token)) {
					literalTokens.add(token);
				}
			}
		}
	}

	/** Check if a list has a specific token inserted */
	public boolean listHasToken(ArrayList<Token> list, Token token) {
		Iterator<Token> iterator = list.iterator();
		boolean hasDuplicated = false;
		while (iterator.hasNext()) {
			Token currentToken = iterator.next();
			if (currentToken.getValue().equals(token.getValue())) {
				hasDuplicated = true;
			}
		}
		return hasDuplicated;
	}

	/**
	 * Get an well-formed token from source file. Throws error if an invalid one has
	 * been generated
	 */
	public Token scanToken() throws IOException, Exception {
		Token token = new Token(null, null);
		// Ignores delimitators
		while ((c == ' ' || c == '\t' || c == '\r' || c == '\b' || c == '\n') && !eof) {
			if (c == '\n') {
				line++;
			}
			getNextChar();
		}
		// Return null if end of file
		if (eof) {
			return null;
		}
		switch (c) {
		// Handle operators and comments
		case ('='):
			if (matchNextChar('=')) {
				token.setToken(Tag.COMPARATOR, null);
				getNextChar();
				return token;
			}
			token.setToken(Tag.ATTR, null);
			return token;
		case ('+'):
			token.setToken(Tag.ADD, null);
			getNextChar();
			return token;
		case ('-'):
			token.setToken(Tag.MINUS, null);
			getNextChar();
			return token;
		case ('*'):
			token.setToken(Tag.MULT, null);
			getNextChar();
			return token;
		case ('/'):
			getNextChar();
			// Ignore one line comments
			if (c == '/') {
				while (c != '\n' && !eof) {
					getNextChar();
				}
				return scanToken();
			}
			// Ignore block comments
			if (c == '*') {
				boolean endOfCommentBlok = false;
				// Read comment block while not found "*/" symbol
				while (!endOfCommentBlok) {
					while (c != '*' && !eof) {
						getNextChar();
					}
					if (eof) {
						throw new Exception("ERROR (line " + line + "): Unclosed comment block.");
					}
					getNextChar();
					if (c == '/') {
						endOfCommentBlok = true;
						return scanToken();
					}
				}
			}
			token.setToken(Tag.DIV, null);
			return token;
		case ('>'):
			if (matchNextChar('=')) {
				token.setToken(Tag.GREATER_THAN_OR_EQUAL, null);
				getNextChar();
				return token;
			}
			token.setToken(Tag.GREATER_THAN, null);
			return token;
		case ('<'):
			if (matchNextChar('=')) {
				token.setToken(Tag.LESS_THAN_OR_EQUAL, null);
				getNextChar();
				return token;
			}
			if (matchNextChar('>')) {
				token.setToken(Tag.GREATER_LESS, null);
				getNextChar();
				return token;
			}
			token.setToken(Tag.LESS_THAN, null);
			return token;
		// Handle symbols
		case ('['):
			token.setToken(Tag.OPEN_BRACE, null);
			getNextChar();
			return token;
		case (']'):
			token.setToken(Tag.CLOSE_BRACE, null);
			getNextChar();
			return token;
		case ('('):
			token.setToken(Tag.OPEN_PARENTHESIS, null);
			getNextChar();
			return token;
		case (')'):
			token.setToken(Tag.CLOSE_PARETHESIS, null);
			getNextChar();
			return token;
		case (';'):
			token.setToken(Tag.SEMICOLON, null);
			getNextChar();
			return token;
		case (','):
			token.setToken(Tag.COLON, null);
			getNextChar();
			return token;
		// Handle string literals
		case ('\"'):
			StringBuffer sb = new StringBuffer();
			for (getNextChar(); c != '\"' && !eof; getNextChar()) {
				sb.append(c);
			}
			// If dont close string literal, throw error
			if (eof) {
				throw new Exception("ERROR (line " + line + "): Excpected '\"' to close literal.");
			}
			// Else, set literal token
			String tokenValue = sb.toString();
			token.setToken(Tag.LITERAL, tokenValue);
			getNextChar();
			return token;
		}
		// Handle identifiers and key words
		if (Character.isLetter(c)) {
			StringBuffer sb = new StringBuffer();
			do {
				sb.append(c);
				getNextChar();
			} while (Character.isLetterOrDigit(c) && !eof);
			String tokenValue = sb.toString();
			// Valid identifier
			if (tokenValue.length() == 1 || Character.isLetter(tokenValue.charAt(0))) {
				// If value of token is a key word, set token with its respective tag
				Tag keyWordTag = getKeyWordTag(tokenValue);
				if (keyWordTag != null) {
					token.setToken(keyWordTag, null);
					return token;
					// Else, set token as identifier
				} else {
					token.setToken(Tag.ID, tokenValue);
					return token;
				}
			} else {
				throw new Exception("ERROR (line " + line + "): Invalid token.");
			}
		}
		// Handle numbers
		if (Character.isDigit(c)) {
			int intValue = 0;
			do {
				intValue = 10 * intValue + Character.digit(c, 10);
				getNextChar();
			} while (Character.isDigit(c) && !eof);
			if (Character.isLetter(c)) {
				throw new Exception("ERROR (line " + line + "): Invalid token.");
			}
			// Float number
			if (c == '.') {
				getNextChar();
				if (Character.isDigit(c)) {
					float floatValue = 0;
					int aux = 0;
					do {
						floatValue = (float) (floatValue + Character.digit(c, 10) / Math.pow(10, aux));
						getNextChar();
						aux++;
					} while (Character.isDigit(c) && !eof);
					if (Character.isLetter(c)) {
						throw new Exception("ERROR (line " + line + "): Invalid token.");
					}
					floatValue = intValue + floatValue / 10;
					token.setToken(Tag.CONST_FLOAT, String.valueOf(floatValue));
					return token;
				} else {
					throw new Exception("ERROR (line " + line + "): Invalid token.");
				}
				// Int number
			} else {
				token.setToken(Tag.CONST_INT, String.valueOf(intValue));
				return token;
			}
		}
		throw new Exception("ERROR (line " + line + "): Invalid character '" + c + "'.");
	}

	/**
	 * Get key word tag if tokenValue match with any Tag enum Otherwise, return null
	 */
	public Tag getKeyWordTag(String tokenValue) {
		Tag tags[] = Tag.values();
		Tag keyWordTag = null;
		for (Tag tag : tags) {
			if (tag.toString().toLowerCase().equals(tokenValue)) {
				keyWordTag = tag;
				break;
			}
		}
		return keyWordTag;
	}

	/** Get next character from source file. Also treats end of file */
	public void getNextChar() throws IOException {
		int value = file.read();
		if (value == -1) {
			eof = true;
		} else {
			c = (char) value;
		}
	}

	/** Check if a specific char match with de next char in the source file */
	public boolean matchNextChar(char ch) throws IOException {
		getNextChar();
		if (c != ch) {
			return false;
		}
		c = ' ';
		return true;
	}

	// Prints tokens list
	public void printTokens() {
		System.out.println(tokensList);
	}

	// Prints symbol table
	public void printSymbolTable() {
		Iterator<Token> identifierIterator = identifiersTokens.iterator();
		while (identifierIterator.hasNext()) {
			Token currentToken = identifierIterator.next();
			System.out.println(currentToken.getTag() + ", '" + currentToken.getValue() + "'");
		}
		Iterator<Token> constantIterator = constantTokens.iterator();
		while (constantIterator.hasNext()) {
			Token currentToken = constantIterator.next();
			System.out.println(currentToken.getTag() + ", '" + currentToken.getValue() + "'");
		}
		Iterator<Token> literalIterator = literalTokens.iterator();
		while (literalIterator.hasNext()) {
			Token currentToken = literalIterator.next();
			System.out.println(currentToken.getTag() + ", '" + currentToken.getValue() + "'");
		}
	}
}
