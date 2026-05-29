package fr.n7.stl.minijava.expression.allocation;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassElement;
import fr.n7.stl.minijava.ast.type.declaration.ConstructorDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class ObjectAllocation  implements AccessibleExpression, AssignableExpression {
	
	protected String name;
	
	protected List<AccessibleExpression> arguments;

	protected ClassDeclaration classDeclaration;

	protected ConstructorDeclaration constructor;

	public ObjectAllocation(String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.arguments = _arguments;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (_scope.knows(this.name)) {
			Declaration decl = _scope.get(this.name);
			if (decl instanceof ClassDeclaration) {
				this.classDeclaration = (ClassDeclaration) decl;
			}
			boolean ok = true;
			for (AccessibleExpression a : this.arguments) {
				ok = ok && a.collectAndPartialResolve(_scope);
			}
			return ok;
		} else {
			Logger.error(this.name + " is undefined");
			return false;
		}
		
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.classDeclaration == null && _scope.knows(this.name)) {
			Declaration decl = _scope.get(this.name);
			if (decl instanceof ClassDeclaration) {
				this.classDeclaration = (ClassDeclaration) decl;
			}
		}
		// Trouver le constructeur correspondant au nombre d'arguments
		if (this.classDeclaration != null) {
			for (ClassElement e : this.classDeclaration.getElements()) {
				if (e instanceof ConstructorDeclaration) {
					ConstructorDeclaration ctor = (ConstructorDeclaration) e;
					if (ctor.getParameters().size() == this.arguments.size()) {
						this.constructor = ctor;
						break;
					}
				}
			}
		}
		for (AccessibleExpression a : this.arguments) {
			ok = ok && a.completeResolve(_scope);
		}
		return ok;
	}

	@Override
	public Type getType() {
		if (this.classDeclaration != null) {
			return new ClassType(this.classDeclaration);
		}
		return new ClassType(name);
	}

	private int computeObjectSize() {
		if (this.classDeclaration == null) return 1;
		int size = 0;
		for (ClassElement e : this.classDeclaration.getElements()) {
			if (e instanceof AttributeDeclaration) {
				size += e.getType().length();
			}
		}
		return Math.max(size, 1);
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = _factory.createFragment();
		int objectSize = computeObjectSize();
		f.add(_factory.createLoadL(objectSize));
		f.add(Library.MAlloc);

		if (this.constructor != null && this.constructor.getLabel() != null) {
			f.add(_factory.createLoad(Register.ST, -1, 1));

			for (AccessibleExpression arg : this.arguments) {
				f.append(arg.getCode(_factory));
			}

			f.add(_factory.createCall(this.constructor.getLabel(), Register.SB));
		}
		return f;
	}
	
	@Override
	public String toString() {
		String image = "";
		image += "new " + this.name + "( ";
		Iterator<AccessibleExpression> iterator = this.arguments.iterator();
		if (iterator.hasNext()) {
			AccessibleExpression argument = iterator.next();
			image += argument;
			while (iterator.hasNext()) {
				 argument = iterator.next();
				 image += " ," + argument;
			}
		}
		image += ")";
		return image;
	}

}
