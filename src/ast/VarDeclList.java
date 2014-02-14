package ast;

import java.util.ArrayList;
import java.util.List;

public class VarDeclList {
	private List<VarDecl> list;
	
	public VarDeclList() {
		list = new ArrayList<VarDecl>();
	}
	
	public void addElement(VarDecl varDecl) {
		list.add(varDecl);
	}
	
	public VarDecl elementAt(int index) {
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
}
