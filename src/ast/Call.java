package ast;

import visitor.Visitor;

public class Call extends Exp {
	private Exp callee;
	private Identifier methodName;
	private ExpList args;
	
	public Call(Exp callee, Identifier methodName, ExpList args) {
		this.callee = callee;
		this.methodName = methodName;
		this.args = args;
	}
	
	public Exp getCallee() {
		return callee;
	}
	
	public Identifier getMethodName() {
		return methodName;
	}
	
	public ExpList getArgs() {
		return args;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
