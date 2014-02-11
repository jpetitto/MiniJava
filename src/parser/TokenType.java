package parser;

/*
 * 
 * A token type is an attribute of a token, which includes identifiers, integer
 * constants, binary operators, reserved words, and punctuation. Only ID and 
 * INT_CONST token types require an associating lexeme or semantic value that 
 * is managed by the lexer.
 * 
 */

public enum TokenType {
	// basic types
	ID, // [a-zA-Z][a-zA-Z0-9_]*
	INT_CONST, // [0-9]+
	EOF, // input stream has been consumed
	UNKNOWN, // character/token could not be processed
	
	// binary operators
	AND, // &&
	LT, // <
	PLUS, // +
	MINUS, // -
	TIMES, // *
	
	// reserved words (case-sensitive)
	CLASS, // class
	PUBLIC, // public
	STATIC, // static
	VOID, // void
	MAIN, // main - relegate as ID (?)
	STRING, // String - relegate as ID (?)
	EXTENDS, // extends
	RETURN, // return
	INT, // int
	BOOLEAN, // boolean
	IF, // if
	ELSE, // else
	WHILE, // while
	TRUE, // true
	FALSE, // false
	THIS, // this
	NEW, // new
	
	// punctuation
	LPAREN, // (
	RPAREN, // )
	LBRACKET, // [
	RBRACKET, // ]
	LBRACE, // {
	RBRACE, // }
	SEMI, // ;
	COMMA, // ,
	DOT, // .
	ASSIGN, // =
	BANG, // !
	
}
