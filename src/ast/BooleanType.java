package ast;

import visitor.Visitor;

public class BooleanType extends Type {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
