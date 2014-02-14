package ast;

import visitor.Visitor;

public class NewObject extends Exp {
	private Identifier id;
	
	public NewObject(Identifier id) {
		this.id = id;
	}
	
	public Identifier getId() {
		return id;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
