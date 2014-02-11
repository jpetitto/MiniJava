package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * 
 * Lexer is given a file and returns the next token from the character stream.
 * The token is grabbed from the getToken() method. This is used by the parser
 * during the next phase of compilation. If a token is unrecognizable, then it
 * is returned with a type of UNKNOWN. A token of type EOF is returned by the
 * lexer when the character stream has been entirely consumed. The lexer also
 * records the semantic values for token types ID (idVal) and INT_CONST 
 * (intVal). Line and column numbers are calculated and stored with each token.
 * 
 */

public class Lexer {
	private BufferedReader stream; // stream of characters to be processed
	private String idVal; // semantic value for ID token types
	private int intVal; // semantic value for INT_CONST token types
	private int nextChar;
	private int lineNum = 1, colNum = 1; // current line and column numbers
	
	public Lexer(FileReader file) {
		this.stream = new BufferedReader(file);
		nextChar = getChar();
	}
	
	// called by parser when token type is ID
	public String getIdVal() {
		return idVal;
	}
	
	// called by parser when token type is INT_CONST
	public int getIntVal() {
		return intVal;
	}
	
	// handles I/O for char stream
	private int getChar() {
		try {
			return stream.read();
		} catch (IOException e) {
			System.err.println("IOException occured in Lexer::getChar()");
			return -1;
		}
	}
	
	// detect and skip possible '\n', '\r' and '\rn' line breaks
	private boolean skipNewline() {
		if (nextChar == '\n') {
			lineNum++;
			colNum = 1;
			nextChar = getChar();
			
			return true;
		}
		
		if (nextChar == '\r') {
			lineNum++;
			colNum = 1;
			nextChar = getChar();
			
			// skip over next char if '\n'
			if (nextChar == '\n')
				nextChar = getChar();
			
			return true;
		}
		
		// newline char not found
		return false;
	}
	
	// retrieves the next token in the input stream (EOF signals end of input)
	public Token getToken() {
		// skip whitespace
		while (Character.isWhitespace(nextChar)) {
			// check if whitespace char is a newline
			if (!skipNewline()) {
				colNum++;
				nextChar = getChar();
			}
		}
		
		// identifier or reserved word ([a-zA-Z][a-zA-Z0-9_]*)
		if (Character.isLetter(nextChar)) {
			// create new idVal starting with first char of identifier
			idVal = Character.toString((char) nextChar);
			colNum++;
			nextChar = getChar();
			
			// include remaining seq. of chars that are letters, digits, or _
			while (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
				idVal += (char) nextChar;
				colNum++;
				nextChar = getChar();
			}
			
			// check for reserved word match
			if (idVal.equals("class"))
				return new Token(TokenType.CLASS, lineNum, colNum - "class".length());
			if (idVal.equals("public"))
				return new Token(TokenType.PUBLIC, lineNum, colNum - "public".length());
			if (idVal.equals("static"))
				return new Token(TokenType.STATIC, lineNum, colNum - "static".length());
			if (idVal.equals("void"))
				return new Token(TokenType.VOID, lineNum, colNum - "void".length());
			if (idVal.equals("main"))
				return new Token(TokenType.MAIN, lineNum, colNum - "main".length());
			if (idVal.equals("String"))
				return new Token(TokenType.STRING, lineNum, colNum - "String".length());
			if (idVal.equals("extends"))
				return new Token(TokenType.EXTENDS, lineNum, colNum - "extends".length());
			if (idVal.equals("return"))
				return new Token(TokenType.RETURN, lineNum, colNum - "return".length());
			if (idVal.equals("int"))
				return new Token(TokenType.INT, lineNum, colNum - "int".length());
			if (idVal.equals("boolean"))
				return new Token(TokenType.BOOLEAN, lineNum, colNum - "boolean".length());
			if (idVal.equals("if"))
				return new Token(TokenType.IF, lineNum, colNum - "if".length());
			if (idVal.equals("else"))
				return new Token(TokenType.ELSE, lineNum, colNum - "else".length());
			if (idVal.equals("while"))
				return new Token(TokenType.WHILE, lineNum, colNum - "while".length());
			if (idVal.equals("true"))
				return new Token(TokenType.TRUE, lineNum, colNum - "true".length());
			if (idVal.equals("false"))
				return new Token(TokenType.FALSE, lineNum, colNum - "false".length());
			if (idVal.equals("this"))
				return new Token(TokenType.THIS, lineNum, colNum - "this".length());
			if (idVal.equals("new"))
				return new Token(TokenType.NEW, lineNum, colNum - "new".length());
			
			// token is an identifier
			return new Token(TokenType.ID, lineNum, colNum - idVal.length());
		}
		
		// integer literal ([0-9]+)
		if (Character.isDigit(nextChar)) {
			// create string representation of number
			String numString = Character.toString((char) nextChar);
			colNum++;
			nextChar = getChar();
			
			// concatenate remaining seq. of digits
			while (Character.isDigit(nextChar)) {
				numString += (char) nextChar;
				colNum++;
				nextChar = getChar();
			}
			
			// convert string representation to integer value
			intVal = Integer.parseInt(numString);
			
			return new Token(TokenType.INT_CONST, lineNum, colNum - numString.length());
		}
		
		// check for start of comment
		if (nextChar == '/') {
			colNum++;
			nextChar = getChar();
			
			// single-line comment (skip to the next line)
			if (nextChar == '/') {
				do {
					colNum++;
					nextChar = getChar();
				} while (!skipNewline() && nextChar != -1);
				
				// grab next token
				return getToken();
			}
			
			// multi-line comment (skip input until matching '*/' is found)
			if (nextChar == '*') {
				colNum++;
				nextChar = getChar();
				
				// keep track of nesting level
				int nestingLevel = 1;
				while (nestingLevel > 0) {
					if (nextChar == '*') {
						// check if it closes a comment
						colNum++;
						nextChar = getChar();
						
						if (nextChar == '/') {
							nestingLevel--;
							colNum++;
							nextChar = getChar();
						}
					
					} else if (nextChar == '/') {
						// check if it starts a nested comment
						colNum++;
						nextChar = getChar();
						
						if (nextChar == '*') {
							nestingLevel++;
							colNum++;
							nextChar = getChar();
						}
						
					} else {
						// check if EOF is reached before comment is terminated
						if (nextChar == -1)
							break;
						
						// process newline chars
						if (!skipNewline()) {
							colNum++;
							nextChar = getChar();
						}
						
					}
				}
				
				// grab next token
				return getToken();
			}
			
			// neither single-line nor multi-line comment
			return new Token(TokenType.UNKNOWN, lineNum, colNum - 1);
		}
		
		// EOF reached
		if (nextChar == -1)
			return new Token(TokenType.EOF, lineNum, colNum);
		
		// check for binops and punctuation
		Token token = null;
		boolean doNotProcess = false; // used when checking for a second char
		colNum++;
		
		switch (nextChar) {
		
			case '&':
				colNum++;
				nextChar = getChar();
				
				// check if next char is '&' to match '&&' binop
				if (nextChar == '&') {
					token = new Token(TokenType.AND, lineNum, colNum - 2);
				} else {
					token = new Token(TokenType.UNKNOWN, lineNum, colNum - 1);
					doNotProcess = true;
				}
				
				break;
				
			case '<':
				token = new Token(TokenType.LT, lineNum, colNum - 1);
				break;
				
			case '+':
				token = new Token(TokenType.PLUS, lineNum, colNum - 1);
				break;
				
			case '-':
				token = new Token(TokenType.MINUS, lineNum, colNum - 1);
				break;
				
			case '*':
				token = new Token(TokenType.TIMES, lineNum, colNum - 1);
				break;
				
			case '(':
				token = new Token(TokenType.LPAREN, lineNum, colNum - 1);
				break;
				
			case ')':
				token = new Token(TokenType.RPAREN, lineNum, colNum - 1);
				break;
				
			case '[':
				token = new Token(TokenType.LBRACKET, lineNum, colNum - 1);
				break;
				
			case ']':
				token = new Token(TokenType.RBRACKET, lineNum, colNum - 1);
				break;
				
			case '{':
				token = new Token(TokenType.LBRACE, lineNum, colNum - 1);
				break;
				
			case '}':
				token = new Token(TokenType.RBRACE, lineNum, colNum - 1);
				break;
				
			case ';':
				token = new Token(TokenType.SEMI, lineNum, colNum - 1);
				break;
				
			case ',':
				token = new Token(TokenType.COMMA, lineNum, colNum - 1);
				break;
				
			case '.':
				token = new Token(TokenType.DOT, lineNum, colNum - 1);
				break;
				
			case '=':
				token = new Token(TokenType.ASSIGN, lineNum, colNum - 1);
				break;
				
			case '!':
				token = new Token(TokenType.BANG, lineNum, colNum - 1);
				break;
			
			// token type is unknown
			default:
				token = new Token(TokenType.UNKNOWN, lineNum, colNum - 1);
				break;
		}
		
		// prime next char if binop/punctuation was more than one char
		if (!doNotProcess) {
			nextChar = getChar();
		}
		
		return token;
	}
}
