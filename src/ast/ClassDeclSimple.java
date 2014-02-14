package ast;

import visitor.Visitor;

public class ClassDeclSimple extends ClassDecl {
	private Identifier classId;
	private VarDeclList fields;
	private MethodDeclList methods;
	
	public ClassDeclSimple(Identifier classId, VarDeclList fields, MethodDeclList methods) {
		this.classId = classId;
		this.fields = fields;
		this.methods = methods;
	}
	
	public Identifier getClassId() {
		return classId;
	}
	
	public VarDeclList getFields() {
		return fields;
	}
	
	public MethodDeclList getMethods() {
		return methods;
	}
	
	public void accept(Visitor v) {
		v.visit(this);
	}
}
