package ast;

import visitor.Visitor;

public abstract class Type {
	public abstract void accept(Visitor v);
}
