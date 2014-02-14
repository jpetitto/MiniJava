package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
	// hash tables for fast lookup
	private final static Map<String, TokenType> reservedWords;
	private final static Map<Character, TokenType> punctuation;
	
	// initialize hash tables statically
	static {
		reservedWords = new HashMap<String, TokenType>();
		reservedWords.put("boolean", TokenType.BOOLEAN);
		reservedWords.put("class", TokenType.CLASS);
		reservedWords.put("else", TokenType.ELSE);
		reservedWords.put("extends", TokenType.EXTENDS);
		reservedWords.put("false", TokenType.FALSE);
		reservedWords.put("if", TokenType.IF);
		reservedWords.put("int", TokenType.INT);
		reservedWords.put("main", TokenType.MAIN);
		reservedWords.put("new", TokenType.NEW);
		reservedWords.put("public", TokenType.PUBLIC);
		reservedWords.put("return", TokenType.RETURN);
		reservedWords.put("static", TokenType.STATIC);
		reservedWords.put("String", TokenType.STRING);
		reservedWords.put("this", TokenType.THIS);
		reservedWords.put("true", TokenType.TRUE);
		reservedWords.put("void", TokenType.VOID);
		reservedWords.put("while", TokenType.WHILE);
		
		punctuation = new HashMap<Character, TokenType>();
		punctuation.put('(', TokenType.LPAREN);
		punctuation.put(')', TokenType.RPAREN);
		punctuation.put('[', TokenType.LBRACKET);
		punctuation.put(']', TokenType.RBRACKET);
		punctuation.put('{', TokenType.LBRACE);
		punctuation.put('}', TokenType.RBRACE);
		punctuation.put(';', TokenType.SEMI);
		punctuation.put(',', TokenType.COMMA);
		punctuation.put('.', TokenType.DOT);
		punctuation.put('=', TokenType.ASSIGN);
		punctuation.put('!', TokenType.BANG);
	}
	
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
			
			// offset colNum for tab chars
			if (nextChar == '\t')
				colNum += 3;
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
			
			// check if identifier is a reserved word
			TokenType type = reservedWords.get(idVal);
			if (type != null)
				return new Token(type, lineNum, colNum - idVal.length());
			
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
		
		// check for binops
		switch (nextChar) {
		
			case '&':
				colNum++;
				nextChar = getChar();
				
				// check if next char is '&' to match '&&' binop
				if (nextChar == '&') {
					nextChar = getChar();
					return new Token(TokenType.AND, lineNum, colNum - 2);
				} else
					return new Token(TokenType.UNKNOWN, lineNum, colNum - 1);
				
			case '<':
				colNum++;
				nextChar = getChar();
				return new Token(TokenType.LT, lineNum, colNum - 1);
				
			case '+':
				colNum++;
				nextChar = getChar();
				return new Token(TokenType.PLUS, lineNum, colNum - 1);
				
			case '-':
				colNum++;
				nextChar = getChar();
				return new Token(TokenType.MINUS, lineNum, colNum - 1);
				
			case '*':
				colNum++;
				nextChar = getChar();
				return new Token(TokenType.TIMES, lineNum, colNum - 1);
		}
		
		// check for punctuation
		TokenType type = punctuation.get((char) nextChar);
		colNum++;
		nextChar = getChar();
		
		// found punctuation token
		if (type != null)
			return new Token(type, lineNum, colNum - 1);
		
		// token type is unknown
		return new Token(TokenType.UNKNOWN, lineNum, colNum - 1);
	}
}
