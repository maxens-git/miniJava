package fr.n7.stl.minijava.ast.type.declaration;

import java.util.Iterator;
import java.util.List;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.minic.ast.type.AtomicType;
import fr.n7.stl.minic.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class ConstructorDeclaration extends ClassElement {
	
	protected List<ParameterDeclaration> parameters;
	
	protected Block body;

	protected ParameterDeclaration thisParam;

	protected String label;

	public String getLabel() {
		return this.label;
	}
	
	public List<ParameterDeclaration> getParameters() {
		return this.parameters;
	}

	public ConstructorDeclaration(String _name, List<ParameterDeclaration> _parameters, Block _body) {
		super( _name);
		this.parameters = _parameters;
		this.body = _body;
	}

	@Override
	public String toString() {
		String image = "";
		image += this.accessRight + " " + this.name + "( ";
		Iterator<ParameterDeclaration> iterator = this.parameters.iterator();
		if (iterator.hasNext()) {
			ParameterDeclaration parameter = iterator.next();
			image += parameter;
			while (iterator.hasNext()) {
				 parameter = iterator.next();
				 image += " ," + parameter;
			}
		}
		image += ")";
		image += this.body; 
		return image;
	}

	@Override
	public Type getType() {
		/*if (!this.body.checkType()) {
			return AtomicType.ErrorType;
		}*/
		return AtomicType.VoidType;
		//throw new SemanticsUndefinedException( "aie aie aie");
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment f = this.body.getCode(_factory);
		int tailleParam = 1; 
		for (ParameterDeclaration p : this.parameters) {
			tailleParam += p.getType().length();
		}
		f.add(_factory.createReturn(0, tailleParam));
		if (this.label != null) {
			f.addPrefix(this.label);
		}
		return f;
	}
}
