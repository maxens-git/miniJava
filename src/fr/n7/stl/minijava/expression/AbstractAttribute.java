package fr.n7.stl.minijava.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.IdentifierAccess;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.AccessRight;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassDeclaration;
import fr.n7.stl.minijava.ast.type.declaration.ClassElement;
import fr.n7.stl.minijava.ast.type.declaration.ElementKind;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public abstract class AbstractAttribute <ObjectKind extends Expression> implements Expression {
	
	protected ObjectKind object;
	protected String name;
	protected AttributeDeclaration attribute;

	public AbstractAttribute(ObjectKind _object, String _name) {
		this.object = _object;
		this.name = _name;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.object.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = this.object.completeResolve(_scope);

		Type nt = this.object.getType();
		if (nt instanceof ClassType) { 
			ClassDeclaration cd = ((ClassType) nt).getDeclaration();
			ClassElement e = cd.get(this.name);
			if (e == null) {
				Logger.error("attribute " + this.name + " is not defined in class " + cd.getName());
				return false;
			} else if (e instanceof AttributeDeclaration) {
				this.attribute = (AttributeDeclaration) e;
				if (!isAccessible(this.attribute.getAccessRight(), cd, _scope)) {
					Logger.error("attribute " + this.name + " is not accessible from " + cd.getName());
					return false;
				}
			} else {
				Logger.error(this.name + " is not an attribute");
				return false;
			}
		} else {
			Logger.error("Not a class type: " + nt);
			return false;
		}
		return ok;
	}

	public boolean isAccessible(AccessRight right, ClassDeclaration classDecl, HierarchicalScope<Declaration> _scope) {
		if (right == AccessRight.PUBLIC || right == AccessRight.PACKAGE) {
			return true;
		}
		ClassDeclaration current = null;
		if (_scope.knows("this")) {
			Type tt = _scope.get("this").getType();
			if (tt instanceof ClassType) {
				current = ((ClassType) tt).getDeclaration();
			}
		}
		return current != null && classDecl != null && current.getName().equals(classDecl.getName());
	}

	@Override
	public Type getType() {
		if (this.attribute == null) {
			Logger.error("Pas la déclaration de " + this.name);
		}
		// TODO Auto-generated method stub
		return this.attribute.getType();
	}
	
	@Override
	public String toString() {
		String image = "";
		image += this.object;
		image += ".";
		image += this.name;
		return image;
	}

}
