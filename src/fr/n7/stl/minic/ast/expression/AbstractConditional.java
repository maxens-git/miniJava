/**
 * 
 */
package fr.n7.stl.minic.ast.expression;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for a conditional expression.
 * @author Marc Pantel
 *
 */
public class AbstractConditional<ExpressionKind extends Expression> implements Expression {

	/**
	 * AST node for the expression whose value is the condition for the conditional expression.
	 */
	protected Expression condition;
	
	/**
	 * AST node for the expression whose value is the then parameter for the conditional expression.
	 */
	protected ExpressionKind thenExpression;
	
	/**
	 * AST node for the expression whose value is the else parameter for the conditional expression.
	 */
	protected ExpressionKind elseExpression;
	
	protected static int suffixLabel = 0;

	/**
	 * Builds a binary expression Abstract Syntax Tree node from the left and right sub-expressions
	 * and the binary operation.
	 * @param _left : Expression for the left parameter.
	 * @param _operator : Binary Operator.
	 * @param _right : Expression for the right parameter.
	 */
	public AbstractConditional(Expression _condition, ExpressionKind _then, ExpressionKind _else) {
		this.condition = _condition;
		this.thenExpression = _then;
		this.elseExpression = _else;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (this.condition.collectAndPartialResolve(_scope)) { 
			Boolean okElse = this.elseExpression.collectAndPartialResolve(_scope);
			return this.thenExpression.collectAndPartialResolve(_scope) && okElse;
		} else {
			Logger.error("Variables dans la condition : " + this.condition + " sont ok.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in ConditionalExpression.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		Boolean okElse = this.elseExpression.completeResolve(_scope);
		return this.thenExpression.completeResolve(_scope) && okElse;
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in ConditionalExpression.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.condition + " ? " + this.thenExpression + " : " + this.elseExpression + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return this.thenExpression.getType();
		//throw new SemanticsUndefinedException( "Semantics getType is undefined in ConditionalExpression.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Integer suffixLabelCopy = suffixLabel;
		suffixLabel ++;

		Fragment f = this.condition.getCode(_factory);

		f.add(_factory.createJumpIf("_sinon_ternaire_" + suffixLabelCopy, 0));
		f.append(this.thenExpression.getCode(_factory));
		f.add(_factory.createJump("_fin_ternaire_" + suffixLabelCopy));
		f.addSuffix("_sinon_ternaire_" + suffixLabelCopy);
		f.append(this.elseExpression.getCode(_factory));
		f.addSuffix("_fin_ternaire_" + suffixLabelCopy);
		
		return f;
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in ConditionalExpression.");
	}

}
