package ast;

import visitor.Visitor;

public class True extends Exp {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
