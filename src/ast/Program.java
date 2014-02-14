package ast;

import visitor.Visitor;

public class Program {
	private MainClass main;
	private ClassDeclList classList;
	
	public Program(MainClass main, ClassDeclList classList) {
		this.main = main;
		this.classList = classList;
	}
	
	public MainClass getMainClass() {
		return main;
	}
	
	public ClassDeclList getClassDeclList() {
		return classList;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
