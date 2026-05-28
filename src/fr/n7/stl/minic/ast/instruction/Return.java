/**
 * 
 */
package fr.n7.stl.minic.ast.instruction;

import java.security.InvalidParameterException;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a return instruction.
 * @author Marc Pantel
 *
 */
public class Return implements Instruction {

	protected Expression value;
	
	protected FunctionDeclaration function;

	public Return(Expression _value) {
		this.value = _value;
		this.function = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ((this.function != null)?("// Return in function : " + this.function.getName() + "\n"):"") + "return " + this.value + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		return this.value.collectAndPartialResolve(_scope);
		//throw new SemanticsUndefinedException( "Semantics collect is undefined in Return.");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.value.completeResolve(_scope);
		//throw new SemanticsUndefinedException( "Semantics resolve is undefined in Return.");
	}
	
	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		if (this.function == null) {
			this.function = _container;		
		} else {
			throw new InvalidParameterException("Trying to set a function declaration to a return instruction when one has already been set.");
		}
		return this.collectAndPartialResolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		if (this.function != null) {
			return this.function.getType().compatibleWith(this.value.getType());
		}
		System.out.println("Attention pas de function");
		return true;
		//throw new SemanticsUndefinedException("Semantics checkType undefined in Return.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		return 0; // TODO : function declaration
		//throw new SemanticsUndefinedException("Semantics allocateMemory undefined in Return.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = this.value.getCode(_factory);
		int tailleParam = 0;
		for (ParameterDeclaration param : this.function.getParameters()) {
			tailleParam = tailleParam + param.getType().length();
		}
		f.add(_factory.createReturn(this.value.getType().length(), tailleParam));
		return f;
		//throw new SemanticsUndefinedException("Semantics getCode undefined in Return.");
	}

}
