package visitor;

import ast.*;

/*
 * 
 * Pretty printing for abstract syntax trees. Allows verification that the
 * trees for a program's syntax were properly constructed during the parsing 
 * stage. It is utilized by the TestParser class in MiniJava/test.
 * 
 */

public class PrettyPrintVisitor implements Visitor {

	@Override
	public void visit(Program prog) {
		if (prog == null) return;
		
		prog.getMainClass().accept(this);
		
		if (prog.getClassDeclList() == null) return;
		
		for (int i = 0; i < prog.getClassDeclList().size(); i++) {
			if (prog.getClassDeclList().elementAt(i) == null)
				continue;
			
			System.out.println();
			prog.getClassDeclList().elementAt(i).accept(this);
		}
	}

	@Override
	public void visit(MainClass main) {
		if (main == null) return;
		
		System.out.print("class ");
		if (main.getClassId() != null) main.getClassId().accept(this);
		System.out.println(" {");
		
		System.out.print("\tpublic static void main(String[] ");
		if (main.getArgId() != null) main.getArgId().accept(this);
		System.out.println(") {");
		
		System.out.print("\t\t");
		if (main.getStm() != null) main.getStm().accept(this);
		System.out.println();
		System.out.println("\t}");
		System.out.println("}");
	}

	@Override
	public void visit(ClassDeclSimple simpleClass) {
		if (simpleClass == null) return;
		
		System.out.print("class ");
		if (simpleClass.getClassId() != null) simpleClass.getClassId().accept(this);
		System.out.println(" {");
		
		if (simpleClass.getFields() != null) {
			for (int i = 0; i < simpleClass.getFields().size(); i++) {
				if (simpleClass.getFields().elementAt(i) == null)
					continue;
				
				System.out.print("\t");
				simpleClass.getFields().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		if (simpleClass.getMethods() != null) {
			for (int i = 0; i < simpleClass.getMethods().size(); i++) {
				if (simpleClass.getMethods().elementAt(i) == null)
					continue;
				
				System.out.print("\t");
				simpleClass.getMethods().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		System.out.println("}");
	}

	@Override
	public void visit(ClassDeclExtends extendsClass) {
		if (extendsClass == null) return;
		
		System.out.print("class ");
		if (extendsClass.getClassId() != null) extendsClass.getClassId().accept(this);
		System.out.print(" extends ");
		if (extendsClass.getSuperId() != null) extendsClass.getSuperId().accept(this);
		System.out.println(" {");
		
		if (extendsClass.getFields() != null) {
			for (int i = 0; i < extendsClass.getFields().size(); i++) {
				if (extendsClass.getFields().elementAt(i) == null)
					continue;
				
				System.out.print("\t");
				extendsClass.getFields().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		if (extendsClass.getMethods() != null) {
			for (int i = 0; i < extendsClass.getMethods().size(); i++) {
				if (extendsClass.getMethods().elementAt(i) == null)
					continue;
				
				System.out.print("\t");
				extendsClass.getMethods().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		System.out.println("}");
	}

	@Override
	public void visit(VarDecl var) {
		if (var == null) return;
		
		if (var.getType() != null) var.getType().accept(this);
		System.out.print(" ");
		if (var.getId() != null) var.getId().accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(MethodDecl method) {
		if (method == null) return;
		
		System.out.print("public ");
		if (method.getReturnType() != null) method.getReturnType().accept(this);
		System.out.print(" ");
		if (method.getId() != null) method.getId().accept(this);
		System.out.print(" (");
		
		if (method.getParams() != null) {
			for (int i = 0; i < method.getParams().size(); i++) {
				if (method.getParams().elementAt(i) == null)
					continue;
				
				method.getParams().elementAt(i).accept(this);
				if (i < method.getParams().size() - 1)
					System.out.print(", ");
			}
		}
		
		System.out.println(") {");
		
		if (method.getVars() != null) {
			for (int i = 0; i < method.getVars().size(); i++) {
				if (method.getVars().elementAt(i) == null)
					continue;
				
				System.out.print("\t\t");
				method.getVars().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		if (method.getStms() != null) {
			for (int i = 0; i < method.getStms().size(); i++) {
				if (method.getStms().elementAt(i) == null)
					continue;
				
				System.out.print("\t\t");
				method.getStms().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		System.out.print("\t\treturn ");
		if (method.getReturnExp() != null) method.getReturnExp().accept(this);
		System.out.println(";");
		System.out.println("\t}");
	}

	@Override
	public void visit(Formal param) {
		if (param == null) return;
		
		if (param.getType() != null) param.getType().accept(this);
		System.out.print(" ");
		if (param.getId() != null) param.getId().accept(this);
	}

	@Override
	public void visit(IntArrayType intArrayT) {
		System.out.print("int[]");
	}

	@Override
	public void visit(BooleanType boolT) {
		System.out.print("boolean");
	}

	@Override
	public void visit(IntegerType intT) {
		System.out.print("int");
	}

	@Override
	public void visit(IdentifierType idT) {
		if (idT != null)
			System.out.print(idT.getName());
	}

	@Override
	public void visit(Block blockStm) {
		if (blockStm == null) return;
		
		System.out.println("{");
		
		if (blockStm.getStms() != null) {
			for (int i = 0; i < blockStm.getStms().size(); i++) {
				if (blockStm.getStms().elementAt(i) == null)
					continue;
				
				System.out.print("\t\t\t");
				blockStm.getStms().elementAt(i).accept(this);
				System.out.println();
			}
		}
		
		System.out.println("\t\t}");
	}

	@Override
	public void visit(If ifStm) {
		if (ifStm == null) return;
		
		System.out.print("if (");
		if (ifStm.getCondExp() != null) ifStm.getCondExp().accept(this);
		System.out.println(")");
		
		System.out.print("\t\t\t");
		if (ifStm.getTrueStm() != null) ifStm.getTrueStm().accept(this);
		System.out.println();
		
		System.out.println("\t\telse");
		System.out.print("\t\t\t");
		if (ifStm.getFalseStm() != null) ifStm.getFalseStm().accept(this);
	}

	@Override
	public void visit(While whileStm) {
		if (whileStm == null) return;
		
		System.out.print("while (");
		if (whileStm.getCondExp() != null) whileStm.getCondExp().accept(this);
		System.out.print(")");
		if (whileStm.getStm() != null) whileStm.getStm().accept(this);
	}

	@Override
	public void visit(Print printStm) {
		if (printStm == null) return;
		
		System.out.print("System.out.println(");
		if (printStm.getExp() != null) printStm.getExp().accept(this);
		System.out.print(");");
	}

	@Override
	public void visit(Assign assignStm) {
		if (assignStm == null) return;
		
		if (assignStm.getId() != null) assignStm.getId().accept(this);
		System.out.print(" = ");
		if (assignStm.getValue() != null) assignStm.getValue().accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(ArrayAssign arrayAssignStm) {
		if (arrayAssignStm == null) return;
		
		if (arrayAssignStm.getId() != null) arrayAssignStm.getId().accept(this);
		System.out.print("[");
		if (arrayAssignStm.getIndex() != null) arrayAssignStm.getIndex().accept(this);
		System.out.print("] = ");
		if (arrayAssignStm.getValue() != null) arrayAssignStm.getValue().accept(this);
		System.out.print(";");
	}

	@Override
	public void visit(And andExp) {
		if (andExp == null) return;
		
		System.out.print("(");
		if (andExp.getLHS() != null) andExp.getLHS().accept(this);
		System.out.print(" && ");
		if (andExp.getRHS() != null) andExp.getRHS().accept(this);
		System.out.print(")");
	}

	@Override
	public void visit(LessThan lessThanExp) {
		if (lessThanExp == null) return;
		
		System.out.print("(");
		if (lessThanExp.getLHS() != null) lessThanExp.getLHS().accept(this);
		System.out.print(" < ");
		if (lessThanExp.getRHS() != null) lessThanExp.getRHS().accept(this);
		System.out.print(")");
	}

	@Override
	public void visit(Plus plusExp) {
		if (plusExp == null) return;
		
		System.out.print("(");
		if (plusExp.getLHS() != null) plusExp.getLHS().accept(this);
		System.out.print(" + ");
		if (plusExp.getRHS() != null) plusExp.getRHS().accept(this);
		System.out.print(")");
	}

	@Override
	public void visit(Minus minusExp) {
		if (minusExp == null) return;
		
		System.out.print("(");
		if (minusExp.getLHS() != null) minusExp.getLHS().accept(this);
		System.out.print(" - ");
		if (minusExp.getRHS() != null) minusExp.getRHS().accept(this);
		System.out.print(")");
	}

	@Override
	public void visit(Times timesExp) {
		if (timesExp == null) return;
		
		System.out.print("(");
		if (timesExp.getLHS() != null) timesExp.getLHS().accept(this);
		System.out.print(" * ");
		if (timesExp.getRHS() != null) timesExp.getRHS().accept(this);
		System.out.print(")");
	}

	@Override
	public void visit(ArrayLookup arrayLookup) {
		if (arrayLookup == null) return;
		
		if (arrayLookup.getArray() != null) arrayLookup.getArray().accept(this);
		System.out.print("[");
		if (arrayLookup.getIndex() != null) arrayLookup.getIndex().accept(this);
	}

	@Override
	public void visit(ArrayLength length) {
		if (length == null) return;
		
		if (length.getArray() != null) length.getArray().accept(this);
		System.out.print(".length");
	}

	@Override
	public void visit(Call callExp) {
		if (callExp == null) return;
		
		if (callExp.getCallee() != null) callExp.getCallee().accept(this);
		System.out.print(".");
		if (callExp.getMethodName() != null) callExp.getMethodName().accept(this);
		System.out.print("(");
		
		if (callExp.getArgs() != null) {
			for (int i = 0; i < callExp.getArgs().size(); i++) {
				if (callExp.getArgs().elementAt(i) == null)
					continue;
				
				callExp.getArgs().elementAt(i).accept(this);
				if (i < callExp.getArgs().size() - 1)
					System.out.print(", ");
			}
		}
		
		System.out.print(")");
	}

	@Override
	public void visit(IntegerLiteral intLiteral) {
		if (intLiteral != null)
			System.out.print(intLiteral.getValue());
	}

	@Override
	public void visit(True trueLiteral) {
		System.out.print("true");
	}

	@Override
	public void visit(False falseLiteral) {
		System.out.print("false");
	}

	@Override
	public void visit(IdentifierExp identExp) {
		if (identExp != null)
			System.out.print(identExp.getName());
	}

	@Override
	public void visit(This thisLiteral) {
		System.out.print("this");
	}

	@Override
	public void visit(NewArray array) {
		if (array == null) return;
		
		System.out.print("new int [");
		if (array.getArraySize() != null) array.getArraySize().accept(this);
		System.out.print("]");
	}

	@Override
	public void visit(NewObject object) {
		if (object == null) return;
		
		System.out.print("new ");
		if (object.getId() != null) object.getId().accept(this);
		System.out.print("()");
	}

	@Override
	public void visit(Not notExp) {
		if (notExp == null) return;
		
		System.out.print("!");
		if (notExp.getExp() != null) notExp.getExp().accept(this);
	}

	@Override
	public void visit(Identifier id) {
		if (id != null)
			System.out.print(id.getName());
	}

}
