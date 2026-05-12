/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Iteration implements Instruction {

	protected Expression condition;
	protected Block body;
	protected static Integer suffixLabel = 0;

	public Iteration(Expression _condition, Block _body) {
		this.condition = _condition;
		this.body = _body;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "while (" + this.condition + " )" + this.body;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (this.condition.collectAndPartialResolve(_scope)) {
			return this.body.collectAndPartialResolve(_scope);
		} else {
			Logger.error("Variables dans la condition : " + this.condition + " sont ok.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in Iteration.");
	}
	
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		if (this.condition.collectAndPartialResolve(_scope)) { // TODO Container
			return this.body.collectAndPartialResolve(_scope);
		} else {
			Logger.error("Variables dans la condition : " + this.condition + " sont ok.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in Iteration.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.body.completeResolve(_scope);
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in Iteration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		Type typeCond = this.condition.getType();
		Boolean condOk = typeCond.compatibleWith(AtomicType.BooleanType);
		if (!condOk) {
			Logger.error(typeCond + " is not compatible with " + AtomicType.BooleanType);
		}
		return condOk && this.body.checkType();

		//throw new SemanticsUndefinedException( "Semantics checkType is undefined in Iteration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.body.allocateMemory(_register, _offset);
		return 0;
		//throw new SemanticsUndefinedException( "Semantics allocateMemory is undefined in Iteration.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Integer suffixLabelCopy = suffixLabel;
		suffixLabel++;

		Fragment f = this.condition.getCode(_factory);

		f.addPrefix("tant_que_" + suffixLabelCopy);
		f.add(_factory.createJumpIf("fin_tant_que_" + suffixLabelCopy, 0));

		f.append(this.body.getCode(_factory));

		f.add(_factory.createJump("tant_que_" + suffixLabelCopy));

		f.addSuffix("fin_tant_que_" + suffixLabelCopy);
		
		return f;
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in Iteration.");
	}

}
