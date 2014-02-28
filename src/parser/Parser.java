package parser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import ast.*;

/*
 * 
 * Parser passes on a file to the lexer to be tokenized. The parser uses a
 * recursive-descent method of parsing and also integrates the idea of 
 * operator-precedence parsing to correctly deal with parsing binary operator
 * expressions. Syntactic errors are reported by the parser to the console and
 * proper error recovery is implemented (eventually) to minimize the cascading
 * of errors. ASTs are produced by the parser for use in the semantic analysis
 * phase and for generating the IR.
 * 
 */

public class Parser {
	private Lexer lexer;
	private Token token;
	private Exp objectMethodCall; // used for parsing exps with dot operator
	private int errors;
	private Token errorToken;
	
	// hash table for operator precedence levels
	private final static Map<TokenType, Integer> binopLevels;
	
	static {
		binopLevels = new HashMap<TokenType, Integer>();
		binopLevels.put(TokenType.AND, 10);
		binopLevels.put(TokenType.LT, 20);
		binopLevels.put(TokenType.PLUS, 30);
		binopLevels.put(TokenType.MINUS, 30);
		binopLevels.put(TokenType.TIMES, 40);
		binopLevels.put(TokenType.DOT, 50); // method calls
		binopLevels.put(TokenType.LBRACKET, 50); // array look-up
	}
	
	public Parser(FileReader file) {
		lexer = new Lexer(file);
		token = lexer.getToken(); // prime the pump
	}
	
	// verifies current token type and grabs next token or reports error
	private boolean eat(TokenType type) {
		if (token.getType() == type) {
			token = lexer.getToken();
			return true;
		} else {
			error(type);
			return false;
		}
	}
	
	private void skipTo(TokenType... follow) {
		while (token.getType() != TokenType.EOF) {
			for (TokenType skip : follow) {
				if (token.getType() == skip)
					return;
			}
			token = lexer.getToken();
		}
	}
	
	// reports an error to the console
	private void error(TokenType type) {
		// only report error once per erroneous token
		if (token == errorToken)
			return;
		
		System.err.print("ERROR: " + token.getType());
		System.err.print(" at line " + token.getLineNum() + ", column " + token.getColNum());
		System.err.println("; Expected " + type);
		errorToken = token;
		errors++;
		// token = lexer.getToken();
	}
	
	// number of reported syntax errors
	public int getErrorCount() {
		return errors;
	}
	
	// a helper method for parsing an identifier
	private Identifier parseIdentifier() {
		Identifier id = null;
		
		// grab ID value if token type is ID
		if (token.getType() == TokenType.ID)
			id = new Identifier(lexer.getIdVal());
		
		eat(TokenType.ID);
		
		return id;
	}
	
	// top-level parsing method: MainClass ClassDecl*
	public Program parseProgram() {
		MainClass main = parseMainClass();
		
		ClassDeclList classList = new ClassDeclList();
		while (token.getType() != TokenType.EOF)
			classList.addElement(parseClassDecl());
		
		return new Program(main, classList);
	}
	
	// Class w/ main method:
	// class id { public static void main ( String [] id ) { Statement } }
	private MainClass parseMainClass() {
		if (!eat(TokenType.CLASS))
			skipTo(TokenType.ID, TokenType.LBRACE, TokenType.RBRACE);
		
		// check for class identifier name
		Identifier className = parseIdentifier();
		
		if (!eat(TokenType.LBRACE))
			skipTo(TokenType.PUBLIC, TokenType.STATIC, TokenType.VOID, 
					TokenType.MAIN, TokenType.LPAREN, TokenType.RPAREN);
		
		if (!eat(TokenType.PUBLIC))
			skipTo(TokenType.STATIC, TokenType.VOID, TokenType.MAIN, 
					TokenType.LPAREN, TokenType.RPAREN);
		
		if (!eat(TokenType.STATIC))
			skipTo(TokenType.VOID, TokenType.MAIN, TokenType.LPAREN, 
					TokenType.RPAREN);
		
		if (!eat(TokenType.VOID))
			skipTo(TokenType.MAIN, TokenType.LPAREN, TokenType.RPAREN);
		
		if (!eat(TokenType.MAIN))
			skipTo(TokenType.LPAREN, TokenType.RPAREN);
		
		if (!eat(TokenType.LPAREN))
			skipTo(TokenType.STRING, TokenType.LBRACKET, TokenType.ID, 
					TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);
		
		if (!eat(TokenType.STRING))
			skipTo(TokenType.LBRACKET, TokenType.ID, TokenType.RPAREN, 
					TokenType.LBRACE, TokenType.RBRACE);
		
		if (!eat(TokenType.LBRACKET))
			skipTo(TokenType.RBRACKET, TokenType.ID, TokenType.RPAREN, 
					TokenType.LBRACE, TokenType.RBRACE);
		
		if (!eat(TokenType.RBRACKET))
			skipTo(TokenType.ID, TokenType.RPAREN, TokenType.LBRACE, 
					TokenType.RBRACE);
		
		Identifier argName = parseIdentifier();
		
		if (!eat(TokenType.RPAREN))
			skipTo(TokenType.LBRACE, TokenType.RBRACE);
		
		if (!eat(TokenType.LBRACE))
			skipTo(TokenType.RBRACE);
		
		Statement stm = parseStatement();
		
		if (!eat(TokenType.RBRACE))
			skipTo(TokenType.RBRACE, TokenType.CLASS);
		
		if (!eat(TokenType.RBRACE))
			skipTo(TokenType.CLASS);
		
		return new MainClass(className, argName, stm);
	}
	
	/*
	 * Class declaration w/out main method:
	 * class id { VarDecl* MethodDecl* }
	 * class id extends id { VarDecl* MethodDecl* }
	 */
	private ClassDecl parseClassDecl() {
		eat(TokenType.CLASS);
		
		Identifier className = parseIdentifier();
		
		VarDeclList fields = new VarDeclList();
		MethodDeclList methods = new MethodDeclList();
		
		// check whether class extends a superclass or not
		if (token.getType() == TokenType.EXTENDS) {
			eat(TokenType.EXTENDS);
			
			// check for superclass identifier name
			Identifier superName = parseIdentifier();
			
			if (!eat(TokenType.LBRACE)) skipTo(TokenType.RBRACE);
			
			// parse entire class body
			while (token.getType() != TokenType.RBRACE && token.getType() != TokenType.EOF) {
				// parse method or field
				if (token.getType() == TokenType.PUBLIC)
					methods.addElement(parseMethodDecl());
				else
					fields.addElement(parseVarDecl());
			}
			if (!eat(TokenType.RBRACE)) skipTo(TokenType.CLASS);
			
			return new ClassDeclExtends(className, superName, fields, methods);
			
		} else {
			if (!eat(TokenType.LBRACE))
				skipTo(TokenType.RBRACE);
			
			// parse entire class body
			while (token.getType() != TokenType.RBRACE && token.getType() != TokenType.EOF) {
				// parse method or field
				if (token.getType() == TokenType.PUBLIC)
					methods.addElement(parseMethodDecl());
				else
					fields.addElement(parseVarDecl());
			}
			if (!eat(TokenType.RBRACE)) skipTo(TokenType.CLASS);
			
			return new ClassDeclSimple(className, fields, methods);
			
		}
	}
	
	private Statement parseStatement() {
		// Statement block: { Statement* }
		if (token.getType() == TokenType.LBRACE) {
			eat(TokenType.LBRACE);
			
			// recursively call parseStatement() until closing brace
			StatementList stms = new StatementList();
			while (token.getType() != TokenType.RBRACE && token.getType() != TokenType.EOF)
				stms.addElement(parseStatement());
			
			if (!eat(TokenType.RBRACE)) 
				skipTo(TokenType.RBRACE, TokenType.SEMI);
			
			return new Block(stms);
		}
		
		// If statement: if ( Exp ) Statement else Statement
		if (token.getType() == TokenType.IF) {
			eat(TokenType.IF);
			
			// parse conditional expression
			if (!eat(TokenType.LPAREN))
				skipTo(TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);
			
			Exp condExp = parseExp();
			
			if (!eat(TokenType.RPAREN))
				skipTo(TokenType.LBRACE, TokenType.SEMI, TokenType.RBRACE);
			
			// parse true and false statements
			Statement trueStm = parseStatement();
			
			if (!eat(TokenType.ELSE))
				skipTo(TokenType.LBRACE, TokenType.SEMI, TokenType.RBRACE);
			
			Statement falseStm = parseStatement();
			
			return new If(condExp, trueStm, falseStm);
		}
		
		// While statement: while ( Exp ) Statement
		if (token.getType() == TokenType.WHILE) {
			eat(TokenType.WHILE);
			
			// parse looping condition
			if (!eat(TokenType.LPAREN))
				skipTo(TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);
			
			Exp condExp = parseExp();
			
			if (!eat(TokenType.RPAREN))
				skipTo(TokenType.LBRACE, TokenType.SEMI, TokenType.RBRACE);
			
			// parse looping statement
			Statement loopStm = parseStatement();
			
			return new While(condExp, loopStm);
		}
		
		// Identifier statement
		if (token.getType() == TokenType.ID) {
			Identifier id = new Identifier(lexer.getIdVal());
			eat(TokenType.ID);
			
			// Parse potential print statement: System.out.println ( Exp ) ;
			if (id.getName().equals("System") && token.getType() == TokenType.DOT) {
				eat(TokenType.DOT);
				
				if (token.getType() == TokenType.ID && lexer.getIdVal().equals("out"))
					eat(TokenType.ID);
				else {
					eat(TokenType.STATEMENT);
					return null;
				}
				
				if (!eat(TokenType.DOT)) {
					eat(TokenType.STATEMENT);
					return null;
				}
				
				if (token.getType() == TokenType.ID && lexer.getIdVal().equals("println"))
					eat(TokenType.ID);
				else {
					eat(TokenType.STATEMENT);
					return null;
				}
				
				if (!eat(TokenType.LPAREN))
					skipTo(TokenType.RPAREN, TokenType.SEMI);
				
				Exp printExp = parseExp();
				
				if (!eat(TokenType.RPAREN))
					skipTo(TokenType.SEMI);
				
				eat(TokenType.SEMI);
				
				return new Print(printExp);
			}
			
			// Assignment statement: id = Exp ;
			if (token.getType() == TokenType.ASSIGN) {
				eat(TokenType.ASSIGN);
				Exp value = parseExp();
				eat(TokenType.SEMI);
				
				return new Assign(id, value);
			}
			
			// Array value assignment statement: id [ Exp ] = Exp ;
			if (token.getType() == TokenType.LBRACKET) {
				eat(TokenType.LBRACKET);
				Exp index = parseExp();
				
				if (!eat(TokenType.RBRACKET))
					skipTo(TokenType.ASSIGN, TokenType.SEMI);
				
				if (!eat(TokenType.ASSIGN))
					skipTo(TokenType.SEMI);
				
				Exp value = parseExp();
				eat(TokenType.SEMI);
				
				return new ArrayAssign(id, index, value);
			}
		}
		
		// statement type unknown
		eat(TokenType.STATEMENT);
		token = lexer.getToken();
		return null;
	}
	
	// top-level parsing function for an expression
	private Exp parseExp() {
		Exp lhs = parsePrimaryExp();
		return parseBinopRHS(0, lhs); // check for binops following exp
	}
	
	// parse exp before any binop
	private Exp parsePrimaryExp() {
		switch (token.getType()) {
			
			case INT_CONST:
				int value = lexer.getIntVal();
				eat(TokenType.INT_CONST);
				return new IntegerLiteral(value);
			
			case TRUE:
				eat(TokenType.TRUE);
				return new True();
			
			case FALSE:
				eat(TokenType.FALSE);
				return new False();
			
			case ID:
				Identifier id = parseIdentifier();
				
				// the dot operator preceded current exp
				if (objectMethodCall != null) {
					// save exp before dot operator and reset for future parsing
					Exp obj = objectMethodCall;
					objectMethodCall = null;
					
					// check if its an array length lookup or an object method call
					if (id.getName().equals("length"))
						return new ArrayLength(obj);
					else {
						if (!eat(TokenType.LPAREN))
							skipTo(TokenType.RPAREN);
						
						// collect arguments to method call
						ExpList args = new ExpList();
						if (token.getType() != TokenType.RPAREN) {
							args.addElement(parseExp());
							
							while (token.getType() == TokenType.COMMA) {
								eat(TokenType.COMMA);
								args.addElement(parseExp());
							}
						}
						eat(TokenType.RPAREN);
						
						return new Call(obj, id, args);
					}
				}
				
				// if not preceded by dot operator, it is a simple id exp
				return new IdentifierExp(id.getName());
			
			case THIS:
				eat(TokenType.THIS);
				return new This();
			
			case BANG:
				eat(TokenType.BANG);
				return new Not(parseExp());
			
			case LPAREN:
				eat(TokenType.LPAREN);
				Exp exp = parseExp();
				eat(TokenType.RPAREN);
				return exp;
			
			case NEW:
				eat(TokenType.NEW);
				
				// check whether it is a new array or a new object instance
				if (token.getType() == TokenType.INT) {
					eat(TokenType.INT);
					eat(TokenType.LBRACKET);
					Exp arraySize = parseExp();
					eat(TokenType.RBRACKET);
					return new NewArray(arraySize);
				} else {
					Identifier objectType = parseIdentifier();
					eat(TokenType.LPAREN);
					eat(TokenType.RPAREN);
					return new NewObject(objectType);
				}
			
			default:
				// unrecognizable expression
				eat(TokenType.EXPRESSION);
				token = lexer.getToken();
				return null;
				
		}
	}
	
	// parse expressions according to operator precedence levels
	private Exp parseBinopRHS(int level, Exp lhs) {
		// continuously parse exp until a lower order operator comes up
		while (true) {
			// grab operator precedence (-1 for non-operator token)
			Integer val = binopLevels.get(token.getType());
			int tokenLevel = (val != null) ? val.intValue() : -1;
			
			// either op precedence is lower than prev op or token is not an op
			if (tokenLevel < level)
				return lhs;
			
			// save binop before parsing rhs of exp
			TokenType binop = token.getType();
			eat(binop);
			
			// set objectMethodCall to lhs to deal with ambiguity of dot op
			if (binop == TokenType.DOT)
				objectMethodCall = lhs;
			
			Exp rhs = parsePrimaryExp(); // parse rhs of exp
			
			// grab operator precedence (-1 for non-operator token)
			val = binopLevels.get(token.getType());
			int nextLevel = (val != null) ? val.intValue() : -1;
			
			// if next op has higher precedence than prev op, make recursive call
			if (tokenLevel < nextLevel)
				rhs = parseBinopRHS(tokenLevel + 1, rhs);
			
			// build AST for exp
			switch (binop) {
				case AND:
					lhs = new And(lhs, rhs);
					break;
				case LT:
					lhs = new LessThan(lhs, rhs);
					break;
				case PLUS:
					lhs = new Plus(lhs, rhs);
					break;
				case MINUS:
					lhs = new Minus(lhs, rhs);
					break;
				case TIMES:
					lhs = new Times(lhs, rhs);
					break;
				case LBRACKET:
					lhs = new ArrayLookup(lhs, rhs);
					eat(TokenType.RBRACKET);
					break;
				case DOT:
					lhs = rhs;
					break;
				default:
					eat(TokenType.OPERATOR);
					break;
			}
		}
	}
	
	// Variable declaration: Type id ;
	private VarDecl parseVarDecl() {
		Type type = parseType();
		Identifier id = parseIdentifier();
		eat(TokenType.SEMI);
		
		return new VarDecl(type, id);
	}
	
	// Method declaration: public Type id ( FormalList ) { VarDecl* Statement* return Exp ; }
	private MethodDecl parseMethodDecl() {
		if (!eat(TokenType.PUBLIC))
			skipTo(TokenType.INT, TokenType.BOOLEAN, TokenType.ID, TokenType.
					LPAREN, TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);
		
		Type returnType = parseType();
		Identifier methodName = parseIdentifier();
		
		if (!eat(TokenType.LPAREN))
			skipTo(TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);
		
		// collect formal params
		FormalList params = new FormalList();
		if (token.getType() != TokenType.RPAREN) {
			params.addElement(parseFormal());
			
			// check for additional params
			while (token.getType() == TokenType.COMMA) {
				eat(TokenType.COMMA);
				params.addElement(parseFormal());
			}
		}
		
		if (!eat(TokenType.RPAREN))
			skipTo(TokenType.LBRACE, TokenType.RBRACE);
		
		if (!eat(TokenType.LBRACE))
			skipTo(TokenType.RBRACE);
		
		VarDeclList vars = new VarDeclList();
		StatementList stms = new StatementList();
		
		/* collect all var declarations and statements */
		while (token.getType() != TokenType.RETURN && token.getType() != TokenType.EOF) {
			
			switch (token.getType()) {
				
				// int and boolean signals start of var declaration
				case INT:
				case BOOLEAN:
					vars.addElement(parseVarDecl());
					break;
				
				// identifier requires peeking at next token to determine if
				// it's a var declaration or a statement in the method
				case ID:
					// id followed by another id is a var declaration
					if (lexer.peek().getType() == TokenType.ID) {
						vars.addElement(parseVarDecl());
						break;
					}
					
					// otherwise it is a statement, fall through
				default:
					stms.addElement(parseStatement());
			}
		}
		
		if (!eat(TokenType.RETURN))
			skipTo(TokenType.SEMI, TokenType.RBRACE);
		
		Exp returnExp = parseExp();
		
		if (!eat(TokenType.SEMI))
			skipTo(TokenType.RBRACE);
		
		eat(TokenType.RBRACE);
		
		return new MethodDecl(returnType, methodName, params, vars, stms, returnExp);
	}
	
	/*
	 * Type:
	 * int (IntegerType)
	 * int [] (IntArrayType)
	 * boolean (BooleanType)
	 * id (IdentifierType)
	 */
	private Type parseType() {
		switch (token.getType()) {
			
			case INT:
				eat(TokenType.INT);
				
				// check for integer array type
				if (token.getType() == TokenType.LBRACKET) {
					eat(TokenType.LBRACKET);
					
					if (token.getType() == TokenType.RBRACKET) {
						eat(TokenType.RBRACKET);
						return new IntArrayType();
					}
					
					// invalid integer type declaration
					eat(TokenType.TYPE);
					return null;
				}
				
				return new IntegerType();
		
			case BOOLEAN:
				eat(TokenType.BOOLEAN);
				return new BooleanType();
				
			case ID:
				String id = lexer.getIdVal();
				eat(TokenType.ID);
				return new IdentifierType(id);
			
			default:
				// unknown type
				eat(TokenType.TYPE);
				return null;
				
		}
	}
	
	// Formal method parameter: Type id
	private Formal parseFormal() {
		Type type = parseType();
		Identifier id = parseIdentifier();
		
		return new Formal(type, id);
	}
}
