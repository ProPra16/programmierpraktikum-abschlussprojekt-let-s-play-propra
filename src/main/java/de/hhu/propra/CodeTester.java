package de.hhu.propra;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import vk.core.api.*;

public class CodeTester {
	private static JavaStringCompiler compiler;
	private static String dateiname;
	public static String testCode(String code){
		dateiname = "Test";
		CompilationUnit unit1 = new CompilationUnit(dateiname, code, false);
		compiler = CompilerFactory.getCompiler(unit1);

		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
			return ("Fehler beim Codeausfuehren: " + e.toString());
		}

		if (compiler.getCompilerResult().hasCompileErrors()){
			return fehlerString(unit1);
		}

        writeExternalFile(code);

		return externCompile();
	}

	private static String externCompile() {
		String ergebnis = "";
		try {
			Process process = Runtime.getRuntime().exec("cmd /C start code\\temp_compile.cmd");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				ergebnis += "\n" + line;
			}
		} catch (Exception e) {
			return e.toString();
		}
		return ergebnis;
	}

	private static String fehlerString(CompilationUnit unit){
		String fehler = "";
		fehler = "Dein Quellcode enthaelt Fehler";
		for (CompileError error : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit)){
			fehler += "\nin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n";
			fehler += error.getCodeLineContainingTheError() + "\n";
			fehler += error.getMessage() + "\n";
		}
		return fehler;
	}

    public static void writeExternalFile(String code){
        try {
            FileWriter writer = new FileWriter("code/" + dateiname + ".java");
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            System.err.println("Konnte keine externen Dateien erstellen: " + e);
        }
    }
}
