/**
 * 
 */
package fr.n7.stl.minijava.ast.type.declaration;

import java.util.ArrayList;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * 
 */
public class ClassDeclaration implements Instruction, Declaration {
	
	protected List<ClassElement> elements;
	
	protected boolean concrete;
	
	protected String name;
	
	protected String ancestor;

	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, String _ancestor, List<ClassElement> _elements) {
		this.concrete = _concrete;
		this.name = _name;
		this.ancestor = _ancestor;
		this.elements = _elements;
	}
	
	/**
	 * 
	 */
	public ClassDeclaration(boolean _concrete, String _name, List<ClassElement> _elements) {
		this( _concrete, _name, null, _elements);
	}
	
	public ClassElement get(String classElemName) {
        for (ClassElement e : this.elements) {
            if (e.getName().equals(classElemName)) {
				return e;
			}
        }
        return null;
    }

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (_scope.accepts(this)) {
			_scope.register(this);
			List<String> elems = new ArrayList<>();
			for (ClassElement e : this.elements) {
				if (elems.contains(e.getName())) {
					Logger.error("duplicate element ");
					return false;
				} else {
					elems.add(e.getName());
				}
			}
			boolean ok = true;

			HierarchicalScope<Declaration> attributeScope = new SymbolTable(_scope);
			for (ClassElement e : this.elements) {
				if (e instanceof AttributeDeclaration) {
					attributeScope.register(e);
				}
			}

			for (ClassElement e : this.elements) {
				if (e instanceof AttributeDeclaration) {
					
				} else if (e instanceof MethodDeclaration) {
					
					HierarchicalScope<Declaration> scopeParameters = new SymbolTable(attributeScope);
					for (ParameterDeclaration p : ((MethodDeclaration) e).parameters) {
						scopeParameters.register(p);
					} 
					ok &= ((MethodDeclaration) e).body.collectAndPartialResolve(scopeParameters);

				} else if (e instanceof ConstructorDeclaration) {

					HierarchicalScope<Declaration> scopeParameters = new SymbolTable(attributeScope);
					for (ParameterDeclaration p : ((ConstructorDeclaration) e).parameters) {
						scopeParameters.register(p);
					}
					ok &= ((ConstructorDeclaration) e).body.collectAndPartialResolve(scopeParameters);

				} else {
					if (!(e instanceof AttributeDeclaration)) {
						Logger.error("Unknown type in " + this + " class");
					}
				}
			}
			return ok;
		}
		
		return false;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		throw new SemanticsUndefinedException( "Semantics resolve is undefined in ClassDeclaration.");
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.ancestor != null) {
			ok = ok & _scope.knows(this.ancestor);
		}

		HierarchicalScope<Declaration> attributeScope = new SymbolTable(_scope);
		for (ClassElement e : this.elements) {
			if (e instanceof AttributeDeclaration) {
				attributeScope.register(e);
			}
		}

		for (ClassElement e : this.elements) {
			if (e instanceof AttributeDeclaration) {
            	ok &= ((AttributeDeclaration) e).getType().completeResolve(_scope);
			} else if (e instanceof MethodDeclaration) {
				HierarchicalScope<Declaration> scopeParameters = new SymbolTable(attributeScope);
				for (ParameterDeclaration p : ((MethodDeclaration) e).parameters) {
					scopeParameters.register(p);
					ok &= p.getType().completeResolve(scopeParameters);
				}

				ok &= ((MethodDeclaration) e).body.completeResolve(scopeParameters);

			} else if (e instanceof ConstructorDeclaration) {
				HierarchicalScope<Declaration> scopeParameters = new SymbolTable(attributeScope);
				for (ParameterDeclaration p : ((ConstructorDeclaration) e).parameters) {
					scopeParameters.register(p);
					ok &= p.getType().completeResolve(scopeParameters);
				}
				ok &= ((ConstructorDeclaration) e).body.completeResolve(scopeParameters);

			} else {
				Logger.error("Unknown type in " + this + " class");
			}
		}

		return ok;
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in ClassDeclaration.");
	}

	@Override
	public boolean checkType() {
		for (ClassElement element : this.elements) {
			Type typeElem = element.getType();
			if (typeElem instanceof AtomicType) {
				if ((AtomicType) typeElem == AtomicType.ErrorType) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		throw new SemanticsUndefinedException( "Semantics allocation memory is undefined in ClassDeclaration.");
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		throw new SemanticsUndefinedException( "Semantics get code is undefined in ClassDeclaration.");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return new ClassType(name);
	}
	
	@Override
	public String toString() {
		String image = "";
		if (! this.concrete) {
			image += "abstract ";
		}
		image += "class " + this.name + " ";
		if (this.ancestor != null) {
			image += "extends " + this.ancestor + " ";
		}
		image += "{\n";
		for (ClassElement e : this.elements) {
			image += e;
		}
		image += "}\n";
		return image;
	}

}
