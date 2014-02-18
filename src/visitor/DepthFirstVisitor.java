package visitor;

import ast.*;

public class DepthFirstVisitor implements Visitor {

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.getMainClass().accept(this);
    for ( int i = 0; i < n.getClassDeclList().size(); i++ ) {
        n.getClassDeclList().elementAt(i).accept(this);
    }
  }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    n.getClassId().accept(this);
    n.getArgId().accept(this);
    n.getStm().accept(this);
  }
  
  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    n.getClassId().accept(this);
    for ( int i = 0; i < n.getFields().size(); i++ ) {
        n.getFields().elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.getMethods().size(); i++ ) {
        n.getMethods().elementAt(i).accept(this);
    }
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    n.getClassId().accept(this);
    n.getSuperId().accept(this);
    for ( int i = 0; i < n.getFields().size(); i++ ) {
        n.getFields().elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.getMethods().size(); i++ ) {
        n.getMethods().elementAt(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.getType().accept(this);
    n.getId().accept(this);
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    n.getReturnType().accept(this);
    n.getId().accept(this);
    for ( int i = 0; i < n.getParams().size(); i++ ) {
        n.getParams().elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.getVars().size(); i++ ) {
        n.getVars().elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.getStms().size(); i++ ) {
        n.getStms().elementAt(i).accept(this);
    }
    n.getReturnExp().accept(this);
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.getType().accept(this);
    n.getId().accept(this);
  }

  public void visit(IntArrayType n) {
  }

  public void visit(BooleanType n) {
  }

  public void visit(IntegerType n) {
  }

  // String s;
  public void visit(IdentifierType n) {
  }

  // StatementList sl;
  public void visit(Block n) {
    for ( int i = 0; i < n.getStms().size(); i++ ) {
        n.getStms().elementAt(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    n.getCondExp().accept(this);
    n.getTrueStm().accept(this);
    n.getFalseStm().accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    n.getCondExp().accept(this);
    n.getStm().accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    n.getExp().accept(this);
  }
  
  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.getId().accept(this);
    n.getValue().accept(this);
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.getId().accept(this);
    n.getIndex().accept(this);
    n.getValue().accept(this);
  }

  // Exp e1,e2;
  public void visit(And n) {
    n.getLHS().accept(this);
    n.getRHS().accept(this);
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.getLHS().accept(this);
    n.getRHS().accept(this);
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.getLHS().accept(this);
    n.getRHS().accept(this);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.getLHS().accept(this);
    n.getRHS().accept(this);
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.getLHS().accept(this);
    n.getRHS().accept(this);
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
	n.getArray().accept(this);
    n.getIndex().accept(this);
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.getArray().accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.getCallee().accept(this);
    n.getMethodName().accept(this);
    for ( int i = 0; i < n.getArgs().size(); i++ ) {
        n.getArgs().elementAt(i).accept(this);
    }
  }

  // int i;
  public void visit(IntegerLiteral n) {
  }

  public void visit(True n) {
  }

  public void visit(False n) {
  }

  // String s;
  public void visit(IdentifierExp n) {
  }

  public void visit(This n) {
  }

  // Exp e;
  public void visit(NewArray n) {
    n.getArraySize().accept(this);
  }

  // Identifier i;
  public void visit(NewObject n) {
  }

  // Exp e;
  public void visit(Not n) {
    n.getExp().accept(this);
  }

  // String s;
  public void visit(Identifier n) {
  }
}
