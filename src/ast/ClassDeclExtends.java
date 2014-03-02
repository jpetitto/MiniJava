package ast;

import visitor.Visitor;

public class ClassDeclExtends implements ClassDecl {
	private Identifier classId, superId;
	private VarDeclList fields;
	private MethodDeclList methods;
	
	public ClassDeclExtends(Identifier classId, Identifier superId, VarDeclList fields,
			MethodDeclList methods) {
		
		this.classId = classId;
		this.superId = superId;
		this.fields = fields;
		this.methods = methods;
	}
	
	public Identifier getClassId() {
		return classId;
	}
	
	public Identifier getSuperId() {
		return superId;
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
