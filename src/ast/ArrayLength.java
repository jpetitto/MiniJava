package ast;

import visitor.Visitor;

public class ArrayLength extends Exp {
	private Exp array;
	
	public ArrayLength(Exp array) {
		this.array = array;
	}
	
	public Exp getArray() {
		return array;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
