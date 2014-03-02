package ast;

import visitor.Visitor;

public class IntArrayType implements Type {
	public void accept(Visitor v) {
		v.visit(this);
	}
}
