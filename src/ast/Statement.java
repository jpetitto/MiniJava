package ast;

import visitor.Visitor;

public abstract class Statement {
	public abstract void accept(Visitor v);
}
