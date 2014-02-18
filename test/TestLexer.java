import java.io.FileNotFoundException;
import java.io.FileReader;

import parser.Lexer;
import parser.Token;
import parser.TokenType;

public class TestLexer {
	public static void main(String[] args) {
		if (args.length == 0)
			System.err.println("No file arguments given");
		else {
			// parse each file argument given
			for (int i = 0; i < args.length; i++) {
				FileReader file;
				
				// attempt to open file
				try {
					file = new FileReader("programs/" + args[i]);
				} catch (FileNotFoundException e) {
					System.err.println(args[i] + " was not found in MiniJava/programs");
					continue; // try next file
				}
				
				// create lexer
				Lexer lexer = new Lexer(file);
				
				// start tokenizing file
				System.out.println("Tokenizing " + args[i] + "...");
				long startTime = System.currentTimeMillis();
				int numTokens = 0;
				Token token;
				do {
					token = lexer.getToken();
					numTokens++;
					
					// print token type and location
					System.out.print(token.getType());
					System.out.print(" (" + token.getLineNum() + "," + token.getColNum() + ")");
					
					// print out semantic values for ID and INT_CONST tokens
					if (token.getType() == TokenType.ID)
						System.out.println(": " + lexer.getIdVal());
					else if (token.getType() == TokenType.INT_CONST)
						System.out.println(": " + lexer.getIntVal());
					else
						System.out.println();
					
				} while (token.getType() != TokenType.EOF);
				
				long endTime = System.currentTimeMillis();
				
				// print out statistics
				System.out.println("---");
				System.out.println("Number of tokens: " + numTokens);
				System.out.println("Execution time: " + (endTime - startTime) + "ms");
				System.out.println();
			}
		}
	}
}
