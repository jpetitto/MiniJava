package parser;

/*
 * 
 * A token has a type as well as line and column numbers for error reporting
 * during the syntax and semantic analysis phase of compilation. Tokens with
 * the types ID or INT_CONST will have an associating lexeme (semantic value)
 * that is recorded by the lexer.
 * 
 */

public class Token {
	private TokenType type;
	private int lineNum, colNum; // for error reporting
	
	public Token(TokenType type, int lineNum, int colNum) {
		this.type = type;
		this.lineNum = lineNum;
		this.colNum = colNum;
	}
	
	public TokenType getType() {
		return type;
	}
	
	public int getLineNum() {
		return lineNum;
	}
	
	public int getColNum() {
		return colNum;
	}
}
