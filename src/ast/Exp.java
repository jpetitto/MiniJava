package ast;

import visitor.Visitor;

public abstract class Exp {
	public abstract void accept(Visitor v);
}
