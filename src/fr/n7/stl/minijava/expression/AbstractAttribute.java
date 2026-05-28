package fr.n7.stl.minijava.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minic.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.minijava.ast.type.ClassType;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
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
		System.out.println(this.object.getClass());
		Type nt = this.object.getType();
		if (nt instanceof ClassType) {
			while (nt instanceof ClassType) {
				//nt = this.object.
			}
		} else if (nt instanceof ClassElement) {
			if (((ClassElement) nt).getElementKind() == ElementKind.OBJECT) {
				this.attribute = (AttributeDeclaration) ((ClassElement) nt);
			} else {
				Logger.error("Attribute is not an attribute");
			}
		}
		if (this.attribute == null) {
			Logger.error("Attribute not defined");
			return false;
		}
		return ok;
	}

	@Override
	public Type getType() {
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
