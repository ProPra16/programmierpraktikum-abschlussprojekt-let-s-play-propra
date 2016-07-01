package de.hhu.propra;

import vk.core.api.*;

public class CodeTester {
	private static JavaStringCompiler compiler;
	
	
	public static void testCode(String code){
		CompilationUnit unit1 = new CompilationUnit("Test", code, false);
		compiler = CompilerFactory.getCompiler(unit1);
		
		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
			System.out.println("Dein Quellcode ist nicht compilierbar!");
		}
		
		if (compiler.getCompilerResult().hasCompileErrors())
			fehlerausgabe(unit1);
		else 
			log("gut!\n");
	}
	
	private static void fehlerausgabe(CompilationUnit unit){
		log("Dein Quellcode enthält Fehler ");
		for (CompileError error : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit)){
			log ("in Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n");
			log(error.getCodeLineContainingTheError() + "\n");
			log(error.getMessage());
		}
	}
	
	private static void log(String message){
		// Hier soll später statt println() dann die entsprechende Ausgabe auf der neuen, virtuellen Konsole stehen.
		System.out.print(message);
	}
}
