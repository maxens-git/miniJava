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
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	protected static int suffixLabel = 0;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (this.condition.collectAndPartialResolve(_scope)) {
			boolean okElseBranch;
			if (this.elseBranch == null) {
				okElseBranch = true;
			} else {
				okElseBranch = this.elseBranch.collectAndPartialResolve(_scope);
			}
			return this.thenBranch.collectAndPartialResolve(_scope) && okElseBranch;
		} else {
			Logger.error("Variables dans la condition : " + this.condition + " sont ok.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in Conditional.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		if (this.condition.collectAndPartialResolve(_scope)) { // TODO container
			boolean okElseBranch;
			if (this.elseBranch == null) {
				okElseBranch = true;
			} else {
				okElseBranch = this.elseBranch.collectAndPartialResolve(_scope);
			}
			return this.thenBranch.collectAndPartialResolve(_scope) && okElseBranch;
		} else {
			Logger.error("Variables dans la condition : " + this.condition + " sont ok.");
			return false;
		}
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in Conditional.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean okElseBranch;
		if (this.elseBranch == null) {
			okElseBranch = true;
		} else {
			okElseBranch = this.elseBranch.completeResolve(_scope);
		}
		return this.thenBranch.completeResolve(_scope) && okElseBranch;
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in Conditional.");
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
		Boolean elseOk ;
		if (this.elseBranch == null) {
			elseOk = true;
		} else {
			elseOk = this.elseBranch.checkType();
		}
		return condOk && this.thenBranch.checkType() && elseOk ;
		//throw new SemanticsUndefinedException( "Semantics checkType is undefined in Conditional.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.thenBranch.allocateMemory(_register, _offset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, _offset);
		}
		return 0;
		//throw new SemanticsUndefinedException( "Semantics allocateMemory is undefined in Conditional.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Integer suffixLabelCopy = suffixLabel;
		suffixLabel ++;

		Fragment f = this.condition.getCode(_factory);

		if (this.elseBranch == null) {
			f.add(_factory.createJumpIf("_fin_si_" + suffixLabelCopy, 0));
			f.append(this.thenBranch.getCode(_factory));
		} else {
			f.add(_factory.createJumpIf("_sinon_" + suffixLabelCopy, 0));
			f.append(this.thenBranch.getCode(_factory));
			f.add(_factory.createJump("_fin_si_" + suffixLabelCopy));
			f.addSuffix("_sinon_" + suffixLabelCopy);
			f.append(this.elseBranch.getCode(_factory));
		}
		f.addSuffix("_fin_si_" + suffixLabelCopy);
		
		return f;
		//throw new SemanticsUndefinedException( "Semantics getCode is undefined in Conditional.");
	}

}
