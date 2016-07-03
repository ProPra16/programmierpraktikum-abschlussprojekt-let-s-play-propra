package de.hhu.propra;

import java.io.FileWriter;
import java.io.IOException;

import vk.core.api.*;

public class CodeTester {
	private static JavaStringCompiler compiler;
	
	public static String testCode(String code){
		String dateiname = "Test";
		CompilationUnit unit1 = new CompilationUnit(dateiname, code, false);
		compiler = CompilerFactory.getCompiler(unit1);
		
		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
//			log("Fehler beim Codeausführen!");
		}
		
		if (compiler.getCompilerResult().hasCompileErrors()){
			return fehlerString(unit1);
		}
		
		
		
		try {
			FileWriter writer = new FileWriter("temp\\" + dateiname + ".java");
			writer.write(code);
			
			Process process = Runtime.getRuntime().exec("cmd /c start temp\temp_compile.bat");
			process.waitFor();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
