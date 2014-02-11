import java.io.FileNotFoundException;
import java.io.FileReader;

import parser.Lexer;
import parser.Token;
import parser.TokenType;

public class TestLexer {
	public static void main(String[] args) {
		if (args.length != 1)
			System.err.println("No file argument given");
		else {
			try {
				FileReader file = new FileReader("programs/" + args[0]);
				Lexer lexer = new Lexer(file);
				
				long startTime = System.currentTimeMillis();
				int numTokens = 0;
				
				Token token;
				do {
					token = lexer.getToken();
					numTokens++;
					
					// print token type and location
					System.out.print(token.getType() + " (" + token.getLineNum() + "," + token.getColNum() + ")");
					
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
				
			} catch (FileNotFoundException e) {
				System.err.println(args[0] + " was not found in MiniJava/programs");
			}
		}
	}
}
