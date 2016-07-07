package de.hhu.propra;

import java.io.*;

import vk.core.api.*;

public class CodeTester {
	private static JavaStringCompiler compiler;
	private static String dateiname;

	public static String testCode(String code, String tabname){
		dateiname = tabname;
		CompilationUnit unit = new CompilationUnit(dateiname, code, false);
		compiler = CompilerFactory.getCompiler(unit);

		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
			return ("Fehler beim Codeausfuehren: " + e.toString());
		}

		if (compiler.getCompilerResult().hasCompileErrors()){
			return fehlerString(unit, tabname);
		}

        writeExternalFile(code);

		return "Erfolgreich compiliert.";
	}

	/* Das funktioniert nicht ! Wieso nicht? Weiﬂ ich auch nicht !

	private static String externCompile() {
		String ergebnis = "";
		try {
			Process process = Runtime.getRuntime().exec("cmd /C start code/temp_compile2.bat");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				ergebnis += "\n" + line;
			}
		} catch (Exception e) {
			return "Fehler auﬂerhalb: " + e;
		}
		return ergebnis;
	}*/

	private static String fehlerString(CompilationUnit unit, String klasse){
		String fehler = "";
		fehler = "Klasse " + klasse + ": Dein Quellcode enthaelt Fehler";
		for (CompileError error : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit)){
			fehler += "\n\tin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n\t";
			fehler += error.getCodeLineContainingTheError() + "\n\t";
			fehler += error.getMessage() + "\n\t";
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
