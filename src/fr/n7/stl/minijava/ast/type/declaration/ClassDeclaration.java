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
import fr.n7.stl.util.Logger;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * 
 */
public class ClassDeclaration implements Instruction, Declaration {

	protected List<ClassElement> elements;

	public List<ClassElement> getElements() {
		return this.elements;
	}

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
		this(_concrete, _name, null, _elements);
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
					Logger.error("duplicate element " + e.getName());
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
					scopeParameters.register(new ParameterDeclaration("this", new ClassType(this)));
					for (ParameterDeclaration p : ((MethodDeclaration) e).parameters) {
						scopeParameters.register(p);
					}
					ok &= ((MethodDeclaration) e).body.collectAndPartialResolve(scopeParameters);

				} else if (e instanceof ConstructorDeclaration) {

					HierarchicalScope<Declaration> scopeParameters = new SymbolTable(attributeScope);
					scopeParameters.register(new ParameterDeclaration("this", new ClassType(this)));
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
		Logger.error("class " + this.name + " is already defined");
		return false;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		throw new SemanticsUndefinedException("Semantics resolve is undefined in ClassDeclaration.");
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.ancestor != null) {
			if (!_scope.knows(this.ancestor)) {
				Logger.error("unknown class " + this.ancestor);
				ok = false;
			}
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
				scopeParameters.register(new ParameterDeclaration("this", new ClassType(this)));
				for (ParameterDeclaration p : ((MethodDeclaration) e).parameters) {
					scopeParameters.register(p);
					ok &= p.getType().completeResolve(scopeParameters);
				}

				ok &= ((MethodDeclaration) e).body.completeResolve(scopeParameters);

			} else if (e instanceof ConstructorDeclaration) {
				HierarchicalScope<Declaration> scopeParameters = new SymbolTable(attributeScope);
				scopeParameters.register(new ParameterDeclaration("this", new ClassType(this)));
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
		// throw new SemanticsUndefinedException( "Semantics resolve is undefined in
		// ClassDeclaration.");
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
		for (ClassElement element : this.elements) {
			Type typeElem = element.getType();
			if (typeElem instanceof AtomicType) {
				if ((AtomicType) typeElem == AtomicType.ErrorType) {
					return false;
				}
			}
			if (element instanceof MethodDeclaration) {
				ok &= ((MethodDeclaration) element).body.checkType();
			} else if (element instanceof ConstructorDeclaration) {
				ok &= ((ConstructorDeclaration) element).body.checkType();
			}
		}
		return ok;
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		// Calcul des offsets pour les attributs (dans le tas)
		int attributeOffset = 0;
		for (ClassElement e : this.elements) {
			if (e instanceof AttributeDeclaration) {
				int length = e.getType().length();
				((AttributeDeclaration) e).setOffset(attributeOffset);
				attributeOffset += length;
			}
		}

		int nbMots = 3;
		for (ClassElement e : this.elements) {
			if (e instanceof MethodDeclaration) {
				MethodDeclaration method = (MethodDeclaration) e;
				method.label = "method_" + this.name + "_" + method.getName();
				int totalParamSize = 1;
				for (ParameterDeclaration p : method.parameters) {
					totalParamSize += p.getType().length();
				}
				int paramOffset = -totalParamSize;
				method.thisParam = new ParameterDeclaration("this", new ClassType(this));
				method.thisParam.setOffset(paramOffset);
				paramOffset += 1;
				for (ParameterDeclaration p : method.parameters) {
					p.setOffset(paramOffset);
					paramOffset += p.getType().length();
				}
				method.body.allocateMemory(Register.LB, nbMots);
			} else if (e instanceof ConstructorDeclaration) {
				ConstructorDeclaration constructor = (ConstructorDeclaration) e;
				constructor.label = "constructor_" + this.name + "_" + constructor.parameters.size();
				int totalParamSize = 1;
				for (ParameterDeclaration p : constructor.parameters) {
					totalParamSize += p.getType().length();
				}
				int paramOffset = -totalParamSize;
				constructor.thisParam = new ParameterDeclaration("this", new ClassType(this));
				constructor.thisParam.setOffset(paramOffset);
				paramOffset += 1;
				for (ParameterDeclaration p : constructor.parameters) {
					p.setOffset(paramOffset);
					paramOffset += p.getType().length();
				}
				constructor.body.allocateMemory(Register.LB, nbMots);
			}
		}
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = _factory.createFragment();
		for (ClassElement e : this.elements) {
			f.append(e.getCode(_factory));
		}
		return f;
		// throw new SemanticsUndefinedException( "Semantics get code is undefined in
		// ClassDeclaration.");
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
		if (!this.concrete) {
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
