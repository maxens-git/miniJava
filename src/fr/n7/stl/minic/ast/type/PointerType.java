/**
 * 
 */
package fr.n7.stl.minic.ast.type;

import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for a pointer type.
 * @author Marc Pantel
 *
 */
public class PointerType implements Type {

	protected Type element;

	public PointerType(Type _element) {
		this.element = _element;
	}
	
	public Type getPointedType() {
		return this.element;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		if (_other instanceof PointerType) {
			return this.element.compatibleWith(((PointerType)_other).element);
		} else {
			Logger.error(this + " is not equal to " + _other);
			return false;
		}
		//throw new SemanticsUndefinedException("Semantics equalsTo undefined in PointerType.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) { // TODO 
		if (_other instanceof PointerType) {
			PointerType other = (PointerType) _other;
			Boolean ok = this.element.compatibleWith(other.element);
			if (!ok) {
				Logger.error(this.element + " is not compatible with " + other.element);
			}
			return ok;
		//} else if (_other instanceof AtomicType) {
		} else {
			
			return false;
		}
		//throw new SemanticsUndefinedException("Semantics compatibleWith undefined in PointerType.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		if (_other instanceof PointerType) {
			return new PointerType(this.element.merge(((PointerType)_other).element));
		} else {
			Logger.error("Cannot merge pointer type " + this + " with " + _other);
			return AtomicType.ErrorType;
		}
		//throw new SemanticsUndefinedException("Semantics merge undefined in PointerType.");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		return 1;
		//throw new SemanticsUndefinedException("Semantics length undefined in PointerType.");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.element + " *)";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		return this.element.completeResolve(_scope);
	}

}
