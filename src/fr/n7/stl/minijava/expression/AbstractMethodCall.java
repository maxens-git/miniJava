package fr.n7.stl.minijava.expression;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.Expression;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.ast.type.declaration.MethodDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public abstract class AbstractMethodCall <ObjectKind extends Expression> implements Expression {
	
	protected String name;
	
	protected MethodDeclaration declaration;
	
	protected ObjectKind target;
	
	protected List<AccessibleExpression> arguments;

	public AbstractMethodCall(ObjectKind _target, String _name, List<AccessibleExpression> _arguments) {
		this.target = _target;
		this.name = _name;
		this.arguments = _arguments;
	}
	
	public AbstractMethodCall(String _name, List<AccessibleExpression> _arguments) {
		this( null, _name, _arguments);
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		if (_scope.knows(this.name)) {
			boolean ok = true;
			this.declaration = (MethodDeclaration) _scope.get(name);
			if (this.target != null) {
				ok = this.target.collectAndPartialResolve(_scope);
			}
			for (AccessibleExpression arg : this.arguments) {
				ok &= arg.collectAndPartialResolve(_scope);
			}
			return ok;
		} else {
			// System.out.println(_scope);
			Logger.error(this.name + " is not a member of class" + this.target);
			return false;
		}
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		if (this.target != null) {
			ok &= this.target.completeResolve(_scope);
		}
		for (AccessibleExpression a : this.arguments) {
			ok &= a.completeResolve(_scope);
		}
		return ok;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return this.declaration.getType();
		//throw new SemanticsUndefinedException("aie aie aie");
	}
	
	@Override
	public String toString() {
		String image = "";
		if (this.target != null) {
			image += this.target + ".";
		}
		image += this.name +"( ";
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
