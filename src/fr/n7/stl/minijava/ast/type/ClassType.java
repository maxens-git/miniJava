package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.util.Logger;

public class ClassType implements Type {
	
	protected String name;

	public ClassType(String _name) {
		this.name = _name;
	}

	@Override
	public boolean equalsTo(Type _other) {
		// TODO Auto-generated method stub
		if (_other instanceof ClassType) {
			return this.name.equals(_other.toString().trim()); // trim pour enlever les espaces 
		} else {
			Logger.error(_other + " n'est pas compatible avec" + this);
			return false;
		}
		//throw new SemanticsUndefinedException("aie aie aie");
	}

	@Override
	public boolean compatibleWith(Type _other) {
		// TODO Auto-generated method stub
		if (_other instanceof ClassType) {
			return this.equals(_other);
		} else {
			Logger.error(_other + " n'est pas compatible avec" + this);
			return false;
		}
		//throw new SemanticsUndefinedException("aie aie aie");
	}

	@Override
	public Type merge(Type _other) {
		// TODO Auto-generated method stub
		throw new SemanticsUndefinedException("aie aie aie");
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		//throw new SemanticsUndefinedException("aie aie aie");
		return _scope.knows(this.name);
	}
	
	public String toString() {
		return " " + this.name + " ";
	}

}
