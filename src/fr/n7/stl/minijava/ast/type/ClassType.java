package fr.n7.stl.minijava.ast.type;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.util.Logger;

public class ClassType implements Type {

	protected String name;

	protected ClassDeclaration declaration;

	public ClassType(String _name) {
		this.name = _name;
		this.declaration = null;
	}

	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof ClassType) {
			return this.name.equals(((ClassType)_other).name); 
		} else {
			Logger.error(_other + " n'est pas compatible avec" + this);
			return false;
		}
		//throw new SemanticsUndefinedException("aie aie aie");
	}

	@Override
	public boolean compatibleWith(Type _other) {
		if (_other instanceof ClassType) {
			return this.equalsTo(_other);
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
		if (!_scope.knows(this.name)) {
			Logger.error(this + "is not defined");
			return false;
		} else {
			Declaration decl = _scope.get(this.name);
			if (decl instanceof ClassDeclaration) {
				this.declaration = (ClassDeclaration) decl;
				return true;
			} else {
				Logger.error(this + "is not a class");
				return false;
			}
		}
	}
	
	public String toString() {
		return " " + this.name + " ";
	}

	public ClassDeclaration getDeclaration() {
		return this.declaration;
	}

}
