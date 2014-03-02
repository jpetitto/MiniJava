package ast;

import visitor.Visitor;

public interface ClassDecl {
	public void accept(Visitor v);
}
