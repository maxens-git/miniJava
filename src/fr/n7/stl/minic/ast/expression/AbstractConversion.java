/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 * @author Marc Pantel
 *
 */
public abstract class AbstractConversion<TargetType> implements Expression {

	protected TargetType target;
	protected Type type;
	protected String name;

	public AbstractConversion(TargetType _target, String _type) {
		this.target = _target;
		this.name = _type;
		this.type = null;
	}
	
	public AbstractConversion(TargetType _target, Type _type) {
		this.target = _target;
		this.name = null;
		this.type = _type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (this.type == null) {
			return "(" + this.name + ") " + this.target;
		} else {
			return "(" + this.type + ") " + this.target;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		if (this.target instanceof Expression) {
			if (!this.type.compatibleWith(((Expression)this.target).getType())) {
				Logger.error(this.type + " is not compatible with " + ((Expression)this.target).getType());
			} else {
				return this.type;
			}
		} else {
			Logger.error("ALED jsp ce que c'est que TargetType");
		}
		return AtomicType.ErrorType;
		//throw new SemanticsUndefinedException("Semantics getType undefined in TypeConversion.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (this.target instanceof Expression) {
			return ((Expression)this.target).collectAndPartialResolve(_scope);
		} else {
			Logger.error("ALED jsp ce que c'est que TargetType");
		}
		return true;
		//throw new SemanticsUndefinedException("Semantics collect undefined in TypeConversion.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if (this.target instanceof Expression) {
			if (this.type == null) {
				if (_scope.knows(this.name)) {
					this.type = _scope.get(this.name).getType();
				}
				return ((Expression)this.target).collectAndPartialResolve(_scope) && _scope.knows(this.name);
			} else {
				return ((Expression)this.target).collectAndPartialResolve(_scope) && this.type.completeResolve(_scope);
			}
		} else {
			Logger.error("ALED jsp ce que c'est que TargetType");
		}
		return false;
		//throw new SemanticsUndefinedException("Semantics resolve undefined in TypeConversion.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		if (this.target instanceof Expression) {
			return ((Expression)this.target).getCode(_factory);
		} else {
			Logger.error("ALED jsp ce que c'est que TargetType");
		}
		return _factory.createFragment();
		//throw new SemanticsUndefinedException("Semantics getCode undefined in TypeConversion.");
	}

}
