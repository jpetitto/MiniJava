package visitor;

import ast.*;

public class PrettyPrintVisitor implements Visitor {

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.getMainClass().accept(this);
    for ( int i = 0; i < n.getClassDeclList().size(); i++ ) {
        System.out.println();
        n.getClassDeclList().elementAt(i).accept(this);
    }
  }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    System.out.print("class ");
    n.getClassId().accept(this);
    System.out.println(" {");
    System.out.print("  public static void main (String [] ");
    n.getArgId().accept(this);
    System.out.println(") {");
    System.out.print("    ");
    n.getStm().accept(this);
    System.out.println("  }");
    System.out.println("}");
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    System.out.print("class ");
    n.getClassId().accept(this);
    System.out.println(" { ");
    for ( int i = 0; i < n.getFields().size(); i++ ) {
        System.out.print("  ");
        n.getFields().elementAt(i).accept(this);
        if ( i+1 < n.getFields().size() ) { System.out.println(); }
    }
    for ( int i = 0; i < n.getMethods().size(); i++ ) {
        System.out.println();
        n.getMethods().elementAt(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    System.out.print("class ");
    n.getClassId().accept(this);
    System.out.println(" extends ");
    n.getSuperId().accept(this);
    System.out.println(" { ");
    for ( int i = 0; i < n.getFields().size(); i++ ) {
        System.out.print("  ");
        n.getFields().elementAt(i).accept(this);
        if ( i+1 < n.getFields().size() ) { System.out.println(); }
    }
    for ( int i = 0; i < n.getMethods().size(); i++ ) {
        System.out.println();
        n.getMethods().elementAt(i).accept(this);
    }
    System.out.println();
    System.out.println("}");
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.getType().accept(this);
    System.out.print(" ");
    n.getId().accept(this);
    System.out.print(";");
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    System.out.print("  public ");
    n.getReturnType().accept(this);
    System.out.print(" ");
    n.getId().accept(this);
    System.out.print(" (");
    for ( int i = 0; i < n.getParams().size(); i++ ) {
        n.getParams().elementAt(i).accept(this);
        if (i+1 < n.getParams().size()) { System.out.print(", "); }
    }
    System.out.println(") { ");
    for ( int i = 0; i < n.getVars().size(); i++ ) {
        System.out.print("    ");
        n.getVars().elementAt(i).accept(this);
        System.out.println("");
    }
    for ( int i = 0; i < n.getStms().size(); i++ ) {
        System.out.print("    ");
        n.getStms().elementAt(i).accept(this);
        if ( i < n.getStms().size() ) { System.out.println(""); }
    }
    System.out.print("    return ");
    n.getReturnExp().accept(this);
    System.out.println(";");
    System.out.print("  }");
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    n.getType().accept(this);
    System.out.print(" ");
    n.getId().accept(this);
  }

  public void visit(IntArrayType n) {
    System.out.print("int []");
  }

  public void visit(BooleanType n) {
    System.out.print("boolean");
  }

  public void visit(IntegerType n) {
    System.out.print("int");
  }

  // String s;
  public void visit(IdentifierType n) {
    System.out.print(n.getName());
  }

  // StatementList sl;
  public void visit(Block n) {
    System.out.println("{ ");
    for ( int i = 0; i < n.getStms().size(); i++ ) {
        System.out.print("      ");
        n.getStms().elementAt(i).accept(this);
        System.out.println();
    }
    System.out.print("    } ");
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    System.out.print("if (");
    n.getCondExp().accept(this);
    System.out.println(") ");
    System.out.print("    ");
    n.getTrueStm().accept(this);
    System.out.println();
    System.out.print("    else ");
    n.getFalseStm().accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    System.out.print("while (");
    n.getCondExp().accept(this);
    System.out.print(") ");
    n.getStm().accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    System.out.print("System.out.println(");
    n.getExp().accept(this);
    System.out.print(");");
  }
  
  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    n.getId().accept(this);
    System.out.print(" = ");
    n.getValue().accept(this);
    System.out.print(";");
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    n.getId().accept(this);
    System.out.print("[");
    n.getIndex().accept(this);
    System.out.print("] = ");
    n.getValue().accept(this);
    System.out.print(";");
  }

  // Exp e1,e2;
  public void visit(And n) {
    System.out.print("(");
    n.getLHS().accept(this);
    System.out.print(" && ");
    n.getRHS().accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    System.out.print("(");
    n.getLHS().accept(this);
    System.out.print(" < ");
    n.getRHS().accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    System.out.print("(");
    n.getLHS().accept(this);
    System.out.print(" + ");
    n.getRHS().accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    System.out.print("(");
    n.getLHS().accept(this);
    System.out.print(" - ");
    n.getRHS().accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(Times n) {
    System.out.print("(");
    n.getLHS().accept(this);
    System.out.print(" * ");
    n.getRHS().accept(this);
    System.out.print(")");
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.getArray().accept(this);
    System.out.print("[");
    n.getIndex().accept(this);
    System.out.print("]");
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.getArray().accept(this);
    System.out.print(".length");
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.getCallee().accept(this);
    System.out.print(".");
    n.getMethodName().accept(this);
    System.out.print("(");
    for ( int i = 0; i < n.getArgs().size(); i++ ) {
        n.getArgs().elementAt(i).accept(this);
        if ( i+1 < n.getArgs().size() ) { System.out.print(", "); }
    }
    System.out.print(")");
  }

  // int i;
  public void visit(IntegerLiteral n) {
    System.out.print(n.getValue());
  }

  public void visit(True n) {
    System.out.print("true");
  }

  public void visit(False n) {
    System.out.print("false");
  }

  // String s;
  public void visit(IdentifierExp n) {
    System.out.print(n.getName());
  }

  public void visit(This n) {
    System.out.print("this");
  }

  // Exp e;
  public void visit(NewArray n) {
    System.out.print("new int [");
    n.getArraySize().accept(this);
    System.out.print("]");
  }

  // Identifier i;
  public void visit(NewObject n) {
    System.out.print("new ");
    System.out.print(n.getId().getName());
    System.out.print("()");
  }

  // Exp e;
  public void visit(Not n) {
    System.out.print("!");
    n.getExp().accept(this);
  }

  // String s;
  public void visit(Identifier n) {
    System.out.print(n.getName());
  }
}
