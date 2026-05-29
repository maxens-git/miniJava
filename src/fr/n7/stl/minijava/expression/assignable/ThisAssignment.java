package fr.n7.stl.minijava.expression.assignable;

import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.minijava.expression.AbstractThis;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ThisAssignment extends AbstractThis<AssignableExpression> implements AssignableExpression {

	public ThisAssignment() {
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = _factory.createFragment();
		f.add(_factory.createLoadA(Register.LB, this.declaration.getOffset()));
		return f;
	}

}
