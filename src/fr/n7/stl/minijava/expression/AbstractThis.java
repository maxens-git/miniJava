package fr.n7.stl.minijava.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.util.Logger;

public abstract class AbstractThis <ObjectKind extends Expression> implements Expression {

	protected Type type;

	public AbstractThis() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (!_scope.knows("this")) { 
			Logger.error("aie this unknown"); 
			return false; 
		}
		this.type = _scope.get("this").getType();
    	return true;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if (!_scope.knows("this")) { 
			Logger.error("aie this unknown"); 
			return false; 
		}
		this.type = _scope.get("this").getType();
    	return true;
		//throw new SemanticsUndefinedException("aie aie aie");
	}

	@Override
	public Type getType() {
		return this.type;
		//throw new SemanticsUndefinedException("aie aie aie");
	}
	
	@Override
	public String toString() {
		return "this";
	}
}
