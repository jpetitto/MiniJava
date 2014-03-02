package ast;

import visitor.Visitor;

public class This implements Exp {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
