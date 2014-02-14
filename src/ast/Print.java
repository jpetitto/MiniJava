package ast;

import visitor.Visitor;

public class Print extends Statement {
	private Exp exp;
	
	public Print(Exp exp) {
		this.exp = exp;
	}
	
	public Exp getExp() {
		return exp;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
