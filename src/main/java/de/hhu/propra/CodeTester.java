package de.hhu.propra;

import java.io.*;
import java.net.URISyntaxException;

import javafx.beans.property.SimpleStringProperty;
import vk.core.api.*;

public class CodeTester extends SimpleStringProperty {
	private JavaStringCompiler compiler;
	private String dateiname;

	public void testCode(String code, String tabname){
        set("");
		dateiname = tabname;
		CompilationUnit unit = new CompilationUnit(dateiname, code, false);
		compiler = CompilerFactory.getCompiler(unit);

		try {
			compiler.compileAndRunTests();
		} catch (Exception e){
            set("Fehler beim Codeausfuehren: " + e);
			return;
		}

		if (compiler.getCompilerResult().hasCompileErrors()){
			set(fehlerString(unit, tabname));
            return;
		}

        writeExternalFile(code);
        set(externCompile());
	}

	// Das funktioniert nicht ! Wieso nicht? Weiﬂ ich auch nicht!

	private static String externCompile() {
		String ergebnis = "";
		try {
            String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            path = path.substring(1,path.lastIndexOf("/"));
            path = path + "/code/";

			Process process = Runtime.getRuntime().exec(path + "temp_compile.bat");
            // process.waitFor();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				ergebnis += line + "\n";
			}
		} catch (Exception e) {
			return "Fehler auﬂerhalb: " + e;
		}
		return ergebnis;
	}

	private String fehlerString(CompilationUnit unit, String klasse){
		String fehler = "";
		fehler = "Klasse " + klasse + ": Dein Quellcode enthaelt Fehler";
		for (CompileError error : compiler.getCompilerResult().getCompilerErrorsForCompilationUnit(unit)){
			fehler += "\n\tin Zeile " + error.getLineNumber() + ": " + error.getMessage() + ": \n\t";
			fehler += error.getCodeLineContainingTheError() + "\n\t";
			fehler += error.getMessage() + "\n\t";
		}
		return fehler;
	}

    public void writeExternalFile(String code) {
		try {
			String path = CodeTester.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			path = path.substring(1,path.lastIndexOf("/"));
			path = path + "/code/";

            FileWriter writer = new FileWriter(path + dateiname + ".java");
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            set("Konnte keine externen Dateien erstellen: " + e);
        }
    }

    public void speichern(){

    }
}
