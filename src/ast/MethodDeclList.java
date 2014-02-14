package ast;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclList {
	private List<MethodDecl> list;
	
	public MethodDeclList() {
		list = new ArrayList<MethodDecl>();
	}
	
	public void addElement(MethodDecl methodDecl) {
		list.add(methodDecl);
	}
	
	public MethodDecl elementAt(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
}
