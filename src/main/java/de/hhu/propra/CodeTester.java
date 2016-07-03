package main.java.de.hhu.propra;

import vk.core.api.*;

public class CodeTester {
	private static JavaStringCompiler compiler;
	
	
	public static String testCode(String code){
		CompilationUnit unit1 = new CompilationUnit("Test", code, false);
		compiler = CompilerFactory.getCompiler(unit1);
		
		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
			
		}
		
		if (compiler.getCompilerResult().hasCompileErrors()){
			return fehlerString(unit1);
		}
		return ("Fehlerfrei");
	}
	
	private static String fehlerString(CompilationUnit unit){
		String fehler = "";
		fehler = "Dein Quellcode enthält Fehler";
		for (CompileError error : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit)){
			fehler += "\nin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n";
			fehler += error.getCodeLineContainingTheError() + "\n";
			fehler += error.getMessage() + "\n";
		}
		return fehler;
	}
	
//	private static void log(String message){
		// Hier soll später statt println() dann die entsprechende Ausgabe auf der neuen, virtuellen Konsole stehen.
//		System.out.println(message);
//	}
}
