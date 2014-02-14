package ast;

import visitor.Visitor;

public abstract class ClassDecl {
	public abstract void accept(Visitor v);
}
