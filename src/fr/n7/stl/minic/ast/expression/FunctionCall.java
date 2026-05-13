/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.expression.accessible.ConstantAccess;
import fr.n7.stl.minic.ast.expression.accessible.VariableAccess;
import fr.n7.stl.minic.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a function call expression.
 * @author Marc Pantel
 *
 */
public class FunctionCall implements AccessibleExpression {

	/**
	 * Name of the called function.
	 * TODO : Should be an expression.
	 */
	protected String name;
	
	/**
	 * Declaration of the called function after name resolution.
	 * TODO : Should rely on the VariableUse class.
	 */
	protected FunctionDeclaration function;
	
	/**
	 * List of AST nodes that computes the values of the parameters for the function call.
	 */
	protected List<AccessibleExpression> arguments;
	
	/**
	 * @param _name : Name of the called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
	public FunctionCall(String _name, List<AccessibleExpression> _arguments) {
		this.name = _name;
		this.function = null;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = ((this.function == null)?this.name:this.function) + "( ";
		Iterator<AccessibleExpression> _iter = this.arguments.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
		}
		while (_iter.hasNext()) {
			_result += " ," + _iter.next();
		}
		return  _result + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
			for (AccessibleExpression parametre : this.arguments) {
				if(!parametre.collectAndPartialResolve(_scope)) {
					return false;
				};
			}

			Declaration _declaration = _scope.get(this.name);
			if (_declaration instanceof FunctionDeclaration) {
				this.function = (FunctionDeclaration) _declaration;
				return this.function.collectAndPartialResolve(_scope, this.function);
			} else {
				Logger.error("La déclaration n'est pas du bon type");
				return false;
			}
		} else {
			Logger.error("Variable : " + this.name + " is not defined.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in FunctionCall.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		if (((HierarchicalScope<Declaration>)_scope).knows(this.name)) {
			for (AccessibleExpression parametre : this.arguments) {
				if(!parametre.completeResolve(_scope)) {
					return false;
				};
			}
			return true;
		} else {
			Logger.error("Variable : " + this.name + " is not defined.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in FunctionCall.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		for (int i = 0; i < this.arguments.size(); i++) {
			if (!this.arguments.get(i).getType().compatibleWith(this.function.getParameters().get(i).getType())) {
				Logger.error("Parameter " + this.arguments.get(i) + "has wrong type" + "(" + this.function.getParameters().get(i).getType() + ")");
				return AtomicType.ErrorType;
			}
		}

		return this.function.getType();
		//throw new SemanticsUndefinedException( "Semantics getType is undefined in FunctionCall.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = this.function.getCode(_factory);
		for (AccessibleExpression arg : this.arguments) {
			f.append(arg.getCode(_factory));
		}
		f.add(_factory.createCall("function_" + this.name, Register.SB));
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in FunctionCall.");
		return f;
		
	}

}
