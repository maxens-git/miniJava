/**
 *
 */
package fr.n7.stl.minic.ast.expression.assignable;

import fr.n7.stl.minic.ast.expression.AbstractIdentifier;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.AttributeDeclaration;
import fr.n7.stl.minijava.expression.assignable.AttributeAssignment;
import fr.n7.stl.minijava.expression.assignable.ThisAssignment;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a variable.
 * @author Marc Pantel
 *
 */
public class VariableAssignment extends AbstractIdentifier implements AssignableExpression {

	protected VariableDeclaration declaration;
	protected AssignableExpression attributeAssignment;

	/**
	 * Creates a variable assignment expression Abstract Syntax Tree node.
	 * @param _name Name of the assigned variable.
	 */
	public VariableAssignment(String _name) {
		super(_name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.AbstractIdentifier#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
			Declaration _declaration = _scope.get(this.name);
			if (_declaration instanceof VariableDeclaration) {
				this.declaration = ((VariableDeclaration) _declaration);
				return true;
			} else if (_declaration instanceof AttributeDeclaration) {
				this.attributeAssignment = new AttributeAssignment(new ThisAssignment(), this.name);
				return this.attributeAssignment.collectAndPartialResolve(_scope);
			} else {
				Logger.error("The declaration for " + this.name + " is of the wrong kind.");
				return false;
			}
		} else {
			Logger.error("The identifier " + this.name + " has not been found.");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.AbstractIdentifier#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if (this.attributeAssignment != null) {
			return this.attributeAssignment.completeResolve(_scope);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.VariableUseImpl#getType()
	 */
	@Override
	public Type getType() {
		if (this.attributeAssignment != null) {
			return this.attributeAssignment.getType();
		}
		Type type = this.declaration.getType();
		return type;
		//throw new SemanticsUndefinedException("Semantics getType undefined in VariableAssignment.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.VariableUseImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		if (this.attributeAssignment != null) {
			return this.attributeAssignment.getCode(_factory);
		}
		Fragment f = _factory.createFragment();
		f.add(_factory.createLoadA(this.declaration.getRegister(), this.declaration.getOffset()));
		return f;
		//throw new SemanticsUndefinedException("Semantics getCode undefined in VariableAssignment.");
	}

}
