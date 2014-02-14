package ast;

import visitor.Visitor;

public class MainClass {
	private Identifier classId, argId;
	private Statement stm;
	
	public MainClass(Identifier classId, Identifier argId, Statement stm) {
		this.classId = classId;
		this.argId = argId;
		this.stm = stm;
	}
	
	public Identifier getClassId() {
		return classId;
	}
	
	public Identifier getArgId() {
		return argId;
	}
	
	public Statement getStm() {
		return stm;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
