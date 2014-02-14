package ast;

import visitor.Visitor;

public class This extends Exp {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
