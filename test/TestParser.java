import java.io.FileNotFoundException;
import java.io.FileReader;

import ast.Program;

import parser.Parser;
import visitor.PrettyPrintVisitor;

public class TestParser {
	public static void main(String[] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		FileReader file = new FileReader("programs/BinarySearch.java");
		Parser parser = new Parser(file);
		Program p = parser.parseProgram();
		System.out.println("done!");
		System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
		
		PrettyPrintVisitor print = new PrettyPrintVisitor();
		print.visit(p);
	}
}
