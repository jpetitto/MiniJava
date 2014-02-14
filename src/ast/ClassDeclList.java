package ast;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclList {
	private List<ClassDecl> list;
	
	public ClassDeclList() {
		list = new ArrayList<ClassDecl>();
	}
	
	public void addElement(ClassDecl classDecl) {
		list.add(classDecl);
	}
	
	public ClassDecl elementAt(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
}
