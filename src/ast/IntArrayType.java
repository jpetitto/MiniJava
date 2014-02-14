package ast;

import visitor.Visitor;

public class IntArrayType extends Type {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
