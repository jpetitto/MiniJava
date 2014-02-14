package ast;

import visitor.Visitor;

public class IntegerLiteral extends Exp {
	private int value;
	
	private IntegerLiteral(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
