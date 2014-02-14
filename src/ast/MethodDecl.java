package ast;

import visitor.Visitor;

public class MethodDecl {
	private Type returnType;
	private Identifier id;
	private FormalList params;
	private VarDeclList vars;
	private StatementList stms;
	private Exp returnExp;
	
	public MethodDecl(Type returnType, Identifier id, FormalList params,
			VarDeclList vars, StatementList stms, Exp returnExp) {
		
		this.returnType = returnType;
		this.id = id;
		this.params = params;
		this.vars = vars;
		this.stms = stms;
		this.returnExp = returnExp;
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	public Identifier getId() {
		return id;
	}
	
	public FormalList getParams() {
		return params;
	}
	
	public VarDeclList getVars() {
		return vars;
	}
	
	public StatementList getStms() {
		return stms;
	}
	
	public Exp getReturnExp() {
		return returnExp;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
