package fr.n7.stl.minijava.expression.accessible;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.minijava.expression.AbstractAttribute;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAccess extends AbstractAttribute<AccessibleExpression>  implements AccessibleExpression {

	public AttributeAccess(AccessibleExpression _object, String _name) {
		super(_object,_name);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return this.attribute.getType();
		//throw new SemanticsUndefinedException( "aie aie aie");
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = _factory.createFragment();
		f.append(this.object.getCode(_factory));
		f.add(_factory.createLoadL(this.attribute.getOffset()));
		f.add(Library.IAdd);
		f.add(_factory.createLoadI(this.attribute.getType().length()));
		return f;
	}

}
