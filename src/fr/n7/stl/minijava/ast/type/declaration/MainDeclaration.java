package fr.n7.stl.minijava.ast.type.declaration;

import java.util.List;

import fr.n7.stl.minic.ast.Block;
import fr.n7.stl.minic.ast.SemanticsUndefinedException;
import fr.n7.stl.minic.ast.instruction.Instruction;
import fr.n7.stl.minic.ast.instruction.declaration.DeclarationInstruction;
import fr.n7.stl.minic.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.minic.ast.scope.Declaration;
import fr.n7.stl.minic.ast.scope.HierarchicalScope;
import fr.n7.stl.minic.ast.scope.SymbolTable;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class MainDeclaration implements Instruction {
	
	protected String name;
	
	protected List<Declaration> declarations;
	
	protected Block main;

	public MainDeclaration(String _name, List<Declaration> _declarations, Block _main) {
		this.name = _name;
		this.declarations = _declarations;
		this.main = _main;
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof DeclarationInstruction) {
				DeclarationInstruction instructionDeclaration = (DeclarationInstruction) declaration;
				ok &= instructionDeclaration.collectAndPartialResolve(_scope);
			} else {
				Logger.error(declaration.getName() + " is not an instruction declaration");
			}
			
		}
		return ok & this.main.collectAndPartialResolve(_scope);
	}

	@Override
	public boolean collectAndPartialResolve(HierarchicalScope<Declaration> _scope, FunctionDeclaration _container) {
		// TODO Auto-generated method stub
		throw new SemanticsUndefinedException( "Semantics collect is undefined in MainDeclaration.");
		//return false;
	}

	@Override
	public boolean completeResolve(HierarchicalScope<Declaration> _scope) {
		boolean ok = true;
		for (Declaration declaration : this.declarations) {
			if (declaration instanceof DeclarationInstruction) {
				DeclarationInstruction instructionDeclaration = (DeclarationInstruction) declaration;
				ok &= instructionDeclaration.completeResolve(_scope);
			} else {
				Logger.error(declaration.getName() + " is not an instruction declaration");
			}
			
		}
		return ok & this.main.completeResolve(_scope);
	}

	@Override
	public boolean checkType() {
		boolean ok = true;
		for (Declaration d : this.declarations) {
			if (d instanceof DeclarationInstruction) {
				DeclarationInstruction instructionDeclaration = (DeclarationInstruction) d;
				ok = ok & instructionDeclaration.checkType();
			} else {
				Logger.error(d.getName() + " is not an instruction declaration");
			}
		}
		return ok & this.main.checkType();
	}

	@Override
	public int allocateMemory(Register _register, int _offset) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		// TODO Auto-generated method stub
		throw new SemanticsUndefinedException( "aie aie aie");
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String image = "";
		image += "public class " + this.name + " ";
		image += "{\n";
		image += "\n";
		for (Declaration uneDeclaration : this.declarations) {
			image += uneDeclaration;
			image += "\n";
		}
		image += "\tpublic static void Main( String[] args) ";
		image += this.main;
		image += "\n";
		image += "}\n";
		return image;
	}

}
